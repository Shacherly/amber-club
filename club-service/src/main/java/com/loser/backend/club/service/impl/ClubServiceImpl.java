package com.loser.backend.club.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Range;
import com.loser.backend.club.common.enums.ClubGiftStatusEnum;
import com.loser.backend.club.config.ClubConfig;
import com.loser.backend.club.config.ExecutorConfiguration;
import com.loser.backend.club.config.GlobalConfig;
import com.loser.backend.club.constant.RedisKey;
import com.loser.backend.club.controller.request.ClubLevelBindingParam;
import com.loser.backend.club.controller.response.BriefClubVO;
import com.loser.backend.club.controller.response.ClubEarnProdVO;
import com.loser.backend.club.controller.response.ClubGiftVO;
import com.loser.backend.club.domain.ClubGiftCompare;
import com.loser.backend.club.domain.ClubGiftConfig;
import com.loser.backend.club.domain.ClubLevel;
import com.loser.backend.club.exception.BusinessException;
import com.loser.backend.club.exception.ExceptionEnum;
import com.loser.backend.club.exception.VisibleException;
import com.loser.backend.club.kafka.message.ClubUpgradeModel;
import com.loser.backend.club.mapper.ClubLevelMapper;
import com.loser.backend.club.pojo.loserClub;
import com.loser.backend.club.service.IClubPercepService;
import com.loser.backend.club.service.IClubService;
import com.loser.backend.club.service.ICouponServiceApi;
import com.loser.backend.club.service.IEarnServiceApi;
import com.loser.backend.club.service.IGiftCompareService;
import com.loser.backend.club.task.ClubSyncTask;
import com.loser.backend.club.util.TemporalUtil;
import com.trading.backend.coupon.common.cache.RedisService;
import com.trading.backend.coupon.common.util.Functions;
import com.trading.backend.coupon.common.util.Predicator;
import com.trading.backend.coupon.http.response.club.ClubPossessVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;


@Slf4j
@Service
public class ClubServiceImpl implements IClubService {

    @Autowired
    private ClubLevelMapper clubLevelMapper;
    @Autowired
    private IGiftCompareService giftCompareService;
    @Autowired
    private ICouponServiceApi couponServiceApi;
    @Autowired
    private IEarnServiceApi earnServiceApi;
    @Autowired
    private RedisService redis;
    @Autowired
    private ClubSyncTask clubSyncTask;
    @Autowired
    private ExecutorConfiguration executor;
    @Autowired
    private IClubPercepService clubPercepService;
    @Autowired
    private GlobalConfig globalConfig;


    @Override
    public ClubLevel getClubLevel(String uid) {
        Example example = new Example(ClubLevel.class);
        example.createCriteria().andEqualTo("uid", uid);
        if (!globalConfig.loserAccessible())
            example.excludeProperties("loserLevel");
        return clubLevelMapper.selectOneByExample(example);
    }

    @Override
    public loserClub getloserClub(String uid) {
        return clubLevelMapper.getloserClub(uid);
    }

    @Override
    public List<loserClub> getloserClubs(Collection<String> uids) {
        return clubLevelMapper.getloserClubs(uids);
    }

    @Override
    public Map<String, ClubLevel> getClubLevels(List<String> uids) {
        if (CollectionUtil.isEmpty(uids)) return Collections.emptyMap();
        if (uids.size() == 1) return Collections.singletonMap(uids.get(0), getClubLevel(uids.get(0)));

        Example example = new Example(ClubLevel.class);
        example.createCriteria().andIn("uid", uids.stream().distinct().collect(Collectors.toList()));
        List<ClubLevel> clubLevels = clubLevelMapper.selectByExample(example);
        Map<String, ClubLevel> uidLevelMap = Functions.toMap(clubLevels, ClubLevel::getUid);
        // 填充null k-v，保证返回和入参的size一致
        uids.forEach(uid -> uidLevelMap.putIfAbsent(uid, null));
        return uidLevelMap;
    }

    @Override
    public BriefClubVO getBriefClub(String uid) {
        ClubLevel club = getClubLevel(uid);
        log.info("#getBriefClub = {}", club);
        return Optional.ofNullable(club).map(this::levelUpgrade).orElseGet(() -> BriefClubVO.ordinary(uid));
    }

    @Override
    public List<BriefClubVO> getBriefClubs(List<String> uids) {
        if (CollectionUtil.size(uids) > 200)
            throw new VisibleException(ExceptionEnum.ARGS_SIZE_EXCEED, 200);

        Map<String, ClubLevel> clubLevels = getClubLevels(uids);
        List<BriefClubVO> result = new ArrayList<>();
        clubLevels.forEach((uid, clubLevel) -> {
            BriefClubVO briefClubVO = Optional.ofNullable(clubLevel).map(this::levelUpgrade).orElseGet(() -> BriefClubVO.ordinary(uid));
            result.add(briefClubVO);
        });
        return result;
    }

    @Override
    public BriefClubVO levelUpgrade(ClubLevel origin) {
        Integer latestLevel = ClubConfig.getLevel(origin.getClubPoint());
        boolean toDegrade = latestLevel < origin.getClubLevel();
        if (toDegrade) {
            log.info("NotUpdateDay, level preserved");
            return BriefClubVO.preserve(origin);
        }
        return BriefClubVO.ofLatest(origin);
    }

    // @Override
    // public ClubLevel upgrade(Integer newLevel, ClubLevel origin) {
    //     LocalDateTime now = LocalDateTime.now();
    //     origin.setUpgradeTime(now);
    //     origin.setClubLevel(newLevel);
    //     origin.setUpgradeRead(false);
    //
    //     clubLevelMapper.updateByPrimaryKeySelective(origin);
    //     return origin;
    // }
    //
    // @Override
    // public ClubLevel degrade(Integer newLevel, ClubLevel origin) {
    //     LocalDateTime now = LocalDateTime.now();
    //     origin.setUpgradeTime(now);
    //     origin.setClubLevel(newLevel);
    //     LocalDateTime nextMonthStart = LocalDateTime.of(now.toLocalDate().plusMonths(1), LocalTime.MIN);
    //     origin.setValidUntil(nextMonthStart);
    //     clubLevelMapper.updateByPrimaryKeySelective(origin);
    //     return origin;
    // }

    @Override
    public boolean upgradeRead(String uid) {
        ClubLevel clubLevel = new ClubLevel();
        clubLevel.setUpgradeRead(true);
        Example example = new Example(ClubLevel.class);
        example.createCriteria().andEqualTo("uid", uid).andEqualTo("upgradeRead", false);
        int i = clubLevelMapper.updateByExampleSelective(clubLevel, example);
        return i == 1;
    }

    @Override
    public boolean upgradeAppear(String uid) {
        // null证明新用户还没同步club等级返回true
        return !Optional.ofNullable(clubLevelMapper.getUpgradeConfirm(uid)).orElse(true);
    }


    @Override
    public Boolean bindRefeLevel(ClubLevelBindingParam param) {
        ClubLevel clubLevel = new ClubLevel();
        clubLevel.setUid(param.getUid());
        clubLevel.setClubLevel(param.getClubLevel());
        Range<Long> range = ClubConfig.getLevelRange(param.getClubLevel());
        clubLevel.setClubPoint(range.lowerEndpoint());
        clubLevel.setUpgradeRead(false);
        clubLevel.setValidUntil(TemporalUtil.offSetDaysStart(30));

        try {
            clubLevelMapper.insertSelective(clubLevel);

            ClubUpgradeModel model = new ClubUpgradeModel()
                    .setUid(param.getUid()).setOldLevel(-1).setUpdateOrigin("INVITE")
                    .setLatestLevel(param.getClubLevel()).setUpdateTime(TemporalUtil.getEpochMilli());
            executor.getEXECUTOR().execute(() -> clubPercepService.clubUpdateNofity(Collections.singletonList(model)));
        } catch (Exception e) {
            throw new BusinessException(e, ExceptionEnum.RECORD_EXISTS);
        }
        return true;
    }

    @Async
    @Override
    public void assetSynCallback() {
        Object signal = Optional.ofNullable(redis.getCacheObject(RedisKey.ASSET_SYNC_SIGNAL)).orElse(false);
        if (Objects.equals(signal, true)) {
            log.error("AssetHasBeenSynced For a while");
            return;
        }
        redis.setCacheObject(RedisKey.ASSET_SYNC_SIGNAL, true, Duration.of(18, ChronoUnit.HOURS));
        log.info("Callback getAndSyncloser");
        clubSyncTask.getAndSyncloser();
        log.info("Callback syncloser2tradingAsset");
        clubSyncTask.syncloser2tradingAsset();
    }

    @Override
    public ClubGiftVO getClubGift(String uid) {
        ClubLevel clubLevel = getClubLevel(uid);
        Integer currLevel = Optional.ofNullable(clubLevel).map(ClubLevel::getClubLevel).orElse(-1);
        log.info("#getClubGift#currLevel = {}", clubLevel);

        // 获取所有club等级奖励配置  本级礼券和对比等级的礼券
        List<ClubGiftConfig> allGifts = giftCompareService.getGiftConfigs(currLevel);
        // log.info("CurrentLevelComparation = {}", allGifts);
        // 筛选出coupon奖励 本级的 + 对比的等级的
        List<ClubGiftConfig> couponGifts = allGifts.stream().filter(val -> val.getGiftType().startsWith("coupon")).collect(Collectors.toList());
        // 获取礼券对比配置
        List<ClubGiftCompare> couponCompares = giftCompareService.getGiftCompares("coupon_");
        ClubGiftCompare currLevelCompare = couponCompares.stream().filter(Predicator.isEqual(ClubGiftCompare::getClubLevel, currLevel))
                                                         .findFirst().orElse(null);

        List<ClubPossessVO> clubPossess = couponServiceApi.getClubPossess(uid, couponGifts.stream().map(ClubGiftConfig::getGiftId).map(Long::parseLong).collect(Collectors.toList()));
        Map<Long, ClubPossessVO> idClubPossVo = Functions.toMap(clubPossess, ClubPossessVO::getCouponId, Function.identity());
        Set<String> confClubCouponIds = idClubPossVo.keySet().stream().map(String::valueOf).collect(Collectors.toSet());
        // 区分当前等级filter
        Comparator<ClubGiftVO.GiftSortBase> giftSorter =
                Comparator.comparingInt(ClubGiftVO.GiftSortBase::getSort)
                          .thenComparing(Comparator.comparing(ClubGiftVO.GiftSortBase::getSortUpdateTime, Comparator.nullsLast(Long::compareTo)).reversed());
        Predicate<ClubGiftConfig> currentFilter = Predicator.isEqual(ClubGiftConfig::getClubLevel, currLevel);

        Function<ClubGiftConfig, ClubGiftVO.ClubGiftCoupon> mapping = config -> buildCouponGift(config, currLevel, idClubPossVo);

        List<ClubGiftVO.ClubGiftCoupon> reached =
                couponGifts.stream().filter(currentFilter).map(mapping).filter(Predicate.isEqual(null).negate())
                           .sorted(giftSorter).collect(Collectors.toList());
        Predicate<ClubGiftConfig> compareFilter = conf -> conf.getClubLevel().equals(currLevelCompare.getClubLevelCp());
                // Predicator.isEqual(ClubGiftConfig::getClubLevel, currLevelCompare.getClubLevelCp());
        List<ClubGiftVO.ClubGiftCoupon> unReached = Objects.isNull(currLevelCompare) ? Collections.emptyList()
                : couponGifts.stream().filter(compareFilter).map(mapping)
                             .filter(Predicate.isEqual(null).negate()).sorted(giftSorter).collect(Collectors.toList());


        // boundary========================================================================================================

        // 获取all利率对比配置
        List<ClubGiftCompare> giftCompares = giftCompareService.getGiftCompares("earn_");

        // 筛选出所有理财奖励
        List<ClubGiftConfig> earnGifts = Functions.filter(allGifts, gift -> gift.getGiftType().startsWith("earn"));
        // 当前等级的奖励
        List<ClubGiftConfig> currLevelGifts = Functions.filter(earnGifts, Predicator.isEqual(ClubGiftConfig::getClubLevel, currLevel));
        List<String> currProdIds = Functions.toList(currLevelGifts, ClubGiftConfig::getGiftId);
        HashSet<String> currProdIdSet = new HashSet<>(currProdIds);


        List<ClubGiftCompare> currCompare = Functions.filter(giftCompares, Predicator.isEqual(ClubGiftCompare::getClubLevel, currLevel));
        if (CollectionUtil.isEmpty(currCompare)) {
            ClubGiftCompare placeHolder = new ClubGiftCompare().setClubLevel(-100).setClubLevelCp(-100);
            currCompare = Collections.singletonList(placeHolder);
        }
        ClubGiftCompare clubGiftCompare = currCompare.get(0);

        // 按等级group 每个等级仅有一个对比配置ClubGiftCompare
        // Map<Integer, ClubGiftCompare> levelCompareMap = giftCompares.stream().collect(Collectors.toMap(ClubGiftCompare::getClubLevel, Function.identity(), (v1, v2) -> v1));


        // 对比等级的奖励
        List<ClubGiftConfig> oppositeLevelGifts = Functions.filter(earnGifts, Predicator.isEqual(ClubGiftConfig::getClubLevel, clubGiftCompare.getClubLevelCp()));
        List<String> oppoProdIds = Functions.toList(oppositeLevelGifts, ClubGiftConfig::getGiftId);
        HashSet<String> oppoProdIdSet = new HashSet<>(oppoProdIds);
        if (CollectionUtil.isEmpty(currProdIds)
                && CollectionUtil.isEmpty(oppoProdIds)) {
            return new ClubGiftVO(reached, unReached, Collections.emptyList());
        }

        List<String> allProdIds = new ArrayList<>(CollectionUtil.union(currProdIds, oppoProdIds));
        if (CollectionUtil.isEmpty(allProdIds))
            return new ClubGiftVO(reached, unReached, Collections.emptyList());

        // 由上面筛选出来的earnGifts集合productId，获取ClubEarnProdVO集合
        List<ClubEarnProdVO> allEarnProds = earnServiceApi.getClubEarnProds(allProdIds);
        // group by productId
        Map<String, ClubEarnProdVO> currProdMap = Functions.toMap(
                Functions.filter(allEarnProds, prod -> currProdIdSet.contains(prod.getProductId())), ClubEarnProdVO::getProductId);
        Map<String, ClubEarnProdVO> oppoProdMap = Functions.toMap(
                Functions.filter(allEarnProds, prod -> oppoProdIdSet.contains(prod.getProductId())), ClubEarnProdVO::getProductId);

        if (CollectionUtil.isEmpty(currProdMap)
                && CollectionUtil.isEmpty(oppoProdMap)) {
            return new ClubGiftVO(reached, unReached, Collections.emptyList());
        }

        Function<ClubGiftConfig, ClubGiftVO.ClubGiftRate> mapping2 = config -> buildOwnedEarnGift(currLevel, config, currProdMap);
        if (CollectionUtil.isNotEmpty(currProdMap)
                && CollectionUtil.isEmpty(oppoProdMap)) {
            List<ClubGiftVO.ClubGiftRate> collect = currLevelGifts.stream().map(mapping2).filter(Predicator.nonNull()).sorted(giftSorter).collect(Collectors.toList());
            return new ClubGiftVO(reached, unReached, collect);
        }

        Function<ClubGiftConfig, ClubGiftVO.ClubGiftRate> mapping3 = config -> buildOppositeEarnGift(currLevel, config, oppoProdMap);
        if (CollectionUtil.isEmpty(currProdMap)
                && CollectionUtil.isNotEmpty(oppoProdMap)) {
            List<ClubGiftVO.ClubGiftRate> collect = oppositeLevelGifts.stream().map(mapping3).filter(Predicator.nonNull()).sorted(giftSorter).collect(Collectors.toList());
            return new ClubGiftVO(reached, unReached, collect);
        }

        Map<String, ClubEarnProdVO> allEarnProdMap = new HashMap<>(currProdMap);
        allEarnProdMap.putAll(oppoProdMap);
        Function<ClubGiftConfig, ClubGiftVO.ClubGiftRate> mapping1 = config -> buildEarnGift(config, currLevel, oppositeLevelGifts, clubGiftCompare, allEarnProdMap);
        List<ClubGiftVO.ClubGiftRate> collect = currLevelGifts.stream().map(mapping1).filter(Predicator.nonNull()).sorted(giftSorter).collect(Collectors.toList());
        return new ClubGiftVO(reached, unReached, collect);
    }

    private ClubGiftVO.ClubGiftCoupon buildCouponGift(
            ClubGiftConfig currGift, Integer currLevel, Map<Long, ClubPossessVO> idClubPossVo) {

        ClubPossessVO clubPossVo = idClubPossVo.get(Long.parseLong(currGift.getGiftId()));
        if (Objects.isNull(clubPossVo)) return null;

        ClubGiftVO.ClubGiftCoupon giftVo = new ClubGiftVO.ClubGiftCoupon();
        giftVo.setGiftType(clubPossVo.getCouponType());
        giftVo.setBindClubLevel(currGift.getClubLevel());
        giftVo.setGiftId(currGift.getGiftId());
        // 当前等级的券
        if (Objects.equals(currLevel, currGift.getClubLevel())) {
            giftVo.setValidUntil(clubPossVo.getExprTime());
            if (Objects.isNull(clubPossVo.getPossessId()))
                giftVo.setButtonStatus(clubPossVo.getCouponValid() ? ClubGiftStatusEnum.CURRENT_LEVEL.getStatus() : ClubGiftStatusEnum.DISABLED.getStatus());
            else
                giftVo.setButtonStatus(clubPossVo.getBusinessStage());
        }
        // 下个等级的券
        else {
            giftVo.setValidUntil(clubPossVo.getExprTime());
            giftVo.setButtonStatus(ClubGiftStatusEnum.NEXT_LEVEL.getStatus());
        }
        giftVo.setGiftWorth(clubPossVo.getWorth());
        giftVo.setGiftCoin(clubPossVo.getWorthCoin());
        giftVo.setSort(currGift.getSort());
        giftVo.setSortUpdateTime(TemporalUtil.toEpochMilli(currGift.getSortUpdateTime()));
        return giftVo;
    }

    /**
     * 根据currentLevel获取 List<ClubGiftConfig>，遍历当前等级的ClubGiftConfig获取对比的ClubGiftConfig，
     * 两个ClubGiftConfig获取理财产品配置进行对比组装
     */
    private ClubGiftVO.ClubGiftRate buildEarnGift(
            ClubGiftConfig currGift, Integer currLevel,
            List<ClubGiftConfig> oppoGifts,
            ClubGiftCompare giftCompare,
            Map<String, ClubEarnProdVO> idClubEarnVo) {

        // 获取当前等级的earn product
        ClubEarnProdVO currProdVo = idClubEarnVo.get(currGift.getGiftId());
        if (Objects.isNull(currProdVo)) return null;

        ClubGiftVO.ClubGiftRate giftVo = new ClubGiftVO.ClubGiftRate();
        giftVo.setCoin(currProdVo.getApplyCoin());
        giftVo.setDays(currProdVo.getDuration());
        giftVo.setGiftId(currProdVo.getProductId());
        giftVo.setIsEffect(currGift.getIsEffect() ? "Y" : "N");
        giftVo.setSort(currGift.getSort());
        giftVo.setClubLevel(currGift.getClubLevel());
        giftVo.setAnnualRate(currProdVo.getAnnualRate());
        giftVo.setSortUpdateTime(TemporalUtil.toEpochMilli(currGift.getSortUpdateTime()));
        // 10级没有对比信息，所以 < 10级的对比才有意义
        if (currLevel < ClubConfig.getLevelMax()) {
            // 获取当前等级的对比信息
            // ClubGiftCompare compare = giftCompare.get(currLevel);
            if (Objects.isNull(giftCompare)) return giftVo;

            // 获取对比等级的gifts
            // List<ClubGiftConfig> compareGifts = oppoGifts.get(giftCompare.getClubLevelCp());
            // 对比等级的gifts是数组但不一定都能对上，遍历对比币种和天数即可
            for (ClubGiftConfig comparedGift : oppoGifts) {
                ClubEarnProdVO compProdVo = idClubEarnVo.get(comparedGift.getGiftId());
                if (Objects.isNull(compProdVo) || !currProdVo.match(compProdVo)) continue;
                giftVo.setClubLevelCp(comparedGift.getClubLevel());
                giftVo.setAnnualRateCp(compProdVo.getAnnualRate());
                giftVo.setStandardRateCp(Optional.ofNullable(comparedGift.getStandardRate()).map(BigDecimal::toPlainString).orElse(null));
                giftVo.setExclusiveRateCp(Optional.ofNullable(comparedGift.getExclusiveRate()).map(BigDecimal::toPlainString).orElse(null));
                return giftVo;
            }
        }
        return giftVo;
    }


    /**
     * 当前等级的利率为空，只展示对比的利率
     *
     * @param currLevel
     * @param oppositGift
     * @param oppositeClubEarnVo
     * @return
     */
    private ClubGiftVO.ClubGiftRate buildOppositeEarnGift(
            Integer currLevel, ClubGiftConfig oppositGift, Map<String, ClubEarnProdVO> oppositeClubEarnVo) {

        ClubEarnProdVO compProdVo = oppositeClubEarnVo.get(oppositGift.getGiftId());
        if (Objects.isNull(compProdVo)) return null;
        ClubGiftVO.ClubGiftRate giftVo = new ClubGiftVO.ClubGiftRate();
        giftVo.setCoin(compProdVo.getApplyCoin());
        giftVo.setDays(compProdVo.getDuration());
        giftVo.setClubLevel(currLevel);
        giftVo.setGiftId(compProdVo.getProductId());
        giftVo.setIsEffect(oppositGift.getIsEffect() ? "Y" : "N");
        giftVo.setSort(oppositGift.getSort());
        giftVo.setSortUpdateTime(TemporalUtil.toEpochMilli(oppositGift.getSortUpdateTime()));

        giftVo.setClubLevelCp(oppositGift.getClubLevel());
        giftVo.setAnnualRateCp(compProdVo.getAnnualRate());
        giftVo.setStandardRateCp(Optional.ofNullable(oppositGift.getStandardRate()).map(BigDecimal::toPlainString).orElse(null));
        giftVo.setExclusiveRateCp(Optional.ofNullable(oppositGift.getExclusiveRate()).map(BigDecimal::toPlainString).orElse(null));
        return giftVo;
    }


    /**
     * 对比等级的利率为空，只展示当前的利率
     * @param currLevel
     * @param ownedGift
     * @param ownedClubEarnVo
     * @return
     */
    private ClubGiftVO.ClubGiftRate buildOwnedEarnGift(
            Integer currLevel, ClubGiftConfig ownedGift, Map<String, ClubEarnProdVO> ownedClubEarnVo) {

        ClubEarnProdVO currProdVo = ownedClubEarnVo.get(ownedGift.getGiftId());
        if (Objects.isNull(currProdVo)) return null;
        ClubGiftVO.ClubGiftRate giftVo = new ClubGiftVO.ClubGiftRate();
        giftVo.setCoin(currProdVo.getApplyCoin());
        giftVo.setDays(currProdVo.getDuration());
        giftVo.setClubLevel(currLevel);
        giftVo.setAnnualRate(currProdVo.getAnnualRate());
        giftVo.setGiftId(currProdVo.getProductId());
        giftVo.setIsEffect(ownedGift.getIsEffect() ? "Y" : "N");
        giftVo.setSort(ownedGift.getSort());
        giftVo.setSortUpdateTime(TemporalUtil.toEpochMilli(ownedGift.getSortUpdateTime()));
        return giftVo;
    }

    @Override
    public int updateByExampleSelective(ClubLevel clubLevel, Supplier<Example> supplier) {
        return clubLevelMapper.updateByExampleSelective(clubLevel, supplier.get());
    }
}

package com.loser.backend.club.task;


import com.github.pagehelper.PageHelper;
import com.loser.backend.club.ambermapper.ConfigClubBaseMapper;
import com.loser.backend.club.ambermapper.ConfigClubCompareMapper;
import com.loser.backend.club.ambermapper.UserMonthAverageAssetMapper;
import com.loser.backend.club.annotation.Traceable;
import com.loser.backend.club.config.ClubConfig;
import com.loser.backend.club.config.ExecutorConfiguration;
import com.loser.backend.club.domain.AverageMonthlyAsset;
import com.loser.backend.club.domain.ClubGiftCompare;
import com.loser.backend.club.domain.ClubGiftConfig;
import com.loser.backend.club.domain.ConfigClubBase;
import com.loser.backend.club.domain.ConfigClubCompare;
import com.loser.backend.club.domain.UserMonthAverageAsset;
import com.loser.backend.club.mapper.AverageMonthlyAssetMapper;
import com.loser.backend.club.mapper.ClubGiftCompareMapper;
import com.loser.backend.club.mapper.ClubGiftConfigMapper;
import com.loser.backend.club.service.IAvgMonthService;
import com.trading.backend.coupon.common.util.Functions;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;


/**
 * @author ~~ trading.s
 * @date 15:06 10/12/21
 */
@Service
@Slf4j
public class ClubSyncTask {

    @Autowired
    private ClubConfig taskConfig;
    @Autowired
    private IAvgMonthService avgMonthService;
    @Autowired
    private AverageMonthlyAssetMapper monthlyAssetMapper;
    @Autowired(required = false)
    private UserMonthAverageAssetMapper loserAssetMapper;
    @Autowired
    private ClubGiftConfigMapper giftConfigMapper;
    @Autowired(required = false)
    private ConfigClubBaseMapper clubBaseMapper;
    @Autowired
    private ClubGiftCompareMapper giftCompareMapper;
    @Autowired(required = false)
    private ConfigClubCompareMapper clubCompareMapper;
    @Autowired
    private ExecutorConfiguration executor;
    @Value("${spring.profiles.active}")
    private String springEnv;

    private final AtomicInteger COUNTER = new AtomicInteger(-1);

    private boolean isProd() {
        return StringUtils.equalsIgnoreCase(springEnv, "prod");
    }

    /**
     * 固定时刻执行
     */
    @XxlJob("syncClubPoint")
    // @Scheduled(fixedRate = 10000L/*zone = "Asia/Shanghai"*/)
    @Async
    @Traceable
    public void syncClubPoint() {
        log.info("#syncClubPoint");
        if (isProd()) {
            log.info("Getting old datasource...");
            boolean success = getAndSyncloser();
            log.info("XXLSyncClubPoint result = {}", success);
            return;
        }
        log.info("Getting present datasource...");
        boolean success = getAndSync();
        log.info("XXLSyncClubPoint result = {}", success);
    }


    @XxlJob("syncloser2tradingAsset")
    @Async
    @Traceable
    public void syncloser2tradingAsset() {
        Example example = new Example(UserMonthAverageAsset.class);
        long total = loserAssetMapper.selectCountByExample(example);
        if (total == 0) return;

        BiFunction<Integer, Integer, List<UserMonthAverageAsset>> biProducer =
                (page, pageSize) -> PageHelper.startPage(page, pageSize, "uid asc").doSelectPage(() -> loserAssetMapper.selectByExample(example));

        Function<List<AverageMonthlyAsset>, Integer> consumer = assets -> avgMonthService.batchUpsert(assets);
        executor.serial(total, biProducer, this::assetMapping, consumer);
    }

    private AverageMonthlyAsset assetMapping(UserMonthAverageAsset source) {
        return new AverageMonthlyAsset().setUid(source.getUid()).setAvgAsset(source.getAveAsset());
    }

    public boolean getAndSync() {
        Example example = new Example(AverageMonthlyAsset.class);
        long total = monthlyAssetMapper.selectCountByExample(example);
        if (total == 0) return false;

        BiFunction<Integer, Integer, List<AverageMonthlyAsset>> biProducer =
                (page, pageSize) -> PageHelper.startPage(page, pageSize, "uid asc").doSelectPage(() -> monthlyAssetMapper.selectByExample(example));

        Function<List<AverageMonthlyAsset>, Integer> consumer = assets -> avgMonthService.syncAssetSource(assets);
        executor.serial(total, biProducer, Function.identity(), consumer);
        return true;
    }

    public boolean getAndSyncloser() {
        Example example = new Example(UserMonthAverageAsset.class);
        long total = loserAssetMapper.selectCountByExample(example);
        if (total == 0) return false;

        BiFunction<Integer, Integer, List<UserMonthAverageAsset>> biProducer =
                (page, pageSize) -> PageHelper.startPage(page, pageSize, "uid asc").doSelectPage(() -> loserAssetMapper.selectByExample(example));

        Function<List<UserMonthAverageAsset>, Integer> consumer = assets -> avgMonthService.syncloserAssetSource(assets);
        executor.serial(total, biProducer, Function.identity(), consumer);
        return true;
    }

    @XxlJob("syncClubConfigAndCompare")
    public void syncClubConfigAndCompare() {
        List<ConfigClubBase> configClubBases = clubBaseMapper.selectAll();
        log.info("Origin configClubBases size = {}", configClubBases.size());
        List<ClubGiftConfig> giftConfigs = Functions.toList(configClubBases, ClubSyncTask::mapping);
        giftConfigMapper.insertList(giftConfigs);

        List<ConfigClubCompare> clubCompares = clubCompareMapper.selectAll();
        log.info("Origin clubCompares size = {}", clubCompares.size());
        List<ClubGiftCompare> giftCompares = Functions.toList(clubCompares, ClubSyncTask::mapping);
        giftCompareMapper.insertList(giftCompares);
    }

    private static ClubGiftConfig mapping(ConfigClubBase base) {
        ClubGiftConfig clubConfig = new ClubGiftConfig();
        clubConfig.setId(base.getId());
        clubConfig.setClubLevel(base.getClubLevel());
        clubConfig.setGiftId(String.valueOf(base.getGiftId()));
        clubConfig.setGiftType(base.getGiftType());
        clubConfig.setSort(base.getSort());
        clubConfig.setStandardRate(base.getStandardRate());
        clubConfig.setExclusiveRate(base.getExclusiveRate());
        clubConfig.setIsEffect(base.getIsEffect() == 1);
        clubConfig.setSortUpdateTime(base.getSortUpdateTime());
        clubConfig.setCreatedTime(base.getCreatedTime());
        clubConfig.setUpdatedTime(base.getUpdatedTime());
        clubConfig.setCreateBy(base.getCreateBy());
        clubConfig.setUpdateBy(base.getUpdateBy());
        return clubConfig;
    }

    private static ClubGiftCompare mapping(ConfigClubCompare clubCompare) {
        ClubGiftCompare giftCompare = new ClubGiftCompare();
        giftCompare.setId(clubCompare.getId());
        giftCompare.setClubLevel(clubCompare.getClubLevel());
        giftCompare.setClubLevelCp(clubCompare.getClubLevelCp());
        giftCompare.setGiftType(clubCompare.getGiftType());
        giftCompare.setIsEffect(clubCompare.getIsEffect() == 1);
        giftCompare.setCreatedTime(clubCompare.getCreatedTime());
        giftCompare.setUpdatedTime(clubCompare.getUpdatedTime());
        giftCompare.setCreateBy(clubCompare.getCreateBy());
        giftCompare.setUpdateBy(clubCompare.getUpdateBy());
        return giftCompare;
    }
}

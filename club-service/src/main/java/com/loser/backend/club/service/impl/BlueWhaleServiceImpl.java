package com.loser.backend.club.service.impl;


import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSONObject;
import com.loser.backend.club.common.enums.CouponTypeEnum;
import com.loser.backend.club.common.enums.CouponPhaseEnum;
import com.loser.backend.club.common.enums.MissionTypeEnum;
import com.loser.backend.club.constant.Constant;
import com.loser.backend.club.controller.response.BenefitCouponVO;
import com.loser.backend.club.controller.response.BenefitEarnVO;
import com.loser.backend.club.controller.response.BlueloserHomeVO;
import com.loser.backend.club.controller.response.BlueloserProfileVO;
import com.loser.backend.club.controller.response.loserBenefitIndexVO;
import com.loser.backend.club.controller.response.loserBenefitVO;
import com.loser.backend.club.controller.response.loserCustomBenefitVO;
import com.loser.backend.club.domain.ClubGiftConfig;
import com.loser.backend.club.domain.ClubLevel;
import com.loser.backend.club.domain.loserBenefit;
import com.loser.backend.club.domain.loserManager;
import com.loser.backend.club.domain.loserMission;
import com.loser.backend.club.exception.ExceptionEnum;
import com.loser.backend.club.exception.VisibleException;
import com.loser.backend.club.http.ContextHeader;
import com.loser.backend.club.mapper.loserMissionMapper;
import com.loser.backend.club.pojo.ManagerContact;
import com.loser.backend.club.pojo.loserClub;
import com.loser.backend.club.service.IBenefitInventoryService;
import com.loser.backend.club.service.IBenefitService;
import com.loser.backend.club.service.IBlueloserService;
import com.loser.backend.club.service.IClubService;
import com.loser.backend.club.service.ICouponServiceApi;
import com.loser.backend.club.service.IEarnServiceApi;
import com.loser.backend.club.service.IGiftCompareService;
import com.loser.backend.club.service.IManagerService;
import com.loser.backend.club.service.IReportService;
import com.loser.backend.club.util.Builder;
import com.loser.backend.club.util.ContextHolder;
import com.loser.backend.club.util.TemporalUtil;
import com.trading.backend.coupon.bo.BasalExportPossessBO;
import com.trading.backend.coupon.common.enums.CouponTypeEnum;
import com.trading.backend.coupon.common.util.Functions;
import com.trading.backend.coupon.http.response.club.ClubPossessVO;
import com.trading.backend.coupon.http.response.endpoint.FullScalePossessVO;
import com.trading.backend.coupon.util.NumberCriteria;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author ~~ trading.Shi
 * @date 20:54 02/15/22
 */
@Service @Slf4j
public class BlueloserServiceImpl implements IBlueloserService {

    @Autowired
    private IManagerService managerService;
    @Autowired
    private IClubService clubService;
    @Autowired
    private IGiftCompareService giftCompareService;
    @Autowired
    private ICouponServiceApi couponServiceApi;
    @Autowired
    private IBenefitInventoryService inventoryService;
    @Autowired
    private loserMissionMapper missionMapper;
    @Autowired
    private IEarnServiceApi earnService;
    @Autowired
    private IReportService reportService;
    @Autowired
    private IBenefitService benefitService;



    @Override
    public BlueloserHomeVO getBlueloserHome(String uid) {
        String language = ContextHolder.get().getClientLanguage();
        BlueloserHomeVO blueloserHome = new BlueloserHomeVO();

        loserManager mana = managerService.getManager(uid);
        Optional.ofNullable(mana)
                .ifPresent(manager -> {
                    // set manager
                    ManagerContact contact = JSONObject.toJavaObject(manager.getloserContact(), ManagerContact.class);
                    blueloserHome.setManagerName(manager.toLocalized("loserName"));
                    blueloserHome.setManagerResume(manager.toLocalized("loserProfile"));
                    blueloserHome.setManagerPhoto(manager.getloserPhoto());
                    blueloserHome.setManagerContact(contact);
                });

        // set benefits
        List<loserBenefit> allBenefits = benefitService.getAllBenefits();

        List<loserBenefitIndexVO> collect1 =
                allBenefits.stream().sorted(Comparator.comparingInt(loserBenefit::getPriority))
                           .map(bene -> new loserBenefitIndexVO(bene.getBenefitIndicator(), bene.toLocalized(bene::getBenefitName)))
                           .collect(Collectors.toList());

        blueloserHome.setBenefitIndexs(collect1);

        blueloserHome.setUpToApr(getHighestApr(uid));

        blueloserHome.setPeriods(reportService.getPeriods());
        List<FullScalePossessVO> benefitCoupons = getBenefitCoupons(uid);
        long count = benefitCoupons.stream().filter(bc -> Objects.equals(bc.getBusinessStage(), 0)).count();
        if (count > 0) {
            blueloserHome.setCoupons((int) count);
            blueloserHome.setCouponStatus("TO_USE");
        }
        else {
            long count1 = benefitCoupons.stream().filter(bc -> Objects.equals(bc.getBusinessStage(), -1)).count();
            blueloserHome.setCoupons((int) count1);
            blueloserHome.setCouponStatus("TO_REDEEM");
        }

        blueloserHome.setBenefitQuantity(inventoryService.getAvailableCount(uid));

        // set CustomBenefits
        List<loserMission> missions = missionMapper.getByMissionType(uid, MissionTypeEnum.CUSTOM.getType());
        blueloserHome.setCustomBenefits(Functions.toList(missions, loserCustomBenefitVO::fromMission));
        return blueloserHome;
    }

    @Override
    public boolean pageAccess(ContextHeader header) {
        String platform = header.getClientPlatform();
        String uid = header.getXGwUser();
        if (StringUtils.equalsAny(platform, Constant.PLATFORM_WEB, Constant.PLATFORM_H5)) {
            int i = clubService.updateByExampleSelective(
                    new ClubLevel().setFirstAccess(false).setHomeFirstAccess(false),
                    () -> {
                        Example example = new Example(ClubLevel.class);
                        example.createCriteria().andEqualTo("uid", uid);
                        return example;
                    }
            );
            return i == 1;
        }
        else if (StringUtils.equalsAny(platform, Constant.PLATFORM_ANDROID, Constant.PLATFORM_IOS)) {
            int i = clubService.updateByExampleSelective(
                    new ClubLevel().setHomeFirstAccess(false),
                    () -> {
                        Example example = new Example(ClubLevel.class);
                        example.createCriteria().andEqualTo("uid", uid);
                        return example;
                    }
            );
            return i == 1;
        }
        throw new VisibleException(ExceptionEnum.HTTP_HEADER_UNVALID, "client_platform", platform);
    }

    @Override
    public List<loserBenefitVO> getUniqueBenefit(String uid, String indicator) {
        return null;
    }

    final static private Map<Integer, Integer> COUPON_TYPE_SORT;

    static {
        COUPON_TYPE_SORT = new HashMap<>(8);
        COUPON_TYPE_SORT.put(0, 1);
        COUPON_TYPE_SORT.put(5, 2);
        COUPON_TYPE_SORT.put(1, 3);
    }

    @Override
    public List<FullScalePossessVO> getBenefitCoupons(String uid) {
        ClubLevel clubLevel = clubService.getClubLevel(uid);
        log.info("getBenefitCoupons, club_level = {}", clubLevel);

        Integer loserLevel = Optional.ofNullable(clubLevel).map(ClubLevel::getloserLevel).orElse(-1);
        if (loserLevel < 1) return Collections.emptyList();

        List<ClubGiftConfig> loserCoupons = giftCompareService.getConfigs("bwc_coupon", loserLevel);
        // if (CollUtil.isEmpty(loserCoupons)) return Collections.emptyList();

        List<FullScalePossessVO> coupons = couponServiceApi.getPeriodFullScalePossess(uid, Functions.toList(loserCoupons, ClubGiftConfig::getGiftId, Long::parseLong));

        Comparator<BasalExportPossessBO> comparator =
                Comparator.comparing(BasalExportPossessBO::getCouponType, Comparator.comparingInt(COUPON_TYPE_SORT::get))
                          .thenComparing(Comparator.comparing(BasalExportPossessBO::getWorth).reversed())
                          .thenComparing(BasalExportPossessBO::getAvailableEnd, Comparator.nullsLast(Long::compareTo))
                          .thenComparing(Comparator.comparing(BasalExportPossessBO::getBusinessStage, Comparator.nullsFirst(Integer::compareTo)).reversed());
        coupons.sort(comparator);
        return coupons;
    }


    @Override
    public List<BenefitEarnVO> getEarnBenefits(String uid) {
        List<BenefitEarnVO> bwcEarnProds = earnService.getDirectedEarnProds(uid);
        Map<String, BenefitEarnVO> map1 = Functions.toMap(bwcEarnProds, BenefitEarnVO::getProductId);

        Integer loserLevel = Optional.ofNullable(clubService.getClubLevel(uid)).map(ClubLevel::getloserLevel).orElse(null);
        List<BenefitEarnVO> clubAndBlueEarns = giftCompareService.getClubAndBlueEarns(loserLevel);
        Map<String, BenefitEarnVO> map2 = Functions.toMap(clubAndBlueEarns, BenefitEarnVO::getProductId);
        map1.putAll(map2);
        return new ArrayList<>(map1.values());
    }

    @Override
    public BlueloserProfileVO getloserProfile(String uid) {
        loserClub loserClub = clubService.getloserClub(uid);
        if (Objects.isNull(loserClub))
            return BlueloserProfileVO.empty(uid);
        ClubLevel clubLevel = loserClub.getClubLevel();

        return new BlueloserProfileVO(
                uid, Optional.ofNullable(clubLevel).map(ClubLevel::getloserLevel).orElse(-1),
                Optional.ofNullable(loserClub.getAvgAsset()).map(NumberCriteria::routineScale).map(NumberCriteria::stripTrailing).orElse("0.00"),
                Optional.ofNullable(clubLevel).map(ClubLevel::getloserUntil).map(TemporalUtil::toEpochMilli).orElse(null)
        );
    }


    @Override
    public String getHighestApr(String uid) {
        List<BenefitEarnVO> earnBenefits = getEarnBenefits(uid);
        return earnBenefits.stream().map(vo -> NumberUtil.add(vo.getBasicRate(), vo.getExtraRate()))
                .max(Comparator.comparing(Function.identity())).orElse(BigDecimal.ZERO).toPlainString();
    }

    private BenefitCouponVO buildBenefitCoupon(ClubPossessVO possessVo) {
        Builder<BenefitCouponVO> builder = Builder.of(BenefitCouponVO::new);
        builder.with(BenefitCouponVO::setCouponTitle, possessVo.getCouponTitle())
               .with(BenefitCouponVO::setCouponDescr, possessVo.getCouponDescr())
               .with(BenefitCouponVO::setCouponPhase, getCouponPhase(possessVo))
               .with(BenefitCouponVO::setCouponType, getCouponType(possessVo))
               .with(BenefitCouponVO::setWorth, possessVo.getWorth())
               .with(BenefitCouponVO::setCouponCoin, possessVo.getWorthCoin())
               .with(BenefitCouponVO::setValidUntil, String.valueOf(possessVo.getExprTime()));
        return builder.build();
    }

    private String getCouponPhase(ClubPossessVO possessVo) {
        if (Objects.isNull(possessVo.getPossessId())) {
            return CouponPhaseEnum.REDEEM.getPhase();
        }
        return CouponPhaseEnum.getPhase(possessVo.getBusinessStage());
    }

    private String getCouponType(ClubPossessVO possessVo) {
        if (CouponTypeEnum.INTEREST_TYPE == CouponTypeEnum.getByCode(possessVo.getCouponType())) {
            return CouponTypeEnum.INTEREST.name();
        }
        else if (CouponTypeEnum.DEDUCTION_TYPE == CouponTypeEnum.getByCode(possessVo.getCouponType())) {
            if (Objects.equals(possessVo.getDeductWay(), 1)) {
                return CouponTypeEnum.DEDUCTION_1.name();
            }
            return CouponTypeEnum.DEDUCTION_2.name();
        }
        else if (CouponTypeEnum.CASHRETURN_TYPE == CouponTypeEnum.getByCode(possessVo.getCouponType())) {
            return CouponTypeEnum.CASHRETURN.name();
        }
        return null;
    }
}

package com.loser.backend.club.service.impl;


import com.loser.backend.club.config.ClubConfig;
import com.loser.backend.club.controller.response.BenefitEarnVO;
import com.loser.backend.club.controller.response.ClubEarnProdVO;
import com.loser.backend.club.domain.ClubGiftCompare;
import com.loser.backend.club.domain.ClubGiftConfig;
import com.loser.backend.club.mapper.ClubGiftCompareMapper;
import com.loser.backend.club.mapper.ClubGiftConfigMapper;
import com.loser.backend.club.service.IEarnServiceApi;
import com.loser.backend.club.service.IGiftCompareService;
import com.trading.backend.coupon.common.util.Functions;
import com.trading.backend.coupon.common.util.Predicator;
import com.trading.backend.coupon.util.NumberCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service @Slf4j
public class GiftCompareServiceImpl implements IGiftCompareService {

    @Autowired
    private ClubGiftCompareMapper giftCompareMapper;
    @Autowired
    private ClubGiftConfigMapper giftConfigMapper;
    @Autowired
    private IEarnServiceApi earnService;
    // @Autowired
    // private RedisService redis;

    @Override
    public List<ClubGiftConfig> getGiftConfigs(Integer currentLevel) {
        return giftConfigMapper.getAllGiftConfigs(currentLevel);
    }

    @Override
    public List<ClubGiftCompare> getGiftCompares(String giftType) {
        Example example = new Example(ClubGiftCompare.class);
        example.createCriteria().andLike("giftType", giftType + "%").andEqualTo("isEffect", true);
        return giftCompareMapper.selectByExample(example);
    }

    @Override
    public List<ClubGiftConfig> getConfigs(String giftType, Integer level) {
        Example example = new Example(ClubGiftConfig.class);
        example.createCriteria()
               .andEqualTo("clubLevel", level)
               .andEqualTo("giftType", giftType)
               .andEqualTo("isEffect", true);
        return giftConfigMapper.selectByExample(example);
    }

    @Override
    public List<BenefitEarnVO> getClubAndBlueEarns(Integer loserLevel) {
        if (loserLevel == null || loserLevel < 1) {
            return Collections.emptyList();
        }
        Integer benefitLevel = ClubConfig.getBenefitLevel(loserLevel);

        Example example = new Example(ClubGiftConfig.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("giftType", "bwc_earn")
                .andEqualTo("clubLevel", loserLevel)
                .andEqualTo("isEffect", true);
        example.or().andEqualTo("giftType", "earn_0000")
               .andEqualTo("clubLevel", benefitLevel)
               .andEqualTo("isEffect", true);
        // example.selectProperties("giftId");

        List<ClubGiftConfig> clubGiftConfigs = giftConfigMapper.selectByExample(example);

        List<String> collect = clubGiftConfigs.stream().map(ClubGiftConfig::getGiftId).distinct().collect(Collectors.toList());
        log.info("Owned earn prodIds = {}", collect);
        List<ClubEarnProdVO> clubEarnProds = earnService.getClubEarnProds(collect);
        Map<String, ClubEarnProdVO> earnProdVoMap = Functions.toMap(clubEarnProds, ClubEarnProdVO::getProductId);
        Set<String> requestedIds = earnProdVoMap.keySet();
        log.info("Requested earn prodIds = {}", requestedIds);

        return clubGiftConfigs
                .stream()
                .filter(conf -> requestedIds.contains(conf.getGiftId())).map(conf -> {
                    ClubEarnProdVO prodVo = earnProdVoMap.get(conf.getGiftId());
                    return new BenefitEarnVO(
                            NumberCriteria.stripTrailing(conf.getStandardRate()),
                            NumberCriteria.stripTrailing(conf.getExclusiveRate()),
                            conf.getGiftId(), prodVo.getDuration(), prodVo.getApplyCoin()
                    );
                })
                .collect(Collectors.toList());

    }

}

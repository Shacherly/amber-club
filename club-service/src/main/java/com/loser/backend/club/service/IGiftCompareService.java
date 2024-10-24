package com.loser.backend.club.service;


import com.loser.backend.club.controller.response.BenefitEarnVO;
import com.loser.backend.club.domain.ClubGiftCompare;
import com.loser.backend.club.domain.ClubGiftConfig;

import java.util.List;

public interface IGiftCompareService {

    List<ClubGiftConfig> getGiftConfigs(Integer currentLevel);

    List<ClubGiftCompare> getGiftCompares(String giftType);

    List<ClubGiftConfig> getConfigs(String giftType, Integer level);

    List<BenefitEarnVO> getClubAndBlueEarns(Integer loserLevel);

}

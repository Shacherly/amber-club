package com.loser.backend.club.service;

import com.loser.backend.club.controller.response.BenefitEarnVO;
import com.loser.backend.club.controller.response.ClubEarnProdVO;

import java.util.List;

public interface IEarnServiceApi {

    List<ClubEarnProdVO> getClubEarnProds(List<String> prodIds);


    List<BenefitEarnVO> getDirectedEarnProds(String uid);

    List<BenefitEarnVO> getBWCEarnProds(List<String> prodIds);

    String getHighestApr(String uid);

}

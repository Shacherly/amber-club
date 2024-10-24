package com.loser.backend.club.service;

import com.loser.backend.club.controller.response.BenefitEarnVO;
import com.loser.backend.club.controller.response.BlueloserHomeVO;
import com.loser.backend.club.controller.response.BlueloserProfileVO;
import com.loser.backend.club.controller.response.loserBenefitVO;
import com.loser.backend.club.http.ContextHeader;
import com.trading.backend.coupon.http.response.endpoint.FullScalePossessVO;

import java.util.List;

public interface IBlueloserService {


    BlueloserHomeVO getBlueloserHome(String uid);

    boolean pageAccess(ContextHeader header);

    List<loserBenefitVO> getUniqueBenefit(String uid, String indicator);

    List<FullScalePossessVO> getBenefitCoupons(String uid);

    List<BenefitEarnVO> getEarnBenefits(String uid);

    BlueloserProfileVO getloserProfile(String uid);

    String getHighestApr(String uid);

}

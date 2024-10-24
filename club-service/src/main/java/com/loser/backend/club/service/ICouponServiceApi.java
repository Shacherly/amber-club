package com.loser.backend.club.service;

import com.trading.backend.coupon.http.response.club.ClubPossessVO;
import com.trading.backend.coupon.http.response.endpoint.FullScalePossessVO;

import java.util.List;

public interface ICouponServiceApi {


    List<ClubPossessVO> getClubPossess(String uid, List<Long> couponIds);

    List<FullScalePossessVO> getPeriodFullScalePossess(String uid, List<Long> couponIds);
}

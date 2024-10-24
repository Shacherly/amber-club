package com.loser.backend.club.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.TypeReference;
import com.loser.backend.club.api.http.Response;
import com.loser.backend.club.config.RemoteServerProperty;
import com.loser.backend.club.service.ICouponServiceApi;
import com.loser.backend.club.util.RemoteCaller;
import com.trading.backend.coupon.http.request.FullScaleCouponParam;
import com.trading.backend.coupon.http.request.club.ClubPossessParam;
import com.trading.backend.coupon.http.response.club.ClubPossessVO;
import com.trading.backend.coupon.http.response.endpoint.FullScalePossessVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ~~ trading.s
 * @date 10:55 10/13/21
 */
@Slf4j
@Service
public class CouponServiceApiImpl implements ICouponServiceApi {


    @Autowired
    private RemoteCaller caller;
    @Autowired
    private RemoteServerProperty serverProperty;


    @Override
    public List<ClubPossessVO> getClubPossess(String uid, List<Long> couponIds) {
        if (CollectionUtil.isEmpty(couponIds)) return Collections.emptyList();
        String url = serverProperty.getDomain() + serverProperty.getCouponServer().getClubPossess();
        ClubPossessParam body = new ClubPossessParam(uid, couponIds);
        Response<List<ClubPossessVO>> response = caller.postForEntity(url, body, null, new TypeReference<Response<List<ClubPossessVO>>>() {});
        return response.getData();
    }

    @Override
    public List<FullScalePossessVO> getPeriodFullScalePossess(String uid, List<Long> couponIds) {
        // if (CollectionUtil.isEmpty(couponIds)) return Collections.emptyList();

        String url = serverProperty.getDomain() + serverProperty.getCouponServer().getPeriodPossess();
        FullScaleCouponParam body = new FullScaleCouponParam();
        body.setUid(uid); body.setCouponIds(couponIds);
        Response<List<FullScalePossessVO>> response = caller.postForEntity(url, body, null, new TypeReference<Response<List<FullScalePossessVO>>>() {});
        return response.getData().stream().peek(vo -> {if (vo.getBusinessStage() == null) vo.setBusinessStage(-1);}).collect(Collectors.toList());
    }
}

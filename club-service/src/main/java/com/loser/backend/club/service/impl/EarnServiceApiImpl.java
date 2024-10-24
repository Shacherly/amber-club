package com.loser.backend.club.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.loser.backend.club.config.RemoteServerProperty;
import com.loser.backend.club.controller.response.BenefitEarnVO;
import com.loser.backend.club.controller.response.ClubEarnProdVO;
import com.loser.backend.club.service.IEarnServiceApi;
import com.loser.backend.club.util.RemoteCaller;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author ~~ trading.s
 * @date 11:56 10/17/21
 */
@Slf4j
@Service
public class EarnServiceApiImpl implements IEarnServiceApi {

    @Autowired
    private RemoteCaller remoteCaller;
    @Autowired
    private RemoteServerProperty serverProperty;


    @Override
    public List<ClubEarnProdVO> getClubEarnProds(List<String> prodIds) {

        if (CollectionUtil.isEmpty(prodIds)) return Collections.emptyList();
        List<ClubEarnProdVO> result = new ArrayList<>();
        String url = serverProperty.getDomain() + serverProperty.getEarnServer().getEarnProductList();
        Map<String, Object> body = new HashMap<>();
        body.put("product_ids", prodIds);
        body.put("page", 1);
        body.put("page_size", 1000);

        List<JSONObject> maps = Lists.newArrayList();
        try {
            maps.addAll(remoteCaller.post4JSONObject(url, body, null));
        } catch (Exception e) {
            log.error("getClubEarnProds error, {}", e.getMessage(), e);
        }
        for (JSONObject map : maps) {
            ClubEarnProdVO vo = new ClubEarnProdVO();
            vo.setApplyCoin(map.getString("coin"));
            vo.setDuration(map.getIntValue("maturity"));
            vo.setAnnualRate(map.getString("apr"));
            vo.setProductId(map.getString("product_id"));
            result.add(vo);
        }
        return result;
    }

    @Override
    public List<BenefitEarnVO> getDirectedEarnProds(String uid) {

        if (StringUtils.isBlank(uid)) return Collections.emptyList();
        String url = serverProperty.getDomain() + serverProperty.getEarnServer().getBwcEarnProducts();
        Map<String, Object> body = new HashMap<>();
        body.put("user_id", uid);
        // "id": "cc4e4eba8063a03efb005bnb-9",
        // "coin": "BNB",
        // "duration": 10,
        // "original_interest_rate": "0.1000000000000000",
        // "event_interest_rate": "0.0100000000000000"
        List<JSONObject> maps = Lists.newArrayList();
        try {
            maps.addAll(remoteCaller.post4JSONObject(url, body, null));
        } catch (Exception e) {
            log.error("getDirectedEarnProds error, {}", e.getMessage(), e);
        }
        return maps.parallelStream().map(this::bwcEarnMapping).collect(Collectors.toList());
    }

    @Override
    public String getHighestApr(String uid) {
        List<BenefitEarnVO> bwcEarnProds = getDirectedEarnProds(uid);
        return bwcEarnProds.stream().map(vo -> NumberUtil.add(vo.getBasicRate(), vo.getExtraRate()))
                .max(Comparator.comparing(Function.identity())).orElse(BigDecimal.ZERO).toPlainString();
    }

    @Override
    public List<BenefitEarnVO> getBWCEarnProds(List<String> prodIds) {
        if (CollectionUtil.isEmpty(prodIds)) return Collections.emptyList();

        String url = serverProperty.getDomain() + serverProperty.getEarnServer().getEarnProductList();
        Map<String, Object> body = new HashMap<>();
        body.put("product_ids", prodIds);
        body.put("page", 1);
        body.put("page_size", 1000);

        List<JSONObject> maps = Lists.newArrayList();
        try {
            maps.addAll(remoteCaller.post4JSONObject(url, body, null));
        } catch (Exception e) {
            log.error("getBWCEarnProdsWithId error, {}", e.getMessage(), e);
        }
        return maps.parallelStream().map(this::bwcEarnMapping).collect(Collectors.toList());
    }


    public BenefitEarnVO bwcEarnMapping(JSONObject obj) {
        BenefitEarnVO vo = new BenefitEarnVO();
        vo.setBasicRate(obj.getString("original_interest_rate"));
        vo.setExtraRate(obj.getString("event_interest_rate"));
        vo.setProductId(obj.getString("id"));
        vo.setDuration(obj.getInteger("duration"));
        vo.setApplyCoin(StringUtils.upperCase(obj.getString("coin")));
        return vo;
    }

    public BenefitEarnVO clubEarnMapping(JSONObject obj) {
        BenefitEarnVO vo = new BenefitEarnVO();
        vo.setBasicRate(obj.getString("original_interest_rate"));
        vo.setExtraRate(obj.getString("event_interest_rate"));
        vo.setProductId(obj.getString("id"));
        vo.setDuration(obj.getInteger("duration"));
        vo.setApplyCoin(StringUtils.upperCase(obj.getString("coin")));
        return vo;
    }
}


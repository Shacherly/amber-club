package com.loser.backend.club;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.loser.backend.club.api.http.Response;
import com.loser.backend.club.config.RemoteServerProperty;
import com.loser.backend.club.controller.response.ClubEarnProdVO;
import com.loser.backend.club.domain.AverageMonthlyAsset;
import com.loser.backend.club.service.IAvgMonthService;
import com.loser.backend.club.service.ICouponServiceApi;
import com.loser.backend.club.service.IEarnServiceApi;
import com.loser.backend.club.task.ClubSyncTask;
import com.loser.backend.club.util.RemoteCaller;
import com.trading.backend.coupon.http.request.CouponDetailParam;
import com.trading.backend.coupon.http.response.club.ClubPossessVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@ActiveProfiles("localsit")
@SpringBootTest(classes = ClubServiceApplication.class)
public class ClubServiceApplicationTests {

    @Autowired
    RemoteCaller caller;
    @Autowired
    private ClubSyncTask clubSyncTask;
    @Autowired
    private ICouponServiceApi couponServiceApi;
    @Autowired
    private RemoteServerProperty serverProperty;
    @Autowired
    private IEarnServiceApi earnServiceApi;
    @Autowired
    private IAvgMonthService avgMonthService;

    @Test
    public void test() {
        String url = "http://localhost:10000/coupon/internal/v1/possess/detail";
        CouponDetailParam body = new CouponDetailParam(Lists.newArrayList(222L, 170L));
        caller.postForEntity(url, body, null, new TypeReference<Response<List<ClubPossessVO>>>() {});
    }

    @Test
    public void test1() {
        // clubSyncTask.syncloserData();
        clubSyncTask.getAndSync();
    }

    @Test
    public void test2() {
        List<ClubPossessVO> clubPossess = couponServiceApi.getClubPossess("123", Lists.newArrayList(1L));
    }

    @Test
    public void testYamlConfig() {
        RemoteServerProperty.CouponServer couponServer = serverProperty.getCouponServer();
        System.out.println();
    }

    @Test
    public void testEarnAPi() {
        List<ClubEarnProdVO> list = earnServiceApi.getClubEarnProds(Lists.newArrayList("e62f86b5e0731e737a4e4c0d", "94016e85c72fd86a3fc8051a"));
        System.out.println();
    }

    @Test
    public void testCLubTransfer() {
        clubSyncTask.syncClubConfigAndCompare();
    }

    @Test
    public void testBatchInsertAsset() {
        ArrayList<AverageMonthlyAsset> assets = Lists.newArrayList(new AverageMonthlyAsset().setUid("zzzzzzzz").setAvgAsset(BigDecimal.TEN));
        avgMonthService.batchUpsert(assets);

    }
}

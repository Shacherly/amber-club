package com.loser.backend.club.controller;


import com.loser.backend.club.controller.response.BriefClubVO;
import com.loser.backend.club.api.ClubServiceApi;
import com.loser.backend.club.api.http.Response;
import com.loser.backend.club.api.http.request.InternalMultiParamUid;
import com.loser.backend.club.api.http.request.InternalParamUid;
import com.loser.backend.club.controller.request.ClubLevelBindingParam;
import com.loser.backend.club.service.IClubService;
import com.loser.backend.club.util.BeanCopier;
import com.loser.backend.club.util.ContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author ~~ trading.s
 * @date 12:01 09/27/21
 */
@Slf4j
@Api(tags = "Club-Api-Internal（模块间调用）")
@RestController
@RequestMapping("/internal/v1")
public class InternalClubController implements ClubServiceApi {

    @Autowired
    private IClubService clubService;


    @PostMapping("/brief/me")
    @ApiOperation(value = "查询用户club等级信息")
    public Response<com.loser.backend.club.api.http.response.BriefClubVO> getPersonalClub(@Validated @RequestBody InternalParamUid param) {

        BriefClubVO briefClub = clubService.getBriefClub(param.getUid());
        return Response.ok(BeanCopier.copy(briefClub, com.loser.backend.club.api.http.response.BriefClubVO::new));
    }

    @PostMapping("/brief/multi")
    @ApiOperation(value = "批量查询用户club等级信息")
    public Response<List<com.loser.backend.club.api.http.response.BriefClubVO>> getMultiClubs(@Validated @RequestBody InternalMultiParamUid param) {

        List<BriefClubVO> briefClubs = clubService.getBriefClubs(param.getUids());
        return Response.ok(BeanCopier.copy(briefClubs, com.loser.backend.club.api.http.response.BriefClubVO::new));
    }

    @GetMapping("/upgrade/appear")
    @ApiOperation(value = "升级消息是否应该弹出")
    public Response<Boolean> upgradeAppear(@Validated InternalParamUid request) {

        boolean appear = clubService.upgradeAppear(request.getUid());
        return Response.ok(appear);
    }

    @PostMapping("/referral/binding")
    @ApiOperation(value = "referral邀请新用户绑定club等级")
    public Response<Boolean> bindRefeLevel(@Validated @RequestBody ClubLevelBindingParam param) {

        Boolean aBoolean = clubService.bindRefeLevel(param);
        return Response.ok(aBoolean);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "origin_channel", required = true, dataTypeClass = String.class),
    })
    @ApiOperation(value = "DA 资产同步回调")
    @PostMapping("/asset-sync/signal")
    public Response<String> assetSynced() {
        String channel = ContextHolder.get().getOriginChannel();
        if (!StringUtils.equals(channel, "DATA_ANALYSIS")) {
            return Response.fail("Unexpected origin channel of header!");
        }
        clubService.assetSynCallback();
        return Response.ok();
    }

}

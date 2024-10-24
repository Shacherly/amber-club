package com.loser.backend.club.controller;


import com.loser.backend.club.annotation.Authentication;
import com.loser.backend.club.api.http.Response;
import com.loser.backend.club.api.http.request.ExternalHeaderUid;
import com.loser.backend.club.controller.response.BriefClubVO;
import com.loser.backend.club.controller.response.ClubGiftVO;
import com.loser.backend.club.http.ContextHeader;
import com.loser.backend.club.service.IClubService;
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


/**
 * @author ~~ trading.s
 * @date 12:01 09/27/21
 */

@Api(tags = "Club-Api（trading调用）")
@Slf4j
@RestController
@RequestMapping("/v1")
public class tradingClubController {


    @Autowired
    private IClubService clubService;


    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "x-gw-user", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(paramType = "header", name = "access_token", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(paramType = "header", name = "refresh_token", required = true, dataTypeClass = String.class),
    })
    @Authentication
    @GetMapping("/brief/me")
    @ApiOperation(value = "个人中心club信息查询")
    public Response<BriefClubVO> getPersonalClub(@Validated ExternalHeaderUid param) {

        BriefClubVO briefClub = clubService.getBriefClub(param.getHeaderUid());
        return Response.ok(briefClub);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "x-gw-user", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(paramType = "header", name = "access_token", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(paramType = "header", name = "refresh_token", required = true, dataTypeClass = String.class),
    })
    @Authentication
    @GetMapping("/gift")
    @ApiOperation(value = "club会员奖励查询")
    public Response<ClubGiftVO> getClubCouponGift(@Validated ExternalHeaderUid param) {

        if (ContextHeader.isMockEnv())
            return Response.ok(ClubGiftVO.mock());
        ClubGiftVO clubGift = clubService.getClubGift(param.getHeaderUid());
        return Response.ok(clubGift);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "x-gw-user", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(paramType = "header", name = "access_token", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(paramType = "header", name = "refresh_token", required = true, dataTypeClass = String.class),
    })
    @Authentication
    @PostMapping("/upgrade/read")
    @ApiOperation(value = "升级消息确认")
    public Response<Integer> upgradeRead(@RequestBody(required = false) @Validated ExternalHeaderUid request) {

        boolean b = clubService.upgradeRead(request.getHeaderUid());
        return Response.ok(1);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "x-gw-user", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(paramType = "header", name = "access_token", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(paramType = "header", name = "refresh_token", required = true, dataTypeClass = String.class),
    })
    @Authentication
    @GetMapping("/upgrade/appear")
    @ApiOperation(value = "升级消息是否应该弹出")
    public Response<Boolean> upgradeAppear(@Validated ExternalHeaderUid request) {

        boolean appear = clubService.upgradeAppear(request.getHeaderUid());
        return Response.ok(appear);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "x-gw-user", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(paramType = "header", name = "access_token", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(paramType = "header", name = "refresh_token", required = true, dataTypeClass = String.class),
    })
    @Authentication
    @GetMapping("/member/valid")
    @ApiOperation(value = "是否club有效会员等级（1~10级）", hidden = true)
    public Response<Boolean> isEffectMemeber(@Validated ExternalHeaderUid request) {

        BriefClubVO briefClub = clubService.getBriefClub(request.getHeaderUid());
        return Response.ok(briefClub.getClubLevel() > 0);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "x-gw-user", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(paramType = "header", name = "access_token", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(paramType = "header", name = "refresh_token", required = true, dataTypeClass = String.class),
    })
    @Authentication
    @GetMapping("/claimed/whether")
    @ApiOperation(value = "是否使用过会员权益", hidden = true)
    public Response<Boolean> hasClaimed(@Validated ExternalHeaderUid request) {

        return Response.ok(true);
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

package com.loser.backend.club.controller;


import com.loser.backend.club.api.http.Response;
import com.loser.backend.club.common.http.PageResult;
import com.loser.backend.club.config.GlobalConfig;
import com.loser.backend.club.controller.request.whale.BenefitModuleParam;
import com.loser.backend.club.controller.request.whale.ReportParam;
import com.loser.backend.club.controller.request.whale.ReportReadParam;
import com.loser.backend.club.controller.request.whale.ReportRecommendParam;
import com.trading.backend.club.controller.response.*;
import com.loser.backend.club.service.IBenefitInventoryService;
import com.loser.backend.club.service.IBlueloserService;
import com.loser.backend.club.service.IManagerService;
import com.loser.backend.club.service.IMissionService;
import com.loser.backend.club.service.IReportService;
import com.loser.backend.club.util.ContextHolder;
import com.trading.backend.coupon.http.response.endpoint.FullScalePossessVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ~~ trading.s
 * @date 12:01 09/27/21
 */
@ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "x-gw-user", required = false, dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "header", name = "access_token", required = true, dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "header", name = "refresh_token", required = true, dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "header", name = "client_language", required = true, dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "header", name = "origin_channel", required = true, dataTypeClass = String.class),
})
@Api(tags = "BWC-Api")
@Slf4j
@RestController
@RequestMapping("/v1/bule-loser")
public class BuleloserController {


    @Autowired
    private IBlueloserService loserService;
    @Autowired
    private IMissionService missionService;
    @Autowired
    private IBenefitInventoryService inventoryService;
    @Autowired
    private IReportService reportService;
    @Autowired
    private IManagerService managerService;
    @Autowired
    private GlobalConfig globalConfig;

    @GetMapping("/profile")
    @ApiOperation(value = "bule-loser-信息")
    public Response<BlueloserProfileVO> getBlueloser() {
        BlueloserProfileVO profile = loserService.getloserProfile(ContextHolder.get().getXGwUser());
        return Response.ok(profile);
    }


    @GetMapping("/home")
    @ApiOperation(value = "bule-loser-club首页")
    public Response<BlueloserHomeVO> showBenefitHome() {

        BlueloserHomeVO blueloserHome = loserService.getBlueloserHome(ContextHolder.get().getXGwUser());
        return Response.ok(blueloserHome);
    }

    @GetMapping("/benefit/display")
    @ApiOperation(value = "四大权益模块详情")
    public Response<List<loserBenefitVO>> showBenefitModules(@Validated BenefitModuleParam param) {

        List<loserBenefitVO> benefitVos = inventoryService.getStellarBenefitVos(ContextHolder.get().getXGwUser(), param.getBenefit_name());
        return Response.ok(benefitVos);
    }

    @GetMapping("/benefit/display/earn")
    @ApiOperation(value = "专属理财权益")
    public Response<List<BenefitEarnVO>> getEarnBenefits() {

        List<BenefitEarnVO> list = loserService.getEarnBenefits(ContextHolder.get().getXGwUser());
        return Response.ok(list);
    }

    @GetMapping("/benefit/display/coupon")
    @ApiOperation(value = "专属优惠券权益")
    public Response<List<FullScalePossessVO>> getCouponBenefits() {
        List<FullScalePossessVO> benefitCoupons = loserService.getBenefitCoupons(ContextHolder.get().getXGwUser());
        return Response.ok(benefitCoupons);
    }

    @GetMapping("/benefit/display/report")
    @ApiOperation(value = "BWC研报列表")
    public Response<PageResult<BenefitReportVO>> getReportBenefits(@Validated ReportParam param) {
        PageResult<BenefitReportVO> result = reportService.pageReportVo(param);
        return Response.ok(result);
    }

    @PostMapping("/access")
    @ApiOperation(value = "BWC访问")
    public Response<Boolean> blueloserAccess() {
        boolean b = loserService.pageAccess(ContextHolder.get());
        return Response.ok(b);
    }

    @PostMapping("/report/browse")
    @ApiOperation(value = "研报已读")
    public Response<Boolean> browseReport(@Validated @RequestBody ReportReadParam readParam) {
        reportService.readReport(readParam);
        return Response.ok(true);
    }

    @GetMapping("/report/detail")
    @ApiOperation(value = "研报详情")
    public Response<ReportVO> reportDetail(@RequestParam Long id) {
        return Response.ok(reportService.reportDetail(id));
    }

    @GetMapping("/my-manager")
    @ApiOperation(value = "获取个人客户经理")
    public Response<ManagerVO> getMyManager() {
        ManagerVO managerVo = managerService.getManagerVo(ContextHolder.get().getXGwUser());
        return Response.ok(managerVo);
    }


    @GetMapping("/report/recommend")
    @ApiOperation(value = "研报推荐")
    public Response<List<BenefitReportVO>> getReportRecommend(@Validated ReportRecommendParam param) {
        List<BenefitReportVO> recommends = reportService.getRecommends(param);
        return Response.ok(recommends);
    }
}

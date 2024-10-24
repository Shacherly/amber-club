package com.loser.backend.club.controller;

import com.loser.backend.club.api.http.Response;
import com.loser.backend.club.common.enums.ReportClassEnum;
import com.loser.backend.club.common.enums.RoutineMissionTypeEnum;
import com.loser.backend.club.common.enums.VipEnum;
import com.loser.backend.club.common.enums.loserBenefitEnum;
import com.loser.backend.club.common.http.PageResult;
import com.trading.backend.club.controller.request.aceup.*;
import com.trading.backend.club.controller.response.aceup.*;
import com.trading.backend.club.service.*;
import com.loser.backend.club.util.Converter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author ：trading
 * @date ：Created in 2022/2/14 10:53
 * @description：Club Aceup接口
 * @modified By：
 */


@ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "x-gw-requestid", required = true, dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "header", name = "aceup_userid", required = true, dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "header", name = "aceup_username", required = true, dataTypeClass = String.class),
})
@Slf4j
@Api(tags = "Club-Api-Aceup")
@RestController
@RequestMapping("/internal/v1/aceup")
public class AceupClubController {

    @Autowired
    private IBenefitService benefitService;
    @Autowired
    private IBenefitInventoryService benefitInventoryService;
    @Autowired
    private IMissionService missionService;
    @Autowired
    private IReportService reportService;
    @Autowired
    private IBwcLevelService bwcLevelService;

    @PostMapping("/benefit/add")
    @ApiOperation(value = "权益分类新增")
    public Response benefitAdd(@RequestBody @Valid BenefitAddParam param){

        benefitService.saveOrUpdate(Converter.fromRequest(param));
        return Response.ok();
    }

    @PostMapping("/benefit/edit")
    @ApiOperation(value = "权益分类修改")
    public Response benefitEdit(@RequestBody @Valid BenefitEditParam param){
        benefitService.saveOrUpdate(Converter.fromRequest(param));
        return Response.ok();
    }

    @GetMapping(value = "/benefit/list")
    @ApiOperation(value = "权益分类列表")
    public Response<PageResult<BenefitListQueryVO>> benefitListQuery(BenefitListQueryParam param){
        return Response.ok(benefitService.list(param));
    }

    @DeleteMapping(value = "/benefit/delete")
    @ApiOperation(value = "权益分类删除")
    public Response benefitDelete(@RequestParam Long id){
        benefitService.delete(id);
        return Response.ok();
    }

    @DeleteMapping(value = "/benefit/deleteMany")
    @ApiOperation(value = "权益分类批量删除")
    public Response benefitDeleteMany(@RequestParam List<Long> ids){
        benefitService.deleteMany(ids);
        return Response.ok();
    }

    @PostMapping("/benefitInventory/add")
    @ApiOperation(value = "权益清单新增")
    public Response benefitInventoryAdd(@RequestBody @Valid BenefitInventoryAddParam param){
        benefitInventoryService.saveOrUpdate(Converter.fromRequest(param));
        return Response.ok();
    }

    @PostMapping("/benefitInventory/edit")
    @ApiOperation(value = "权益清单修改")
    public Response benefitInventoryEdit(@RequestBody @Valid BenefitInventoryEditParam param){
        benefitInventoryService.saveOrUpdate(Converter.fromRequest(param));
        return Response.ok();
    }

    @GetMapping(value = "/benefitInventory/list")
    @ApiOperation(value = "权益清单列表")
    public Response<PageResult<BenefitInventoryListQueryVO>> benefitInventoryListQuery(BenefitInventoryListQueryParam param){
        return Response.ok(benefitInventoryService.list(param));
    }

    @GetMapping(value = "/benefitInventory/detail")
    @ApiOperation(value = "权益清单详情")
    public Response<BenefitInventoryDetailVO> benefitInventoryDetail(@RequestParam Long id){
        return Response.ok(benefitInventoryService.detail(id));
    }

    @DeleteMapping(value = "/benefitInventory/delete")
    @ApiOperation(value = "权益清单删除")
    public Response benefitInventoryDelete(@RequestParam Long id){
        benefitInventoryService.delete(id);
        return Response.ok();
    }

    @DeleteMapping(value = "/benefitInventory/deleteMany")
    @ApiOperation(value = "权益清单批量删除")
    public Response benefitInventoryDeleteMany(@RequestParam List<Long> ids){
        benefitInventoryService.deleteMany(ids);
        return Response.ok();
    }

    @PostMapping("/mission/add")
    @ApiOperation(value = "任务新增")
    public Response missionAdd(@RequestBody @Valid MissionAddParam param) {
        missionService.saveOrUpdate(Converter.fromRequest(param));
        return Response.ok();
    }

    @PostMapping("/mission/edit")
    @ApiOperation(value = "任务修改")
    public Response missionEdit(@RequestBody @Valid MissionEditParam param){
        missionService.saveOrUpdate(Converter.fromRequest(param));
        return Response.ok();
    }

    @GetMapping(value = "/mission/list")
    @ApiOperation(value = "任务列表")
    public Response<PageResult<MissionListQueryVO>> missionQuery(MissionListQueryParam param){
        return Response.ok(missionService.list(param));
    }

    @GetMapping(value = "/mission/detail")
    @ApiOperation(value = "任务详情")
    public Response<MissionDetailVO> missionDetail(@RequestParam Long id){
        return Response.ok(missionService.detail(id));
    }

    @DeleteMapping(value = "/mission/delete")
    @ApiOperation(value = "任务删除")
    public Response missionDelete(@RequestParam Long id){
        missionService.delete(id);
        return Response.ok();
    }

    @DeleteMapping(value = "/mission/deleteMany")
    @ApiOperation(value = "任务批量删除")
    public Response missionDelete(@RequestParam List<Long> ids){
        missionService.deleteMany(ids);
        return Response.ok();
    }

    @PostMapping("/report/add")
    @ApiOperation(value = "研报新增")
    public Response reportAdd(@RequestBody @Valid ReportAddParam param) {
        reportService.saveOrUpdate(Converter.fromRequest(param));
        return Response.ok();
    }

    @PostMapping("/report/edit")
    @ApiOperation(value = "研报修改")
    public Response reportEdit(@RequestBody @Valid ReportEditParam param){
        reportService.saveOrUpdate(Converter.fromRequest(param));
        return Response.ok();
    }

    @GetMapping(value = "/report/list")
    @ApiOperation(value = "研报列表")
    public Response<PageResult<ReportListQueryVO>> reportQuery(ReportListQureyParam param){
        return Response.ok(reportService.list(param));
    }

    @GetMapping(value = "/report/detail")
    @ApiOperation(value = "研报详情")
    public Response<ReportDetailVO> reportDetail(@RequestParam Long id){
        return Response.ok(reportService.detail(id));
    }

    @DeleteMapping(value = "/report/delete")
    @ApiOperation(value = "研报删除")
    public Response reportDelete(@RequestParam Long id){
        reportService.delete(id);
        return Response.ok();
    }

    @DeleteMapping(value = "/report/deleteMany")
    @ApiOperation(value = "研报批量删除")
    public Response reportDelete(@RequestParam List<Long> ids){
        reportService.deleteMany(ids);
        return Response.ok();
    }

    @GetMapping(value = "/mapping/benefit")
    @ApiOperation(value = "获取所有权益分类映射(id-name[json])")
    public Response<List<MappingVO>> getBenefitMapping(){
        return Response.ok(benefitService.benefitMapping());
    }

    @GetMapping(value = "/mapping/benefitInventory")
    @ApiOperation(value = "获取所有权益清单映射(id-name[json])")
    public Response<List<MappingVO>> getBenefitInventoryMapping(){
        return Response.ok(benefitInventoryService.benefitInventoryMapping());
    }

    @GetMapping(value = "/mapping/benefitInventory/assign")
    @ApiOperation(value = "根据权益分类获取权益清单映射(id-name[json])")
    public Response<List<MappingVO>> getBenefitInventoryMappingByBenefit(@RequestParam Long id){
        return Response.ok(benefitInventoryService.benefitInventoryMappingByBenefitId(id));
    }

    @GetMapping(value = "/mapping/reportClass")
    @ApiOperation(value = "研报分类映射(class-name)")
    public Response<Map<String,String>> getReportClassMapping(){
        return Response.ok(ReportClassEnum.getReportClassMap());
    }

    @GetMapping(value = "/loser-level/list")
    @ApiOperation(value = "等级列表")
    public Response<PageResult<BwcListQueryVO>> reportQuery(BwcListQueryParam param){
        return Response.ok(bwcLevelService.list(param));
    }

    @PostMapping("/loser-level/modify")
    @ApiOperation(value = "等级修改")
    public Response modifyloserLevel(@Valid @RequestBody loserLevelModifyParam param) {
        bwcLevelService.updateloserLevel(param);
        return Response.ok();
    }

    @GetMapping(value = "/loser-level/history/list")
    @ApiOperation(value = "等级变更列表")
    public Response<PageResult<loserLevelHistoryListQueryVO>> historyQuery(loserLevelHistoryListQueryParam param){
        return Response.ok(bwcLevelService.listHistory(param));
    }

    @GetMapping("/benefit/indicators")
    @ApiOperation(value = "权益类型下拉框")
    public Response<List<String>> benefitIndicators() {
        return Response.ok(loserBenefitEnum.getBenefits());
    }

    @GetMapping("/loser-level/levels")
    @ApiOperation(value = "等级下拉框")
    public Response<List<String>> vipLevels() {
        return Response.ok(VipEnum.getVipList());
    }


    @GetMapping("/mission/routine/types")
    @ApiOperation(value = "常规权益类型下拉框")
    public Response<List<String>> getRoutineTypes() {
        return Response.ok(RoutineMissionTypeEnum.getRoutineMissionTypes());
    }

}

package com.loser.backend.club.util;

import com.loser.backend.club.controller.response.BenefitReportVO;
import com.loser.backend.club.controller.response.ReportVO;
import com.loser.backend.club.controller.response.aceup.BenefitInventoryListQueryVO;
import com.loser.backend.club.controller.response.aceup.BenefitListQueryVO;
import com.loser.backend.club.controller.response.aceup.MissionListQueryVO;
import com.loser.backend.club.controller.response.aceup.ReportListQueryVO;
import com.loser.backend.club.domain.loserBenefit;
import com.loser.backend.club.domain.loserBenefitInventory;
import com.loser.backend.club.domain.loserMission;
import com.loser.backend.club.domain.loserReport;
import com.trading.backend.club.controller.response.aceup.*;
import com.trading.backend.club.domain.*;
import com.loser.backend.club.pojo.User;

import java.util.Map;
import java.util.Optional;

/**
 * @author ：trading
 * @date ：Created in 2022/2/18 10:41
 * @description：转换VO工具类
 * @modified By：
 */

public class VOConverter {

    public static BenefitListQueryVO fromDomain(loserBenefit domain){
        Builder<BenefitListQueryVO> builder = Builder.of(BenefitListQueryVO::new);
        builder.with(BenefitListQueryVO::setBenefitName, domain.getBenefitName())
                .with(BenefitListQueryVO::setStatus, domain.getStatus())
                .with(BenefitListQueryVO::setPriority, domain.getPriority())
                .with(BenefitListQueryVO::setDescr, domain.getDescr())
                .with(BenefitListQueryVO::setId, domain.getId())
                .with(BenefitListQueryVO::setBenefitIndicator, domain.getBenefitIndicator())
                .with(BenefitListQueryVO::setCreateBy, domain.getCreateBy())
                .with(BenefitListQueryVO::setCtime, TemporalUtil.toEpochMilli(domain.getCtime()))
                .with(BenefitListQueryVO::setUtime, TemporalUtil.toEpochMilli(domain.getUtime()));
        return builder.build();
    }

    public static BenefitInventoryListQueryVO fromDomain(loserBenefitInventory domain){
        Builder<BenefitInventoryListQueryVO> builder = Builder.of(BenefitInventoryListQueryVO::new);
        builder.with(BenefitInventoryListQueryVO::setId, domain.getId())
                .with(BenefitInventoryListQueryVO::setBenefitId, domain.getBenefitId())
                .with(BenefitInventoryListQueryVO::setBenefitTitle, domain.getBenefitTitle())
                .with(BenefitInventoryListQueryVO::setloserLevel, domain.getloserLevel())
                .with(BenefitInventoryListQueryVO::setStatus, domain.getStatus())
                .with(BenefitInventoryListQueryVO::setPriority, domain.getPriority())
                .with(BenefitInventoryListQueryVO::setId, domain.getId())
                .with(BenefitInventoryListQueryVO::setRemark, domain.getRemark())
                .with(BenefitInventoryListQueryVO::setCreateBy, domain.getCreateBy())
                .with(BenefitInventoryListQueryVO::setCtime, TemporalUtil.toEpochMilli(domain.getCtime()))
                .with(BenefitInventoryListQueryVO::setUtime, TemporalUtil.toEpochMilli(domain.getUtime()));
        return builder.build();
    }

    public static MissionListQueryVO fromDomain(loserMission domain){
        Builder<MissionListQueryVO> builder = Builder.of(MissionListQueryVO::new);
        builder.with(MissionListQueryVO::setId, domain.getId())
                .with(MissionListQueryVO::setMissionTitle, domain.getMissionTitle())
                .with(MissionListQueryVO::setBenefitId, domain.getBenefitId())
                .with(MissionListQueryVO::setBenefitInventoryId, domain.getBenefitInventoryId())
                .with(MissionListQueryVO::setPriority, domain.getPriority())
                .with(MissionListQueryVO::setMissionType, domain.getMissionType())
                .with(MissionListQueryVO::setStatus, domain.getStatus())
                .with(MissionListQueryVO::setRemark, domain.getRemark())
                .with(MissionListQueryVO::setCreateBy, domain.getCreateBy())
                .with(MissionListQueryVO::setCtime, TemporalUtil.toEpochMilli(domain.getCtime()))
                .with(MissionListQueryVO::setUtime, TemporalUtil.toEpochMilli(domain.getUtime()));
        Optional.ofNullable(domain.getMissionType())
                .filter(type -> "CUSTOM".equals(type))
                .ifPresent(type ->{
                    builder.with(MissionListQueryVO::setValidBegin, TemporalUtil.toEpochMilli(domain.getValidBegin()))
                            .with(MissionListQueryVO::setValidEnd, TemporalUtil.toEpochMilli(domain.getValidEnd()));
                });
        return builder.build();
    }

    public static ReportListQueryVO fromDomain(loserReport domain){
        Builder<ReportListQueryVO> builder = Builder.of(ReportListQueryVO::new);
        builder.with(ReportListQueryVO::setId, domain.getId())
                .with(ReportListQueryVO::setReportTitle, domain.getReportTitle())
                .with(ReportListQueryVO::setReportClass, domain.getReportClass())
                .with(ReportListQueryVO::setStatus, domain.getStatus())
                .with(ReportListQueryVO::setRemark, domain.getRemark())
                .with(ReportListQueryVO::setCreateBy, domain.getCreateBy())
                .with(ReportListQueryVO::setCtime, TemporalUtil.toEpochMilli(domain.getCtime()))
                .with(ReportListQueryVO::setUtime, TemporalUtil.toEpochMilli(domain.getUtime()));
        return builder.build();
    }

    public static BenefitReportVO makeInto(loserReport domain) {
        Builder<BenefitReportVO> builder = Builder.of(BenefitReportVO::new);
        builder.with(BenefitReportVO::setId, domain.getId())
               .with(BenefitReportVO::setIssueTime, String.valueOf(TemporalUtil.toEpochMilli(domain.getCtime())))
               .with(BenefitReportVO::setReportTitle, domain.toLocalized(domain::getReportTitle))
               .with(BenefitReportVO::setResourceUrl, domain.toLocalized(domain::getReportSource))
               .with(BenefitReportVO::setScreenshot, domain.toLocalized(domain::getReportScreenshot));
        return builder.build();
    }

    public static loserLevelHistoryListQueryVO fromDomain(loserLevelHistory domain, Map<String, User> userMap) {
        Builder<loserLevelHistoryListQueryVO> builder = Builder.of(loserLevelHistoryListQueryVO::new);
        builder.with(loserLevelHistoryListQueryVO::setUid, domain.getUid())
                .with(loserLevelHistoryListQueryVO::setUpdateTime, TemporalUtil.toEpochMilli(domain.getCtime()))
                .with(loserLevelHistoryListQueryVO::setTarget, domain.getTarget())
                .with(loserLevelHistoryListQueryVO::setOldValue, domain.getOldValue())
                .with(loserLevelHistoryListQueryVO::setLatestValue, domain.getLatestValue())
                .with(loserLevelHistoryListQueryVO::setRemark, domain.getRemark())
                .with(loserLevelHistoryListQueryVO::setOperator, domain.getCreateBy());
        Optional.ofNullable(userMap.get(domain.getUid()))
                .ifPresent(user -> {
                    builder.with(loserLevelHistoryListQueryVO::setName, user.getKycName())
                            .with(loserLevelHistoryListQueryVO::setPhone, user.getPhoneNumberDs())
                            .with(loserLevelHistoryListQueryVO::setEmail, user.getEmailDs());
                });
        return builder.build();
    }

    public static ReportVO detailFromDomain(loserReport domain) {
        Builder<ReportVO> builder = Builder.of(ReportVO::new);
        builder.with(ReportVO::setReportTitle, domain.toLocalized(domain::getReportTitle))
               .with(ReportVO::setReportScreenshot, domain.toLocalized(domain::getReportScreenshot))
               .with(ReportVO::setReportSource, domain.toLocalized(domain::getReportSource))
               .with(ReportVO::setReportContent, domain.toLocalized(domain::getReportContent))
               .with(ReportVO::setCtime, TemporalUtil.toEpochMilli(domain.getCtime()))
               .with(ReportVO::setReportClass, domain.getReportClass())
               .with(ReportVO::setReportId, domain.getId());

        return builder.build();
    }
}

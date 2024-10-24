package com.loser.backend.club.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trading.backend.club.controller.request.aceup.*;
import com.trading.backend.club.domain.*;
import com.loser.backend.club.kafka.message.ClientManagerInfo;
import com.loser.backend.club.kafka.message.ClientRelation;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author ：trading
 * @date ：Created in 2022/2/16 11:42
 * @description：转换类
 * @modified By：
 */

public class Converter {

    public static loserBenefit fromRequest(BenefitAddParam param){
        String operator = ContextHolder.get().getAceupUsername();
        Builder<loserBenefit> builder = Builder.of(loserBenefit::new);
        builder.with(loserBenefit::setBenefitName,param.getBenefitName())
                .with(loserBenefit::setPriority, param.getPriority())
                .with(loserBenefit::setStatus, param.getStatus())
                .with(loserBenefit::setDescr, param.getDescr())
                .with(loserBenefit::setBenefitIndicator, param.getBenefitIndicator())
                .with(loserBenefit::setCtime, LocalDateTime.now())
                .with(loserBenefit::setUtime, LocalDateTime.now())
                .with(loserBenefit::setCreateBy, operator)
                .with(loserBenefit::setUpdateBy, operator);
        return builder.build();
    }

    public static loserBenefit fromRequest(BenefitEditParam param){
        String operator = ContextHolder.get().getAceupUsername();
        Builder<loserBenefit> builder = Builder.of(loserBenefit::new);
        builder.with(loserBenefit::setId, param.getId())
                .with(loserBenefit::setBenefitName,param.getBenefitName())
                .with(loserBenefit::setPriority, param.getPriority())
                .with(loserBenefit::setStatus, param.getStatus())
                .with(loserBenefit::setDescr, param.getDescr())
                .with(loserBenefit::setBenefitIndicator, param.getBenefitIndicator())
                .with(loserBenefit::setUtime, LocalDateTime.now())
                .with(loserBenefit::setUpdateBy, operator);
        return builder.build();
    }

    public static loserBenefitInventory fromRequest(BenefitInventoryAddParam param){
        String operator = ContextHolder.get().getAceupUsername();
        Builder<loserBenefitInventory> builder = Builder.of(loserBenefitInventory::new);
        builder.with(loserBenefitInventory::setBenefitTitle, param.getBenefitTitle())
                .with(loserBenefitInventory::setBenefitId, param.getBenefitId())
                .with(loserBenefitInventory::setBenefitDescr, param.getBenefitDescr())
                .with(loserBenefitInventory::setLevelDescr, param.getLevelDescr())
                .with(loserBenefitInventory::setBenefitImg, param.getBenefitImg())
                .with(loserBenefitInventory::setPriority, param.getPriority())
                .with(loserBenefitInventory::setloserLevel, param.getloserLevel())
                .with(loserBenefitInventory::setStatus, param.getStatus())
                .with(loserBenefitInventory::setRemark, param.getRemark())
                .with(loserBenefitInventory::setCtime, LocalDateTime.now())
                .with(loserBenefitInventory::setUtime, LocalDateTime.now())
                .with(loserBenefitInventory::setCreateBy, operator)
                .with(loserBenefitInventory::setUpdateBy, operator);
        return builder.build();
    }

    public static loserBenefitInventory fromRequest(BenefitInventoryEditParam param){
        String operator = ContextHolder.get().getAceupUsername();
        Builder<loserBenefitInventory> builder = Builder.of(loserBenefitInventory::new);
        builder.with(loserBenefitInventory::setId, param.getId())
                .with(loserBenefitInventory::setBenefitTitle, param.getBenefitTitle())
                .with(loserBenefitInventory::setBenefitId, param.getBenefitId())
                .with(loserBenefitInventory::setBenefitDescr, param.getBenefitDescr())
                .with(loserBenefitInventory::setLevelDescr, param.getLevelDescr())
                .with(loserBenefitInventory::setBenefitImg, param.getBenefitImg())
                .with(loserBenefitInventory::setPriority, param.getPriority())
                .with(loserBenefitInventory::setloserLevel, param.getloserLevel())
                .with(loserBenefitInventory::setStatus, param.getStatus())
                .with(loserBenefitInventory::setRemark, param.getRemark())
                .with(loserBenefitInventory::setUtime, LocalDateTime.now())
                .with(loserBenefitInventory::setUpdateBy, operator);
        return builder.build();
    }


    public static loserMission fromRequest(MissionAddParam param) {
        String operator = ContextHolder.get().getAceupUsername();
        LocalDateTime now = LocalDateTime.now();

        Builder<loserMission> builder = Builder.of(loserMission::new);
        builder.with(loserMission::setMissionTitle, param.getMissionTitle())
               .with(loserMission::setBenefitId, param.getBenefitId())
               .with(loserMission::setBenefitInventoryId, param.getBenefitInventoryId())
               .with(loserMission::setMissionDescr, param.getMissionDescr())
               .with(loserMission::setMissionIcon, param.getMissionIcon())
               .with(loserMission::setMissionImg, param.getMissionImg())
               .with(loserMission::setForwardLink, param.getForwardLink())
               .with(loserMission::setPriority, param.getPriority())
               .with(loserMission::setMissionType, param.getMissionType())
               .with(loserMission::setStatus, param.getStatus())
               .with(loserMission::setCreateBy, operator)
               .with(loserMission::setUpdateBy, operator)
               .with(loserMission::setRemark, param.getRemark())
               .with(loserMission::setCtime, now)
               .with(loserMission::setUtime, now);
        Optional.ofNullable(param.getUserRangeType())
                .filter(StringUtils::isNotBlank)
                .ifPresent(type -> {
                    builder.with(loserMission::setClientType, type)
                           .with(loserMission::setClientAttaches, param.getUserRangeParam());
                });
        Optional.ofNullable(param.getMissionType())
                //ROUTINE、CUSTOM
                .filter(type -> "CUSTOM".equals(type))
                .ifPresent(type ->{
                    builder.with(loserMission::setValidBegin, TemporalUtil.ofEpochMilli(param.getValidBegin()))
                            .with(loserMission::setValidEnd, TemporalUtil.ofEpochMilli(param.getValidEnd()));
                });
        return builder.build();
    }

    public static loserMission fromRequest(MissionEditParam param) {
        String operator = ContextHolder.get().getAceupUsername();
        LocalDateTime now = LocalDateTime.now();

        Builder<loserMission> builder = Builder.of(loserMission::new);
        builder.with(loserMission::setId, param.getId())
                .with(loserMission::setMissionTitle, param.getMissionTitle())
                .with(loserMission::setBenefitId, param.getBenefitId())
                .with(loserMission::setBenefitInventoryId, param.getBenefitInventoryId())
                .with(loserMission::setMissionDescr, param.getMissionDescr())
                .with(loserMission::setMissionIcon, param.getMissionIcon())
                .with(loserMission::setMissionImg, param.getMissionImg())
                .with(loserMission::setForwardLink, param.getForwardLink())
                .with(loserMission::setPriority, param.getPriority())
                .with(loserMission::setMissionType, param.getMissionType())
                .with(loserMission::setStatus, param.getStatus())
                .with(loserMission::setUpdateBy, operator)
                .with(loserMission::setRemark, param.getRemark())
                .with(loserMission::setUtime, now);
        Optional.ofNullable(param.getUserRangeType())
                .filter(StringUtils::isNotBlank)
                .ifPresent(type -> {
                    builder.with(loserMission::setClientType, type)
                            .with(loserMission::setClientAttaches, param.getUserRangeParam());
                });
        Optional.ofNullable(param.getMissionType())
                //ROUTINE、CUSTOM
                .filter(type -> "CUSTOM".equals(type))
                .ifPresent(type ->{
                    builder.with(loserMission::setValidBegin, TemporalUtil.ofEpochMilli(param.getValidBegin()))
                            .with(loserMission::setValidEnd, TemporalUtil.ofEpochMilli(param.getValidEnd()));
                });
        return builder.build();
    }

    public static loserReport fromRequest(ReportAddParam param){
        String operator = ContextHolder.get().getAceupUsername();
        Builder<loserReport> builder = Builder.of(loserReport::new);
        builder.with(loserReport::setReportTitle, param.getReportTitle())
                .with(loserReport::setReportClass, param.getReportClass())
                .with(loserReport::setReportScreenshot, param.getReportScreenshot())
                .with(loserReport::setReportContent, param.getReportContent())
                .with(loserReport::setReportSource, param.getReportSource())
                .with(loserReport::setStatus, param.getStatus())
                .with(loserReport::setRemark, param.getRemark())
                .with(loserReport::setRemark, param.getRemark())
                .with(loserReport::setCtime, LocalDateTime.now())
                .with(loserReport::setUtime, LocalDateTime.now())
                .with(loserReport::setCreateBy, operator)
                .with(loserReport::setUpdateBy, operator);
        return builder.build();
    }

    public static loserReport fromRequest(ReportEditParam param){
        String operator = ContextHolder.get().getAceupUsername();
        Builder<loserReport> builder = Builder.of(loserReport::new);
        builder.with(loserReport::setId, param.getId())
                .with(loserReport::setReportTitle, param.getReportTitle())
                .with(loserReport::setReportClass, param.getReportClass())
                .with(loserReport::setReportScreenshot, param.getReportScreenshot())
                .with(loserReport::setReportContent, param.getReportContent())
                .with(loserReport::setReportSource, param.getReportSource())
                .with(loserReport::setStatus, param.getStatus())
                .with(loserReport::setRemark, param.getRemark())
                .with(loserReport::setRemark, param.getRemark())
                .with(loserReport::setUtime, LocalDateTime.now())
                .with(loserReport::setUpdateBy, operator);
        return builder.build();
    }

    public static loserManager fromRemote(ClientManagerInfo info) {
        Builder<loserManager> builder = Builder.of(loserManager::new);
        builder.with(loserManager::setManagerIdentity, info.getCmid())
                .with(loserManager::setManagerUid, info.getUid())
                .with(loserManager::setManagerName, info.getInternalName())
                .with(loserManager::setManagerEmail, info.getInternalEmail())
                .with(loserManager::setloserPhoto, info.getPhoto())
                .with(loserManager::setIsDefault, info.getDefaultManager())
                .with(loserManager::setCtime, LocalDateTime.now())
                .with(loserManager::setUtime, LocalDateTime.now());

        //对外姓名
        Optional.ofNullable(info.getBwcName())
                .filter(StringUtils::isNotBlank)
                .map(JSON::parseObject)
                .ifPresent(name ->{
                    builder.with(loserManager::setloserName,name);
                });
        //个人介绍
        Optional.ofNullable(info.getProfile())
                .filter(StringUtils::isNotBlank)
                .map(JSON::parseObject)
                .ifPresent(profile ->{
                    builder.with(loserManager::setloserProfile,profile);
                });
        //对外联系方式
        JSONObject contact = new JSONObject();
        Optional.ofNullable(info.getBwcEmail()).ifPresent(obj -> contact.put("email",obj));
        Optional.ofNullable(info.getBwcPhone()).ifPresent(obj -> contact.put("phone",obj));
        Optional.ofNullable(info.getWhatsapp()).ifPresent(obj -> contact.put("whats_app",obj));
        Optional.ofNullable(info.getTelegram()).ifPresent(obj -> contact.put("telegram",obj));
        Optional.ofNullable(info.getTwitter()).ifPresent(obj -> contact.put("twitter",obj));
        Optional.ofNullable(info.getFacebook()).ifPresent(obj -> contact.put("facebook",obj));
        builder.with(loserManager::setloserContact, contact);

        return builder.build();
    }

    public static loserClientRelation fromRemote(ClientRelation relation) {
        Builder<loserClientRelation> builder = Builder.of(loserClientRelation::new);
        builder.with(loserClientRelation::setUid, relation.getCid())
                .with(loserClientRelation::setMid, relation.getCmid())
                .with(loserClientRelation::setCtime, LocalDateTime.now());
        return builder.build();

    }
}

package com.loser.backend.club.service.impl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.loser.backend.club.common.enums.StatusEnum;
import com.loser.backend.club.common.http.PageResult;
import com.loser.backend.club.config.ExecutorConfiguration;
import com.loser.backend.club.controller.request.aceup.MissionListQueryParam;
import com.loser.backend.club.controller.response.aceup.MissionDetailVO;
import com.loser.backend.club.controller.response.aceup.MissionListQueryVO;
import com.loser.backend.club.domain.loserMission;
import com.loser.backend.club.domain.loserMissionBinding;
import com.loser.backend.club.exception.BusinessException;
import com.loser.backend.club.exception.ExceptionEnum;
import com.loser.backend.club.exception.VisibleException;
import com.loser.backend.club.mapper.loserMissionBindingMapper;
import com.loser.backend.club.mapper.loserMissionMapper;
import com.trading.backend.club.service.*;
import com.loser.backend.club.util.ContextHolder;
import com.loser.backend.club.util.PageContext;
import com.loser.backend.club.util.TemporalUtil;
import com.loser.backend.club.util.VOConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author ~~ trading.Shi
 * @date 14:20 02/18/22
 */
@Slf4j @Service
public class MissionServiceImpl implements IMissionService {

    @Autowired
    private loserMissionMapper missionMapper;
    @Autowired
    private ExecutorConfiguration execuConf;
    @Autowired
    private IMissionBindingService missionBindingService;
    @Autowired
    private IBenefitService benefitService;
    @Autowired
    private ICommonService commonService;
    @Autowired
    private ThreadPoolTaskScheduler scheduler;
    @Autowired
    private loserMissionBindingMapper bindingMapper;
    @Autowired
    private IValidator validator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveOrUpdate(loserMission source) {
        return Optional
                .ofNullable(source)
                .map(mission ->{
                    checkParam(mission);
                    return mission;
                })
                .map(mission ->
                        Optional.ofNullable(mission.getId())
                                .map(id -> {
                                    log.info("Update loserMission = {}", source);
                                    return updateByKey(mission);
                                })
                                .orElseGet(() -> {
                                    log.info("Insert loserMission = {}", source);
                                    return save(mission);
                                })
                )
                .orElseThrow(() -> new BusinessException(ExceptionEnum.ARGUMENT_NULL, "source"));
    }

    private void checkParam(loserMission mission) {
        //check priority
        Example query = new Example(loserMission.class);
        Example.Criteria criteria = query.createCriteria();
        criteria.andEqualTo("benefitInventoryId", mission.getBenefitInventoryId());
        criteria.andEqualTo("priority", mission.getPriority());
        criteria.andNotEqualTo("status", StatusEnum.DELETED.getIndicator());
        if (ObjectUtil.isNotNull(mission.getId())){
            criteria.andNotEqualTo("id", mission.getId());
        }

        int count = missionMapper.selectCountByExample(query);
        if (count > 0){
            throw new VisibleException(ExceptionEnum.CONFLICT_PRIORITY_ERROR);
        }

        //check missionImg and forwardLink
        validator.checkMultiPlatform(mission.getMissionImg());
        validator.checkMultiPlatform(mission.getForwardLink());
    }

    @Override
    public PageResult<MissionListQueryVO> list(MissionListQueryParam param) {

        if ((StringUtils.isNotBlank(param.getBenefit_id()) && !NumberUtil.isLong(param.getBenefit_id()))
                || (StringUtils.isNotBlank(param.getBenefit_inventory_id()) && !NumberUtil.isLong(param.getBenefit_inventory_id()))){
            return PageResult.empty();
        }

        Example query = new Example(loserMission.class);
        Example.Criteria criteria = query.createCriteria();
        criteria.andNotEqualTo("status", StatusEnum.DELETED.getIndicator());
        Optional.ofNullable(param.getBenefit_id()).filter(StringUtils::isNotBlank).map(Long::parseLong).ifPresent(obj -> criteria.andEqualTo("benefitId",obj));
        Optional.ofNullable(param.getBenefit_inventory_id()).filter(StringUtils::isNotBlank).map(Long::parseLong).ifPresent(obj -> criteria.andEqualTo("benefitInventoryId",obj));
        Optional.ofNullable(param.getMission_type()).filter(StringUtils::isNotBlank).ifPresent(obj -> criteria.andEqualTo("missionType",obj));
        Optional.ofNullable(param.getStatus()).filter(StringUtils::isNotBlank).ifPresent(obj -> criteria.andEqualTo("status",obj));
        Optional.ofNullable(param.getCtime_start()).ifPresent(obj -> criteria.andGreaterThanOrEqualTo("ctime", TemporalUtil.ofEpochMilli(obj)));
        Optional.ofNullable(param.getCtime_end()).ifPresent(obj -> criteria.andLessThanOrEqualTo("ctime",TemporalUtil.ofEpochMilli(obj)));
        Optional.ofNullable(param.getUtime_start()).ifPresent(obj -> criteria.andGreaterThanOrEqualTo("utime", TemporalUtil.ofEpochMilli(obj)));
        Optional.ofNullable(param.getUtime_end()).ifPresent(obj -> criteria.andLessThanOrEqualTo("utime",TemporalUtil.ofEpochMilli(obj)));

        PageResult<loserMission>  missionList = PageContext.selectPage(() -> missionMapper.selectByExample(query));
        List<MissionListQueryVO> resList = missionList.getItems().stream().map(VOConverter::fromDomain).collect(Collectors.toList());

        //repackage result
        PageResult<MissionListQueryVO> result = PageResult.copyPageNoItems(missionList);
        result.setItems(resList);

        return result;
    }

    @Override
    public MissionDetailVO detail(Long id) {
        Example query = new Example(loserMission.class);
        query.createCriteria().andEqualTo("id",id)
                .andNotEqualTo("status",StatusEnum.DELETED.getIndicator());
        loserMission mission = missionMapper.selectOneByExample(query);
        MissionDetailVO res = new MissionDetailVO();
        BeanUtils.copyProperties(mission,res,new String[]{"validBegin","validEnd"});
        //有效时间
        if ("CUSTOM".equals(mission.getMissionType())){
            res.setValidBegin(TemporalUtil.toEpochMilli(mission.getValidBegin()));
            res.setValidEnd(TemporalUtil.toEpochMilli(mission.getValidEnd()));
        }
        //标签
        res.setUserRangeType(mission.getClientType());
        res.setUserRangeParam(mission.getClientAttaches());
        return res;
    }

    @Override
    public void delete(Long id) {
        List<Long> ids = Collections.singletonList(id);
        commonService.delete(ids, this::canDelete,  loserMission.class, missionMapper);
    }

    @Override
    public void deleteMany(List<Long> ids) {
        commonService.delete(ids, this::canDelete,  loserMission.class, missionMapper);
    }

    @Override
    public void syncLabelMission() {

        Example missionExample = new Example(loserMission.class);
        missionExample.createCriteria().andEqualTo("status", StatusEnum.ENABLE.getIndicator())
                .andEqualTo("clientType", "USER_LABEL");

        int totalLabelMission = missionMapper.selectCountByExample(missionExample);

        if (totalLabelMission <= 0) return;

        BiFunction<Integer,Integer, List<loserMission>> producer =
                (page, pagesize) -> PageContext.selectList(()-> missionMapper.selectByExample(missionExample), page, pagesize, "CTIME ASC");

        Consumer<loserMission> consumer = mission -> {
            if (StringUtils.isNoneBlank(mission.getClientAttaches()))
                missionBindingService.missionBinding(mission);
        };

        execuConf.serialPage(totalLabelMission, producer, Function.identity(), consumer);

    }

    private Long updateByKey(loserMission mission) {

        mission.setUpdateBy(ContextHolder.get().getAceupUsername());
        mission.setUtime(LocalDateTime.now());
        mission.setBenefitIndicator(benefitService.getIndicator(mission.getBenefitId()));
        missionMapper.updateByPrimaryKeySelective(mission);

        if (StringUtils.isNoneBlank(mission.getClientType(),mission.getClientAttaches())){
            execuConf.getEXECUTOR().execute(() -> missionBindingService.missionBinding(mission));
        } else {
            deleleRelation(mission.getId());
        }

        if ("CUSTOM".equals(mission.getMissionType()) && LocalDateTime.now().plusMinutes(30).compareTo(mission.getValidEnd()) >= 0)
            scheduler.schedule(() -> benefitService.expireMission(mission.getId()), DateUtil.toInstant(mission.getValidEnd()));
        return mission.getId();

    }

    private Long save(loserMission mission) {
        mission.setBenefitIndicator(benefitService.getIndicator(mission.getBenefitId()));
        missionMapper.insertSelective(mission);
        if (StringUtils.isNotBlank(mission.getClientType()))
            execuConf.getEXECUTOR().execute(() -> missionBindingService.missionBinding(mission));
        if ("CUSTOM".equals(mission.getMissionType()) && LocalDateTime.now().plusMinutes(30).compareTo(mission.getValidEnd()) >= 0){
                scheduler.schedule(() -> benefitService.expireMission(mission.getId()), DateUtil.toInstant(mission.getValidEnd()));
        }
        return mission.getId();
    }

    private void canDelete(Long id) {
        Optional.ofNullable(missionMapper.selectByPrimaryKey(id))
                .map(mission ->{
                    if (TemporalUtil.isBetweenTime(LocalDateTime.now(), mission.getValidBegin(),mission.getValidEnd())){
                        throw new VisibleException(ExceptionEnum.DELETE_FORBIDDEN_ERROR, "mission is valid!");
                    }
                    return mission;
                })
                .orElseThrow(() -> new VisibleException(ExceptionEnum.DELETE_FORBIDDEN_ERROR, "mission is valid!"));
    }

    private void deleleRelation(Long missionId) {
        Example deleExample = new Example(loserMissionBinding.class);
        deleExample.createCriteria().andEqualTo("missionId", missionId);
        bindingMapper.deleteByExample(deleExample);
    }
}

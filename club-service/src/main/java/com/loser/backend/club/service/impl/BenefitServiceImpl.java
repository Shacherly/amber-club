package com.loser.backend.club.service.impl;

import com.loser.backend.club.common.enums.StatusEnum;
import com.loser.backend.club.common.http.PageResult;
import com.loser.backend.club.controller.request.aceup.BenefitListQueryParam;
import com.loser.backend.club.controller.response.aceup.BenefitListQueryVO;
import com.loser.backend.club.controller.response.aceup.MappingVO;
import com.loser.backend.club.domain.loserBenefit;
import com.loser.backend.club.domain.loserBenefitInventory;
import com.loser.backend.club.domain.loserMission;
import com.loser.backend.club.exception.BusinessException;
import com.loser.backend.club.exception.ExceptionEnum;
import com.loser.backend.club.exception.VisibleException;
import com.loser.backend.club.mapper.loserBenefitInventoryMapper;
import com.loser.backend.club.mapper.loserBenefitMapper;
import com.loser.backend.club.mapper.loserMissionMapper;
import com.loser.backend.club.service.IBenefitService;
import com.loser.backend.club.service.ICommonService;
import com.loser.backend.club.util.ContextHolder;
import com.loser.backend.club.util.PageContext;
import com.loser.backend.club.util.VOConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ：trading
 * @date ：Created in 2022/2/16 11:30
 * @description：
 * @modified By：
 */

@Service
@Slf4j
public class BenefitServiceImpl implements IBenefitService {

    @Autowired
    private loserBenefitMapper loserBenefitMapper;
    @Autowired
    private loserBenefitInventoryMapper loserBenefitInventoryMapper;
    @Autowired
    private loserMissionMapper loserMissionMapper;
    @Autowired
    private ICommonService commonService;

    @Override
    public Long saveOrUpdate(loserBenefit loserBenefit) {
        Optional.ofNullable(loserBenefit)
                .map(source -> {
                    checkParam(source);
                    return source;
                })
                .orElseThrow(() -> new BusinessException(ExceptionEnum.ARGUMENT_NULL, "source"));
        if (Objects.isNull(loserBenefit.getId())){
            log.info("insert benefit = {}", loserBenefit);
            save(loserBenefit);
            return loserBenefit.getId();
        }
        log.info("update benefit = {}", loserBenefit);
        update(loserBenefit);
        return loserBenefit.getId();
    }

    @Override
    public PageResult<BenefitListQueryVO> list(BenefitListQueryParam param) {

        Example query = new Example(loserBenefit.class);
        Example.Criteria criteria = query.createCriteria();
        criteria.andNotEqualTo("status", StatusEnum.DELETED.getIndicator());
        if (StringUtils.isNotEmpty(param.getBenefit_name())){
            criteria.andCondition("benefit_name::text like '%" + param.getBenefit_name() + "%'");
        }

        PageResult<loserBenefit> benefitList = PageContext.selectPage(() -> loserBenefitMapper.selectByExample(query));
        List<BenefitListQueryVO> resList = benefitList.getItems().stream().map(VOConverter::fromDomain).collect(Collectors.toList());

        //repackage result
        PageResult<BenefitListQueryVO> result = PageResult.copyPageNoItems(benefitList);
        result.setItems(resList);

        return result;
    }

    @Override
    public void delete(Long id) {
        List<Long> ids = Collections.singletonList(id);
        commonService.delete(ids, this::canDelete,  loserBenefit.class, loserBenefitMapper);
    }

    @Override
    public void deleteMany(List<Long> ids) {
        commonService.delete(ids, this::canDelete,  loserBenefit.class, loserBenefitMapper);
    }

    private void canDelete(Long id) {
        Example existExample = new Example(loserBenefitInventory.class);
        existExample.createCriteria().andEqualTo("benefitId",id)
                .andNotEqualTo("status",StatusEnum.DELETED.getIndicator());
        int count = loserBenefitInventoryMapper.selectCountByExample(existExample);
        if (count > 0){
            throw new VisibleException(ExceptionEnum.DELETE_FORBIDDEN_ERROR, "benefit inventory is not empty!");
        }
    }

    @Override
    public List<MappingVO> benefitMapping() {
        Example query = new Example(loserBenefit.class);
        query.createCriteria().andNotEqualTo("status",StatusEnum.DELETED.getIndicator());
        return loserBenefitMapper.selectByExample(query).stream().map(benefit ->{
            MappingVO vo = new MappingVO();
            vo.setId(benefit.getId());
            vo.setName(benefit.getBenefitName());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public void expireMission(Long id) {
        loserMission mission = loserMissionMapper.selectByPrimaryKey(id);
        if (mission.getStatus().equals(StatusEnum.DELETED.getIndicator()) || mission.getStatus().equals(StatusEnum.EXPIRED.getIndicator()) || LocalDateTime.now().compareTo(mission.getValidEnd()) < 0){
            log.info("do not need handle mission : {}", id);
            return;
        }

        mission.setStatus(StatusEnum.EXPIRED.getIndicator());
        loserMissionMapper.updateByPrimaryKeySelective(mission);

    }

    @Override
    public String getIndicator(Long benefitId) {
        Example example = new Example(loserBenefit.class);
        example.createCriteria()
                .andEqualTo("id", benefitId)
                .andNotEqualTo("status", StatusEnum.DISABLE.getIndicator());
        example.selectProperties("benefitIndicator");

        loserBenefit loserBenefit = loserBenefitMapper.selectOneByExample(example);
        return Optional.ofNullable(loserBenefit)
                .map(loserBenefit::getBenefitIndicator)
                .orElseThrow(() -> new BusinessException(ExceptionEnum.RECORD_NOT_EXIST));
    }

    @Override
    public List<loserBenefit> getAllBenefits() {
        Example example = new Example(loserBenefit.class);
        example.createCriteria()
               .andEqualTo("status", StatusEnum.ENABLE.getIndicator());
        return loserBenefitMapper.selectByExample(example);
    }

    @Override
    public List<String> getBenefitNames() {
        Example example = new Example(loserBenefit.class);
        example.createCriteria()
               .andEqualTo("status", StatusEnum.ENABLE.getIndicator());
        example.selectProperties("benefitName");
        String language = ContextHolder.get().getClientLanguage();
        List<loserBenefit> loserBenefits = loserBenefitMapper.selectByExample(example);
        return loserBenefits.stream().map(loserBenefit::getBenefitName).map(name -> name.getString(language)).collect(Collectors.toList());
    }

    @Override
    public List<String> getBenefitIndexs() {
        return null;
    }

    private void checkParam(loserBenefit loserBenefit) {

        if (Objects.isNull(loserBenefit.getId())){
            //星球变更 则校验是否存在
            Example query = new Example(loserBenefit.class);
            query.createCriteria().andEqualTo("benefitIndicator",loserBenefit.getBenefitIndicator())
                    .andNotEqualTo("status",StatusEnum.DELETED.getIndicator());
            int count = loserBenefitMapper.selectCountByExample(query);
            if (count > 0){
                throw new BusinessException(ExceptionEnum.INDICATOR_CONFLICT_ERROR);
            }
        }else {
            Example oldQuery = new Example(loserBenefit.class);
            oldQuery.createCriteria().andEqualTo("id",loserBenefit.getId());
            oldQuery.selectProperties("benefitIndicator");
            loserBenefit oldBenefit = loserBenefitMapper.selectOneByExample(oldQuery);

            if (!oldBenefit.getBenefitIndicator().equals(loserBenefit.getBenefitIndicator())){
                //星球变更 则校验是否存在
                Example query = new Example(loserBenefit.class);
                query.createCriteria().andEqualTo("benefitIndicator",loserBenefit.getBenefitIndicator())
                        .andNotEqualTo("status",StatusEnum.DELETED.getIndicator());
                int count = loserBenefitMapper.selectCountByExample(query);
                if (count > 0){
                    throw new BusinessException(ExceptionEnum.INDICATOR_CONFLICT_ERROR);
                }
            }
        }

    }

    private void update(loserBenefit loserBenefit) {
        loserBenefitMapper.updateByPrimaryKeySelective(loserBenefit);
    }

    private void save(loserBenefit loserBenefit) {
        loserBenefitMapper.insertSelective(loserBenefit);
    }
}

package com.loser.backend.club.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.loser.backend.club.common.enums.MissionTypeEnum;
import com.loser.backend.club.common.enums.StatusEnum;
import com.loser.backend.club.common.enums.loserLevelEnum;
import com.loser.backend.club.common.http.PageResult;
import com.loser.backend.club.controller.request.aceup.BenefitInventoryListQueryParam;
import com.loser.backend.club.controller.response.loserBenefitVO;
import com.loser.backend.club.controller.response.aceup.BenefitInventoryDetailVO;
import com.loser.backend.club.controller.response.aceup.BenefitInventoryListQueryVO;
import com.loser.backend.club.controller.response.aceup.MappingVO;
import com.loser.backend.club.domain.ClubLevel;
import com.loser.backend.club.domain.loserBenefit;
import com.loser.backend.club.domain.loserBenefitInventory;
import com.loser.backend.club.domain.loserMission;
import com.loser.backend.club.exception.BusinessException;
import com.loser.backend.club.exception.ExceptionEnum;
import com.loser.backend.club.exception.VisibleException;
import com.loser.backend.club.mapper.CustomloserMissionBindingMapper;
import com.loser.backend.club.mapper.loserBenefitInventoryMapper;
import com.loser.backend.club.mapper.loserBenefitMapper;
import com.loser.backend.club.mapper.loserMissionMapper;
import com.loser.backend.club.service.IBenefitInventoryService;
import com.loser.backend.club.service.IClubService;
import com.loser.backend.club.service.ICommonService;
import com.loser.backend.club.util.ContextHolder;
import com.loser.backend.club.util.PageContext;
import com.loser.backend.club.util.TemporalUtil;
import com.loser.backend.club.util.VOConverter;
import com.trading.backend.coupon.common.util.Functions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ：trading
 * @date ：Created in 2022/2/16 16:22
 * @description：权益清单服务
 * @modified By：
 */

@Service
@Slf4j
public class BenefitInventoryServiceImpl implements IBenefitInventoryService {

    @Autowired
    private loserBenefitInventoryMapper loserBenefitInventoryMapper;
    @Autowired
    private CustomloserMissionBindingMapper customloserMissionBindingMapper;
    @Autowired
    private loserBenefitMapper loserBenefitMapper;
    @Autowired
    private loserMissionMapper loserMissionMapper;
    @Autowired
    private IClubService clubService;
    @Autowired
    private ICommonService commonService;

    @Override
    public Long saveOrUpdate(loserBenefitInventory benefitInventory) {
        Optional.ofNullable(benefitInventory)
                .map(source -> {
                    checkParam(source);
                    fillParam(source);
                    return source;
                })
                .orElseThrow(() -> new BusinessException(ExceptionEnum.ARGUMENT_NULL, "source"));
        if (Objects.isNull(benefitInventory.getId())){
            log.info("insert benefitInventory = {}", benefitInventory);
            save(benefitInventory);
            return benefitInventory.getId();
        }
        log.info("update benefitInventory = {}", benefitInventory);
        update(benefitInventory);
        return benefitInventory.getId();
    }

    private void checkParam(loserBenefitInventory source) {
        Example query = new Example(loserBenefitInventory.class);
        Example.Criteria criteria = query.createCriteria();
        criteria.andEqualTo("benefitId", source.getBenefitId());
        criteria.andEqualTo("priority", source.getPriority());
        criteria.andNotEqualTo("status", StatusEnum.DELETED.getIndicator());
        if (ObjectUtil.isNotNull(source.getId())){
            criteria.andNotEqualTo("id", source.getId());
        }

        int count = loserBenefitInventoryMapper.selectCountByExample(query);
        if (count > 0){
            throw new VisibleException(ExceptionEnum.CONFLICT_PRIORITY_ERROR);
        }
    }

    private void fillParam(loserBenefitInventory source) {
        //填充星球
        loserBenefit loserBenefit = loserBenefitMapper.selectByPrimaryKey(source.getBenefitId());
        source.setBenefitIndicator(loserBenefit.getBenefitIndicator());
    }

    @Override
    public PageResult<BenefitInventoryListQueryVO> list(BenefitInventoryListQueryParam param) {

        if ((StringUtils.isNotBlank(param.getId()) && !NumberUtil.isLong(param.getId()))
            || (StringUtils.isNotBlank(param.getBenefit_id()) && !NumberUtil.isLong(param.getBenefit_id()))){
            return PageResult.empty();
        }

        Example query = new Example(loserBenefitInventory.class);
        Example.Criteria criteria = query.createCriteria();
        criteria.andNotEqualTo("status",StatusEnum.DELETED.getIndicator());
        Optional.ofNullable(param.getId()).filter(StringUtils::isNotBlank).map(Long::parseLong).ifPresent(obj -> criteria.andEqualTo("id",obj));
        Optional.ofNullable(param.getBenefit_id()).filter(StringUtils::isNotBlank).map(Long::parseLong).ifPresent(obj -> criteria.andEqualTo("benefitId",obj));
        Optional.ofNullable(param.getStatus()).filter(StringUtils::isNotBlank).ifPresent(obj -> criteria.andEqualTo("status",obj));
        Optional.ofNullable(param.getloser_level()).filter(StringUtils::isNotBlank).ifPresent(obj -> criteria.andLike("loserLevel",String.join(obj,"%","%")));
        Optional.ofNullable(param.getCtime_start()).ifPresent(obj -> criteria.andGreaterThanOrEqualTo("ctime", TemporalUtil.ofEpochMilli(obj)));
        Optional.ofNullable(param.getCtime_end()).ifPresent(obj -> criteria.andLessThanOrEqualTo("ctime", TemporalUtil.ofEpochMilli(obj)));
        Optional.ofNullable(param.getUtime_start()).ifPresent(obj -> criteria.andGreaterThanOrEqualTo("utime", TemporalUtil.ofEpochMilli(obj)));
        Optional.ofNullable(param.getUtime_end()).ifPresent(obj -> criteria.andLessThanOrEqualTo("utime", TemporalUtil.ofEpochMilli(obj)));

        PageResult<loserBenefitInventory> benefitInventoryPage = PageContext.selectPage(() -> loserBenefitInventoryMapper.selectByExample(query));
        List<BenefitInventoryListQueryVO> resList = benefitInventoryPage.getItems().stream()
                .map(domain ->{
                    BenefitInventoryListQueryVO vo = VOConverter.fromDomain(domain);
                    vo.setBindNumber(customloserMissionBindingMapper.countBindNumber(domain.getId()));
                    vo.setBindPeople(customloserMissionBindingMapper.countBindPeople(domain.getId()));
                    return vo;
                })
                .collect(Collectors.toList());

        PageResult<BenefitInventoryListQueryVO> res = PageResult.copyPageNoItems(benefitInventoryPage);
        res.setItems(resList);

        return res;
    }

    @Override
    public BenefitInventoryDetailVO detail(Long id) {
        loserBenefitInventory domain = loserBenefitInventoryMapper.selectByPrimaryKey(id);
        BenefitInventoryDetailVO vo = new BenefitInventoryDetailVO();
        BeanUtils.copyProperties(domain,vo,new String[]{"ctime","utime"});
        vo.setBindNumber(customloserMissionBindingMapper.countBindNumber(id));
        vo.setBindPeople(customloserMissionBindingMapper.countBindPeople(id));
        vo.setCtime(TemporalUtil.toEpochMilli(domain.getCtime()));
        vo.setUtime(TemporalUtil.toEpochMilli(domain.getUtime()));
        return vo;
    }

    @Override
    public void delete(Long id) {
        List<Long> ids = Collections.singletonList(id);
        commonService.delete(ids, this::canDelete,  loserBenefitInventory.class, loserBenefitInventoryMapper);
    }

    @Override
    public void deleteMany(List<Long> ids) {
        commonService.delete(ids, this::canDelete,  loserBenefitInventory.class, loserBenefitInventoryMapper);
    }

    @Override
    public List<MappingVO> benefitInventoryMapping() {
        Example query = new Example(loserBenefitInventory.class);
        query.createCriteria().andNotEqualTo("status",StatusEnum.DELETED.getIndicator());
        return loserBenefitInventoryMapper.selectByExample(query).stream().map(benefitInventory ->{
            MappingVO vo = new MappingVO();
            vo.setId(benefitInventory.getId());
            vo.setName(benefitInventory.getBenefitTitle());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<MappingVO> benefitInventoryMappingByBenefitId(Long benefitId) {
        Example query = new Example(loserBenefitInventory.class);
        query.createCriteria().andNotEqualTo("status",StatusEnum.DELETED.getIndicator())
                .andEqualTo("benefitId",benefitId);
        return loserBenefitInventoryMapper.selectByExample(query).stream().map(benefitInventory ->{
            MappingVO vo = new MappingVO();
            vo.setId(benefitInventory.getId());
            vo.setName(benefitInventory.getBenefitTitle());
            return vo;
        }).collect(Collectors.toList());
    }


    /**
     * @author ~~ trading.Shi
     * @date 15:12 02/21/22
     */
    @Override
    public List<loserBenefitInventory> getRoutineMissionBenefits(String uid, String benefitIndicat) {
        return loserBenefitInventoryMapper.getByUid(uid, MissionTypeEnum.ROUTINE.getType(), benefitIndicat);
    }

    @Override
    public List<loserBenefitInventory> getCustomMissionBenefits(String uid, String benefitIndicat) {
        return loserBenefitInventoryMapper.getByUid(uid, MissionTypeEnum.CUSTOM.getType(), benefitIndicat);
    }

    @Override
    public List<loserBenefitInventory> getStellarBenefits(String benefitIndicat) {
        Example example = new Example(loserBenefitInventory.class);
        example.createCriteria()
               .andEqualTo("benefitIndicator", StringUtils.upperCase(benefitIndicat))
               .andEqualTo("status", "ENABLE");
        List<loserBenefitInventory> list = loserBenefitInventoryMapper.selectByExample(example);
        return Functions.sort(list, Comparator.comparingInt(loserBenefitInventory::getPriority));
    }

    @Override
    public List<loserBenefitVO> getStellarBenefitVos(String uid, String benefitName) {
        ClubLevel clubLevel = clubService.getClubLevel(uid);
        String language = ContextHolder.get().getClientLanguage();
        List<loserBenefitInventory> benefits = getStellarBenefits(benefitName);

        log.info("User {} club_level {},  owns benefits {}", uid, clubLevel, benefits);
        return benefits.stream().map(benefit -> {
            Set<Integer> applyLevels = loserLevelEnum.getLevels(benefit.getloserLevel());
            boolean claimed = applyLevels.contains(Optional.ofNullable(clubLevel).map(ClubLevel::getloserLevel).orElse(-1));
            return new loserBenefitVO(
                    claimed,
                    benefit.toLocalized(benefit.getBenefitTitle()),
                    benefit.toLocalized(benefit.getBenefitImg()),
                    benefit.toLocalized(benefit.getBenefitDescr()),
                    benefit.toLocalized(benefit.getLevelDescr())
            );
        }).collect(Collectors.toList());
    }

    @Override
    public int count4Level(String uid) {
        ClubLevel clubLevel = clubService.getClubLevel(uid);
        return Optional.ofNullable(clubLevel)
                .map(ClubLevel::getloserLevel)
                .filter(level -> level > 0)
                .map(level -> {
                    Example example = new Example(loserBenefitInventory.class);
                    example.createCriteria()
                           .andEqualTo("status", StatusEnum.ENABLE.getIndicator())
                           .andCondition(" loser_level::text LIKE '%" + level + "%' ");
                    return loserBenefitInventoryMapper.selectCountByExample(example);
                })
                .orElse(0);
    }

    @Override
    public int getCustomCount(String uid) {
        return loserBenefitInventoryMapper.countIvtries(uid, MissionTypeEnum.CUSTOM.getType());
    }

    @Override
    public int getAvailableCount(String uid) {
        int available = count4Level(uid) - 3 - getCustomCount(uid);
        return Math.max(available, 0);
    }

    private void update(loserBenefitInventory benefitInventory) {
        loserBenefitInventoryMapper.updateByPrimaryKeySelective(benefitInventory);
    }

    private void save(loserBenefitInventory benefitInventory) {
        loserBenefitInventoryMapper.insertSelective(benefitInventory);
    }

    private void canDelete(Long id) {
        Example existExample = new Example(loserMission.class);
        existExample.createCriteria().andEqualTo("benefitInventoryId",id)
                .andNotEqualTo("status", StatusEnum.DELETED.getIndicator());
        int count = loserMissionMapper.selectCountByExample(existExample);
        if (count > 0){
            throw new VisibleException(ExceptionEnum.DELETE_FORBIDDEN_ERROR, "mission is not empty!");
        }
    }
}

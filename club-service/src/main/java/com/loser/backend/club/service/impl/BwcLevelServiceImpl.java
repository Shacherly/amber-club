package com.loser.backend.club.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.loser.backend.club.client.IUserServiceApi;
import com.loser.backend.club.common.enums.VipEnum;
import com.loser.backend.club.common.http.PageResult;
import com.loser.backend.club.config.ClubConfig;
import com.loser.backend.club.controller.request.aceup.BwcListQueryParam;
import com.loser.backend.club.controller.request.aceup.loserLevelHistoryListQueryParam;
import com.loser.backend.club.controller.request.aceup.loserLevelModifyParam;
import com.loser.backend.club.controller.response.aceup.BwcListQueryVO;
import com.loser.backend.club.controller.response.aceup.loserLevelHistoryListQueryVO;
import com.loser.backend.club.domain.ClubLevel;
import com.loser.backend.club.domain.loserLevelHistory;
import com.loser.backend.club.exception.BusinessException;
import com.loser.backend.club.exception.ExceptionEnum;
import com.loser.backend.club.mapper.ClubLevelMapper;
import com.loser.backend.club.mapper.loserLevelHistoryMapper;
import com.loser.backend.club.pojo.User;
import com.loser.backend.club.pojo.VipInfo;
import com.loser.backend.club.service.IBwcLevelService;
import com.loser.backend.club.util.ContextHolder;
import com.loser.backend.club.util.PageContext;
import com.loser.backend.club.util.TemporalUtil;
import com.loser.backend.club.util.VOConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ：trading
 * @date ：Created in 2022/2/23 15:02
 * @description：
 * @modified By：
 */

@Service
public class BwcLevelServiceImpl implements IBwcLevelService {

    @Autowired
    private ClubLevelMapper clubLevelMapper;
    @Autowired
    private IUserServiceApi userServiceApi;
    @Autowired
    private loserLevelHistoryMapper loserLevelHistoryMapper;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public PageResult<BwcListQueryVO> list(BwcListQueryParam param) {

        if (StringUtils.isBlank(param.getUid())){
            return PageResult.empty();
        }

        Example query = new Example(ClubLevel.class);
        Example.Criteria criteria = query.createCriteria();

        Optional.ofNullable(param.getUid()).filter(StringUtils::isNotBlank).ifPresent(obj -> criteria.andEqualTo("uid",obj));

        PageResult<ClubLevel> clubPage = PageContext.selectPage(() -> clubLevelMapper.selectByExample(query));
        Set<String> collectUids = clubPage.getItems().stream().map(clubLevel -> clubLevel.getUid()).collect(Collectors.toSet());
        Map<String, User> userMap = userServiceApi.userMap(collectUids, null, null);

        List<BwcListQueryVO> resList = clubPage.getItems().stream().map(clubLevel -> {
            BwcListQueryVO vo = new BwcListQueryVO();
            vo.setUid(clubLevel.getUid());
            if (Objects.isNull(clubLevel.getloserLevel()) || clubLevel.getloserLevel() == -1){
                //get club_level
                vo.setVipLevel(VipEnum.LV.getType() + clubLevel.getClubLevel());
                vo.setVipUntil(TemporalUtil.toEpochMilli(clubLevel.getValidUntil()));
            }else {
                //get loser_level
                vo.setVipLevel(VipEnum.BWC.getType() + clubLevel.getloserLevel());
                vo.setVipUntil(TemporalUtil.toEpochMilli(clubLevel.getloserUntil()));
            }
            Optional.ofNullable(userMap.get(clubLevel.getUid())).ifPresent(user -> {
                vo.setEmail(user.getEmailDs());
                vo.setPhone(user.getPhoneNumberDs());
                vo.setName(user.getKycName());
            });
            return vo;
        }).collect(Collectors.toList());

        PageResult<BwcListQueryVO> res = PageResult.copyPageNoItems(clubPage);
        res.setItems(resList);

        return res;
    }

    @Override
    public void updateloserLevel(loserLevelModifyParam param) {

        Example query = new Example(ClubLevel.class);
        query.createCriteria().andEqualTo("uid",param.getUid());

        Optional.ofNullable(clubLevelMapper.selectOneByExample(query)).ifPresent(clubLevel -> {

            StringBuilder target = new StringBuilder();
            StringBuilder oldValue = new StringBuilder();
            StringBuilder latesdValue = new StringBuilder();

            //get current vip_info
            VipInfo oldVipInfo = getVipInfoByClub(clubLevel);

            //vip string length
            int clubStrLength = VipEnum.LV.getType().length();
            int bwcStrLength = VipEnum.BWC.getType().length();

            int oldLevel = oldVipInfo.getVipLevel().startsWith(VipEnum.LV.getType()) ? NumberUtil.parseInt(oldVipInfo.getVipLevel().substring(clubStrLength)) : NumberUtil.parseInt(oldVipInfo.getVipLevel().substring(bwcStrLength));
            int newLevel = param.getUpdateLevel().startsWith(VipEnum.LV.getType()) ? NumberUtil.parseInt(param.getUpdateLevel().substring(clubStrLength)) : NumberUtil.parseInt(param.getUpdateLevel().substring(bwcStrLength));

            if ((oldVipInfo.getVipLevel().startsWith(VipEnum.LV.getType()) && param.getUpdateLevel().startsWith(VipEnum.LV.getType()))
                    || (oldVipInfo.getVipLevel().startsWith(VipEnum.BWC.getType()) && param.getUpdateLevel().startsWith(VipEnum.BWC.getType()))){
                //club --> club  || bwc --> bwc
                if (oldLevel > newLevel){
                    throw new BusinessException(ExceptionEnum.DOWNGRADE_NOT_SUPPORTED_ERROR);
                }else {
                    if (!StringUtils.equals(oldVipInfo.getVipLevel(), param.getUpdateLevel())){
                        target.append("VIP_LEVEL").append(";");
                        oldValue.append(oldVipInfo.getVipLevel()).append(";");
                        latesdValue.append(param.getUpdateLevel()).append(";");
                    }
                    if (!oldVipInfo.getValidUntil().equals(param.getValidUntil())){
                        target.append("EXPIRED_TIME").append(";");
                        oldValue.append(formatter.format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(oldVipInfo.getValidUntil()), ZoneId.of("UTC+8")))).append("(UTC+8)").append(";");
                        latesdValue.append(formatter.format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(param.getValidUntil()), ZoneId.of("UTC+8")))).append("(UTC+8)").append(";");
                    }
                }
            }else if (oldVipInfo.getVipLevel().startsWith(VipEnum.LV.getType()) && param.getUpdateLevel().startsWith(VipEnum.BWC.getType())){
                //club --> bwc
                //level
                target.append("VIP_LEVEL").append(";");
                oldValue.append(oldVipInfo.getVipLevel()).append(";");
                latesdValue.append(param.getUpdateLevel()).append(";");
                //expire_time
                target.append("EXPIRED_TIME").append(";");
                oldValue.append(formatter.format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(oldVipInfo.getValidUntil()), ZoneId.of("UTC+8")))).append("(UTC+8)").append(";");
                latesdValue.append(formatter.format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(param.getValidUntil()), ZoneId.of("UTC+8")))).append("(UTC+8)").append(";");
            }else {
                throw new BusinessException(ExceptionEnum.DOWNGRADE_NOT_SUPPORTED_ERROR);
            }

            if (StringUtils.isNotBlank(target.toString())){
                //update level
                if (param.getUpdateLevel().startsWith(VipEnum.LV.getType())){
                    clubLevel.setClubLevel(newLevel);
                    clubLevel.setValidUntil(TemporalUtil.ofEpochMilli(param.getValidUntil()));
                }else {
                    clubLevel.setloserLevel(newLevel);
                    clubLevel.setloserUntil(TemporalUtil.ofEpochMilli(param.getValidUntil()));
                    clubLevel.setBenefitLevel(ClubConfig.getBenefitLevel(newLevel));
                }
                clubLevel.setUtime(LocalDateTime.now());
                clubLevelMapper.updateByPrimaryKeySelective(clubLevel);

                //add level history record
                loserLevelHistory history = new loserLevelHistory();
                history.setUid(clubLevel.getUid());
                history.setTarget(target.substring(0,target.length()-1));
                history.setOldValue(oldValue.substring(0,oldValue.length()-1));
                history.setLatestValue(latesdValue.substring(0,latesdValue.length()-1));
                history.setCreateBy(ContextHolder.get().getAceupUsername());
                history.setCtime(LocalDateTime.now());
                history.setRemark(param.getRemark());
                loserLevelHistoryMapper.insertSelective(history);
            }
        });
    }

    @Override
    public PageResult<loserLevelHistoryListQueryVO> listHistory(loserLevelHistoryListQueryParam param) {
        Example query = new Example(loserLevelHistory.class);
        Example.Criteria criteria = query.createCriteria();
        Optional.ofNullable(param.getUid()).filter(StringUtils::isNotBlank).ifPresent(obj -> criteria.andEqualTo("uid",obj));
        Optional.ofNullable(param.getOperator()).filter(StringUtils::isNotBlank).ifPresent(obj -> criteria.andLike("createBy",obj));
        Optional.ofNullable(param.getUpdateTimeStart()).ifPresent(obj -> criteria.andGreaterThanOrEqualTo("ctime",TemporalUtil.ofEpochMilli(obj)));
        Optional.ofNullable(param.getUpdateTimeEnd()).ifPresent(obj -> criteria.andLessThanOrEqualTo("ctime",TemporalUtil.ofEpochMilli(obj)));

        PageResult<loserLevelHistory> historyPage = PageContext.selectPage(() -> loserLevelHistoryMapper.selectByExample(query));
        Set<String> collectUids = historyPage.getItems().stream().map(history -> history.getUid()).collect(Collectors.toSet());
        Map<String, User> userMap = userServiceApi.userMap(collectUids, null, null);

        List<loserLevelHistoryListQueryVO> resList = historyPage.getItems().stream().map(history -> VOConverter.fromDomain(history,userMap)).collect(Collectors.toList());

        PageResult<loserLevelHistoryListQueryVO> res = PageResult.copyPageNoItems(historyPage);
        res.setItems(resList);
        return res;
    }

    /**
     * get vip level (club or bwc)
     * @param clubLevel
     * @return
     */
    private VipInfo getVipInfoByClub(ClubLevel clubLevel) {
        VipInfo vipInfo = new VipInfo();
        if (Objects.isNull(clubLevel.getloserLevel()) || clubLevel.getloserLevel() == -1){
            //get club_level
            vipInfo.setVipLevel(VipEnum.LV.getType() + clubLevel.getClubLevel());
            vipInfo.setValidUntil(TemporalUtil.toEpochMilli(clubLevel.getValidUntil()));
        }else {
            //get loser_level
            vipInfo.setVipLevel(VipEnum.BWC.getType() + clubLevel.getloserLevel());
            vipInfo.setValidUntil(TemporalUtil.toEpochMilli(clubLevel.getloserUntil()));
        }
        return vipInfo;
    }
}

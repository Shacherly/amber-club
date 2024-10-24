package com.loser.backend.club.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.loser.backend.club.client.IUserTagServiceApi;
import com.loser.backend.club.common.enums.StatusEnum;
import com.loser.backend.club.common.enums.UserTagEnum;
import com.loser.backend.club.config.ExecutorConfiguration;
import com.loser.backend.club.domain.loserMission;
import com.loser.backend.club.domain.loserMissionBinding;
import com.loser.backend.club.mapper.loserMissionBindingMapper;
import com.loser.backend.club.mapper.loserMissionMapper;
import com.loser.backend.club.service.IMissionBindingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service @Slf4j
public class MissionBindingServiceImpl implements IMissionBindingService {

    @Autowired
    private loserMissionMapper missionMapper;
    @Autowired
    private loserMissionBindingMapper bindingMapper;
    @Autowired
    private IUserTagServiceApi userTagService;
    @Autowired
    private ExecutorConfiguration execuConf;

    private List<loserMissionBinding> buildBinding(Long missionId, List<String> uids) {
        if (CollectionUtil.isEmpty(uids)) return Collections.emptyList();
        LocalDateTime now = LocalDateTime.now();
        return uids.stream().map(uid -> new loserMissionBinding(uid, missionId, now, now)).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void missionBinding(loserMission mission) {
        if (!StatusEnum.isEnable(mission.getStatus())
                || StringUtils.isBlank(mission.getClientType())) {

            log.warn("Blank ClientType or Disable mission {} leads to no binding !", mission);
            return;
        }
        long totalUser = 0;
        BiFunction<Integer, Integer, List<String>> biFunction = null;
        Function<List<String>, Integer> handler = uids -> insertBinding(mission.getId(), uids);
        if (UserTagEnum.sinleTag(mission.getClientType())) {
            totalUser = userTagService.getTagTotal(mission.getClientAttaches());
            biFunction = (page, pageSize) -> userTagService.getTagUidPage(page, pageSize, mission.getClientAttaches()).getList();
        }
        else if (UserTagEnum.multiTag(mission.getClientType())) {
            totalUser = userTagService.getTagsTotal(mission.getClientAttaches());
            biFunction = (page, pageSize) -> userTagService.getTagsUidPage(page, pageSize, mission.getClientAttaches()).getList();
        }

        Example example = new Example(loserMission.class);
        example.setForUpdate(true);
        example.createCriteria().andEqualTo("id", mission.getId());
        example.selectProperties("id");
        missionMapper.selectByExample(example);

        Example deleExample = new Example(loserMissionBinding.class);
        deleExample.createCriteria().andEqualTo("missionId", mission.getId());
        bindingMapper.deleteByExample(deleExample);

        if (totalUser == 0) {
            log.warn("There is no one in condition of {} for mission {}", mission.getClientAttaches(), mission);
            return;
        }

        execuConf.serial(totalUser, biFunction, Function.identity(), handler, 500);

    }

    @Override
    public int insertBinding(List<loserMissionBinding> records) {
        return bindingMapper.insertList(records);
    }

    @Override
    public int insertBinding(Long missionId, List<String> uids) {
        return insertBinding(buildBinding(missionId, uids));
    }

}

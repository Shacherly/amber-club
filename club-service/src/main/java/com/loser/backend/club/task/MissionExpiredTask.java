package com.loser.backend.club.task;

import cn.hutool.core.date.DateUtil;
import com.loser.backend.club.common.enums.StatusEnum;
import com.loser.backend.club.domain.loserMission;
import com.loser.backend.club.mapper.loserMissionMapper;
import com.loser.backend.club.service.IBenefitService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author ：trading
 * @date ：Created in 2022/2/18 18:31
 * @description：任务过期
 * @modified By：
 */

@Service
@Slf4j
public class MissionExpiredTask {
    
    @Autowired
    private loserMissionMapper loserMissionMapper;
    @Autowired
    private ThreadPoolTaskScheduler scheduler;
    @Autowired
    private IBenefitService benefitService;

    private static final List<String> canExpiredList = Arrays.asList(StatusEnum.ENABLE.getIndicator(),StatusEnum.DISABLE.getIndicator());

    @XxlJob("missionExpired")
    @Async
    public void missionExpired() {
        log.info("#missionExpired start");
        doMissionExpired();
        log.info("#missionExpired end");
    }

    private void doMissionExpired() {
        
        List<loserMission> missionList = loadMissionToBeExpire();
        log.info("load expired mission num : {}", missionList.size());
        missionList.forEach(mission ->{
            scheduler.schedule(() -> benefitService.expireMission(mission.getId()), DateUtil.toInstant(mission.getValidEnd()));
        });
        
    }

    private List<loserMission> loadMissionToBeExpire() {
        //handle 30 minute in future
        LocalDateTime now = LocalDateTime.now();
        Example query = new Example(loserMission.class);
        query.createCriteria().andIn("status", canExpiredList)
                .andLessThanOrEqualTo("validEnd",now.plusMinutes(30));
        return loserMissionMapper.selectByExample(query);
    }

}

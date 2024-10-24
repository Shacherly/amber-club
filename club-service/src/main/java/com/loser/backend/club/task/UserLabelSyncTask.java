package com.loser.backend.club.task;

import com.loser.backend.club.service.IMissionService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ：trading
 * @date ：Created in 2022/2/18 18:31
 * @description：标签用户全量同步
 * @modified By：
 */

@Service
@Slf4j
public class UserLabelSyncTask {

    @Autowired
    private IMissionService missionService;

    @XxlJob("userLabelSync")
    public void userLabelSync() {
        log.info("#userLabelSync start");
        doUserLabelSync();
        log.info("#userLabelSync end");
    }

    private void doUserLabelSync() {
        missionService.syncLabelMission();
    }

}

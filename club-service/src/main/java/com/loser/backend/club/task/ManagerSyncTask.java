package com.loser.backend.club.task;

import com.loser.backend.club.client.IBwcCmServiceApi;
import com.loser.backend.club.service.IClientManagerService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ：trading
 * @date ：Created in 2022/2/18 18:31
 * @description：客户经理全量同步
 * @modified By：
 */

@Service
@Slf4j
public class ManagerSyncTask {

    @Autowired
    private IBwcCmServiceApi bwcCmServiceApi;
    @Autowired
    private IClientManagerService clientManagerService;

    @XxlJob("managerSync")
    public void managerSync() {
        log.info("#managerSync start");
        doManagerSync();
        log.info("#managerSync end");
    }

    private void doManagerSync() {
        bwcCmServiceApi.getAllBwcCm().stream()
                .map(manager -> {
                    manager.setDelete(false);
                    return manager;
                })
                .forEach(clientManagerService::saveOrUpdateDetail);
    }


}

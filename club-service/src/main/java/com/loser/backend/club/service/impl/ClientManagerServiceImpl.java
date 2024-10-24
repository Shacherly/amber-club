package com.loser.backend.club.service.impl;

import com.loser.backend.club.client.IUserServiceApi;
import com.loser.backend.club.domain.loserClientRelation;
import com.loser.backend.club.domain.loserManager;
import com.loser.backend.club.kafka.message.ClientManagerInfo;
import com.loser.backend.club.kafka.message.ClientRelation;
import com.loser.backend.club.kafka.message.UserTouchModel;
import com.loser.backend.club.mapper.loserClientRelationMapper;
import com.loser.backend.club.mapper.loserManagerMapper;
import com.loser.backend.club.pojo.ManagerProfileSetter;
import com.loser.backend.club.pojo.notify.NotifyTemplate;
import com.loser.backend.club.service.IClientManagerService;
import com.loser.backend.club.service.ICommonService;
import com.loser.backend.club.util.Converter;
import com.loser.backend.club.util.SensorsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.groovy.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

/**
 * @author ：trading
 * @date ：Created in 2022/2/24 12:01
 * @description：客户经理
 * @modified By：
 */

@Service
@Slf4j
public class ClientManagerServiceImpl implements IClientManagerService {

    @Autowired
    private loserManagerMapper loserManagerMapper;
    @Autowired
    private loserClientRelationMapper loserClientRelationMapper;
    @Autowired
    private ICommonService commonService;
    @Autowired
    private IUserServiceApi userServiceApi;
    @Autowired
    private SensorsUtil sensor;
    @Autowired
    private NotifyTemplate notifyTemplate;

    @Override
    public void saveOrUpdateDetail(ClientManagerInfo info) {

        if (info.getDelete()){
            deleteManagerByCmid(info.getCmid());
            return;
        }

        loserManager manager = Converter.fromRemote(info);

        loserManagerMapper.upsertloserManager(manager);

    }

    @Override
    @Transactional
    public void saveOrUpdateMapping(ClientRelation relation) {

        lockByUid(relation.getCid());

        //however, delete by cid
        deleteRelationByUid(relation.getCid());

        if (relation.getDelete()){
            return;
        }

        loserClientRelation clientRelation = Converter.fromRemote(relation);

        saveMapping(clientRelation);

        sendTouchThenTrack(clientRelation.getUid(), clientRelation.getMid());
    }

    private void lockByUid(String uid) {
        Example lockExample =  new Example(loserClientRelation.class);
        lockExample.createCriteria().andEqualTo("uid", uid);
        lockExample.setForUpdate(true);
        loserClientRelationMapper.selectOneByExample(lockExample);
    }

    private void saveMapping(loserClientRelation relation) {
        loserClientRelationMapper.insertSelective(relation);
    }

    private void deleteRelationByUid(String uid) {
        Example delete = new Example(loserClientRelation.class);
        delete.createCriteria().andEqualTo("uid",uid);
        loserClientRelationMapper.deleteByExample(delete);
    }

    private void deleteManagerByCmid(String cmid) {
        Example delete = new Example(loserManager.class);
        delete.createCriteria().andEqualTo("managerIdentity",cmid);
        loserManagerMapper.deleteByExample(delete);
    }

    private void saveManager(loserManager manager) {
        manager.setCtime(LocalDateTime.now());
        loserManagerMapper.insertSelective(manager);
    }

    private void updateManager(loserManager manager) {
        Example query = new Example(loserManager.class);
        query.createCriteria().andEqualTo("managerIdentity", manager.getManagerIdentity());
        manager.setCtime(null);//do not update create_time
        manager.setUtime(LocalDateTime.now());
        loserManagerMapper.updateByExampleSelective(manager, query);
    }

    private void sendTouchThenTrack(String uid, String mid) {
        Example query = new Example(loserManager.class);
        query.createCriteria().andEqualTo("managerIdentity", mid);
        Optional.ofNullable(loserManagerMapper.selectOneByExample(query))
                .ifPresent(manager ->{
                    userServiceApi.kafkaNotify(uid, Collections.singletonList(notifyTemplate.getOfPush().getUpdateClientManager()),
                            Maps.of("name", manager.getloserName().getString("en-US")),
                            Collections.singletonList(UserTouchModel.TYPE_AURORA), null
                    );

                    log.info("UpdatingManager, {} for uid {}", manager, uid);
                    ManagerProfileSetter profile = ManagerProfileSetter.getProfile(manager, uid);
                    sensor.profileSet(profile);
                });
    }

}

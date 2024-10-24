package com.loser.backend.club.service;

import com.loser.backend.club.kafka.message.ClientManagerInfo;
import com.loser.backend.club.kafka.message.ClientRelation;

public interface IClientManagerService {

    void saveOrUpdateDetail(ClientManagerInfo info);

    void saveOrUpdateMapping(ClientRelation relation);

}

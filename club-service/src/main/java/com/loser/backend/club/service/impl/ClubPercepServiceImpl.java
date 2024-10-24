package com.loser.backend.club.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.loser.backend.club.kafka.message.ClubUpgradeModel;
import com.loser.backend.club.kafka.publisher.KafkaPublisher;
import com.loser.backend.club.service.IClubPercepService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author ~~ trading.s
 * @date 16:50 01/14/22
 * @desc
 */
@Service @Slf4j
public class ClubPercepServiceImpl implements IClubPercepService {

    @Autowired
    private KafkaPublisher publisher;
    @Value("${topics.publisher.club-upgrade}")
    private String clubUpdate;

    @Override
    public void clubUpdateNofity(List<ClubUpgradeModel> models) {
        if (CollectionUtil.isEmpty(models)) return;
        models.forEach(val -> publisher.publish(clubUpdate, val));
    }
}

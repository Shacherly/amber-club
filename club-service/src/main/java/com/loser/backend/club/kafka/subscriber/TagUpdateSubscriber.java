package com.loser.backend.club.kafka.subscriber;


import com.alibaba.fastjson.JSON;
import com.loser.backend.club.service.IMissionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 * @author ~~ trading.luo
 * @date 14:27 02/23/22
 */
@Component @Slf4j
@Profile({"dev", "sit", "uat", "prod"})
public class TagUpdateSubscriber {

    @Autowired
    private IMissionService missionService;

    @KafkaListener(topics = "${topics.subscriber.aceup-tag-update}")
    public void onManagerUpdate(ConsumerRecord<?, ?> record) {
        Optional.ofNullable(record.value())
                .map(String::valueOf)
                .map(JSON::parseObject)
                .ifPresent(value ->{
                    log.info("receive tag msg : {}", value);
                    if ("tag".equals(value.getString("key"))){
                        missionService.syncLabelMission();
                    }
                });
    }
}

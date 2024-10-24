package com.loser.backend.club.kafka.subscriber;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loser.backend.club.config.ExecutorConfiguration;
import com.loser.backend.club.kafka.message.ClientManagerInfo;
import com.loser.backend.club.kafka.message.ClientRelation;
import com.loser.backend.club.service.IClientManagerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 * @author ~~ trading.Shi
 * @date 14:27 02/23/22
 */
@Component @Slf4j
@Profile({"dev", "sit", "uat", "prod"})
public class ClientManagerSubscriber {

    @Autowired
    private ExecutorConfiguration executeConf;
    @Autowired
    private IClientManagerService clientManagerService;


    @KafkaListener(topics = "${topics.subscriber.aceup-manager-sync}", groupId = "${topics.group.aceup-manager-sync}")
    public void onManagerUpdate(ConsumerRecord<?, ?> record) {
        executeConf.getEXECUTOR().execute(() -> {
            Optional.ofNullable(record.value())
                    .map(String::valueOf)
                    .map(JSON::parseObject)
                    .ifPresent(value ->{
                        log.info("receive manager msg : {}", value);
                        String type = value.getString("type");
                        JSONObject data = value.getJSONObject("data");
                        if ("MAPPING".equals(type)){
                            //客户与客户经理关系
                            ClientRelation relation = data.toJavaObject(ClientRelation.class);
                            clientManagerService.saveOrUpdateMapping(relation);
                        }else if ("DETAIL".equals(type)){
                            //客户经理BWC信息
                            ClientManagerInfo managerInfo = data.toJavaObject(ClientManagerInfo.class);
                            clientManagerService.saveOrUpdateDetail(managerInfo);
                        }
                    });
        });
    }
}

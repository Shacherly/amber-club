package com.loser.backend.club.kafka.publisher;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;


/**
 * @author ~~ trading.s
 * @date 19:16 10/29/21
 */
@Slf4j
@Component
public class KafkaPublisher {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    /**
     * pushlish message
     * @param topic
     * @param model
     */
    public void publish(String topic, AbstractProducerModel model) {
        String message = JSONObject.toJSONString(model);
        try {
            kafkaTemplate.send(topic, message);
            log.info("PUBLISHED_MESSAGE {} TO TOPIC {}", message, topic);
        } catch (Exception e) {
            // log.error("PUBLISH_MESSAGE {} TO TOPIC {} FAILED, cause {}", message, topic, e.getMessage(), e);
            throw new RuntimeException(
                    MessageFormat.format("PUBLISH_MESSAGE {0} TO TOPIC {1} FAILED, cause {2}", message, topic, e.getMessage()));
        }
    }
}

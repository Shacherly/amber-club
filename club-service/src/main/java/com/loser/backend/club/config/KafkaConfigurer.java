package com.loser.backend.club.config;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

/**
 * @author ~~ trading.s
 * @date 16:21 01/14/22
 * @desc
 */
@Configuration
public class KafkaConfigurer {

    @Bean
    public KafkaListenerContainerFactory<?> batchListenerContainerFactory(KafkaProperties kafkaProperties) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        Map<String, Object> properties = kafkaProperties.getConsumer().buildProperties();
        properties.putAll(kafkaProperties.buildAdminProperties());
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(properties));
        factory.setBatchListener(true);
        return factory;
    }

}

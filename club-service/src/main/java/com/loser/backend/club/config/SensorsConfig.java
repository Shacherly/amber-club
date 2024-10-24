package com.loser.backend.club.config;

import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

/**
 * @author ：trading
 * @date ：Created in 2021/12/24 17:02
 * @description：神策配置类
 * @modified By：
 */

@Configuration
@Slf4j
public class SensorsConfig {

    @Value("${sensors.log-path}")
    private String logPath;

    @Bean
    public SensorsAnalytics sa() throws IOException {
        File file = new File(logPath);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdir();
        }
        return new SensorsAnalytics(new SensorsAnalytics.ConcurrentLoggingConsumer(logPath));
    }


}

package com.loser.backend.club;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


// @EnableFeignClients("com.trading.backend.club")
// @MapperScan(basePackages = "com.trading.backend.club.mapper")
@SpringBootApplication(
        exclude = {
                SecurityAutoConfiguration.class,
                ManagementWebSecurityAutoConfiguration.class
        }
)
// @EnableConfigurationProperties
@EnableScheduling
@EnableAsync
public class ClubServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(ClubServiceApplication.class, args);
    }

}

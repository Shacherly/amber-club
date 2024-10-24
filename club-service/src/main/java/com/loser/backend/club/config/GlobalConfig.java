package com.loser.backend.club.config;


import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @author ~~trading
 * @date 18:19 2022/03/23
 */
@Component @Setter
@ConfigurationProperties(prefix = "system-property")
public class GlobalConfig {


    private String blueloserAccessible;

    public boolean loserAccessible() {
        return StringUtils.equalsIgnoreCase(blueloserAccessible, "true");
    }
}

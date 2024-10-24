package com.loser.backend.club.pojo.notify;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * @author ~~trading
 * @date 12:45 2022/03/16
 */
@Getter @Configuration @Setter
@ConfigurationProperties(prefix = "notify.template")
@EnableConfigurationProperties(NotifyTemplate.class)
public class NotifyTemplate {


    private OfEmail ofEmail;

    // private OfShortMsg ofShortMsg;

    // private OfInnerMsg ofInnerMsg;

    private OfPush ofPush;


    @Getter @Setter
    public static class OfEmail {

        private String firstBlueloser;
        private String upgradeBuleloser;
    }

    @Getter @Setter
    public static class OfShortMsg {
    }

    @Getter @Setter
    public static class OfInnerMsg {
    }

    @Getter @Setter
    public static class OfPush {
        private String upgradeBuleloser;
        private String updateClientManager;
    }

}


package com.loser.backend.club.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @author ~~ trading.s
 * @date 12:02 10/18/21
 */
@Getter @Setter
@Component
@ConfigurationProperties(prefix = "remote-call")
public class RemoteServerProperty {


    private String domain = "https://internal-gateway-dev.loserainsider.com";

    private CouponServer couponServer;

    private EarnServer earnServer;

    @Getter @Setter
    public static class CouponServer {

        private String couponDetail = "11";

        private String clubPossess = "22";

        private String periodPossess;
    }

    @Getter @Setter
    public static class EarnServer {
        private String earnProductList;

        private String bwcEarnProducts;
    }
}

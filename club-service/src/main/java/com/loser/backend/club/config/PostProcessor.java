package com.loser.backend.club.config;


import io.sentry.Sentry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * @author ~~ trading.s
 * @date 11:20 10/15/21
 */
@Order(1)
@Component
public class PostProcessor implements ApplicationRunner {

    @Value("${spring.profiles.active}")
    private String env;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Sentry.init(options -> {
            options.setDsn("http://f9d1f5d733c44ce1ad2cb76a9ba000a9@10.140.34.30:9000/29");
            options.setTracesSampleRate(1.0);
            options.setEnvironment(env);
            options.setDebug(true);
        });
    }
}

package com.loser.backend.club.api.fallback;


import com.loser.backend.club.api.ClubServiceApi;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author ~~trading
 * @date 16:05 04/07/22
 */
@Slf4j
@Component
public class ClubApiFallbackFactory implements FallbackFactory<ClubServiceApi> {


    @Override
    public ClubServiceApi create(Throwable throwable) {
        throw new RuntimeException("Fallback need to be overridden!");
    }
}

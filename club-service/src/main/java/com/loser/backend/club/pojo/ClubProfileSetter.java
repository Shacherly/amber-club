package com.loser.backend.club.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.loser.backend.club.domain.ClubLevel;
import com.loser.backend.club.pojo.sensor.AbstractSensorModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;


/**
 * @author ~~trading
 * @date 17:55 2022/03/16
 */
@EqualsAndHashCode(callSuper = true) @Service @Data @Slf4j
public class ClubProfileSetter extends AbstractSensorModel {


    @JSONField(name = "user_level")
    private String userLevel;


    public static ClubProfileSetter getProfile(ClubLevel formaer, ClubLevel present) {
        if (Objects.equals(formaer.getClubLevel(), present.getClubLevel())
                && Objects.equals(formaer.getloserLevel(), present.getloserLevel())) {
            return null;
        }
        ClubProfileSetter profile = new ClubProfileSetter();
        profile.setUid(present.getUid());
        profile.setUserLevel(
                Optional.ofNullable(present.getloserLevel()).filter(level -> level > 0)
                        .map(level -> "bwc" + level).orElseGet(() -> "vip" + present.getClubLevel())
        );
        return profile;
    }
}

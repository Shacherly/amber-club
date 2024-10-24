package com.loser.backend.club.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.loser.backend.club.domain.loserManager;
import com.loser.backend.club.pojo.sensor.AbstractSensorModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * @author ~~trading
 * @date 17:55 2022/03/16
 */
@EqualsAndHashCode(callSuper = true)
@Service
@Data
@Slf4j @Accessors(chain = true)
public class ManagerProfileSetter extends AbstractSensorModel {


    @JSONField(name = "relationship_manager")
    private String relationshipManager;

    public static ManagerProfileSetter getProfile(loserManager manager, String uid) {
        ManagerProfileSetter profile = new ManagerProfileSetter().setRelationshipManager(manager.getloserName().getString("en-US"));
        profile.setUid(uid);
        return profile;
    }
}

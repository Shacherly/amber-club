package com.loser.backend.club.util;

import com.alibaba.fastjson.JSONObject;
import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
import com.loser.backend.club.pojo.sensor.AbstractSensorModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Map;

/**
 * @author ：trading
 * @date ：Created in 2021/12/24 17:09
 * @description：神策工具类
 * @modified By：
 */
@Component
@Slf4j
public class SensorsUtil {

    @Resource(name = "sa")
    private SensorsAnalytics sa;

    /**
     * 写入本地日志，使用logAgent来处理
     * @param uid
     * @param event
     * @param properties
     */
    public void track(String uid, String event,  Map<String, Object> properties) {
        try {
            sa.track(uid, true, event, properties);
            sa.flush();
        } catch (Exception e) {
            log.error("Sensors track exception {}", e.getMessage(), e);
        }
    }

    public void track(String event, AbstractSensorModel model) {
        try {
            sa.track(model.getUid(), true, event, (JSONObject) JSONObject.toJSON(model));
            log.info("Tracked model {} for event {}", model, event);
            sa.flush();
        } catch (Exception e) {
            log.error("Sensors track exception {}", e.getMessage(), e);
        }
    }

    public void profileSet(AbstractSensorModel model) {
        try {
            sa.profileSet(model.getUid(), true, (JSONObject) JSONObject.toJSON(model));
            log.info("ProfileSettled model {}", model);
            sa.flush();
        } catch (Exception e) {
            log.error("Sensors profileSet exception {}", e.getMessage(), e);
        }
    }

}

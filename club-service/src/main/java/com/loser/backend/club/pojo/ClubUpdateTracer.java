package com.loser.backend.club.pojo;


import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.loser.backend.club.domain.ClubLevel;
import com.loser.backend.club.pojo.sensor.AbstractSensorModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * @author ~~trading
 * @date 11:38 03/03/22
 */
@EqualsAndHashCode(callSuper = true) @Service @Data @Slf4j
public class ClubUpdateTracer extends AbstractSensorModel {
    private static final long serialVersionUID = 5944028669285352570L;
    public static final DateTimeFormatter FORMATTER_1 = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss");

    @JSONField(name = "level_before")
    private String levelBefore;
    @JSONField(name = "level_after")
    private String levelAfter;
    @JSONField(name = "relationship_manager")
    private String relationshipManager;
    @JSONField(name = "modify_time")
    private Long modifyTime;
    @JSONField(name = "update_result")
    private String updateResult;
    @JSONField(name = "net_assets")
    private String netAssets;

    private static final Map<String, Integer> STEP_MAP = new HashMap<>();

    static {
        STEP_MAP.put("LV-1", 0);
        STEP_MAP.put("LV0", 0);
        STEP_MAP.put("LV1", 1);
        STEP_MAP.put("LV2", 2);
        STEP_MAP.put("LV3", 3);
        STEP_MAP.put("LV4", 4);
        STEP_MAP.put("LV5", 5);
        STEP_MAP.put("LV6", 6);
        STEP_MAP.put("LV7", 7);
        STEP_MAP.put("LV8", 7);
        STEP_MAP.put("LV9", 8);
        STEP_MAP.put("LV10", 9);
        STEP_MAP.put("BWC1", 7);
        STEP_MAP.put("BWC2", 8);
        STEP_MAP.put("BWC3", 9);

    }


    public static ClubUpdateTracer getTracer(ClubLevel former, ClubLevel present, LocalDateTime instant, ClientMapping clientMapping) {
        ClubUpdateTracer tracer = new ClubUpdateTracer();
        tracer.setLevelBefore(
                Optional.ofNullable(former.getloserLevel()).filter(level -> level > 0)
                        .map(level -> "BWC" + level).orElseGet(() -> "LV" + former.getClubLevel())
        );
        tracer.setLevelAfter(
                Optional.ofNullable(present.getloserLevel()).filter(level -> level > 0)
                        .map(level -> "BWC" + level).orElseGet(() -> "LV" + present.getClubLevel())
        );
        int step = STEP_MAP.get(tracer.getLevelAfter()) - STEP_MAP.get(tracer.getLevelBefore());
        if (step == 0) return null;
        tracer.setRelationshipManager(Optional.ofNullable(clientMapping).map(ClientMapping::getCmname).orElse(""));
        tracer.setModifyTime(DateUtil.toInstant(instant).toEpochMilli());
        tracer.setUpdateResult(step > 0 ? "levelup" + step : "leveldown" + (-step));
        tracer.setUid(former.getUid());
        tracer.setNetAssets(String.valueOf(present.getClubPoint() * 100));
        log.info("ClubUpdateTracerUpdate {} at uid {}", tracer.getUpdateResult(), former.getUid());
        return tracer;
    }
}

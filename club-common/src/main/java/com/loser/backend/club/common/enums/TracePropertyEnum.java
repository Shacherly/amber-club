package com.loser.backend.club.common.enums;


import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


/**
 * @author ~~ trading.Shi
 * @date 10:23 03/03/22
 */
@Getter
public enum TracePropertyEnum {


    UPDATE_USER_LEVEL("UpdateUserLevel"),


    ;


    private final String name;

    TracePropertyEnum(String name) {
        this.name = name;
    }

    public static final Map<TracePropertyEnum, Map<String, Object>> PROPERTY_MAPPING;


    private static final Map<String, Object> TRACE_PROPERTY;
    static {
        TRACE_PROPERTY = new HashMap<>();
        TRACE_PROPERTY.put("level_before", null);
        TRACE_PROPERTY.put("level_after", null);
        TRACE_PROPERTY.put("relationship_manager", null);
        TRACE_PROPERTY.put("modify_time", null);
        TRACE_PROPERTY.put("net_assets", null);
        TRACE_PROPERTY.put("update_result", null);


        PROPERTY_MAPPING = new HashMap<>();
        PROPERTY_MAPPING.put(UPDATE_USER_LEVEL, TRACE_PROPERTY);
    }
}

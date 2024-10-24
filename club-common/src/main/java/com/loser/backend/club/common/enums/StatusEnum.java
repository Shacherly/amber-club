package com.loser.backend.club.common.enums;


import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum StatusEnum {


    DISABLE("DISABLE", 0),

    ENABLE("ENABLE", 1),

    DELETED("DELETED", 2),

    EXPIRED("EXPIRED", 3),

    ;

    private final String indicator;
    private final Integer phase;

    StatusEnum(String indicator, Integer phase) {
        this.indicator = indicator;
        this.phase = phase;
    }

    public static boolean isEnable(String status) {
        return StringUtils.equalsIgnoreCase(status, ENABLE.getIndicator());
    }
}

package com.loser.backend.club.common.enums;


import lombok.Getter;

@Getter
public enum MissionTypeEnum {

    ROUTINE("ROUTINE"),

    CUSTOM("CUSTOM"),
    ;


    private final String type;

    MissionTypeEnum(String type) {
        this.type = type;
    }
}

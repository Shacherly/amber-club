package com.loser.backend.club.common.enums;


import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum RoutineMissionTypeEnum {

    EXCLUSIVE_EARN("EXCLUSIVE_EARN"),

    EXCLUSIVE_COUPON("EXCLUSIVE_COUPON"),

    INDUSTRY_REPORT("INDUSTRY_REPORT"),


    ;


    private final String type;


    RoutineMissionTypeEnum(String type) {
        this.type = type;
    }


    public static List<String> getRoutineMissionTypes() {
        RoutineMissionTypeEnum[] values = RoutineMissionTypeEnum.values();
        return Arrays.stream(values).map(Enum::name).collect(Collectors.toList());
    }
}

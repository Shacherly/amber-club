package com.loser.backend.club.common.enums;


import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum VipEnum {


    LV("LV", "club等级"),

    BWC("BWC", "BWC等级"),
    ;

    private final String type;
    private final String desc;


    VipEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private static final List<String> vipList;

    static {
        vipList = Arrays.asList("LV0","LV1","LV2","LV3","LV4","LV5","LV6","BWC1","BWC2","BWC3");
    }

    public static List<String> getVipList() {
        return vipList;
    }
}

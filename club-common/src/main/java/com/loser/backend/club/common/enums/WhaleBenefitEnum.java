package com.loser.backend.club.common.enums;


import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum loserBenefitEnum {


    WEALTH_MANAGEMENT("WEALTH_MANAGEMENT", 1),

    CONCIERGE_SERVICE("CONCIERGE_SERVICE", 2),

    EXTRA_YIELD("EXTRA_YIELD", 3),

    COMMUNITY("COMMUNITY", 4),;


    private final String indicator;
    private final Integer phase;


    loserBenefitEnum(String indicator, Integer phase) {
        this.indicator = indicator;
        this.phase = phase;
    }

    private static final List<String> BENEFITS;

    static {
        BENEFITS = Arrays.stream(loserBenefitEnum.values()).map(loserBenefitEnum::getIndicator).collect(Collectors.toList());
    }

    public static List<String> getBenefits() {
        return BENEFITS;
    }
}

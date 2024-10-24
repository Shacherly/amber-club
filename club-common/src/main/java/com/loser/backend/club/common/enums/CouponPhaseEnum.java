package com.loser.backend.club.common.enums;


import lombok.Getter;

import java.util.Objects;

@Getter
public enum CouponPhaseEnum {

    REDEEM("REDEEM", -1),

    TO_CONSUME("TO_CONSUME", 0),

    PENDING("PENDING", 2),

    CONSUMED("CONSUMED", 6),



    ;


    private final String phase;
    private final Integer status;

    CouponPhaseEnum(String phase, Integer status) {
        this.phase = phase;
        this.status = status;
    }

    public static String getPhase(Integer status) {
        CouponPhaseEnum[] values = CouponPhaseEnum.values();
        for (CouponPhaseEnum value : values) {
            if (Objects.equals(status, value.getStatus())) {
                return value.getPhase();
            }
        }
        throw new IllegalArgumentException("Illegal CouponPhase status of " + status);
    }
}

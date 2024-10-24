package com.loser.backend.club.common.enums;


import lombok.Getter;


@Getter
public enum CouponTypeEnum {

    /**
     * 加息券
     */
    INTEREST("0", 0),

    /**
     * 减息券-折扣减免
     */
    DEDUCTION_1("11", 1),

    /**
     * 减息券-利率减免
     */
    DEDUCTION_2("12", 1),


    /**
     * 资产券（返现券）
     */
    CASHRETURN("5", 5),

    ;


    private final String type;
    private final Integer code;

    CouponTypeEnum(String type, Integer code) {
        this.type = type;
        this.code = code;
    }

    public static void main(String[] args) {
        System.out.println(DEDUCTION_2);
        System.out.println(DEDUCTION_2.name());
        System.out.println(DEDUCTION_2.ordinal());
    }
}

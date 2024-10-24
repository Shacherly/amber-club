package com.loser.backend.club.common.enums;

public enum ClubGiftStatusEnum {

    /**
     * 下个等级可领取
     */
    NEXT_LEVEL(-2),

    /**
     * 当前等级可领取
     */
    CURRENT_LEVEL(-1),

    /**
     * 已持有-待使用
     */
    TO_USE(0),

    /**
     * 已过期（无关乎领取与否）
     */
    EXPIRED(1),

    /**
     * 已使用、已激活 Working、Contributing、Running
     */
    CONTRIBUTING(2),

    /**
     * 已失效-Disabled by system admin
     */
    DISABLED(3),

    /**
     * 发放失败-Early termination、Activate failed
     */
    BREAK_OFF(4),

    /**
     * 已发放-Ending、Mature、Arrival
     */
    COMPLETE(6),
    ;

    private Integer status;


    ClubGiftStatusEnum(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}

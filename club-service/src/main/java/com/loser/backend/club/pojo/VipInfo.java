package com.loser.backend.club.pojo;

import lombok.Data;

/**
 * @author ：trading
 * @date ：Created in 2022/3/8 10:36
 * @description：vip信息
 * @modified By：
 */

@Data
public class VipInfo {

    //vip等级（LV0,LV1,LV2,LV3,LV4,LV5,LV6,BWC1,BWC2,BWC3）
    private String vipLevel;

    //vip有效期
    private Long validUntil;

}

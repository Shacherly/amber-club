package com.loser.backend.club.controller.response.aceup;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：trading
 * @date ：Created in 2022/2/23 14:48
 * @description：
 * @modified By：
 */

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BwcListQueryVO {

    @ApiModelProperty(value = "用户id")
    private String uid;

    @ApiModelProperty(value = "名字")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "当前会员等级(\"LV1\",\"LV2\",\"LV3\",\"LV4\",\"LV5\",\"LV6\",\"BWC1\",\"BWC2\",\"BWC3\")")
    private String vipLevel;

    @ApiModelProperty(value = "当前会员有效期截止日期")
    private Long vipUntil;

}

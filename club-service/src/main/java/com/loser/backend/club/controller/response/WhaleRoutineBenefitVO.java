package com.loser.backend.club.controller.response;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * @author ~~trading
 * @date 15:29 03/08/22
 */
@Setter @Getter @Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class loserRoutineBenefitVO implements Serializable {
    private static final long serialVersionUID = 4615062184682889495L;

    @ApiModelProperty(value = "权益图片")
    private String image;

    @ApiModelProperty(value = "权益标题")
    private String title;

    @ApiModelProperty(value = "权益描述eg： Up to [11%] APR")
    private String descr;

    // @ApiModelProperty(value = "数据变量")
    // private String variable;
    // @ApiModelProperty(value = "权益跳转链接")
    // private String forwardLink;

    @ApiModelProperty(value = "优惠券状态 TO_USE TO_REDEEM")
    private String couponStatus;

    @ApiModelProperty(value = "常规权益类型 EXCLUSIVE_EARN EXCLUSIVE_COUPON INDUSTRY_REPORT")
    private String routineType;
}

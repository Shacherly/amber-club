package com.loser.backend.club.controller.response;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author ~~ trading.Shi
 * @date 11:07 02/14/22
 */
@Setter @Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BenefitCouponVO implements Serializable {
    private static final long serialVersionUID = -8569444980267145851L;

    @ApiModelProperty(value = "券id")
    private Long couponId;

    @ApiModelProperty(value = "多语言化券标题")
    private String couponTitle;

    @ApiModelProperty(value = "多语言化券描述")
    private String couponDescr;

    @ApiModelProperty(value = "具体券的业务进行状态（对应按钮状态）：初始状态默认0是未使用的状态</br>" +
            "->加息券(0使用1已过期3已禁用6已使用)；<br>" +
            "->资产券(0激活1已过期2待发放3已禁用4发放失败6已发放)；<br>" +
            "->减息券(0使用1已过期3已禁用6已使用)")
    private Integer businessStage;

    @ApiModelProperty(value = "券状态")
    private String couponPhase;

    @ApiModelProperty(value = "券类型</br> " +
            "INTEREST 加息券</br> " +
            "DEDUCTION_1 减息券-折扣减免</br> " +
            "DEDUCTION_2 减息券-利率减免</br> " +
            "CASHRETURN 资产券")
    private String couponType;

    @ApiModelProperty(value = "券的抽象价值（加息率、资产券金额、减息率、减息比例）")
    private String worth;

    @ApiModelProperty(value = "资产券币种")
    private String couponCoin;

    @ApiModelProperty(value = "有效期")
    private String validUntil;

    @ApiModelProperty(value = "有效结束时刻")
    private Long availableEnd;
}

package com.loser.backend.club.controller.response;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.loser.backend.club.pojo.ManagerContact;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;


/**
 * @author ~~ trading.Shi
 * @date 11:07 02/14/22
 */
@Setter @Getter @Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BlueloserHomeVO implements Serializable {
    private static final long serialVersionUID = -8143637910799758683L;

    // @ApiModelProperty(value = "用户权益")
    // private List<loserBenefitVO> benefits;

    // @ApiModelProperty(value = "四大权益名称")
    // private List<String> benefits;
    //
    // @ApiModelProperty(value = "四大权益标识（排序和名称一一对应）")
    // private List<String> benefitIndex;
    //
    // @ApiModelProperty(value = "四大权益名称和标识对应")
    // private Map<String, String> benefits;
    @ApiModelProperty(value = "权益标识和名称")
    private List<loserBenefitIndexVO> benefitIndexs;

    @ApiModelProperty(value = "cm名称")
    private String managerName;

    @ApiModelProperty(value = "cm头像")
    private String managerPhoto;

    @ApiModelProperty(value = "cm简介")
    private String managerResume;

    @ApiModelProperty(value = "客户经理联系方式")
    private ManagerContact managerContact;

    @ApiModelProperty(value = "专属利率")
    private String upToApr;

    @ApiModelProperty(value = "可用优惠券数量")
    private Integer coupons;

    @ApiModelProperty(value = "优惠券状态 TO_USE TO_REDEEM")
    private String couponStatus;

    @ApiModelProperty(value = "研报期数")
    private Integer periods;

    @ApiModelProperty(value = "可用权益数")
    private Integer benefitQuantity;

    // @ApiModelProperty(value = "常规权益列表")
    // private List<loserRoutineBenefitVO> routineBenefits;

    @ApiModelProperty(value = "定制权益展示")
    private List<loserCustomBenefitVO> customBenefits;

    //
    // public static BlueloserHomeVO mock() {
    //     return new BlueloserHomeVO()
    //             .setBenefits(loserBenefitEnum.getBenefits())
    //             .setManagerName("handsome")
    //             .setManagerPhoto("https://img0.baidu.com/it/u=4168010673,707269819&fm=253&fmt=auto&app=120&f=JPEG?w=700&h=700")
    //             .setManagerContact(ManagerContact.mock())
    //             .setUpToApr("0.11")
    //             .setCoupons(10)
    //             .setPeriods(2)
    //             .setCustomBenefits(Lists.newArrayList(loserCustomBenefitVO.mock(), loserCustomBenefitVO.mock(), loserCustomBenefitVO.mock()));
    // }
}

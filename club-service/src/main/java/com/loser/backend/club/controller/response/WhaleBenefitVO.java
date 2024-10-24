package com.loser.backend.club.controller.response;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.StringJoiner;


/**
 * @author ~~ trading.Shi
 * @date 11:07 02/14/22
 */
@Setter @Getter @AllArgsConstructor @NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class loserBenefitVO implements Serializable {
    private static final long serialVersionUID = -3921926078069476425L;

    @ApiModelProperty(value = "是否解锁")
    private Boolean claimed;

    @ApiModelProperty(value = "权益名称")
    private String benefitName;

    @ApiModelProperty(value = "权益logo")
    private String benefitLogo;

    @ApiModelProperty(value = "权益介绍")
    private String benefitDescr;

    @ApiModelProperty(value = "适用等级")
    private String applyLevel;

    // @ApiModelProperty(value = "客户经理联系方式")
    // private ManagerContact managerContact;

    @Override
    public String toString() {
        return new StringJoiner(", ", loserBenefitVO.class.getSimpleName() + "[", "]")
                .add("claimed=" + claimed)
                .add("benefitName='" + benefitName + "'")
                .add("benefitLogo='" + benefitLogo + "'")
                .add("benefitDescr='" + benefitDescr + "'")
                .add("applyLevel='" + applyLevel + "'")
                // .add("managerContact=" + managerContact)
                .toString();
    }
}

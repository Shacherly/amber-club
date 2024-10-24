package com.loser.backend.club.controller.response;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


/**
 * @author ~~ trading.Shi
 * @date 11:07 02/14/22
 */
@Setter @Getter @AllArgsConstructor @NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BenefitEarnVO implements Serializable {
    private static final long serialVersionUID = -8466372844877914642L;


    @ApiModelProperty(value = "基础利率")
    private String basicRate;

    @ApiModelProperty(value = "加息利率")
    private String extraRate;

    @ApiModelProperty(value = "产品id")
    private String productId;

    // @ApiModelProperty(value = "产品名称")
    // private String productName;

    @ApiModelProperty(value = "申购天数")
    private Integer duration;

    @ApiModelProperty(value = "适用币种")
    private String applyCoin;


    public static BenefitEarnVO mock1() {
        return new BenefitEarnVO("0.001", "0.002", "aa14xzca123", 90, "BTC");
    }

    public static BenefitEarnVO mock2() {
        return new BenefitEarnVO("0.002", "0.002", "aa14xzssscaasad123", 90, "ETH");
    }

    public static List<BenefitEarnVO> mock() {
        return Lists.newArrayList(mock1(), mock2());
    }
}

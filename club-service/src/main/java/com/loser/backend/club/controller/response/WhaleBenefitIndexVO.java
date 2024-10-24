package com.loser.backend.club.controller.response;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.loser.backend.club.domain.base.MultiLocaliable;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * @author ~~ trading.Shi
 * @date 11:07 02/14/22
 */
@Setter @Getter @Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class loserBenefitIndexVO implements Serializable, MultiLocaliable {
    private static final long serialVersionUID = -6221673955977675923L;


    @ApiModelProperty(value = "权益标识符")
    private String indicator;

    @ApiModelProperty(value = "权益多语言名称")
    private String name;

    public loserBenefitIndexVO(String indicator, String name) {
        this.indicator = indicator;
        this.name = name;
    }

    public loserBenefitIndexVO() {
    }

    @Override
    public String toString() {
        return "loserBenefitIndexVO{" +
                "indicator='" + indicator + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
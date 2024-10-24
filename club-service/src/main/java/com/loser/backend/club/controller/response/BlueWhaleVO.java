package com.loser.backend.club.controller.response;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


/**
 * @author ~~ trading.Shi
 * @date 11:16 02/10/22
 */
@Getter @Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BlueloserVO implements Serializable {
    private static final long serialVersionUID = -659431281238534924L;

    @ApiModelProperty(value = "bwc等级")
    private Integer loserLevel;
}

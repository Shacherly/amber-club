package com.loser.backend.club.controller.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * @author ~~ trading.Shi
 * @date 11:07 02/14/22
 */
@Setter @Getter @Accessors(chain = true)
@AllArgsConstructor @NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BlueloserProfileVO implements Serializable {
    private static final long serialVersionUID = 3185043135919147814L;

    @ApiModelProperty(value = "uid")
    @JsonProperty(index = 1)
    private String uid;

    @ApiModelProperty(value = "bwc等级 123")
    @JsonProperty(index = 2)
    private Integer loserLevel;

    @ApiModelProperty(value = "过往30日日均资产")
    @JsonProperty(index = 3)
    private String avgAsset;

    @ApiModelProperty(value = "bwc等级有效期截止")
    @JsonProperty(index = 4)
    private Long validUntil;



    public static BlueloserProfileVO empty(String uid) {
        return new BlueloserProfileVO(uid, -1, null, null);
    }
}

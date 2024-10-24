package com.loser.backend.club.controller.request.aceup;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.io.Serializable;


@Getter @Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class loserLevelModifyParam implements Serializable {
    private static final long serialVersionUID = 7557226965983169036L;

    @ApiModelProperty(value = "用户id")
    private String uid;

    //"LV0","LV1","LV2","LV3","LV4","LV5","LV6","BWC1","BWC2","BWC3"
    @NotBlank
    @Pattern(regexp = "^(LV0|LV1|LV2|LV3|LV4|LV5|LV6|BWC1|BWC2|BWC3)$")
    @ApiModelProperty(value = "变更的新等级(LV0,LV1,LV2,LV3,LV4,LV5,LV6,BWC1,BWC2,BWC3))")
    private String updateLevel;

    @NotNull
    @ApiModelProperty(value = "有效期止")
    private Long validUntil;

    @ApiModelProperty(value = "备注")
    private String remark;
}

package com.loser.backend.club.controller.request;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.loser.backend.club.api.http.request.InternalParamUid;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter @Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ClubLevelBindingParam extends InternalParamUid implements Serializable {
    private static final long serialVersionUID = 871248085652630567L;

    @Min(value = 0)
    @NotNull
    @ApiModelProperty(value = "初始化绑定的club等级", required = true)
    private Integer clubLevel;


    // private Long clubPoint;
    //
    //
    // private LocalDateTime validUntil;
    //
    //
    // private LocalDateTime upgradeTime;

    @ApiModelProperty(value = "备注")
    private String remark;

}

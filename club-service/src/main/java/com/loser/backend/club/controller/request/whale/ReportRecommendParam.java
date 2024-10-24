package com.loser.backend.club.controller.request.whale;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Getter @Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReportRecommendParam implements Serializable {
    private static final long serialVersionUID = 4359940506600627388L;


    @ApiModelProperty(
            value = "研报类型 RESEARCH、OBSERVE忽略大小写）",
            example = "RESEARCH"
    )
    @NotBlank
    @Pattern(regexp = "^(?i)(RESEARCH|OBSERVE)$", message = "Unexpected type of report!")
    private String report_type;


    @ApiModelProperty(
            value = "研报类型id"
    )
    @NotNull
    @Min(1)
    private Long report_id;
}

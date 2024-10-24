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
public class BenefitReportVO implements Serializable {
    private static final long serialVersionUID = -2379720444177626068L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "发布时间")
    private String issueTime;

    @ApiModelProperty(value = "研报标题")
    private String reportTitle;

    @ApiModelProperty(value = "研报文件地址")
    private String resourceUrl;

    @ApiModelProperty(value = "研报预览图片")
    private String screenshot;


    /**
     * 是否显示角标采用缓存执行
     */
    @ApiModelProperty(value = "是否显示new角标")
    private Boolean lastet;
}

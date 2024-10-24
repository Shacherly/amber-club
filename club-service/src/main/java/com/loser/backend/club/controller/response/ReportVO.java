package com.loser.backend.club.controller.response;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：trading
 * @date ：Created in 2022/2/22 16:51
 * @description：
 * @modified By：
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReportVO {

    @ApiModelProperty(value = "标题")
    private String reportTitle;

    @ApiModelProperty(value = "图片")
    private String reportScreenshot;

    @ApiModelProperty(value = "内容")
    private String reportContent;

    @ApiModelProperty(value = "研报文件")
    private String reportSource;

    @ApiModelProperty(value = "发布时间")
    private Long ctime;

    @ApiModelProperty(value = "是否最新一期？决定new角标")
    private Boolean latest;

    @ApiModelProperty(value = "研报分类")
    private String reportClass;

    @ApiModelProperty(value = "研报id")
    private Long reportId;

}

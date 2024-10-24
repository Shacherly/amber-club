package com.loser.backend.club.controller.response.aceup;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ：trading
 * @date ：Created in 2022/2/22 16:51
 * @description：
 * @modified By：
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReportDetailVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "标题")
    private JSONObject reportTitle;

    @ApiModelProperty(value = "分类(OBSERVE、RESEARCH)")
    private String reportClass;

    @ApiModelProperty(value = "图片")
    private JSONObject reportScreenshot;

    @ApiModelProperty(value = "内容")
    private JSONObject reportContent;

    @ApiModelProperty(value = "研报文件")
    private JSONObject reportSource;

    @ApiModelProperty(value = "状态:(ENABLE、DISABLE)")
    private String status;

    @ApiModelProperty(value = "备注")
    private String remark;


}

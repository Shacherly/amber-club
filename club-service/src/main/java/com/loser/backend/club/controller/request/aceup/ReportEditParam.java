package com.loser.backend.club.controller.request.aceup;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author ：trading
 * @date ：Created in 2022/2/22 16:31
 * @description：研报新增实体
 * @modified By：
 */

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReportEditParam {

    @NotNull
    @ApiModelProperty(value = "id")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "标题")
    private JSONObject reportTitle;

    @NotBlank
    @ApiModelProperty(value = "分类(OBSERVE、RESEARCH)")
    private String reportClass;

    @NotNull
    @ApiModelProperty(value = "图片")
    private JSONObject reportScreenshot;

    @ApiModelProperty(value = "内容")
    private JSONObject reportContent;

    @NotNull
    @ApiModelProperty(value = "研报文件")
    private JSONObject reportSource;

    @NotBlank
    @Pattern(regexp = "^(ENABLE|DISABLE)$")
    @ApiModelProperty(value = "状态:(ENABLE、DISABLE)")
    private String status;

    @ApiModelProperty(value = "")
    private String remark;
}

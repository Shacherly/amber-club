package com.loser.backend.club.controller.request.aceup;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author ：trading
 * @date ：Created in 2022/2/14 15:30
 * @description：任务修改请求实体
 * @modified By：
 */

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MissionEditParam {

    @NotNull
    @ApiModelProperty(value = "任务id")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "任务标题")
    private JSONObject missionTitle;

    @NotNull
    @ApiModelProperty(value = "所属权益分类id")
    private Long benefitId;

    @NotNull
    @ApiModelProperty(value = "所属权属权益清单id")
    private Long benefitInventoryId;

    @ApiModelProperty(value = "任务描述")
    private JSONObject missionDescr;

    @ApiModelProperty(value = "任务列表icon")
    private JSONObject missionIcon;

    @ApiModelProperty(value = "任务详情图片")
    private JSONObject missionImg;

    @ApiModelProperty(value = "跳转链接")
    private JSONObject forwardLink;

    @NotNull
    @ApiModelProperty(value = "优先级")
    private Integer priority;

    @NotNull
    @ApiModelProperty(value = "任务类型 ROUTINE、CUSTOM")
    private String missionType;

    @ApiModelProperty(value = "有效起")
    private Long validBegin;

    @ApiModelProperty(value = "有效止")
    private Long validEnd;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^(ENABLE|DISABLE)$")
    @ApiModelProperty(value = "状态:(ENABLE、DISABLE)")
    private String status;

    @JsonProperty(value = "userRangeType")
    @Pattern(regexp = "^(ALL|SELECTED_USER|IMPORTED_USER|USER_LABEL|SINGLE_USER)$")
    @ApiModelProperty(value = "关联的客户类型 ALL SELECTED_USER IMPORTED_USER USER_LABEL SINGLE_USER")
    private String userRangeType;

    @JsonProperty(value = "userRangeParam")
    @ApiModelProperty(value = "用户关联的查询参数")
    private String userRangeParam;

    @ApiModelProperty(value = "备注")
    private String remark;

}

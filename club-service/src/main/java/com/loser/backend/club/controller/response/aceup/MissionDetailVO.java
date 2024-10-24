package com.loser.backend.club.controller.response.aceup;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author ：trading
 * @date ：Created in 2022/2/14 16:03
 * @description：任务详情实体
 * @modified By：
 */

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MissionDetailVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "任务标题")
    private JSONObject missionTitle;

    @ApiModelProperty(value = "所属权益分类id")
    private Long benefitId;

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

    @ApiModelProperty(value = "优先级")
    private Integer priority;

    @ApiModelProperty(value = "任务类型 ROUTINE、CUSTOM")
    private String missionType;

    @ApiModelProperty(value = "有效起")
    private Long validBegin;

    @ApiModelProperty(value = "有效止")
    private Long validEnd;

    @ApiModelProperty(value = "状态:(ENABLE、DISABLE)")
    private String status;

    @JsonProperty(value = "userRangeType")
    @ApiModelProperty(value = "关联的客户类型 ALL SELECTED_USER IMPORTED_USER USER_LABEL SINGLE_USER")
    private String userRangeType;

    @JsonProperty(value = "userRangeParam")
    @ApiModelProperty(value = "用户关联的查询参数")
    private String userRangeParam;

    @ApiModelProperty(value = "备注")
    private String remark;
}

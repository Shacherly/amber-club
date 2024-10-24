package com.loser.backend.club.controller.response.aceup;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：trading
 * @date ：Created in 2022/2/14 15:58
 * @description：任务列表查询返回实体
 * @modified By：
 */

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MissionListQueryVO {

    @ApiModelProperty(value = "任务id")
    private Long id;

    @ApiModelProperty(value = "任务标题")
    private JSONObject missionTitle;

    @ApiModelProperty(value = "所属权益分类id")
    private Long benefitId;

    @ApiModelProperty(value = "所属权属权益清单id")
    private Long benefitInventoryId;

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

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Long ctime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private Long utime;

}

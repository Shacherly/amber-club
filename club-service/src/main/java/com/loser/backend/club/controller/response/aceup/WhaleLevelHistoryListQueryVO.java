package com.loser.backend.club.controller.response.aceup;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：trading
 * @date ：Created in 2022/2/23 16:50
 * @description：bwc等级变更记录
 * @modified By：
 */

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class loserLevelHistoryListQueryVO {

    @ApiModelProperty(value = "用户id")
    private String uid;

    @ApiModelProperty(value = "名字")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "变更时间")
    private Long updateTime;

    @ApiModelProperty(value = "变更对象")
    private String target;

    @ApiModelProperty(value = "变更前")
    private String oldValue;

    @ApiModelProperty(value = "变更后")
    private String latestValue;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "操作人")
    private String operator;

}

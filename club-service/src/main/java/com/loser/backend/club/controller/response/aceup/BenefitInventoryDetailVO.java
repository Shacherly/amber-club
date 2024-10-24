package com.loser.backend.club.controller.response.aceup;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author ：trading
 * @date ：Created in 2022/2/14 15:08
 * @description：权益清淡详情
 * @modified By：
 */

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BenefitInventoryDetailVO {

    @ApiModelProperty(value = "权益清单id")
    private Long id;

    @ApiModelProperty(value = "所属权益分类id")
    private Long benefitId;

    @ApiModelProperty(value = "标题")
    private JSONObject benefitTitle;

    @NotNull
    @ApiModelProperty(value = "权益描述")
    private JSONObject benefitDescr;

    @NotNull
    @ApiModelProperty(value = "适用等级")
    private JSONObject levelDescr;

    @ApiModelProperty(value = "权益图片")
    private JSONObject benefitImg;

    @ApiModelProperty(value = "优先级")
    private Integer priority;

    @ApiModelProperty(value = "状态:(ENABLE、DISABLE)")
    private String status;

    @ApiModelProperty(value = "会员等级,多个用英文逗号拼接，example:1,2,3")
    private String loserLevel;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Long ctime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private Long utime;

    @ApiModelProperty(value = "绑定人数")
    private Integer bindNumber;

    @ApiModelProperty(value = "绑定人次")
    private Integer bindPeople;
}

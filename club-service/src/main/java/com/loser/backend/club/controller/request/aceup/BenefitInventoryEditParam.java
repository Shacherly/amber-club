package com.loser.backend.club.controller.request.aceup;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author ：trading
 * @date ：Created in 2022/2/14 14:27
 * @description：权益清单新增实体
 * @modified By：
 */

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BenefitInventoryEditParam {

    @NotNull
    @ApiModelProperty(value = "权益清单id")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "所属权益分类id")
    private Long benefitId;

    @NotNull
    @ApiModelProperty(value = "标题")
    private JSONObject benefitTitle;

    @NotNull
    @ApiModelProperty(value = "权益描述")
    private JSONObject benefitDescr;

    @NotNull
    @ApiModelProperty(value = "适用等级")
    private JSONObject levelDescr;

    @NotNull
    @ApiModelProperty(value = "权益图片")
    private JSONObject benefitImg;

    @NotNull
    @Min(value = 1)
    @ApiModelProperty(value = "优先级")
    private Integer priority;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^(ENABLE|DISABLE)$")
    @ApiModelProperty(value = "状态:(ENABLE、DISABLE)")
    private String status;

    @NotNull
    @Pattern(regexp = "^(1|2|3|,)*$")
    @ApiModelProperty(value = "会员等级,多个用英文逗号拼接，example:1,2,3")
    private String loserLevel;

    @ApiModelProperty(value = "备注")
    private String remark;

}

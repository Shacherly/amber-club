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
 * @date ：Created in 2022/2/14 11:03
 * @description：权益分类编辑实体
 * @modified By：
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BenefitEditParam {

    @ApiModelProperty(value = "权益分类id")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "权益分类名称")
    private JSONObject benefitName;

    @NotNull
    @Min(value = 1)
    @ApiModelProperty(value = "优先级1~N")
    private Integer priority;

    @NotNull
    @Pattern(regexp = "^(WEALTH_MANAGEMENT|CONCIERGE_SERVICE|EXTRA_YIELD|COMMUNITY)$")
    @ApiModelProperty(value = "星球(WEALTH_MANAGEMENT、CONCIERGE_SERVICE、EXTRA_YIELD、COMMUNITY)")
    private String benefitIndicator;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^(ENABLE|DISABLE)$")
    @ApiModelProperty(value = "状态:(ENABLE、DISABLE)")
    private String status;

    @ApiModelProperty(value = "描述")
    private String descr;

}

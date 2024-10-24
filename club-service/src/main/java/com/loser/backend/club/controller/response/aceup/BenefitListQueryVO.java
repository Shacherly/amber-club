package com.loser.backend.club.controller.response.aceup;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author ：trading
 * @date ：Created in 2022/2/14 14:12
 * @description：权益分类列表查询返回实体
 * @modified By：
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BenefitListQueryVO {

    @ApiModelProperty(value = "权益分类id")
    private Long id;

    @ApiModelProperty(value = "权益名称")
    private JSONObject benefitName;

    @ApiModelProperty(value = "星球(WEALTH_MANAGEMENT、CONCIERGE_SERVICE、EXTRA_YIELD、COMMUNITY)")
    private String benefitIndicator;

    @ApiModelProperty(value = "优先级1~N")
    private Integer priority;

    @ApiModelProperty(value = "状态:(ENABLE、DISABLE)")
    private String status;

    @ApiModelProperty(value = "描述")
    private String descr;

    @ApiModelProperty(value = "创建时间")
    private Long ctime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private Long utime;

}

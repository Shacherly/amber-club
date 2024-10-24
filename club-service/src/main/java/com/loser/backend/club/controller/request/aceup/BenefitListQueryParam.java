package com.loser.backend.club.controller.request.aceup;

import com.loser.backend.club.common.http.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author ：trading
 * @date ：Created in 2022/2/14 14:07
 * @description：权益分类列表查询请求实体
 * @modified By：
 */

@Data
public class BenefitListQueryParam extends PageRequest {

    @ApiModelProperty(value = "权益分类名称")
    private String benefit_name;

}

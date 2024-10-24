package com.loser.backend.club.controller.request.aceup;

import com.loser.backend.club.common.http.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：trading
 * @date ：Created in 2022/2/14 14:41
 * @description：权益清单列表查询请求实体
 * @modified By：
 */

@Data
public class BenefitInventoryListQueryParam extends PageRequest {

    @ApiModelProperty(value = "权益清单id")
    private String id;

    @ApiModelProperty(value = "所属权益分类id")
    private String benefit_id;

    @ApiModelProperty(value = "状态:(ENABLE、DISABLE)")
    private String status;

    @ApiModelProperty(value = "会员等级")
    private String loser_level;

    @ApiModelProperty(value = "创建时间开始")
    private Long ctime_start;

    @ApiModelProperty(value = "创建时间结束")
    private Long ctime_end;

    @ApiModelProperty(value = "更新时间开始")
    private Long utime_start;

    @ApiModelProperty(value = "更新时间结束")
    private Long utime_end;

}

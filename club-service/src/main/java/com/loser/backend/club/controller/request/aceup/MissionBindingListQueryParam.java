package com.loser.backend.club.controller.request.aceup;

import com.loser.backend.club.common.http.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：trading
 * @date ：Created in 2022/2/14 16:08
 * @description：任务用户绑定关系请求实体
 * @modified By：
 */

@Data
public class MissionBindingListQueryParam extends PageRequest {

    @ApiModelProperty(value = "任务id")
    private Long mission_id;

    @ApiModelProperty(value = "用户id")
    private String uid;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;
}

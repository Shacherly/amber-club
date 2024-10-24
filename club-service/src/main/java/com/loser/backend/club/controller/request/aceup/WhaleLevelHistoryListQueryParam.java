package com.loser.backend.club.controller.request.aceup;

import com.loser.backend.club.common.http.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：trading
 * @date ：Created in 2022/2/23 16:44
 * @description：
 * @modified By：
 */

@Data
public class loserLevelHistoryListQueryParam extends PageRequest {

    @ApiModelProperty(value = "用户id")
    private String uid;

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "变更时间-开始")
    private Long updateTimeStart;

    @ApiModelProperty(value = "变更时间-结束")
    private Long updateTimeEnd;

}

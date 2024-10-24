package com.loser.backend.club.controller.request.aceup;

import com.loser.backend.club.common.http.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：trading
 * @date ：Created in 2022/2/22 16:45
 * @description：
 * @modified By：
 */

@Data
public class ReportListQureyParam extends PageRequest {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "创建时间-开始")
    private Long ctime_start;

    @ApiModelProperty(value = "创建时间-结束")
    private Long ctime_end;

}

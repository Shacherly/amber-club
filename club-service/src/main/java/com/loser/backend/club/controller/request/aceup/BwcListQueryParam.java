package com.loser.backend.club.controller.request.aceup;

import com.loser.backend.club.common.http.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：trading
 * @date ：Created in 2022/2/23 14:46
 * @description：
 * @modified By：
 */

@Data
public class BwcListQueryParam extends PageRequest {

    @ApiModelProperty(value = "用户ID")
    private String uid;

}

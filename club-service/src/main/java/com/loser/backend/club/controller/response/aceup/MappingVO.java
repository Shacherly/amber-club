package com.loser.backend.club.controller.response.aceup;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：trading
 * @date ：Created in 2022/2/14 15:24
 * @description：权益分类或权益清单映射(id-name)
 * @modified By：
 */

@Data
public class MappingVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "多语言名称")
    private JSONObject name;

}

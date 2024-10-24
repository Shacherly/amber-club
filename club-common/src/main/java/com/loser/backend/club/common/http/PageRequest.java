package com.loser.backend.club.common.http;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * page request
 * @author trading
 * @date 2021/7/7 11:20
 */
@Getter
@Setter
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PageRequest {

    @NotNull(message = "page can't be null")
    @Min(value = 1, message = "page can't be less than 1")
    @ApiModelProperty(value = "页码", required = true)
    private Integer page;

    @NotNull(message = "page_size can't be null")
    @Min(value = 1, message = "page_size can't be less than 1")
    @ApiModelProperty(value = "每页数量", required = true)
    private Integer page_size;

    public static String DEFAULT_ORDER = "CREATED_TIME DESC, UPDATED_TIME DESC";
}

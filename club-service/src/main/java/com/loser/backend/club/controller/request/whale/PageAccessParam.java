package com.loser.backend.club.controller.request.whale;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;


@Getter @Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PageAccessParam implements Serializable {
    private static final long serialVersionUID = 1325588100755422627L;



    @ApiModelProperty(
            value = "访问页面 HOME、BLUE_loser忽略大小写）",
            example = "BLUE_loser"
    )
    @NotBlank
    @Pattern(regexp = "^(?i)(HOME|BLUE_loser)$", message = "Unexpected access page!")
    private String page;
}

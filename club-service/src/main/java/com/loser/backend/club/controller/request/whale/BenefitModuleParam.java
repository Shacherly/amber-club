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
public class BenefitModuleParam implements Serializable {
    private static final long serialVersionUID = 5684907353303267422L;

    @ApiModelProperty(
            value = "权益名称 WEALTH_MANAGEMENT、CONCIERGE_SERVICE、EXTRA_YIELD、COMMUNITY （忽略大小写）",
            example = "WEALTH_MANAGEMENT"
    )
    @NotBlank
    @Pattern(regexp = "^(?i)(WEALTH_MANAGEMENT|CONCIERGE_SERVICE|EXTRA_YIELD|COMMUNITY)$", message = "Unexpected name of benefit!")
    private String benefit_name;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BenefitModuleParam{");
        sb.append("benefit_name='").append(benefit_name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

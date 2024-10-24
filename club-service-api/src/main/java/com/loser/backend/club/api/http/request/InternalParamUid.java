package com.loser.backend.club.api.http.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;



/**
 * @author ~~ trading.s
 * @date 14:40 09/27/21
 */
@Getter
@Setter @Accessors(chain = true)
public class InternalParamUid implements Serializable {

    private static final long serialVersionUID = 7266884184105123299L;

    @NotBlank
    @ApiModelProperty(value = "用户唯一id", required = true)
    private String uid;

}

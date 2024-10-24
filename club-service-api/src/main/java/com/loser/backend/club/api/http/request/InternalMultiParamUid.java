package com.loser.backend.club.api.http.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;


/**
 * @author ~~ trading.s
 * @date 14:40 09/27/21
 */
@Getter
@Setter @Accessors(chain = true)
public class InternalMultiParamUid implements Serializable {

    private static final long serialVersionUID = 7266884184105123299L;

    @NotEmpty
    @ApiModelProperty(value = "用户唯一id", required = true)
    private List<String> uids;

}

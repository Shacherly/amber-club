package com.loser.backend.club.controller.response;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.loser.backend.club.pojo.ManagerContact;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.StringJoiner;


/**
 * @author ~~ trading.Shi
 * @date 10:45 02/25/22
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ManagerVO implements Serializable {
    private static final long serialVersionUID = -1336964478852467059L;


    @ApiModelProperty(value = "cm名称")
    private String managerName;

    @ApiModelProperty(value = "cm头像")
    private String managerPhoto;

    @ApiModelProperty(value = "cm简介")
    private String managerResume;

    @ApiModelProperty(value = "客户经理联系方式")
    private ManagerContact managerContact;


    @Override
    public String toString() {
        return new StringJoiner(", ", ManagerVO.class.getSimpleName() + "[", "]")
                .add("managerName='" + managerName + "'")
                .add("managerPhoto='" + managerPhoto + "'")
                .add("managerResume='" + managerResume + "'")
                .add("managerContact=" + managerContact)
                .toString();
    }

    private final static ManagerVO NON = new ManagerVO();

    public static ManagerVO non() {
        return NON;
    }
}

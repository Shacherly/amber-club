package com.loser.backend.club.controller.response;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.loser.backend.club.domain.loserMission;
import com.loser.backend.club.util.ContextHolder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.StringJoiner;


/**
 * @author ~~ trading.Shi
 * @date 11:07 02/14/22
 */
@Setter @Getter @Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class loserCustomBenefitVO implements Serializable {
    private static final long serialVersionUID = -6221673955977675923L;


    @ApiModelProperty(value = "权益标题")
    private String title;

    @ApiModelProperty(value = "权益描述")
    private String descr;

    @ApiModelProperty(value = "权益图片")
    private String image;

    @ApiModelProperty(value = "权益跳转链接")
    private String forwardLink;

    @Override
    public String toString() {
        return new StringJoiner(", ", loserCustomBenefitVO.class.getSimpleName() + "[", "]")
                .add("title='" + title + "'")
                .add("descr='" + descr + "'")
                .add("forwardLink='" + forwardLink + "'")
                .toString();
    }

    public static loserCustomBenefitVO fromMission(loserMission mission) {
        String channel = ContextHolder.get().getOriginChannel().toLowerCase();
        loserCustomBenefitVO vo = new loserCustomBenefitVO();
        vo.setTitle(mission.toLocalized(mission::getMissionTitle));
        vo.setDescr(mission.toLocalized(mission::getMissionDescr));
        vo.setImage(mission.toLocalized(() -> mission.getMissionImg().getJSONObject(channel)));
        vo.setForwardLink(mission.toLocalized(() -> mission.getForwardLink().getJSONObject(channel)));
        return vo;
    }
}

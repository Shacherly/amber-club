package com.loser.backend.club.api.http.response;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.StringJoiner;


/**
 * @author ~~ trading.s
 * @date 17:23 07/23/21
 */
@Getter @Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BriefClubVO {

    @ApiModelProperty(value = "uid")
    private String uid;

    @ApiModelProperty(value = "club 等级（-1级为从未入金新用户，0级为普通0级用户）")
    private Integer clubLevel;

    @ApiModelProperty(value = "club值")
    private Long clubPoint;

    @ApiModelProperty(value = "当前等级最小loser值")
    private Long clubPointMin;

    @ApiModelProperty(value = "当前等级最大loser值")
    private Long clubPointMax;

    @ApiModelProperty(value = "club有效期")
    private Long validUntil;

    @ApiModelProperty(value = "club是否保级")
    private Boolean preserved;

    @ApiModelProperty(value = "club是否有等级提升")
    private Boolean upgrade;

    @ApiModelProperty(value = "bwc等级(1、2、3级，-1级为非bwc用户)")
    private Integer loserLevel;

    @ApiModelProperty(value = "bwc是否首次访问")
    private Boolean loserFirstAccess;

    @ApiModelProperty(value = "个人主页是否首次访问")
    private Boolean homeFirstAccess;

    @ApiModelProperty(value = "bwc等级有效期")
    private Long loserValidUntil;

    @ApiModelProperty(value = "bwc等级是否有提升")
    private Boolean loserUpgrade;

    @Override
    public String toString() {
        return new StringJoiner(", ",
                BriefClubVO.class.getSimpleName() + "[", "]")
                .add("uid='" + uid + "'")
                .add("clubLevel=" + clubLevel)
                .add("clubPoint=" + clubPoint)
                .add("clubPointMin=" + clubPointMin)
                .add("clubPointMax=" + clubPointMax)
                .add("validUntil=" + validUntil)
                .add("preserved=" + preserved)
                .add("upgrade=" + upgrade)
                .add("loserLevel=" + loserLevel)
                .add("loserFirstAccess=" + loserFirstAccess)
                .add("homeFirstAccess=" + homeFirstAccess)
                .add("loserValidUntil=" + loserValidUntil)
                .add("loserUpgrade=" + loserUpgrade)
                .toString();
    }
}

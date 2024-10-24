package com.loser.backend.club.controller.response;


import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.common.collect.Range;
import com.loser.backend.club.config.ClubConfig;
import com.loser.backend.club.domain.ClubLevel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Optional;


/**
 * @author ~~ trading.s
 * @date 17:23 07/23/21
 */
@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BriefClubVO {

    @JsonProperty(index = 0)
    @ApiModelProperty(value = "uid")
    private String uid;

    @JsonProperty(index = 1)
    @ApiModelProperty(value = "club 等级（-1级为从未入金新用户，0级为普通0级用户）")
    private Integer clubLevel;

    @JsonProperty(index = 2)
    @ApiModelProperty(value = "club值")
    private Long clubPoint;

    @JsonProperty(index = 3)
    @ApiModelProperty(value = "当前等级最小loser值")
    private Long clubPointMin;

    @JsonProperty(index = 4)
    @ApiModelProperty(value = "当前等级最大loser值")
    private Long clubPointMax;

    @JsonProperty(index = 5)
    @ApiModelProperty(value = "club有效期")
    private Long validUntil;

    @JsonProperty(index = 6)
    @ApiModelProperty(value = "club是否保级")
    private Boolean preserved;

    @JsonProperty(index = 7)
    @ApiModelProperty(value = "club是否有等级提升")
    private Boolean upgrade;

    @JsonProperty(index = 8)
    @ApiModelProperty(value = "bwc等级(1、2、3级，-1级为非bwc用户)")
    private Integer loserLevel;

    @JsonProperty(index = 9)
    @ApiModelProperty(value = "bwc是否首次访问")
    private Boolean loserFirstAccess;

    @JsonProperty(index = 10)
    @ApiModelProperty(value = "个人主页是否首次访问")
    private Boolean homeFirstAccess;

    @JsonProperty(index = 11)
    @ApiModelProperty(value = "bwc等级有效期")
    private Long loserValidUntil;

    @JsonProperty(index = 12)
    @ApiModelProperty(value = "bwc等级是否有提升")
    private Boolean loserUpgrade;


    public static BriefClubVO mock() {
        BriefClubVO club = new BriefClubVO();
        club.setClubLevel(3);
        club.setClubPointMin(200L);
        club.setClubPoint(876L);
        club.setClubPointMax(1500L);
        club.setValidUntil(1631246115000L);
        club.setPreserved(true);
        club.setUpgrade(true);
        return club;
    }

    public static BriefClubVO ordinary(String uid) {
        BriefClubVO club = new BriefClubVO();
        club.setUid(uid);
        club.setClubLevel(-1);
        club.setPreserved(false);
        club.setUpgrade(false);
        club.setloserLevel(-1);
        return club;
    }

    public static BriefClubVO ofLatest(ClubLevel latest) {
        Range<Long> levelRange = ClubConfig.getLevelRange(latest.getClubLevel());
        BriefClubVO club = new BriefClubVO();
        club.setUid(latest.getUid());
        club.setClubLevel(latest.getClubLevel());
        club.setClubPointMin(levelRange.lowerEndpoint());
        club.setClubPoint(latest.getClubPoint());
        club.setClubPointMax(levelRange.upperEndpoint());
        club.setValidUntil(DateUtil.toInstant(latest.getValidUntil()).toEpochMilli());
        club.setPreserved(false);
        club.setUpgrade(!Optional.ofNullable(latest.getUpgradeRead()).orElse(true));
        // 从未入金的0级用户本质上为-1级
        if (latest.getClubLevel() == 0 && !latest.getUpgradeRead()) {
            club.setClubLevel(-1);
        }
        return Optional.ofNullable(latest.getloserLevel())
                       .map(loserLevel -> {
                           club.setloserLevel(loserLevel);
                           club.setloserFirstAccess(Optional.ofNullable(latest.getFirstAccess()).orElse(true));
                           club.setHomeFirstAccess(Optional.ofNullable(latest.getHomeFirstAccess()).orElse(true));
                           club.setloserUpgrade(!Optional.ofNullable(latest.getloserUpgradeRead()).orElse(true));
                           club.setloserValidUntil(Optional.ofNullable(latest.getloserUntil()).map(DateUtil::toInstant).map(Instant::toEpochMilli).orElse(null));
                           return club;
                       })
                       .orElseGet(() -> {
                           club.setloserLevel(-1);
                           return club;
                       });
    }

    public static BriefClubVO preserve(ClubLevel latest) {
        BriefClubVO club = ofLatest(latest);
        club.setPreserved(true);
        return club;
    }
}

package com.loser.backend.club.controller.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**·
 * @author ~~ trading.s
 * @date 17:23 07/23/21
 */
@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ClubGiftVO implements Serializable {

    private static final long serialVersionUID = 6021751249809799035L;

    @ApiModelProperty(value = "当前club等级所拥有的奖励列表")
    private List<ClubGiftCoupon> reachedCoupon;

    @ApiModelProperty(value = "对比club等级的奖励列表（0级和10级没有）")
    private List<ClubGiftCoupon> unReachedCoupon;

    @ApiModelProperty(value = "专享利率对比列表")
    private List<ClubGiftRate> exclvRateCompare;

    public ClubGiftVO(List<ClubGiftCoupon> reachedCoupon, List<ClubGiftCoupon> unReachedCoupon, List<ClubGiftRate> exclvRateCompare) {
        this.reachedCoupon = reachedCoupon;
        this.unReachedCoupon = unReachedCoupon;
        this.exclvRateCompare = exclvRateCompare;
    }

    public ClubGiftVO() {
    }

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class ClubGiftCoupon extends GiftSortBase {

        @ApiModelProperty(value = "对应的club等级")
        private Integer bindClubLevel;

        @ApiModelProperty(value = "club礼物id")
        private String giftId;

        @ApiModelProperty(value = "club礼物类型(0-加息券  5-资产券)")
        private Integer giftType;

        @ApiModelProperty(value = "有效期截至（可以为null）")
        private Long validUntil;

        // @ApiModelProperty(value = "")
        private String giftAmount;

        @ApiModelProperty(value = "加息率")
        private String giftRate;

        @ApiModelProperty(value = "券的抽象价值（加息券的加息率、减息券的减息率、资产券的返现金额）")
        private String giftWorth;

        @ApiModelProperty(value = "券币种")
        private String giftCoin;

        @ApiModelProperty(value = "按钮状态：不同券状态有差异<br>" +
                "加息券：-2-下个等级可领取 -1-领取 0-去使用 1-已过期 3-已失效（失效后禁止领取或使用，按钮应该置灰）6-已使用 <br>" +
                "资产券：-2-下个等级可领取 -1-领取 0-去激活 1-已过期 2-待发放 3-已失效（失效后禁止领取或使用，按钮应该置灰）4-发放失败 6-已发放")
        private Integer buttonStatus;

        public static ClubGiftCoupon empty() {
            return new ClubGiftCoupon();
        }
    }


    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class ClubGiftRate extends GiftSortBase {

        @ApiModelProperty(value = "当前club等级")
        @JsonProperty(index = 6)
        private Integer clubLevel;

        @ApiModelProperty(value = "理财产品id")
        @JsonProperty(index = 3)
        private String giftId;

        @ApiModelProperty(value = "币种")
        @JsonProperty(index = 0)
        private String coin;

        @ApiModelProperty(value = "理财期限")
        @JsonProperty(index = 1)
        private Integer days;

        @ApiModelProperty(value = "年化收益率")
        @JsonProperty(index = 7)
        private String annualRate;

        @ApiModelProperty(value = "对比club等级")
        @JsonProperty(index = 8)
        private Integer clubLevelCp;

        @ApiModelProperty(value = "对比年化收益率")
        @JsonProperty(index = 9)
        private String annualRateCp;

        @ApiModelProperty(value = "对比基准利率")
        @JsonProperty(index = 10)
        private String standardRateCp;

        @ApiModelProperty(value = "对比专享利率")
        @JsonProperty(index = 11)
        private String exclusiveRateCp;

        @ApiModelProperty(value = "是否生效，默认返回 Y 生效")
        @JsonProperty(index = 4)
        private String isEffect;

        public static ClubGiftRate empty() {
            return new ClubGiftRate();
        }
    }

    @Getter @Setter
    public static class GiftSortBase {

        @JsonProperty(index = -100)
        @ApiModelProperty(value = "优先级")
        private Integer sort;

        @JsonIgnore
        @ApiModelProperty(hidden = true)
        private Long sortUpdateTime;
    }

    public static ClubGiftVO mock() {
        ClubGiftVO club = new ClubGiftVO();

        ClubGiftCoupon gift0 = new ClubGiftCoupon();
        gift0.setGiftAmount("2");
        gift0.setGiftCoin("usdt");
        gift0.setGiftType(5);
        gift0.setValidUntil(1631536205000L);
        gift0.setGiftId("180");
        gift0.setButtonStatus(-1);
        ClubGiftCoupon gift1 = new ClubGiftCoupon();
        gift1.setGiftAmount("10");
        gift1.setGiftCoin("usd");
        gift1.setGiftType(5);
        gift1.setValidUntil(1631536205000L);
        gift1.setGiftId("168");
        gift1.setButtonStatus(0);
        ClubGiftCoupon gift4 = new ClubGiftCoupon();
        gift4.setGiftRate("0.002");
        gift4.setGiftType(0);
        gift4.setValidUntil(1631536205000L);
        gift4.setGiftId("203");
        gift4.setButtonStatus(1);
        club.setReachedCoupon(Arrays.asList(gift0, gift1));

        ClubGiftCoupon gift2 = new ClubGiftCoupon();
        gift2.setGiftRate("0.002");
        gift2.setGiftType(0);
        gift2.setValidUntil(1631536205000L);
        gift2.setGiftId("8");
        gift2.setButtonStatus(-2);
        ClubGiftCoupon gift3 = new ClubGiftCoupon();
        gift3.setGiftRate("0.002");
        gift3.setGiftType(0);
        gift3.setValidUntil(1631536205000L);
        gift3.setGiftId("2");
        gift3.setButtonStatus(-2);

        club.setUnReachedCoupon(Arrays.asList(gift2, gift3));

        ClubGiftRate rate = new ClubGiftRate();
        rate.setClubLevel(1);
        rate.setClubLevelCp(2);
        rate.setCoin("btc");
        rate.setDays(10);
        rate.setAnnualRate("0.001");
        rate.setAnnualRateCp("0.002");
        rate.setSort(1);
        rate.setExclusiveRateCp("0.001");
        rate.setStandardRateCp("0.003");
        club.setExclvRateCompare(Arrays.asList(rate));
        return club;
    }

}

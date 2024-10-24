package com.loser.backend.club.controller.response;


import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;


@Getter @Setter @Accessors(chain = true)
@AllArgsConstructor @NoArgsConstructor
public class ClubEarnProdVO implements Serializable {

    private static final long serialVersionUID = -3062074242263153144L;

    private String applyCoin;


    private Integer duration;


    private String annualRate;


    private String productId;


    public boolean match(ClubEarnProdVO another) {
        return Objects.equals(duration, another.getDuration()) && StringUtils.equalsIgnoreCase(applyCoin, another.getApplyCoin());
    }

    public static List<ClubEarnProdVO> mock() {
        // 本级
        ClubEarnProdVO vo1 = new ClubEarnProdVO("btc", 10, "0.055", "10000");
        ClubEarnProdVO vo2 = new ClubEarnProdVO("btc", 20, "0.065", "10001");
        ClubEarnProdVO vo3 = new ClubEarnProdVO("eth", 10, "0.055", "10002");
        ClubEarnProdVO vo4 = new ClubEarnProdVO("eth", 20, "0.065", "10003");

        // 对比等级
        ClubEarnProdVO vo5 = new ClubEarnProdVO("btc", 10, "0.075", "10004");
        ClubEarnProdVO vo6 = new ClubEarnProdVO("btc", 20, "0.085", "10005");
        ClubEarnProdVO vo7 = new ClubEarnProdVO("eth", 10, "0.075", "10006");
        ClubEarnProdVO vo8 = new ClubEarnProdVO("eth", 20, "0.085", "10007");
        return Lists.newArrayList(vo1, vo2, vo3, vo4, vo5, vo6, vo7, vo8);
    }

}

package com.loser.backend.club.common.enums;


import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
public enum loserLevelEnum {

    BWC_NON(-1, "BWC_NON"),
    BWC1(1, "BWC1"),
    BWC2(2, "BWC2"),
    BWC3(3, "BWC3"),
    ;



    private final Integer level;
    private final String alias;

    loserLevelEnum(Integer level, String alias) {
        this.level = level;
        this.alias = alias;
    }

    public static Integer getByAlias(String alias) {
        loserLevelEnum[] values = loserLevelEnum.values();
        for (loserLevelEnum value : values) {
            if (StringUtils.equals(alias, value.getAlias())) {
                return value.getLevel();
            }
        }
        throw new IllegalArgumentException("Illegal loserLevel alias of " + alias);
    }

    public static Set<Integer> getLevels(String loserLevel) {
        // if (StringUtils.isBlank(loserLevel) || !loserLevel.contains(","))
        //     throw new IllegalArgumentException("Illegal loserLevels alias of " + loserLevel);
        // if (StringUtils.containsIgnoreCase(loserLevel, "bwc"))
        //     return Arrays.stream(loserLevel.split(",")).map(loserLevelEnum::getByAlias).collect(Collectors.toSet());
        return Arrays.stream(loserLevel.split(",")).map(Integer::parseInt).collect(Collectors.toSet());
    }

}

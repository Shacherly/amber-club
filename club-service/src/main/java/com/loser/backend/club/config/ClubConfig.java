package com.loser.backend.club.config;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.loser.backend.club.util.TemporalUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author ~~ trading.s
 * @date 14:58 07/30/21
 */
@SuppressWarnings("all")
@Setter @Getter
@Component
@ConfigurationProperties(prefix = "trading-club")
public class ClubConfig {

    @Value("${spring.profiles.active}")
    private String env;

    final private static RangeMap<Long, Integer> RANGE_MAP = TreeRangeMap.create();
    final private static Long L0 = 0L;
    final private static Long L1 = 1L;
    final private static Long L2 = 20L;
    final private static Long L3 = 200L;
    final private static Long L4 = 1500L;
    final private static Long L5 = 5000L;
    final private static Long L6 = 10000L;
    final private static Long L7 = 50000L;
    final private static Long L8 = 100000L;
    final private static Long L9 = 500000L;
    final private static Long L10 = 1000000L;
    final private static Long MAX = Long.MAX_VALUE;

    final private static Integer LEVEL_MAX = 10;
    final private static Integer LEVEL_MIN = 1;

    final private static RangeMap<BigDecimal, Integer> loser_RANGE_MAP = TreeRangeMap.create();

    final private static BigDecimal BLUE_WAHLE_LVL1 = new BigDecimal("5000000");
    final private static BigDecimal BLUE_WAHLE_LVL2 = new BigDecimal("20000000");
    final private static BigDecimal BLUE_WAHLE_LVL3 = new BigDecimal("100000000");
    final private static BigDecimal BLUE_WAHLE_MAX = BigDecimal.valueOf(MAX);

    final private static Map<Integer, Integer> BENEFIT_MAP = new HashMap<>(8);

    /**
     * club等级更新日期设置为当天
     * alpha true
     * prod false 或者 置空
     */
    private String intraday;

    /**
     * loser value计算任务执行间隔
     * prod 一天3次
     * alpha 1分钟一次
     */
    private String compensateRate;

    /**
     * loser值同步时刻
     */
    private String syncMoment;

    /**
     * 下次club更新时间 (固定下月1号，alpha和pro用intraday去区分当天是否更新)
     * @return 下个月一号
     */
    public LocalDateTime nextClubUntil() {
        if (StringUtils.equals(env, "prod"))
            return TemporalUtil.offSetMonthDayHour(1, 1, 8);
        else if (intradayRefresh()) {
            return LocalDateTime.now();
        }
        return TemporalUtil.offSetMonthDayHour(1, 1, 8);
    }

    /**
     * 下次loser-club更新时间 (固定 T+90，alpha和pro用intraday去区分当天是否更新)
     * @return T+90
     */
    public LocalDateTime nextloserUntil() {
        if (StringUtils.equals(env, "prod"))
            return TemporalUtil.offSetDayHour(90, 8);
        else if (intradayRefresh()) {
            return LocalDateTime.now();
        }
        return TemporalUtil.offSetDayHour(90, 8);
    }

    /**
     * 当天是否为club更新时间
     * @return alph始终true | prod为当月一号
     */
    public boolean intradayRefresh() {
        return Boolean.parseBoolean(intraday);
    }


    static {
        RANGE_MAP.put(Range.closedOpen(L0, L1), 0);
        RANGE_MAP.put(Range.closed(L1, L2), 1); // 1 ~ 20  level_1
        RANGE_MAP.put(Range.closed(L2 + 1, L3), 2); // 21 ~ 200  level_2
        RANGE_MAP.put(Range.closed(L3 + 1, L4), 3); // 201 ~1500 level_3
        RANGE_MAP.put(Range.closed(L4 + 1, L5), 4); // 1501 ~ 5000 level_4
        RANGE_MAP.put(Range.closed(L5 + 1, L6), 5); // 5001 ~ 10000 level_5
        RANGE_MAP.put(Range.closed(L6 + 1, L7), 6); // 10001 ~ 50000 level_6
        RANGE_MAP.put(Range.closed(L7 + 1, L8), 7); // 50001 ~ 100000 level_7
        RANGE_MAP.put(Range.closed(L8 + 1, L9), 8); // 100001 ~ 500000 level_8
        RANGE_MAP.put(Range.closed(L9 + 1, L10), 9); // 500001 ~ 1000000 level_9
        RANGE_MAP.put(Range.closedOpen(L10 + 1, MAX), 10); // 1000001 ~ infinity level_10


        loser_RANGE_MAP.put(Range.closedOpen(BLUE_WAHLE_LVL1, BLUE_WAHLE_LVL2), 1);
        loser_RANGE_MAP.put(Range.closedOpen(BLUE_WAHLE_LVL2, BLUE_WAHLE_LVL3), 2);
        loser_RANGE_MAP.put(Range.closedOpen(BLUE_WAHLE_LVL3, BLUE_WAHLE_MAX), 3);

        BENEFIT_MAP.put(1, 8);
        BENEFIT_MAP.put(2, 9);
        BENEFIT_MAP.put(3, 10);
    }

    public static Integer getloserLevel(BigDecimal asset) {
        return Optional.ofNullable(asset).map(loser_RANGE_MAP::get).orElse(-1);
    }

    public static Integer getBenefitLevel(Integer loserLevel) {
        return Optional.ofNullable(loserLevel).map(BENEFIT_MAP::get).orElse(null);
    }


    /**
     * get level by loser value
     * @param clubPoint
     * @return
     */
    public static Integer getLevel(Long clubPoint) {
        return Optional.ofNullable(clubPoint)
                       .filter(value -> clubPoint >= 0)
                       .map(value -> RANGE_MAP.get(value))
                       .orElse(0);
    }

    /**
     * get this value range max
     * @param clubPoint
     * @return
     */
    public static Long getClubPointMax(Long clubPoint) {
        return Optional.ofNullable(clubPoint)
                       .filter(value -> value >= L0 && value <= L10)
                       .map(value -> RANGE_MAP.getEntry(value).getKey().upperEndpoint())
                       .orElse(null);
    }

    /**
     * get this value range min
     * @param clubPoint
     * @return
     */
    public static Long getClubPointMin(Long clubPoint) {
        return Optional.ofNullable(clubPoint)
                       .filter(value -> value >= L0)
                       .map(value -> RANGE_MAP.getEntry(value).getKey().lowerEndpoint())
                       .orElse(null);
    }

    public static Range<Long> getLevelRange(int level) {
        if (level < 0 || level > 10)
            throw new RuntimeException("Input arg level[" + level + "] error, range from 0 to 10 !");
        Map<Range<Long>, Integer> rangeMap = RANGE_MAP.asMapOfRanges();
        Range<Long> range = null;
        for (Map.Entry<Range<Long>, Integer> entry : rangeMap.entrySet()) {
            Range<Long> key = entry.getKey();
            if (entry.getValue() == level) {
                range = Range.closed(key.lowerEndpoint(), key.upperEndpoint());
                break;
            }
        }
        return range;
    }

    public static Integer getLevelMax() {
        return LEVEL_MAX;
    }

    public static Integer getLevelMin() {
        return LEVEL_MIN;
    }

    public static void main(String[] args) {
        // System.out.println(getClubPointMax(L10 + 1));
        // Long aLong = RANGE_MAP.getEntry(1L).getKey().upperEndpoint();
        // System.out.println();

        System.out.println(getClubPointMax(21L));
        System.out.println(getLevelRange(10));
        System.out.println(getLevel(0L));

        System.out.println(getloserLevel(null));
        System.out.println(LocalDateTime.now().withDayOfMonth(1).withHour(8).withMinute(0).withSecond(0).withNano(0));
        System.out.println(Arrays.toString(new Object[]{1, LocalDateTime.now(), null}));
    }
}
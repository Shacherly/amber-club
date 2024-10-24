package com.loser.backend.club.util;


import cn.hutool.core.date.DateUtil;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;
import java.util.Optional;

/**
 * @author ~~ trading.s
 * @date 20:50 10/11/21
 */
public class TemporalUtil {


    /**
     * Min LocalDateTime of the first day of offset month from tody
     * @param offSet
     * @return
     */
    public static LocalDateTime offSetMonthStart(int offSet) {
        LocalDate localDate = LocalDate.now();
        return LocalDateTime.of(localDate.with(ChronoField.DAY_OF_MONTH, 1).plusMonths(offSet), LocalTime.MIN);
    }

    public static LocalDateTime offSetMonthEnd(int offSet) {
        LocalDate localDate = LocalDate.now();
        return LocalDateTime.of(localDate.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(offSet), LocalTime.MAX);
    }

    public static LocalDateTime thisMonthStart() {
        return offSetMonthStart(0);
    }

    public static LocalDateTime nextMonthStart() {
        return offSetMonthStart(1);
    }

    public static LocalDateTime offSet3MonthStart() {
        return offSetMonthStart(1);
    }

    public static LocalDateTime thisMonthEnd() {
        return offSetMonthEnd(0);
    }

    public static LocalDateTime nextMonthEnd() {
        return offSetMonthEnd(1);
    }

    public static LocalDateTime offSetDaysStart(int offSet) {
        LocalDate localDate = LocalDate.now();
        return LocalDateTime.of(localDate.plusDays(offSet), LocalTime.MIN);
    }

    public static LocalDateTime ofEpochMilli(Long millis) {
        Objects.requireNonNull(millis);
        return DateUtil.toLocalDateTime(Instant.ofEpochMilli(millis));
    }

    public static long toEpochMilli(LocalDateTime dateTime) {
        Objects.requireNonNull(dateTime);
        return DateUtil.toInstant(dateTime).toEpochMilli();
    }

    public static long getEpochMilli() {
        return Instant.now().toEpochMilli();
    }

    public static LocalDateTime defaultNow(LocalDateTime now) {
        return Optional.ofNullable(now).orElseGet(LocalDateTime::now);
    }

    public static LocalDateTime offSetMonthDayHour(int month, int day, int hour) {
        LocalDate localDate = LocalDate.now();
        return LocalDateTime.of(
                localDate.with(ChronoField.DAY_OF_MONTH, day).plusMonths(month),
                LocalTime.MIN.plusHours(hour)
        );
    }

    public static LocalDateTime offSetDayHour(int day, int hour) {
        LocalDate localDate = LocalDate.now();
        return LocalDateTime.of(localDate.plusDays(day), LocalTime.MIN.plusHours(hour));
    }

    public static boolean isBetweenTime(LocalDateTime validTime, LocalDateTime validBegin, LocalDateTime validEnd) {
        return validTime.isAfter(validBegin) && validTime.isBefore(validEnd);
    }

    public static void main(String[] args) {
        System.out.println(offSetMonthEnd(0));
        System.out.println(offSetMonthDayHour(1, 1, 8));

        System.out.println(TemporalUtil.offSetDayHour(90, 8));
    }
}

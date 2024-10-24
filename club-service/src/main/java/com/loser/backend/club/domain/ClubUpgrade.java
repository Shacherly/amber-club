package com.loser.backend.club.domain;

import cn.hutool.core.util.ObjectUtil;
import com.loser.backend.club.config.ClubConfig;
import com.loser.backend.club.exception.BusinessException;
import com.loser.backend.club.exception.ExceptionEnum;
import com.loser.backend.club.util.TemporalUtil;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;


/**
 * @author ~~ trading.Shi
 * @date 15:59 02/17/22
 */
public interface ClubUpgrade {

    Integer getClubLevel();

    ClubLevel setClubLevel(Integer clubLevel);

    LocalDateTime getValidUntil();

    ClubLevel setValidUntil(LocalDateTime validUntil);

    Integer getloserLevel();

    ClubLevel setloserLevel(Integer loserLevel);

    LocalDateTime getloserUntil();

    ClubLevel setloserUntil(LocalDateTime loserUntil);

    default boolean clubUpgrable(@NonNull Integer latestLevel) {
        return Optional.ofNullable(latestLevel)
                       .map(level -> getClubLevel() == null || level > getClubLevel())
                       .orElseThrow(() -> new BusinessException(ExceptionEnum.ARGUMENT_NULL, "latestLevel"));
    }

    default boolean clubDegrable(@NonNull Integer latestLevel) {
        return Optional.ofNullable(latestLevel)
                       .map(level -> getClubLevel() != null && level < getClubLevel())
                       .orElseThrow(() -> new BusinessException(ExceptionEnum.ARGUMENT_NULL, "latestLevel"));
    }

    default void clubUpdate(@NonNull Integer latestLevel, @NonNull LocalDateTime updateTime, boolean upgrade) {
        Optional.ofNullable(latestLevel)
                .map(level -> setClubLevel(level).setUpgradeTime(upgrade ? TemporalUtil.defaultNow(updateTime) : null).setUpgradeRead(!upgrade))
                .orElseThrow(() -> new BusinessException(ExceptionEnum.ARGUMENT_NULL, "latestLevel"));
    }



    default void clubRefresh(Integer latestLevel, LocalDateTime now, LocalDateTime nextUntil) {
        if (ObjectUtil.hasNull(latestLevel, now, nextUntil)) {
            throw new BusinessException(
                    ExceptionEnum.ARGUMENT_NULL, Arrays.toString(new Object[]{latestLevel, now, nextUntil})
            );
        }
        LocalDate localDate = now.toLocalDate();

        // new user or toUpgrade user
        if (clubUpgrable(latestLevel)) clubUpdate(latestLevel, now, true);
        Optional.ofNullable(getValidUntil())
                .map(until -> {
                    // In club valid_until day
                    if (localDate.equals(until.toLocalDate())) {
                        setValidUntil(nextUntil);
                        if (clubDegrable(latestLevel)) clubUpdate(latestLevel, now, false);
                    }
                    return until;
                })
                .orElseGet(() -> {
                    setValidUntil(nextUntil);
                    return nextUntil;
                });
    }


    default boolean loserUpgrable(@NonNull Integer latestloserLevel) {
        return Optional.ofNullable(latestloserLevel)
                       .map(level -> getloserLevel() == null || level > getloserLevel())
                       .orElseThrow(() -> new BusinessException(ExceptionEnum.ARGUMENT_NULL, "latestloserLevel"));
    }

    default boolean loserDegrable(@NonNull Integer latestloserLevel) {
        return Optional.ofNullable(latestloserLevel)
                       .map(level -> getloserLevel() != null && level < getloserLevel())
                       .orElseThrow(() -> new BusinessException(ExceptionEnum.ARGUMENT_NULL, "latestloserLevel"));
    }

    default void loserUpdate(@NonNull Integer latestloserLevel, @NonNull LocalDateTime updateTime, @NonNull LocalDateTime nextloserUntil, boolean upgrade) {
        Objects.requireNonNull(nextloserUntil);
        Optional.ofNullable(latestloserLevel)
                .map(level -> setloserLevel(level)
                        .setloserUpgradeTime(upgrade ? TemporalUtil.defaultNow(updateTime) : null)
                        .setloserUpgradeRead(false).setloserUntil(nextloserUntil)
                        .setBenefitLevel(ClubConfig.getBenefitLevel(latestloserLevel))
                )
                .orElseThrow(() -> new BusinessException(ExceptionEnum.ARGUMENT_NULL, "latestloserLevel"));
    }

    default void loserRefresh(Integer latestloserLevel, LocalDateTime now, LocalDateTime nextUntil) {
        if (ObjectUtil.hasNull(latestloserLevel, now, nextUntil)) {
            throw new BusinessException(
                    ExceptionEnum.ARGUMENT_NULL, Arrays.toString(new Object[]{latestloserLevel, now, nextUntil})
            );
        }
        LocalDate localDate = now.toLocalDate();

        if (loserUpgrable(latestloserLevel))
            // loser upgrading lead to loserUntil's refresh
            loserUpdate(latestloserLevel, now, nextUntil, true);
        Optional.ofNullable(getloserUntil())
                .map(until -> {
                    // In loser-club valid_until day
                    // 如果loserUntil 非空，且上面的升级导致loserUntil更新，这里绝对为false
                    if (localDate.equals(until.toLocalDate())) {
                        setloserUntil(nextUntil);
                        if (loserDegrable(latestloserLevel))
                            loserUpdate(latestloserLevel, now, nextUntil, false);
                    }
                    return until;
                })
                .orElseGet(() -> {
                    setloserUntil(nextUntil);
                    return nextUntil;
                });
    }

    default boolean isBlueloser() {
        return Optional.ofNullable(getloserLevel()).map(var -> var > 0).orElse(false);
    }


    default boolean isOrdinaryClub() {
        return !isBlueloser();
    }
}

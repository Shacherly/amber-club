package com.loser.backend.club.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.loser.backend.club.ambermapper.UserMonthAverageAssetMapper;
import com.loser.backend.club.client.IUserServiceApi;
import com.loser.backend.club.config.ClubConfig;
import com.loser.backend.club.config.ExecutorConfiguration;
import com.loser.backend.club.domain.AverageMonthlyAsset;
import com.loser.backend.club.domain.ClubLevel;
import com.loser.backend.club.domain.UserMonthAverageAsset;
import com.loser.backend.club.kafka.message.ClubUpgradeModel;
import com.loser.backend.club.mapper.AverageMonthlyAssetMapper;
import com.loser.backend.club.mapper.ClubLevelMapper;
import com.loser.backend.club.mapper.loserManagerMapper;
import com.loser.backend.club.pojo.ClientMapping;
import com.loser.backend.club.pojo.ClubProfileSetter;
import com.loser.backend.club.pojo.ClubUpdateTracer;
import com.loser.backend.club.pojo.notify.NotifyTemplate;
import com.loser.backend.club.service.IAvgMonthService;
import com.loser.backend.club.service.IClubPercepService;
import com.loser.backend.club.util.CollectUtil;
import com.loser.backend.club.util.MathUtil;
import com.loser.backend.club.util.SensorsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.groovy.util.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Service
public class AvgMonthServiceImpl implements IAvgMonthService {


    @Autowired
    private AverageMonthlyAssetMapper avgAssetMapper;
    @Autowired
    private ClubLevelMapper clubLevelMapper;
    @Autowired
    private ClubConfig clubConfig;
    @Autowired(required = false)
    private UserMonthAverageAssetMapper monthAvgAssetMapper;
    @Autowired
    private ExecutorConfiguration executor;
    @Autowired
    private IClubPercepService clubPercepService;
    @Autowired
    private IUserServiceApi userService;
    @Autowired
    private SensorsUtil sensors;
    @Autowired
    private loserManagerMapper managerMapper;
    @Autowired
    private NotifyTemplate notifyTemplate;

    @Override
    public List<AverageMonthlyAsset> get4Evaluate(int page, int pageSize) {
        return avgAssetMapper.get4Evaluate(pageSize, pageSize * (page - 1));
    }

    @Override
    public List<UserMonthAverageAsset> getloserAsset4Evaluate(int page, int pageSize) {
        return monthAvgAssetMapper.get4Evaluate(pageSize, pageSize * (page - 1));
    }

    @Override
    public int syncAssetSource(List<AverageMonthlyAsset> source) {
        List<String> scanedUids = source.stream().map(AverageMonthlyAsset::getUid).collect(Collectors.toList());
        Example example = new Example(ClubLevel.class);
        example.createCriteria().andIn("uid", scanedUids);
        List<ClubLevel> clubLevels = clubLevelMapper.selectByExample(example);
        Map<String, ClubLevel> collect = clubLevels.stream().collect(Collectors.toMap(ClubLevel::getUid, Function.identity(), (v1, v2) -> v1));

        List<ClientMapping> mappings = managerMapper.getMappings(new HashSet<>(scanedUids));
        Map<String, ClientMapping> collect1 = mappings.stream().collect(Collectors.toMap(ClientMapping::getUid, Function.identity(), (v1, v2) -> v1));

        return reduceClubPoint(source, collect, collect1);
    }

    @Override
    public int syncloserAssetSource(List<UserMonthAverageAsset> source) {
        List<String> scanedUids = source.stream().map(UserMonthAverageAsset::getUid).collect(Collectors.toList());
        Example example = new Example(ClubLevel.class);
        example.createCriteria().andIn("uid", scanedUids);
        List<ClubLevel> clubLevels = clubLevelMapper.selectByExample(example);
        Map<String, ClubLevel> collect = clubLevels.stream().collect(Collectors.toMap(ClubLevel::getUid, Function.identity(), (v1, v2) -> v1));

        List<ClientMapping> mappings = managerMapper.getMappings(new HashSet<>(scanedUids));
        Map<String, ClientMapping> collect1 = mappings.stream().collect(Collectors.toMap(ClientMapping::getUid, Function.identity(), (v1, v2) -> v1));

        return reduceloserClubPoint(source, collect, collect1);
    }

    @Override
    public int batchUpsert(List<AverageMonthlyAsset> records) {
        return avgAssetMapper.batchUpsert(records);
    }

    private int reduceClubPoint(List<AverageMonthlyAsset> source, Map<String, ClubLevel> uidClubMap, Map<String, ClientMapping> uidManaMap) {
        if (CollectionUtil.isEmpty(source)) return 0;
        LocalDateTime nextClubUntil = clubConfig.nextClubUntil();
        LocalDateTime nextloserUntil = clubConfig.nextloserUntil();
        LocalDateTime now = LocalDateTime.now();
        long nowMilli = Instant.now().toEpochMilli();

        List<ClubUpgradeModel> updateList = new ArrayList<>(source.size());
        Map<String, Map<String, Object>> firstloser = new HashMap<>(source.size());
        Map<String, Map<String, Object>> upgradeloser = new HashMap<>(source.size());
        List<ClubUpdateTracer> tracerList = new ArrayList<>(source.size());
        List<ClubProfileSetter> setterList = new ArrayList<>(source.size());
        Function<AverageMonthlyAsset, ClubLevel> mapping =
                record -> {
                    BigDecimal tempPoint = record.getAvgAsset().divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);
                    Long clubPoint = Optional.of(tempPoint).filter(val -> val.compareTo(BigDecimal.ZERO) >= 0).map(BigDecimal::longValue).orElse(0L);
                    Integer latestLevel = ClubConfig.getLevel(clubPoint);

                    ClubLevel clubLevel = Optional.ofNullable(uidClubMap.get(record.getUid()))
                                                  .orElseGet(() -> new ClubLevel().setUid(record.getUid()).setClubLevel(-1).setValidUntil(nextClubUntil));
                    ClubLevel former = new ClubLevel();
                    BeanUtils.copyProperties(clubLevel, former);

                    Integer actulOldLevel = Optional.of(clubLevel).map(ClubLevel::getClubLevel).orElse(-1);

                    Integer loserLevel = ClubConfig.getloserLevel(record.getAvgAsset());
                    Integer benefitLevel = ClubConfig.getBenefitLevel(loserLevel);

                    // SensorsUtil
                    // ObjectUtil.cl
                    clubLevel.setClubPoint(clubPoint);
                    clubLevel.setBenefitLevel(benefitLevel);
                    clubLevel.clubRefresh(latestLevel, now, nextClubUntil);
                    clubLevel.loserRefresh(loserLevel, now, nextloserUntil);
                    if (former.isBlueloser() && clubLevel.isOrdinaryClub()) {
                        clubLevel.setUpgradeRead(true);
                    }

                    // sensors
                    CollectUtil.addIfPresent(tracerList, ClubUpdateTracer.getTracer(former, clubLevel, now, uidManaMap.get(record.getUid())));
                    CollectUtil.addIfPresent(setterList, ClubProfileSetter.getProfile(former, clubLevel));

                    // 这里不能取latestLevel  而应该取clubLevel.getClubLevel()
                    // 因为非1号只升不降，如果latestLevel是降级的，那么最新等级其实不变的
                    clubUpdate(former, clubLevel, nowMilli, updateList);
                    firstloser(former, clubLevel, firstloser);
                    upgradeloser(former, clubLevel, upgradeloser);

                    return clubLevel;
                };
        List<ClubLevel> collect = source.parallelStream().map(mapping).collect(Collectors.toList());
        int i = clubLevelMapper.batchUpsertClubLevel(collect);

        executor.getEXECUTOR().execute(() -> {
            clubPercepService.clubUpdateNofity(updateList);
            userService.kafkaNotifyMulti(firstloser,
                    Lists.newArrayList(notifyTemplate.getOfEmail().getFirstBlueloser()),
                    Lists.newArrayList(0), null
            );
            userService.kafkaNotifyMulti(upgradeloser,
                    Lists.newArrayList(notifyTemplate.getOfEmail().getUpgradeBuleloser(), notifyTemplate.getOfPush().getUpgradeBuleloser()),
                    Lists.newArrayList(0, 2), null
            );
            tracerList.forEach(trace -> sensors.track("UpdateUserLevel", trace));
            setterList.forEach(setter -> sensors.profileSet(setter));
        });
        return source.size();
    }

    private int reduceloserClubPoint(List<UserMonthAverageAsset> source, Map<String, ClubLevel> uidClubMap, Map<String, ClientMapping> uidManaMap) {
        if (CollectionUtil.isEmpty(source)) return 0;
        LocalDateTime nextClubUntil = clubConfig.nextClubUntil();
        LocalDateTime nextloserUntil = clubConfig.nextloserUntil();
        LocalDateTime now = LocalDateTime.now();
        long nowMilli = Instant.now().toEpochMilli();

        List<ClubUpgradeModel> updateList = new ArrayList<>(source.size());
        Map<String, Map<String, Object>> firstloser = new HashMap<>(source.size());
        Map<String, Map<String, Object>> upgradeloser = new HashMap<>(source.size());
        List<ClubUpdateTracer> tracerList = new ArrayList<>(source.size());
        List<ClubProfileSetter> setterList = new ArrayList<>(source.size());
        Function<UserMonthAverageAsset, ClubLevel> mapping =
                record -> {
                    BigDecimal tempPoint = record.getAveAsset().divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);
                    Long clubPoint = Optional.of(tempPoint).filter(val -> val.compareTo(BigDecimal.ZERO) >= 0).map(BigDecimal::longValue).orElse(0L);
                    Integer latestLevel = ClubConfig.getLevel(clubPoint);

                    ClubLevel clubLevel = Optional.ofNullable(uidClubMap.get(record.getUid()))
                                                  .orElseGet(() -> new ClubLevel().setUid(record.getUid()).setClubLevel(-1).setValidUntil(nextClubUntil));
                    ClubLevel former = new ClubLevel();
                    BeanUtils.copyProperties(clubLevel, former);
                    Integer actulOldLevel = Optional.of(clubLevel).map(ClubLevel::getClubLevel).orElse(-1);


                    Integer loserLevel = ClubConfig.getloserLevel(record.getAveAsset());
                    Integer benefitLevel = ClubConfig.getBenefitLevel(loserLevel);

                    clubLevel.setClubPoint(clubPoint);
                    clubLevel.setBenefitLevel(benefitLevel);
                    clubLevel.clubRefresh(latestLevel, now, nextClubUntil);
                    clubLevel.loserRefresh(loserLevel, now, nextloserUntil);
                    if (former.isBlueloser() && clubLevel.isOrdinaryClub()) {
                        clubLevel.setUpgradeRead(true);
                    }

                    // sensors
                    CollectUtil.addIfPresent(tracerList, ClubUpdateTracer.getTracer(former, clubLevel, now, uidManaMap.get(record.getUid())));
                    CollectUtil.addIfPresent(setterList, ClubProfileSetter.getProfile(former, clubLevel));

                    clubUpdate(former, clubLevel, nowMilli, updateList);
                    firstloser(former, clubLevel, firstloser);
                    upgradeloser(former, clubLevel, upgradeloser);
                    return clubLevel;
                };
        List<ClubLevel> collect = source.parallelStream().map(mapping).collect(Collectors.toList());
        int i = clubLevelMapper.batchUpsertClubLevel(collect);

        executor.getEXECUTOR().execute(() -> {
            clubPercepService.clubUpdateNofity(updateList);
            userService.kafkaNotifyMulti(firstloser,
                    Lists.newArrayList(notifyTemplate.getOfEmail().getFirstBlueloser()),
                    Lists.newArrayList(0), null
            );
            userService.kafkaNotifyMulti(upgradeloser,
                    Lists.newArrayList(notifyTemplate.getOfEmail().getUpgradeBuleloser(), notifyTemplate.getOfPush().getUpgradeBuleloser()),
                    Lists.newArrayList(0, 2), null
            );
            tracerList.forEach(trace -> sensors.track("UpdateUserLevel", trace));
            setterList.forEach(setter -> sensors.profileSet(setter));
        });
        return source.size();
    }

    private void clubUpdate(ClubLevel former, ClubLevel present, long nowMilli, List<ClubUpgradeModel> updateList) {
        if (!Objects.equals(former.getClubLevel(), present.getClubLevel())) {
            ClubUpgradeModel upgradeModel = new ClubUpgradeModel()
                    .setUid(former.getUid()).setUpdateTime(nowMilli).setUpdateOrigin("ROUTINE")
                    .setOldLevel(former.getClubLevel()).setLatestLevel(present.getClubLevel());
            updateList.add(upgradeModel);
        }
    }

    private void firstloser(ClubLevel former, ClubLevel present, Map<String, Map<String, Object>> firstloser) {
        Optional.ofNullable(present.getloserLevel())
                .filter(level -> MathUtil.greater(level, 0) && Objects.isNull(former.getloserLevel()))
                .ifPresent(level -> firstloser.put(present.getUid(), null));
    }

    private void upgradeloser(ClubLevel former, ClubLevel present, Map<String, Map<String, Object>> firstloser) {
        Optional.ofNullable(present.getloserLevel())
                .filter(level -> Optional.ofNullable(former.getloserLevel()).map(lvl -> MathUtil.greater(level, lvl)).orElse(false))
                .ifPresent(level -> firstloser.put(present.getUid(), Maps.of("level", String.valueOf(level))));
    }
}

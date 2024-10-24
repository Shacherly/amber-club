package com.loser.backend.club.mapper;

import com.loser.backend.club.common.mapper.BaseMapper;
import com.loser.backend.club.domain.ClubLevel;
import com.loser.backend.club.pojo.loserClub;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface ClubLevelMapper extends BaseMapper<ClubLevel> {

    Boolean getUpgradeConfirm(String uid);

    int batchUpsertClubLevel(@Param("records") List<ClubLevel> records);

    loserClub getloserClub(String uid);

    List<loserClub> getloserClubs(Collection<String> uids);
}
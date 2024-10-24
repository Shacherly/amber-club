package com.loser.backend.club.mapper;

import com.loser.backend.club.common.mapper.BaseMapper;
import com.loser.backend.club.domain.loserMission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface loserMissionMapper extends BaseMapper<loserMission> {

    List<loserMission> getByMissionType(@Param("uid") String uid, @Param("missionType") String missionType);
}
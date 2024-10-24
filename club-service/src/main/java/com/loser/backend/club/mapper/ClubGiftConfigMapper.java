package com.loser.backend.club.mapper;

import com.loser.backend.club.common.mapper.BaseMapper;
import com.loser.backend.club.domain.ClubGiftConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClubGiftConfigMapper extends BaseMapper<ClubGiftConfig> {

    List<ClubGiftConfig> getAllGiftConfigs(@Param("currentLevel") Integer currentLevel);
}
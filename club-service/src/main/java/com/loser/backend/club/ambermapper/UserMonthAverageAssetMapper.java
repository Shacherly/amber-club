package com.loser.backend.club.ambermapper;

import com.loser.backend.club.common.mapper.BaseMapper;
import com.loser.backend.club.domain.UserMonthAverageAsset;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMonthAverageAssetMapper extends BaseMapper<UserMonthAverageAsset> {

    List<UserMonthAverageAsset> get4Evaluate(@Param("limit") int size, @Param("offset") int offset);
}
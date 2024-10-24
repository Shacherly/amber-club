package com.loser.backend.club.mapper;

import com.loser.backend.club.common.mapper.BaseMapper;
import com.loser.backend.club.domain.AverageMonthlyAsset;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AverageMonthlyAssetMapper extends BaseMapper<AverageMonthlyAsset> {

    List<AverageMonthlyAsset> get4Evaluate(@Param("limit") int size, @Param("offset") int offset);

    int batchUpsert(@Param("records") List<AverageMonthlyAsset> records);
}
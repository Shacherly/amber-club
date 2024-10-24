package com.loser.backend.club.mapper;

import com.loser.backend.club.common.mapper.BaseMapper;
import com.loser.backend.club.domain.loserBenefitInventory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface loserBenefitInventoryMapper extends BaseMapper<loserBenefitInventory> {

    List<loserBenefitInventory> getByUid(
            @Param("uid") String uid,
            @Param("missionType") String missionType,
            @Param("benefitIndicat") String benefitIndicat
    );

    int countIvtries(@Param("uid") String uid, @Param("missionType") String missionType);

}
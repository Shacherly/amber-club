package com.loser.backend.club.mapper;


import org.apache.ibatis.annotations.Param;

public interface CustomloserMissionBindingMapper {

    Integer countBindNumber(@Param(value = "benefitInventoryId") Long benefitInventoryId);

    Integer countBindPeople(@Param(value = "benefitInventoryId") Long benefitInventoryId);

}
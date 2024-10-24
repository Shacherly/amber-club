package com.loser.backend.club.service;

import com.loser.backend.club.common.http.PageResult;
import com.loser.backend.club.controller.request.aceup.BenefitInventoryListQueryParam;
import com.loser.backend.club.controller.response.loserBenefitVO;
import com.loser.backend.club.controller.response.aceup.BenefitInventoryDetailVO;
import com.loser.backend.club.controller.response.aceup.BenefitInventoryListQueryVO;
import com.loser.backend.club.controller.response.aceup.MappingVO;
import com.loser.backend.club.domain.loserBenefitInventory;

import java.util.List;

/**
 * @author ：trading
 * @date ：Created in 2022/2/16 16:21
 * @description：
 * @modified By：
 */

public interface IBenefitInventoryService {

    Long saveOrUpdate(loserBenefitInventory loserBenefitInventory);

    PageResult<BenefitInventoryListQueryVO> list(BenefitInventoryListQueryParam param);

    BenefitInventoryDetailVO detail(Long id);

    void delete(Long id);

    void deleteMany(List<Long> ids);

    List<MappingVO> benefitInventoryMapping();

    List<MappingVO> benefitInventoryMappingByBenefitId(Long benefitId);


    List<loserBenefitInventory> getRoutineMissionBenefits(String uid, String benefitIndicat);

    List<loserBenefitInventory> getCustomMissionBenefits(String uid, String benefitIndicat);

    List<loserBenefitInventory> getStellarBenefits(String benefitIndicat);

    List<loserBenefitVO> getStellarBenefitVos(String uid, String benefitName);

    int count4Level(String uid);

    int getCustomCount(String uid);

    int getAvailableCount(String uid);

}

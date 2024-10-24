package com.loser.backend.club.service;

import com.loser.backend.club.common.http.PageResult;
import com.loser.backend.club.controller.request.aceup.BenefitListQueryParam;
import com.loser.backend.club.controller.response.aceup.BenefitListQueryVO;
import com.loser.backend.club.controller.response.aceup.MappingVO;
import com.loser.backend.club.domain.loserBenefit;

import java.util.List;

public interface IBenefitService {

    Long saveOrUpdate(loserBenefit loserBenefit);

    PageResult<BenefitListQueryVO> list(BenefitListQueryParam param);

    void delete(Long id);

    void deleteMany(List<Long> ids);

    List<MappingVO> benefitMapping();

    void expireMission(Long id);

    String getIndicator(Long benefitId);

    List<loserBenefit> getAllBenefits();

    List<String> getBenefitNames();

    List<String> getBenefitIndexs();
}

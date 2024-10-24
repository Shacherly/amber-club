package com.loser.backend.club.service;

import com.loser.backend.club.common.http.PageResult;
import com.loser.backend.club.controller.request.aceup.MissionListQueryParam;
import com.loser.backend.club.controller.response.aceup.MissionDetailVO;
import com.loser.backend.club.controller.response.aceup.MissionListQueryVO;
import com.loser.backend.club.domain.loserMission;

import java.util.List;

public interface IMissionService {

    Long saveOrUpdate(loserMission source);

    PageResult<MissionListQueryVO> list(MissionListQueryParam param);

    MissionDetailVO detail(Long id);

    void delete(Long id);

    void deleteMany(List<Long> ids);

    void syncLabelMission();

}

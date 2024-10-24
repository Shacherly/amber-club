package com.loser.backend.club.service;

import com.loser.backend.club.common.http.PageResult;
import com.loser.backend.club.controller.request.aceup.BwcListQueryParam;
import com.loser.backend.club.controller.request.aceup.loserLevelHistoryListQueryParam;
import com.loser.backend.club.controller.request.aceup.loserLevelModifyParam;
import com.loser.backend.club.controller.response.aceup.BwcListQueryVO;
import com.loser.backend.club.controller.response.aceup.loserLevelHistoryListQueryVO;

public interface IBwcLevelService {

    PageResult<BwcListQueryVO> list(BwcListQueryParam param);

    void updateloserLevel(loserLevelModifyParam param);

    PageResult<loserLevelHistoryListQueryVO> listHistory(loserLevelHistoryListQueryParam param);
}

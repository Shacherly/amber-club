package com.loser.backend.club.service;

import com.loser.backend.club.common.http.PageResult;
import com.loser.backend.club.controller.request.aceup.ReportListQureyParam;
import com.loser.backend.club.controller.request.whale.ReportParam;
import com.loser.backend.club.controller.request.whale.ReportReadParam;
import com.loser.backend.club.controller.request.whale.ReportRecommendParam;
import com.loser.backend.club.controller.response.BenefitReportVO;
import com.loser.backend.club.controller.response.ReportVO;
import com.loser.backend.club.controller.response.aceup.ReportDetailVO;
import com.loser.backend.club.controller.response.aceup.ReportListQueryVO;
import com.loser.backend.club.domain.loserReport;

import java.util.List;

/**
 * @author ：trading
 * @date ：Created in 2022/2/22 16:53
 * @description：
 * @modified By：
 */

public interface IReportService {

    Long saveOrUpdate(loserReport source);

    PageResult<ReportListQueryVO> list(ReportListQureyParam param);

    ReportDetailVO detail(Long id);

    void delete(Long id);

    void deleteMany(List<Long> ids);

    PageResult<loserReport> pageReport(ReportParam param);

    PageResult<BenefitReportVO> pageReportVo(ReportParam param);

    boolean readReport(ReportReadParam param);

    ReportVO reportDetail(Long id);

    int getPeriods();

    List<BenefitReportVO> getRecommends(ReportRecommendParam param);
}

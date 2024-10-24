package com.loser.backend.club.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.loser.backend.club.common.enums.StatusEnum;
import com.loser.backend.club.common.http.PageResult;
import com.loser.backend.club.constant.RedisKey;
import com.loser.backend.club.controller.request.aceup.ReportListQureyParam;
import com.loser.backend.club.controller.request.whale.ReportParam;
import com.loser.backend.club.controller.request.whale.ReportReadParam;
import com.loser.backend.club.controller.request.whale.ReportRecommendParam;
import com.loser.backend.club.controller.response.BenefitReportVO;
import com.loser.backend.club.controller.response.ReportVO;
import com.loser.backend.club.controller.response.aceup.ReportDetailVO;
import com.loser.backend.club.controller.response.aceup.ReportListQueryVO;
import com.loser.backend.club.domain.loserReport;
import com.loser.backend.club.exception.BusinessException;
import com.loser.backend.club.exception.ExceptionEnum;
import com.loser.backend.club.mapper.loserReportMapper;
import com.loser.backend.club.service.ICommonService;
import com.loser.backend.club.service.IReportService;
import com.loser.backend.club.util.ContextHolder;
import com.loser.backend.club.util.PageContext;
import com.loser.backend.club.util.TemporalUtil;
import com.loser.backend.club.util.VOConverter;
import com.trading.backend.coupon.common.cache.RedisService;
import com.trading.backend.coupon.common.util.Functions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ：trading
 * @date ：Created in 2022/2/22 16:55
 * @description：
 * @modified By：
 */

@Service
@Slf4j
public class ReportServiceImpl implements IReportService {

    @Autowired
    private loserReportMapper loserReportMapper;
    @Autowired
    private ICommonService commonService;
    @Autowired
    private RedisService redis;
    @Autowired
    private StringRedisTemplate stringRedis;

    @Override
    public Long saveOrUpdate(loserReport source) {
        Optional.ofNullable(source)
                .orElseThrow(() -> new BusinessException(ExceptionEnum.ARGUMENT_NULL, "source"));
        if (Objects.isNull(source.getId())){
            log.info("insert report = {}", source);
            save(source);
            return source.getId();
        }
        log.info("update report = {}", source);
        update(source);
        return source.getId();
    }

    @Override
    public PageResult<ReportListQueryVO> list(ReportListQureyParam param) {

        if ( StringUtils.isNotBlank(param.getId()) && !NumberUtil.isLong(param.getId())){
            return PageResult.empty();
        }

        Example query = new Example(loserReport.class);
        Example.Criteria criteria = query.createCriteria();
        criteria.andNotEqualTo("status", StatusEnum.DELETED.getIndicator());
        Optional.ofNullable(param.getId()).filter(StringUtils::isNotBlank).map(Long::parseLong).ifPresent(obj -> criteria.andEqualTo("id",obj));
        Optional.ofNullable(param.getCtime_start()).ifPresent(obj -> criteria.andGreaterThanOrEqualTo("ctime", TemporalUtil.ofEpochMilli(obj)));
        Optional.ofNullable(param.getCtime_end()).ifPresent(obj -> criteria.andLessThanOrEqualTo("ctime", TemporalUtil.ofEpochMilli(obj)));

        PageResult<loserReport> reportPage = PageContext.selectPage(() -> loserReportMapper.selectByExample(query));
        List<ReportListQueryVO> resList = reportPage.getItems().stream()
                .map(VOConverter::fromDomain)
                .collect(Collectors.toList());

        PageResult<ReportListQueryVO> res = PageResult.copyPageNoItems(reportPage);
        res.setItems(resList);

        return res;
    }

    @Override
    public ReportDetailVO detail(Long id) {
        Example query = new Example(loserReport.class);
        query.createCriteria().andEqualTo("id",id)
                .andNotEqualTo("status",StatusEnum.DELETED.getIndicator());
        loserReport domain = loserReportMapper.selectOneByExample(query);
        ReportDetailVO res = new ReportDetailVO();
        BeanUtils.copyProperties(domain,res);
        return res;
    }

    @Override
    public void delete(Long id) {
        List<Long> ids = Collections.singletonList(id);
        commonService.delete(ids, reportId -> {},  loserReport.class, loserReportMapper);
    }

    @Override
    public void deleteMany(List<Long> ids) {
        commonService.delete(ids, reportId -> {},  loserReport.class, loserReportMapper);
    }


    @Override
    public PageResult<loserReport> pageReport(ReportParam param) {
        Example query = new Example(loserReport.class);
        query.createCriteria().andEqualTo("reportClass", StringUtils.upperCase(param.getReport_type()))
                .andEqualTo("status",StatusEnum.ENABLE.getIndicator());
        return PageContext.selectPage(() -> loserReportMapper.selectByExample(query));
    }

    @Override
    public PageResult<BenefitReportVO> pageReportVo(ReportParam param) {
        PageResult<BenefitReportVO> result = PageResult.fromAnother(pageReport(param), VOConverter::makeInto);
        if (result.isEmpty()) return result;
        boolean read = Boolean.TRUE.equals(stringRedis.opsForSet().isMember(
                RedisKey.REPORT_READ + StringUtils.lowerCase(param.getReport_type()), ContextHolder.get().getXGwUser()));
        if (!read && param.getPage() == 1)
            result.getItems().get(0).setLastet(true);
        return result;
    }

    @Override
    public boolean readReport(ReportReadParam param) {
        stringRedis.opsForSet().add(
                RedisKey.REPORT_READ + StringUtils.lowerCase(param.getReport_type()), ContextHolder.get().getXGwUser());
        return true;
    }

    @Override
    public ReportVO reportDetail(Long id) {
        return Optional.ofNullable(loserReportMapper.selectByPrimaryKey(id))
                .filter(detail -> StatusEnum.ENABLE.getIndicator().equals(detail.getStatus()))
                .map(VOConverter::detailFromDomain)
                .map(detail -> {
                    boolean read = Boolean.TRUE.equals(stringRedis.opsForSet().isMember(
                            RedisKey.REPORT_READ + StringUtils.lowerCase(detail.getReportClass()), ContextHolder.get().getXGwUser()));
                    if (!read) detail.setLatest(true);
                    return detail;
                })
                .orElse(null);
    }

    @Override
    public int getPeriods() {
        Example example = new Example(loserReport.class);
        return loserReportMapper.selectCountByExample(example);
    }

    @Override
    public List<BenefitReportVO> getRecommends(ReportRecommendParam param) {
        Example example = new Example(loserReport.class);
        example.createCriteria()
               .andNotEqualTo("id", param.getReport_id())
               .andEqualTo("reportClass", StringUtils.upperCase(param.getReport_type()));
        example.setOrderByClause(" ctime DESC LIMIT 3 ");

        List<loserReport> loserReports = loserReportMapper.selectByExample(example);
        return Functions.toList(loserReports, VOConverter::makeInto);
    }

    private void update(loserReport source) {
        loserReportMapper.updateByPrimaryKeySelective(source);
    }

    private void save(loserReport source) {
        loserReportMapper.insertSelective(source);
        stringRedis.delete(RedisKey.REPORT_READ + StringUtils.lowerCase(source.getReportClass()));
    }
}

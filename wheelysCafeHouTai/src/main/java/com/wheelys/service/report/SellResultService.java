package com.wheelys.service.report;

import java.util.Date;
import java.util.List;

import com.wheelys.model.report.ReportOrderDatePaymethod;
import com.wheelys.web.action.report.vo.ReportBackSellProductVo;
import com.wheelys.web.action.report.vo.summaryVo.ReportSummaryActive;
import com.wheelys.web.action.report.vo.summaryVo.ReportSummaryYun;
import com.wheelys.web.action.report.vo.summaryVo.ReportSummaryZi;

public interface SellResultService {

	List<ReportBackSellProductVo> getBackSellList(Long shopid, String cityname, Date month, Date end);

	List<ReportOrderDatePaymethod> findPayMethodList(Long shopid, String cityname, Date begin, Date end);

	ReportSummaryYun findByYunShopSummary(Date begin, Date end);

	ReportSummaryZi findByZiSummary(Date begin, Date end);

	ReportSummaryActive findActiveSummary(Date begin, Date end);

}

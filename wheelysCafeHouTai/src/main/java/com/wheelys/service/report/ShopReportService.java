package com.wheelys.service.report;

import java.util.Date;
import java.util.List;

import com.wheelys.model.report.ReportOrderMemberShopDate;
import com.wheelys.web.action.report.vo.ReportOrderMemberShopDateVo;

public interface ShopReportService {

	List<ReportOrderMemberShopDate> getLatestReport();

	List<ReportOrderMemberShopDateVo> getReportList(Long shopid, Date begin, Date end);

}

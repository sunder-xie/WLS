package com.wheelys.job.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.wheelys.job.JobService;
import com.wheelys.job.ReportJob;
import com.wheelys.service.cafe.CafeShopService;
import com.wheelys.service.report.OrderReportService;

public class ReportJobImpl extends JobService implements ReportJob {

	@Autowired
	@Qualifier("orderReportService")
	private OrderReportService orderReportService;
	@Autowired
	@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;
	
	@Override
	public void everyDayReport() {
		try {
			cafeShopService.positiveClose();
			dbLogger.warn("强制店铺打烊!");
		} catch (Exception e) {
			dbLogger.warn("cafeShopService.positiveClose，强制店铺打烊异常");
		}
		try {
			orderReportService.saveReportByDatePaymenthod(new Date());
			dbLogger.warn("按日期店铺支付方式聚合订单数据!");
		} catch (Exception e) {
			dbLogger.warn("orderReportService.saveReportByDatePaymenthod，集合数据异常");
		}
		try {
			orderReportService.toDayReportProduct(null, new Date());
			dbLogger.warn("按日期店铺品项聚合订单数据!");
		} catch (Exception e) {
			dbLogger.warn("orderReportService.toDayReportProduct，集合数据异常");
		}
		try {
			orderReportService.generateData(new Date());
			dbLogger.warn("按日期店铺品项聚合用户数据!");
		} catch (Exception e) {
			dbLogger.warn("orderReportService.generateData，集合数据异常");
		}
		try {
			orderReportService.generamethodteData(new Date());
			dbLogger.warn("按店铺销售方式聚合数据!");
		} catch (Exception e) {
			dbLogger.warn("orderReportService.generamethodteData，集合数据异常");
		}
	}

}

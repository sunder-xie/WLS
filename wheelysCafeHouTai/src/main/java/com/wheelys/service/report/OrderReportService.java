package com.wheelys.service.report;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wheelys.model.pay.WheelysOrder;
import com.wheelys.model.pay.WheelysOrderDetail;
import com.wheelys.model.report.ReportOrderDatePaymethod;
import com.wheelys.web.action.report.vo.ReportOrderDatePaymethodVo;
import com.wheelys.web.action.report.vo.ReportOrderDateProductVo;

public interface OrderReportService {

	List<ReportOrderDatePaymethod> saveReportByDatePaymenthod(Date date);// 根据日期生成聚合表,并返回这天的数据集合，如果是今天则只查询添加

	List<WheelysOrder> savefindByfindBy(Long shopid, Date fromdate, Date todate, String paymethod);// 根据商铺id,时间,支付方式查询订单列表聚合表

	Map<String, ReportOrderDateProductVo> toDayReportProduct(Long shopid, Date date);// 查询咖啡详情

	List<WheelysOrderDetail> findAllByOderid(Long orderid);// 根据订单id查询订单详情

	ReportOrderDatePaymethod getTodayShopReport(Long shopid, Date date);

	List<ReportOrderDatePaymethodVo> findReport(Long shopid, Date time1, Date time2, String mothed);// 根据条件查询reportWheelys

	List<ReportOrderDateProductVo> getReportProductList(Long shopid, Date begin, Date end, String productName);

	void insertDailyShopReport(String beginDate, String endDate);

	List<ReportOrderDateProductVo> getReportProductListByDate(Long shopid, Date begin, Date end);

	void generateData(Date date);

	void generamethodteData(Date date);

	Map toDayReport();// 页面数据用的对象

}

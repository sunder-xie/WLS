package com.wheelys.web.action.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.util.BeanUtil;
import com.wheelys.util.DateUtil;
import com.wheelys.constant.CityContant;
import com.wheelys.constant.PayConstant;
import com.wheelys.model.CafeShop;
import com.wheelys.model.MemberOrderInfo;
import com.wheelys.model.report.ReportOrderDatePaymethod;
import com.wheelys.service.MemberOrderInfoService;
import com.wheelys.service.cafe.CafeShopService;
import com.wheelys.service.cafe.OrderService;
import com.wheelys.service.report.OrderReportService;
import com.wheelys.service.report.ShopReportService;
import com.wheelys.web.action.AnnotationController;
import com.wheelys.web.action.report.vo.ReportOrderDatePaymethodVo;
import com.wheelys.web.action.report.vo.ReportOrderMemberShopDateVo;

@Controller
public class ReportManageController extends AnnotationController {

	@Autowired
	@Qualifier("orderReportService")
	private OrderReportService orderReportService;
	@Autowired
	@Qualifier("shopReportService")
	private ShopReportService shopReportService;
	@Autowired
	@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;
	@Autowired
	@Qualifier("memberOrderInfoService")
	private MemberOrderInfoService memberOrderInfoService;
	@Autowired
	@Qualifier("orderService")
	private OrderService orderService;

	/*
	 * 主页上商铺的订单详情
	 */
	@RequestMapping("/admin/report/reportOrderList.xhtml")
	public String reportOrderList(Long id, Date fromdate, Date todate, ModelMap model) {
		if (fromdate == null)
			fromdate = new Date();
		if (todate == null)
			todate = new Date();
		model.put("orderList", orderReportService.savefindByfindBy(id, fromdate, todate, null));
		return "/admin/report/reportOrderList.vm";
	}

	/*
	 * 咖啡统计报表
	 */
	@RequestMapping("/admin/report/reportByProduct.xhtml")
	public String reportByProduct(Long shopid, Date begin, Date end, ModelMap model, String productName) {
		if (begin == null)
			begin = new Date();
		if (end == null)
			end = new Date();
		model.put("reportProductList", orderReportService.getReportProductList(shopid, begin, end, productName));
		List<CafeShop> shopList = cafeShopService.getOpenShopList();
		model.put("shopList", shopList);
		model.put("citymap", CityContant.CITYMAP);
		Map<Long, String> citynamemap = cafeShopService.getShopidCityNameMap();
		model.put("citynamemap", citynamemap);
		return "/admin/report/reportProductList.vm";
	}

	/*
	 * 多条件查询报表
	 */
	@RequestMapping("/admin/report/reportByPaymethod.xhtml")
	public String reportByPaymethod(Long shopid, Date begin, Date end, String paymethod, ModelMap model) {
		model.put("paymethodMap", PayConstant.PAYMAP);
		String pay = paymethod;
		model.put("pay", pay);
		if (begin == null)
			begin = DateUtil.addDay(new Date(), -1);
		if (end == null)
			end = DateUtil.addDay(new Date(), -1);
		List<ReportOrderDatePaymethodVo> shopReportList = orderReportService.findReport(shopid, begin, end, paymethod);
		model.put("shopReportList", shopReportList);
		if (shopid == null) {
			Map<String, ReportOrderDatePaymethodVo> shopReportVoMap = new HashMap<String, ReportOrderDatePaymethodVo>();
			for (ReportOrderDatePaymethodVo shopReport : shopReportList) {
				ReportOrderDatePaymethodVo shopReportVo = shopReportVoMap
						.get(shopReport.getShopid() + "_" + shopReport.getPaymethod());
				if (shopReportVo == null) {
					shopReportVo = new ReportOrderDatePaymethodVo();
					shopReportVo.setDate(DateUtil.formatDate(begin) + "~" + DateUtil.formatDate(end));
					shopReportVo = new ReportOrderDatePaymethodVo(
							DateUtil.formatDate(begin) + "~" + DateUtil.formatDate(end));
					BeanUtil.copyProperties(shopReportVo, shopReport);
					if (StringUtils.isBlank(paymethod))
						shopReportVo.setPaymethod("");
					shopReportVoMap.put(shopReport.getShopid() + "_" + shopReport.getPaymethod(), shopReportVo);
				} else {
					shopReportVo.setQuantity(shopReportVo.getQuantity() + shopReport.getQuantity());
					shopReportVo.setTotalfee(shopReportVo.getTotalfee() + shopReport.getTotalfee());
					shopReportVo.setDiscount(shopReportVo.getDiscount() + shopReport.getDiscount());
					shopReportVo.setNetpaid(shopReportVo.getNetpaid() + shopReport.getNetpaid());
					shopReportVo.setOrdercount(shopReportVo.getOrdercount() + shopReport.getOrdercount());
					shopReportVo.setPaybrokerage(shopReportVo.getPaybrokerage() + shopReport.getPaybrokerage());
					shopReportVo
							.setCompanybrokerage(shopReportVo.getCompanybrokerage() + shopReport.getCompanybrokerage());
					shopReportVo
							.setSettlementamount(shopReportVo.getSettlementamount() + shopReport.getSettlementamount());
				}
			}
			List<ReportOrderDatePaymethodVo> voList = new ArrayList<ReportOrderDatePaymethodVo>(
					shopReportVoMap.values());
			Collections.sort(voList, new PropertyComparator("shopid", false, true));
			model.put("shopReportList", voList);
		}
		List<CafeShop> shopList = cafeShopService.getOpenShopList();
		model.put("shopList", shopList);
		model.put("citymap", CityContant.CITYMAP);
		Map<Long, String> citynamemap = cafeShopService.getShopidCityNameMap();
		model.put("citynamemap", citynamemap);
		return "/admin/report/reportByPaymethod.vm";
	}

	/*
	 * 聚合的方法
	 */
	@RequestMapping("/admin/shop/finddate.xhtml")
	public String wheelysorder1(Date todate, ModelMap model) throws Exception {
		if (todate == null)
			todate = new Date();
		List<ReportOrderDatePaymethod> result = orderReportService.saveReportByDatePaymenthod(todate);
		model.put("porductMap", orderReportService.toDayReportProduct(null, todate));
		return this.showJsonSuccess(model, result);
	}

	@RequestMapping("/admin/report/reportByShop.xhtml")
	public String reportByShop(ModelMap model, Long shopid, Date begin, Date end) {
		List<ReportOrderMemberShopDateVo> reportList = shopReportService.getReportList(shopid, begin, end);
		model.put("reportList", reportList);
		List<CafeShop> shopList = cafeShopService.getOpenShopList();
		model.put("shopList", shopList);
		model.put("citymap", CityContant.CITYMAP);
		Map<Long, String> citynamemap = cafeShopService.getShopidCityNameMap();
		model.put("citynamemap", citynamemap);
		return "/admin/report/reportByShop.vm";
	}

	@RequestMapping("/admin/report/findNewRegistOrderMember.xhtml")
	public String findNewRegisterOrderMember(ModelMap model, Long shopid, Date day) {
		List<MemberOrderInfo> infoList = memberOrderInfoService.getNewOrderMemberByShopidAndDay(shopid, day);
		model.put("infoList", infoList);
		return "/admin/report/reportMemberOrderInfo.vm";
	}

	@RequestMapping("/admin/report/findOldRegistOrderMember.xhtml")
	public String findOldRegistOrderMember(ModelMap model, Long shopid, Date day) {
		List<MemberOrderInfo> infoList = memberOrderInfoService.getOldOrderMemberByShopidAndDay(shopid, day);
		model.put("infoList", infoList);
		return "/admin/report/reportMemberOrderInfo.vm";
	}

	@RequestMapping("/admin/report/findOrderedMember.xhtml")
	public String findOrderedMember(ModelMap model, Long shopid, Date day) {
		List<Long> memberids = orderService.getOrderedMemberids(shopid, day);
		List<MemberOrderInfo> infoList = memberOrderInfoService.getMemberInfoByIds(memberids);
		model.put("infoList", infoList);
		return "/admin/report/reportMemberOrderInfo.vm";
	}

	/*
	 * 多条件查询报表导出
	 */
	@RequestMapping("/admin/report/reportByPaymethodExcel.xhtml")
	public String reportByPaymethodExcel(Long shopid, Date begin, Date end, String paymethod, ModelMap model,
			HttpServletResponse response) {
		model.put("paymethodMap", PayConstant.PAYMAP);
		if (begin == null)
			begin = new Date();
		if (end == null)
			end = new Date();
		List<ReportOrderDatePaymethodVo> shopReportList = orderReportService.findReport(shopid, begin, end, paymethod);
		model.put("shopReportList", shopReportList);
		if (shopid == null) {
			Map<String, ReportOrderDatePaymethodVo> shopReportVoMap = new HashMap<String, ReportOrderDatePaymethodVo>();
			for (ReportOrderDatePaymethodVo shopReport : shopReportList) {
				ReportOrderDatePaymethodVo shopReportVo = shopReportVoMap
						.get(shopReport.getShopid() + "_" + shopReport.getPaymethod());
				if (shopReportVo == null) {
					shopReportVo = new ReportOrderDatePaymethodVo();
					shopReportVo.setDate(DateUtil.formatDate(begin) + "~" + DateUtil.formatDate(end));
					BeanUtil.copyProperties(shopReportVo, shopReport);
					if (StringUtils.isBlank(paymethod))
						shopReportVo.setPaymethod("");
					shopReportVoMap.put(shopReport.getShopid() + "_" + shopReport.getPaymethod(), shopReportVo);
				} else {
					shopReportVo.setQuantity(shopReportVo.getQuantity() + shopReport.getQuantity());
					shopReportVo.setTotalfee(shopReportVo.getTotalfee() + shopReport.getTotalfee());
					shopReportVo.setDiscount(shopReportVo.getDiscount() + shopReport.getDiscount());
					shopReportVo.setNetpaid(shopReportVo.getNetpaid() + shopReport.getNetpaid());
					shopReportVo.setOrdercount(shopReportVo.getOrdercount() + shopReport.getOrdercount());
					shopReportVo.setPaybrokerage(shopReportVo.getPaybrokerage() + shopReport.getPaybrokerage());
					shopReportVo
							.setCompanybrokerage(shopReportVo.getCompanybrokerage() + shopReport.getCompanybrokerage());
					shopReportVo
							.setSettlementamount(shopReportVo.getSettlementamount() + shopReport.getSettlementamount());
				}
			}
			List<ReportOrderDatePaymethodVo> voList = new ArrayList<ReportOrderDatePaymethodVo>(
					shopReportVoMap.values());
			Collections.sort(voList, new PropertyComparator("shopid", false, true));
			model.put("shopReportList", voList);
		}
		List<CafeShop> shopList = cafeShopService.getOpenShopList();
		model.put("shopList", shopList);
		model.put("citymap", CityContant.CITYMAP);
		Map<Long, String> citynamemap = cafeShopService.getShopidCityNameMap();
		model.put("citynamemap", citynamemap);
		this.download("xls", response);
		return "/admin/report/exportExcel/reportByPaymethodExcel.vm";
	}

	/**
	 * 店铺用户报表导出
	 * 
	 * @param model
	 * @param shopid
	 * @param begin
	 * @param end
	 * @return
	 */
	@RequestMapping("/admin/report/reportByShopExcel.xhtml")
	public String reportByShopExcel(ModelMap model, Long shopid, Date begin, Date end, HttpServletResponse response) {
		List<ReportOrderMemberShopDateVo> reportList = shopReportService.getReportList(shopid, begin, end);
		model.put("reportList", reportList);
		List<CafeShop> shopList = cafeShopService.getOpenShopList();
		model.put("shopList", shopList);
		model.put("citymap", CityContant.CITYMAP);
		Map<Long, String> citynamemap = cafeShopService.getShopidCityNameMap();
		model.put("citynamemap", citynamemap);
		this.download("xls", response);
		return "/admin/report/exportExcel/reportByShopExcel.vm";
	}

	/*
	 * 咖啡统计报表导出
	 */
	@RequestMapping("/admin/report/reportByProductExcel.xhtml")
	public String reportByProductExcel(Long shopid, Date begin, Date end, ModelMap model,
			HttpServletResponse response) {
		if (begin == null)
			begin = new Date();
		if (end == null)
			end = new Date();
		model.put("reportProductList", orderReportService.getReportProductList(shopid, begin, end, null));
		List<CafeShop> shopList = cafeShopService.getOpenShopList();
		model.put("shopList", shopList);
		model.put("citymap", CityContant.CITYMAP);
		Map<Long, String> citynamemap = cafeShopService.getShopidCityNameMap();
		model.put("citynamemap", citynamemap);
		this.download("xls", response);
		return "/admin/report/exportExcel/reportProductListExcel.vm";
	}

	/*
	 * 测试报表
	 */
	@RequestMapping("/admin/report/testExcel.xhtml")
	public String testExcel(Long shopid, ModelMap model) {
		Date d = new Date();
		orderReportService.toDayReportProduct(shopid, d);
		return this.showJsonSuccess(model, null);

	}
}

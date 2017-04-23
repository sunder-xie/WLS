package com.wheelys.web.action.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.util.DateUtil;
import com.wheelys.constant.CityContant;
import com.wheelys.constant.PayConstant;
import com.wheelys.model.CafeProduct;
import com.wheelys.model.CafeShop;
import com.wheelys.service.cafe.CafeProductService;
import com.wheelys.service.cafe.CafeShopService;
import com.wheelys.service.report.OrderReportService;
import com.wheelys.service.report.ShopReportService;
import com.wheelys.web.action.AnnotationController;
import com.wheelys.web.action.report.vo.ReportOrderDatePaymethodVo;
import com.wheelys.web.action.report.vo.ReportOrderDateProductVo;

@Controller
public class ReportFormatController extends AnnotationController {

	@Autowired
	@Qualifier("orderReportService")
	private OrderReportService orderReportService;
	@Autowired
	@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;
	@Autowired
	@Qualifier("cafeProductService")
	private CafeProductService cafeProductService;
	@Autowired
	@Qualifier("shopReportService")
	private ShopReportService shopReportService;

	@RequestMapping("/admin/report/getReportProductListByDate.xhtml")
	public String reportByProduct(Long shopid, Date begin, Date end, ModelMap model) {
		if (shopid != null) {
			if (begin == null)
				begin = new Date();
			if (end == null)
				end = new Date();
			List<ReportOrderDateProductVo> reportProductList = orderReportService.getReportProductListByDate(shopid,
					begin, end);
			List<CafeProduct> productList = new ArrayList<CafeProduct>();
			Map<String, ReportOrderDateProductVo> productDateMap = new HashMap<String, ReportOrderDateProductVo>();
			Map<String, Integer> totalMap = new HashMap<String, Integer>();
			for (ReportOrderDateProductVo vo : reportProductList) {
				String key = vo.getProductid() + "_" + vo.getDate();
				productDateMap.put(key, vo);
				CafeProduct product = cafeProductService.getProduct(vo.getProductid());
				if (!productList.contains(product)) {
					productList.add(product);
				}
				Integer paidfee = totalMap.get(vo.getDate() + "_paidfee");
				Integer totalfee = totalMap.get(vo.getDate() + "_totalfee");
				Integer quantity = totalMap.get(vo.getDate() + "_quantity");
				if (paidfee == null)
					paidfee = 0;
				if (totalfee == null)
					totalfee = 0;
				if (quantity == null)
					quantity = 0;
				paidfee += vo.getPaidfee();
				totalfee += vo.getTotalfee();
				quantity += vo.getQuantity();
				totalMap.put(vo.getDate() + "_paidfee", paidfee);
				totalMap.put(vo.getDate() + "_totalfee", totalfee);
				totalMap.put(vo.getDate() + "_quantity", quantity);
			}
			model.put("productDateMap", productDateMap);
			model.put("productList", productList);
			model.put("totalMap", totalMap);
			List<String> dateList = new ArrayList<String>();
			int diff = DateUtil.getDiffDay(begin, end);
			for (int i = 0; i <= diff; i++) {
				dateList.add(DateUtil.formatDate(DateUtil.addDay(begin, i)));
			}
			model.put("dateList", dateList);
		}
		List<CafeShop> shopList = cafeShopService.getOpenShopList();
		model.put("shopList", shopList);
		model.put("citymap", CityContant.CITYMAP);
		Map<Long, String> citynamemap = cafeShopService.getShopidCityNameMap();
		model.put("citynamemap", citynamemap);
		return "/admin/report/reportProductFormatList.vm";
	}

	@RequestMapping("/admin/report/getReportShopListByDate.xhtml")
	public String getReportShopListByDate(Long shopid, Date begin, Date end, ModelMap model, String paymethod) {
		if (shopid != null) {
			if (begin == null)
				begin = new Date();
			if (end == null)
				end = new Date();
			if (StringUtils.isBlank(paymethod))
				paymethod = PayConstant.PAYMETHOD_WEIXINH5;
			List<ReportOrderDatePaymethodVo> reportList = orderReportService.findReport(shopid, begin, end, paymethod);
			model.put("reportList", reportList);
			CafeShop shop = cafeShopService.getShop(shopid);
			model.put("shop", shop);
		}
		List<CafeShop> shopList = cafeShopService.getOpenShopList();
		model.put("shopList", shopList);
		model.put("citymap", CityContant.CITYMAP);
		Map<Long, String> citynamemap = cafeShopService.getShopidCityNameMap();
		model.put("citynamemap", citynamemap);
		return "/admin/report/reportByPaymethodFormat.vm";
	}

	/**
	 * 格式报表1导出
	 * 
	 * @param shopid
	 * @param begin
	 * @param end
	 * @param model
	 * @param paymethod
	 * @return
	 */
	@RequestMapping("/admin/report/getReportShopListByDateExcel.xhtml")
	public String getReportShopListByDateExcel(Long shopid, Date begin, Date end, ModelMap model, String paymethod,
			HttpServletResponse response) {
		if (shopid != null) {
			if (begin == null)
				begin = new Date();
			if (end == null)
				end = new Date();
			if (StringUtils.isBlank(paymethod))
				paymethod = PayConstant.PAYMETHOD_WEIXINH5;
			List<ReportOrderDatePaymethodVo> reportList = orderReportService.findReport(shopid, begin, end, paymethod);
			model.put("reportList", reportList);
			CafeShop shop = cafeShopService.getShop(shopid);
			model.put("shop", shop);
		}
		List<CafeShop> shopList = cafeShopService.getOpenShopList();
		model.put("shopList", shopList);
		model.put("citymap", CityContant.CITYMAP);
		Map<Long, String> citynamemap = cafeShopService.getShopidCityNameMap();
		model.put("citynamemap", citynamemap);
		this.download("xls", response);
		return "/admin/report/exportExcel/reportByPaymethodFormatExcel.vm";
	}

	/**
	 * 咖啡统计表格式表导出
	 * 
	 * @param shopid
	 * @param begin
	 * @param end
	 * @param model
	 * @return
	 */
	@RequestMapping("/admin/report/getReportProductListByDateExcel.xhtml")
	public String reportByProductExcel(Long shopid, Date begin, Date end, ModelMap model,
			HttpServletResponse response) {
		if (shopid != null) {
			if (begin == null)
				begin = new Date();
			if (end == null)
				end = new Date();
			List<ReportOrderDateProductVo> reportProductList = orderReportService.getReportProductListByDate(shopid,
					begin, end);
			List<CafeProduct> productList = new ArrayList<CafeProduct>();
			Map<String, ReportOrderDateProductVo> productDateMap = new HashMap<String, ReportOrderDateProductVo>();
			Map<String, Integer> totalMap = new HashMap<String, Integer>();
			for (ReportOrderDateProductVo vo : reportProductList) {
				String key = vo.getProductid() + "_" + vo.getDate();
				productDateMap.put(key, vo);
				CafeProduct product = cafeProductService.getProduct(vo.getProductid());
				if (!productList.contains(product)) {
					productList.add(product);
				}
				Integer paidfee = totalMap.get(vo.getDate() + "_paidfee");
				Integer totalfee = totalMap.get(vo.getDate() + "_totalfee");
				Integer quantity = totalMap.get(vo.getDate() + "_quantity");
				if (paidfee == null)
					paidfee = 0;
				if (totalfee == null)
					totalfee = 0;
				if (quantity == null)
					quantity = 0;
				paidfee += vo.getPaidfee();
				totalfee += vo.getTotalfee();
				quantity += vo.getQuantity();
				totalMap.put(vo.getDate() + "_paidfee", paidfee);
				totalMap.put(vo.getDate() + "_totalfee", totalfee);
				totalMap.put(vo.getDate() + "_quantity", quantity);
			}
			model.put("productDateMap", productDateMap);
			model.put("productList", productList);
			model.put("totalMap", totalMap);
			List<String> dateList = new ArrayList<String>();
			int diff = DateUtil.getDiffDay(begin, end);
			for (int i = 0; i <= diff; i++) {
				dateList.add(DateUtil.formatDate(DateUtil.addDay(begin, i)));
			}
			model.put("dateList", dateList);
		}
		List<CafeShop> shopList = cafeShopService.getOpenShopList();
		model.put("shopList", shopList);
		model.put("citymap", CityContant.CITYMAP);
		Map<Long, String> citynamemap = cafeShopService.getShopidCityNameMap();
		model.put("citynamemap", citynamemap);
		this.download("xls", response);
		return "/admin/report/exportExcel/reportProductFormatListExcel.vm";
	}
}

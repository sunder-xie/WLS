package com.wheelys.web.action.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.service.report.OrderReportService;
import com.wheelys.web.action.AnnotationController;
import com.wheelys.web.action.report.vo.ReportOrderDateProductVo;

@Controller
public class AdminController extends AnnotationController {

	@Autowired
	@Qualifier("orderReportService")
	private OrderReportService orderReportService;
	
	@RequestMapping("/admin/index.xhtml")
	public String index(ModelMap model) {
		Map findToDay = orderReportService.toDayReport();
		model.put("topage", findToDay);
		Map<String, ReportOrderDateProductVo> porductMap = orderReportService.toDayReportProduct(null,new Date());
		List<ReportOrderDateProductVo> porductList = new ArrayList<ReportOrderDateProductVo>();
		porductList.addAll(porductMap.values());
		Collections.sort(porductList, new PropertyComparator("quantity", false, false));
		model.put("porductList", porductList);
		return "/admin/index.vm";
	}

	@RequestMapping("/admin/loadDailyShopReport.xhtml")
	public String loadDailyShopReport(String beginDate, String endDate){
		orderReportService.insertDailyShopReport(beginDate, endDate);
		return "/admin/index.vm";
	}
	
}

package com.wheelys.web.action.admin.order;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.web.util.PageUtil;
import com.wheelys.constant.CafeOrderConstant;
import com.wheelys.constant.CityContant;
import com.wheelys.constant.OrderConstant;
import com.wheelys.constant.PayConstant;
import com.wheelys.model.CafeShop;
import com.wheelys.model.citymanage.CityRegion;
import com.wheelys.service.admin.CityRegionService;
import com.wheelys.service.cafe.CafeShopService;
import com.wheelys.service.cafe.OrderService;
import com.wheelys.web.action.openapi.vo.WheelysOerderVo;

@Controller
public class OrderManagerController {

	@Autowired
	@Qualifier("orderService")
	private OrderService orderService;
	@Autowired
	@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;
	@Autowired
	@Qualifier("cityRegionService")
	private CityRegionService cityRegionService;

	@RequestMapping("/admin/orderrefund/showAllOrder.xhtml")
	public String showAllOrder(ModelMap model, Integer pageNo, String region, String cityCode, Long shopId,
			String status, String tradeno, Date createTimeBegin, Date createTimeEnd, String paymethod, String category,
			String elecCardBatchId) {
		if (pageNo == null) {
			pageNo = 0;
		}
		int rowsPerPage = 10;
		List<WheelysOerderVo> orderList = orderService.findOrderList(pageNo, rowsPerPage, region, cityCode, shopId,
				status, tradeno, createTimeBegin, createTimeEnd, paymethod, category, elecCardBatchId);
		model.put("orderList", orderList);
		model.put("payMap", PayConstant.PAYMAP);
		model.put("statusMap", OrderConstant.ORDER_STATUS_MAP);
		model.put("cityMap", CityContant.CITYMAP);
		model.put("categoryMap", CafeOrderConstant.CATEGORY_MAP);
		List<CafeShop> shopList = cafeShopService.getOpenShopList();
		model.put("shopList", shopList);
		List<CityRegion> regionList = cityRegionService.findAllRegion();
		model.put("regionList", regionList);
		int listCount = orderService.findOrderListCount(region, cityCode, shopId, status, tradeno, createTimeBegin,
				createTimeEnd, paymethod, category, elecCardBatchId);
		PageUtil pageUtil = new PageUtil(listCount, rowsPerPage, pageNo, "admin/orderrefund/showAllOrder.xhtml");
		Map<String, Object> params = new HashMap<>();
		params.put("region", region);
		params.put("cityCode", cityCode);
		params.put("shopId", shopId);
		params.put("status", status);
		params.put("tradeno", tradeno);
		params.put("createTimeBegin", createTimeBegin);
		params.put("createTimeEnd", createTimeEnd);
		params.put("paymethod", paymethod);
		params.put("category", category);
		pageUtil.initPageInfo(params);
		model.put("pageUtil", pageUtil);
		return "/admin/ordermanager/allOrderDetails.vm";
	}

	@RequestMapping("/admin/orderrefund/orderManager/getOrderDetails.xhtml")
	public String getOrderDetails(ModelMap model, String tradeno) {
		WheelysOerderVo order = orderService.findOrderByTradeno(tradeno);
		model.put("order", order);
		model.put("statusMap", OrderConstant.ORDER_STATUS_MAP);
		model.put("payMap", PayConstant.PAYMAP);
		model.put("categoryMap", CafeOrderConstant.CATEGORY_MAP);
		return "/admin/ordermanager/orderDetailByTradeno.vm";
	}
}

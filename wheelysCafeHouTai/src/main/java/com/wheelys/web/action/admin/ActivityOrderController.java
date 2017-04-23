package com.wheelys.web.action.admin;

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
import com.wheelys.constant.CityContant;
import com.wheelys.model.CafeShop;
import com.wheelys.service.admin.ActivityOrdService;
import com.wheelys.service.cafe.CafeShopService;
import com.wheelys.web.action.AnnotationController;
import com.wheelys.web.action.admin.vo.CardCouponsOrderVo;

@Controller
public class ActivityOrderController extends AnnotationController {

	@Autowired
	@Qualifier("activityOrdService")
	private ActivityOrdService activityOrdService;
	@Autowired
	@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;

	@RequestMapping("/admin/orderrefund/showorderList.xhtml")
	public String showactiveOrder(Long shopid, Date begin, Date end, String mobile, Integer pageNo, ModelMap model) {
		if (pageNo == null)
			pageNo = 0;
		int rowsPerPage = 20;
		List<CardCouponsOrderVo> actorderList = activityOrdService.findordvoList(pageNo, mobile, rowsPerPage, shopid,
				begin, end);
		model.put("actorderList", actorderList);
		List<CafeShop> shopList = cafeShopService.getOpenShopList();
		model.put("shopList", shopList);
		model.put("citymap", CityContant.CITYMAP);
		// 获取商品总个数
		int count = activityOrdService.findCount(mobile, shopid, begin, end);
		// 设置分页查询
		PageUtil pageUtil = new PageUtil(count, rowsPerPage, pageNo, "admin/orderrefund/showorderList.xhtml");
		Map params = new HashMap();
		params.put("shopid", shopid);
		params.put("begin", begin);
		params.put("end", end);
		params.put("mobile", mobile);
		pageUtil.initPageInfo(params);
		model.put("pageUtil", pageUtil);
		return "/admin/activeOrder/showActiveOrderList.vm";
	}

}

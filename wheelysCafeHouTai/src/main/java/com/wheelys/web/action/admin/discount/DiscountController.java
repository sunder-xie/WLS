package com.wheelys.web.action.admin.discount;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.util.DateUtil;
import com.wheelys.web.util.PageUtil;
import com.wheelys.constant.DiscountContant;
import com.wheelys.model.CafeItem;
import com.wheelys.model.CafeProduct;
import com.wheelys.model.CafeShop;
import com.wheelys.model.discount.DiscountActivity;
import com.wheelys.service.admin.ProductService;
import com.wheelys.service.cafe.CafeShopService;
import com.wheelys.service.cafe.DiscountService;
import com.wheelys.service.ticket.TicketService;
import com.wheelys.web.action.AnnotationController;
import com.wheelys.web.action.admin.vo.DiscountActivityVo;

@Controller
public class DiscountController extends AnnotationController {
	@Autowired
	@Qualifier("discountService")
	private DiscountService discountService;
	@Autowired
	@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;
	@Autowired
	@Qualifier("productService")
	private ProductService productService;
	@Autowired
	@Qualifier("ticketService")
	private TicketService ticketService;

	@RequestMapping("/admin/ticket/showDiscountList.xhtml")
	public String showDiscountList(Integer pageNo, String actname, String activitytype, ModelMap model) {
		if (pageNo == null)
			pageNo = 0;
		int rowsPerPage = 10;
		Map<String, String> activitymap = DiscountContant.ACTIVITYMAP;
		model.put("activitymap", activitymap);
		Map<String, String> statemap = DiscountContant.STATEMAP;
		model.put("statemap", statemap);
		List<DiscountActivityVo> discountList = discountService.findAllDiscountList(pageNo, actname, activitytype,
				rowsPerPage);
		model.put("discountList", discountList);
		Map<String, String> prioritymap = DiscountContant.PRIORITYMAP;
		model.put("prioritymap", prioritymap);
		// 获取商品个数
		int count = discountService.findAllCount(actname, activitytype);
		// 设置分页查询
		PageUtil pageUtil = new PageUtil(count, rowsPerPage, pageNo, "admin/ticket/showDiscountList.xhtml");
		Map<String, String> params=new HashMap();
		params.put("actname", actname);
		params.put("activitytype", activitytype);
		pageUtil.initPageInfo(params);
		model.put("pageUtil", pageUtil);
		return "/admin/activity/showDiscountList.vm";
	}

	@RequestMapping("/admin/ticket/addDiscount.xhtml")
	public String addDiscount(ModelMap model) {
		Map<String, String> activitymap = DiscountContant.ACTIVITYMAP;
		model.put("activitymap", activitymap);
		Map<String, String> prioritymap = DiscountContant.PRIORITYMAP;
		model.put("prioritymap", prioritymap);
		Map<String, String> weekmap = DiscountContant.WEEKMAP;
		model.put("weekmap", weekmap);
		Map<String, String> appendmap = DiscountContant.APPENDMAP;
		model.put("appendmap", appendmap);
		// 获取所有参与店铺和商品分类
		List<CafeShop> shopList = cafeShopService.getOpenShopList();
		model.put("shopList", shopList);
		List<CafeItem> cafeItemList = productService.getCafeItemList();
		model.put("cafeItemList", cafeItemList);
		Map<Long, List<CafeProduct>> productMap = productService.getCafeProductMap();
		model.put("productMap", productMap);
		return "/admin/activity/addDiscount.vm";
	}

	@RequestMapping("/admin/ticket/updateDiscount.xhtml")
	public String updateDiscount(ModelMap model, String type, String addprice, String name, String discountmark,
			Integer discountrate, Integer fixamount, Timestamp begintime, Timestamp endtime, String week,
			String hourfrom, String hourto, Integer limitamount, String priority, String remark, String shopids,
			String producrids, Integer limitcup, Integer discountamount, Integer limitmaxnum) {
		DiscountActivity discountActivity = new DiscountActivity(type);
		if (StringUtils.isBlank(type)) {
			return this.showJsonError(model, "活动分类不能为空");
		}
		if (StringUtils.isBlank(name)) {
			return this.showJsonError(model, "活动名称不能为空");
		}
		if (discountmark != null) {
			discountActivity.setDiscountmark(discountmark);
		}
		if (StringUtils.equals(DiscountContant.ZHEKOU, type)) {
			if (discountrate != null) {
				discountActivity.setDiscountrate(discountrate);
			}
		}
		if (StringUtils.equals(DiscountContant.SECONDCUP, type)) {
			if (discountrate != null) {
				discountActivity.setDiscountrate(discountrate);
			}
		}
		if (StringUtils.equals(DiscountContant.FIRSTCUP, type) || StringUtils.equals(DiscountContant.FIXAMOUNT, type)) {
			if (fixamount != null) {
				discountActivity.setFixamount(fixamount);
			}

		}
		if (StringUtils.equals(DiscountContant.MANJIAN, type)) {
			if (limitcup != null) {
				discountActivity.setLimitcup(limitcup);
			}
			if (discountamount != null) {
				discountActivity.setDiscountamount(discountamount);
			}
		}
		if (StringUtils.isNotBlank(week)) {
			discountActivity.setWeek(week);
		}
		if (StringUtils.isNotBlank(hourfrom) && StringUtils.isNotBlank(hourto)) {
			discountActivity.setHourfrom(hourfrom);
			discountActivity.setHourto(hourto);
		}
		if (StringUtils.isNotBlank(remark)) {
			discountActivity.setRemark(remark);
		}
		if (StringUtils.isNotBlank(addprice)) {
			discountActivity.setAddprice(addprice);
		}
		if (limitamount != null) {
			discountActivity.setLimitamount(limitamount);
		}
		discountActivity.setName(name);
		discountActivity.setPriority(priority);
		discountActivity.setBegintime(begintime);
		discountActivity.setEndtime(DateUtil.addSecond(DateUtil.getLastTimeOfDay(endtime), -10));
		if (StringUtils.isNotBlank(shopids)) {
			discountActivity.setShopids(shopids);
		}
		String itemids = ticketService.jsonTomapTokey(producrids);
		if (StringUtils.isNotBlank(producrids)) {
			discountActivity.setValiditemid(itemids);
			discountActivity.setValidproductid(producrids);
		}
		if(limitmaxnum != null){
			discountActivity.setLimitmaxnum(limitmaxnum);
			discountActivity.setAllowaddnum(0);
		}
		discountService.savediscount(discountActivity);
		return this.showJsonSuccess(model, null);
	}

	@RequestMapping("/admin/ticket/activitydetail.xhtml")
	public String activitydetail(Long id, ModelMap model) {
		Map<String, String> activitymap = DiscountContant.ACTIVITYMAP;
		model.put("activitymap", activitymap);
		DiscountActivity activity = discountService.findActiveById(id);
		model.put("activity", activity);
		Map<String, String> prioritymap = DiscountContant.PRIORITYMAP;
		model.put("prioritymap", prioritymap);
		Map<String, String> appendmap = DiscountContant.APPENDMAP;
		model.put("appendmap", appendmap);
		String weekstr = activity.getWeek();
		String[] strings = weekstr.split(",");
		Map<String, String> weekmap = DiscountContant.WEEKMAP;
		List<String> weeklist = new ArrayList<String>();
		for (String weekid : strings) {
			String weekname = weekmap.get(weekid);
			weeklist.add(weekname);
		}
		model.put("weeklist", weeklist);
		if (StringUtils.isNotBlank(activity.getShopids())) {
			ArrayList<CafeShop> shopList = new ArrayList<>();
			String str1 = activity.getShopids();
			String[] shopids1 = str1.split(",");
			Long[] shopids = new Long[shopids1.length];
			for (int i = 0; i < shopids1.length; i++) {
				shopids[i] = Long.valueOf(shopids1[i]);
				CafeShop shop = cafeShopService.getShop(shopids[i]);
				shopList.add(shop);
			}
			model.put("shopList", shopList);
		}
		Map<CafeItem, List<CafeProduct>> itemproductMap = ticketService.itemproductMap(activity.getValidproductid());
		model.put("itemproductMap", itemproductMap);

		return "/admin/activity/activitydetail.vm";
	}

	/**
	 * it can be replace by interfacec:/admin/activity/showDiscountList.xhtml
	 * 
	 * @param actname
	 * @param pageNo
	 * @param model
	 * @return
	 */
	@RequestMapping("/admin/ticket/activitysearch.xhtml")
	public String activitysearch(String actname, Integer pageNo, ModelMap model) {
		if (pageNo == null)
			pageNo = 0;
		int rowsPerPage = 10;
		List<DiscountActivity> showactList = discountService.showactivityList(actname, pageNo, rowsPerPage);
		model.put("showactList", showactList);
		Map<String, String> activitymap = DiscountContant.ACTIVITYMAP;
		model.put("activitymap", activitymap);
		Map<String, String> prioritymap = DiscountContant.PRIORITYMAP;
		model.put("prioritymap", prioritymap);
		int count = discountService.findListCount(actname);
		PageUtil pageUtil = new PageUtil(count, rowsPerPage, pageNo, "admin/ticket/activitysearch.xhtml");
		Map params = new HashMap();
		params.put("actname", actname);
		pageUtil.initPageInfo(params);
		model.put("pageUtil", pageUtil);
		return "/admin/activity/searchactivity.vm";
	}

	@RequestMapping("/admin/ticket/actstop.xhtml")
	public String actstop(Long id) {
		DiscountActivity activity = discountService.findActiveById(id);
		if (StringUtils.equals(activity.getStatus(), "Y")) {
			activity.setStatus("Z");
		}
		discountService.savediscount(activity);
		return "forward:/admin/ticket/showDiscountList.xhtml";
	}

	@RequestMapping("/admin/ticket/actbegin.xhtml")
	public String actbegin(Long id, ModelMap model) {
		DiscountActivity activity = discountService.findActiveById(id);
		if (StringUtils.equals(activity.getStatus(), "Z")) {
			activity.setStatus("Y");
		} else if (StringUtils.equals(activity.getStatus(), "Y")) {
			activity.setStatus("Z");
		}
		discountService.savediscount(activity);
		return this.showJsonSuccess(model, activity.getStatus());
	}

	@RequestMapping("/admin/ticket/copyactivity.xhtml")
	public String copyactivity(Long id, ModelMap model) {
		DiscountActivity activity = discountService.findActiveById(id);
		model.put("activity", activity);
		Map<String, String> activitymap = DiscountContant.ACTIVITYMAP;
		model.put("activitymap", activitymap);
		Map<String, String> prioritymap = DiscountContant.PRIORITYMAP;
		model.put("prioritymap", prioritymap);
		Map<String, String> weekmap = DiscountContant.WEEKMAP;
		model.put("weekmap", weekmap);
		Map<String, String> appendmap = DiscountContant.APPENDMAP;
		model.put("appendmap", appendmap);
		List<CafeShop> shopList = cafeShopService.getOpenShopList();
		model.put("shopList", shopList);
		List<CafeItem> cafeItemList = productService.getCafeItemList();
		model.put("cafeItemList", cafeItemList);
		Map<Long, List<CafeProduct>> productMap = productService.getCafeProductMap();
		model.put("productMap", productMap);
		String weekstr = activity.getWeek();
		String[] strings = weekstr.split(",");
		List<String> weeklist = new ArrayList<String>();
		for (String str : strings) {
			weeklist.add(str);
		}
		model.put("weeklist", weeklist);
		List<Long> itemidList = discountService.itemids(id);
		model.put("itemidList", itemidList);
		List<Long> idList = discountService.productids(id);
		model.put("idList", idList);
		return "/admin/activity/copyactivity.vm";
	}

}

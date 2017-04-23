package com.wheelys.web.action.admin.ticket;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wheelys.model.acl.User;
import com.wheelys.util.BeanUtil;
import com.wheelys.util.DateUtil;
import com.wheelys.util.StringUtil;
import com.wheelys.web.util.PageUtil;
import com.wheelys.constant.ElecCardConstant;
import com.wheelys.model.CafeItem;
import com.wheelys.model.CafeProduct;
import com.wheelys.model.CafeShop;
import com.wheelys.model.pay.ElecCardBatch;
import com.wheelys.service.admin.ProductService;
import com.wheelys.service.cafe.CafeProductService;
import com.wheelys.service.cafe.CafeShopService;
import com.wheelys.service.ticket.TicketService;
import com.wheelys.web.action.AnnotationController;

@Controller
public class TicketController extends AnnotationController {

	@Autowired
	@Qualifier("ticketService")
	private TicketService ticketService;
	@Autowired
	@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;
	@Autowired
	@Qualifier("productService")
	private ProductService productService;
	@Autowired
	@Qualifier("cafeProductService")
	private CafeProductService cafeProductService;

	@RequestMapping("/admin/ticket/showTicketList.xhtml")
	public String showTicketList(Integer pageNo, String name, ModelMap model) {
		if (pageNo == null)
			pageNo = 0;
		int rowsPerPage = 10;
		Map<String, String> cardMap = ElecCardConstant.CARDTYPEMAP;
		model.put("cardMap", cardMap);
		// 获取所有商品列表
		List<ElecCardBatch> ticketList = ticketService.findAllTicketList(pageNo, name, rowsPerPage);
		model.put("ticketList", ticketList);
		// 获取商品个数
		int count = ticketService.findAllCount(name);
		// 设置分页查询
		PageUtil pageUtil = new PageUtil(count, rowsPerPage, pageNo, "admin/ticket/showTicketList.xhtml");
		pageUtil.initPageInfo();
		model.put("pageUtil", pageUtil);
		return "/admin/ticket/showTicketList.vm";
	}

	@RequestMapping("/admin/ticket/addTicket.xhtml")
	public String addTicket(ModelMap model) {
		Map<String, String> cardMap = ElecCardConstant.CARDTYPEMAP;
		Map<String, String> jsMap = ElecCardConstant.JSTYPEMAP;
		Map<String, String> priceMAP = ElecCardConstant.MARKAMOUNTMAP;
		model.put("priceMAP", priceMAP);
		model.put("jsMap", jsMap);
		model.put("cardMap", cardMap);
		// 获取所有参与店铺
		List<CafeShop> shopList = cafeShopService.getOpenShopList();
		model.put("shopList", shopList);
		// 获取所有商品及分类
		List<CafeItem> cafeItemList = productService.getCafeItemList();
		model.put("cafeItemList", cafeItemList);
		Map<Long, List<CafeProduct>> productMap = productService.getCafeProductMap();
		model.put("productMap", productMap);
		return "/admin/ticket/addTicket.vm";
	}

	@RequestMapping("/admin/ticket/updateTicket.xhtml")
	public String updateTicket(String cardtype, String cardname, String annexation, Integer amount, Integer minPrice,
			String dhname, Integer maxPrice, Timestamp timefrom, Timestamp timeto, Integer daynum, String description,
			String jstype, Integer limitcup, Integer cardcount, String remark, String validshopid, String producrids,
			String mark,Integer receivenum,Integer cycleday,Integer costprice,
			String command, ModelMap model) {

		User user = this.getLogonMember();
		Long userId = user.getId();
		if (StringUtils.isBlank(cardtype)) {
			return this.showJsonError(model, "卡类型为空");
		}
		if (StringUtils.isBlank(cardname)) {
			return this.showJsonError(model, "卡名称为空");
		}
		ElecCardBatch elecCardBatch = new ElecCardBatch(cardtype);
		if (StringUtils.equals(ElecCardConstant.CARDTYPE_1, cardtype)) {
			if (amount!=null) {
				elecCardBatch.setAmount(amount);
			}
			if (StringUtils.isNotBlank(mark)) {
				elecCardBatch.setAmountmark(mark);
			}
			if (minPrice != null) {
				elecCardBatch.setMinPrice(minPrice);
			}
		} else if (StringUtils.equals(ElecCardConstant.CARDTYPE_2, cardtype)) {
			if (StringUtils.isBlank(dhname)) {
				return this.showJsonError(model, "兑换名称为空");
			}
			elecCardBatch.setDhname(dhname);
			if (maxPrice != null) {
				elecCardBatch.setMaxPrice(maxPrice);
			}
		} else {
			return this.showJsonError(model, "服务端错误");
		}
		if (daynum != null && daynum != 0) {
			elecCardBatch.setDaynum(daynum);
		} else {
			elecCardBatch.setTimefrom(timefrom);
			elecCardBatch.setTimeto(DateUtil.addSecond(DateUtil.getLastTimeOfDay(timeto), -10));
		}
		// 判断拼的json串是否为空
		int shoplength = validshopid.length();
		int prolength = producrids.length();
		if (shoplength == 0 || prolength == 1) {
			return this.showJsonError(model, "保存错误: 请选择您所需要的店铺或商品");
		}
		String itemids = ticketService.jsonTomapTokey(producrids);
		if (itemids != null) {
			elecCardBatch.setValiditemid(itemids);
		}
		elecCardBatch.setValidproductid(producrids);
		elecCardBatch.setValidshopid(validshopid);
		if (annexation != null) {
			elecCardBatch.setAnnexation(annexation);
		}
		if (limitcup != null) {
			elecCardBatch.setLimitcup(limitcup);
		}
		if (receivenum!=null) {
			elecCardBatch.setReceivenum(receivenum);
		}
		if (cycleday!=null) {
			elecCardBatch.setCycleday(cycleday);
		}
		if (costprice!=null) {
			elecCardBatch.setCostprice(costprice);
		}
		elecCardBatch.setCardname(cardname);
		elecCardBatch.setDescription(description);
		elecCardBatch.setJstype(jstype);
		elecCardBatch.setCardcount(cardcount);
		elecCardBatch.setRemark(remark);
		elecCardBatch.setUserId(userId);
		elecCardBatch.setCommand(command);
		ticketService.updateTicket(elecCardBatch);
		return this.showJsonSuccess(model, null);
	}

	public static void main(String[] args) {
		System.out.println(StringUtil.md5(StringUtil.getUID() + System.currentTimeMillis()));
	}

	@RequestMapping("/admin/ticket/ticketdelete.xhtml")
	public String deleteTicket(Long id) {
		User user = this.getLogonMember();
		Long userid = user.getId();
		ElecCardBatch elecCardBatch = ticketService.getTicketDetail(id);
		Long userId2 = elecCardBatch.getUserId();
		if (userid.equals(userId2)) {
			ticketService.updateTicket(id);
		}
		return "redirect:/admin/ticket/showTicketList.xhtml";
	}

	@RequestMapping("/admin/ticket/carddetail.xhtml")
	public String getTicketDetail(Long id, ModelMap model) {
		Map<String, String> cardMap = ElecCardConstant.CARDTYPEMAP;
		model.put("cardMap", cardMap);
		Map<String, String> jsMap = ElecCardConstant.JSTYPEMAP;
		model.put("jsMap", jsMap);
		ElecCardBatch elecCardBatch = ticketService.getTicketDetail(id);
		model.put("elecCardBatch", elecCardBatch);
		Map<String, String> priceMAP = ElecCardConstant.MARKAMOUNTMAP;
		model.put("priceMAP", priceMAP);
		// 根据附加商品id查询相关商品
		String annexation = elecCardBatch.getAnnexation();
		if (StringUtils.isNotBlank(annexation)) {
			List<Long> idList = BeanUtil.getIdList(annexation, ",");
			List<CafeProduct> proList = new ArrayList<>();
			for (Long pid : idList) {
				CafeProduct cafeProduct = cafeProductService.getProductById(pid);
				proList.add(cafeProduct);
			}
			model.put("proList", proList);
		}
		if (StringUtils.isNotBlank(elecCardBatch.getValidshopid())) {
			ArrayList<CafeShop> shopList = new ArrayList<>();
			// 根据优惠券id获取所有参与店铺
			String str1 = elecCardBatch.getValidshopid();
			String[] shopids1 = str1.split(",");
			Long[] shopids = new Long[shopids1.length];
			for (int i = 0; i < shopids1.length; i++) {
				shopids[i] = Long.valueOf(shopids1[i]);
				CafeShop shop = cafeShopService.getShop(shopids[i]);
				shopList.add(shop);
			}
			model.put("shopList", shopList);
		}
		// 获取所有品类对应品项的集合
		Map<CafeItem, List<CafeProduct>> itemproductMap = ticketService
				.itemproductMap(elecCardBatch.getValidproductid());
		model.put("itemproductMap", itemproductMap);

		return "/admin/ticket/ticketDetail.vm";
	}

	@RequestMapping("/admin/ticket/findShopById.xhtml")
	@ResponseBody
	public String findShopById(String annexation) {
		List<Long> idList = BeanUtil.getIdList(annexation, ",");
		String result = "";
		for (Long id : idList) {
			CafeProduct product = cafeProductService.getProductById(id);
			if (product == null) {
				result += "<font color='red'>" + id + ":未查询到匹配商品<br/>" + "</font>";
			} else {
				result += "<font color='green'>" + id + ":" + product.getName() + "<br/>" + "</font>";
			}
		}
		return result;
	}

	@RequestMapping("/admin/ticket/copy.xhtml")
	public String copyTicket(Long id, ModelMap model) {
		Map<String, String> cardMap = ElecCardConstant.CARDTYPEMAP;
		Map<String, String> jsMap = ElecCardConstant.JSTYPEMAP;
		Map<String, String> priceMAP = ElecCardConstant.MARKAMOUNTMAP;
		model.put("priceMAP", priceMAP);
		model.put("jsMap", jsMap);
		model.put("cardMap", cardMap);
		// 获取所有参与店铺
		List<CafeShop> shopList = cafeShopService.getOpenShopList();
		model.put("shopList", shopList);
		// 获取所有商品及分类
		List<CafeItem> cafeItemList = productService.getCafeItemList();
		model.put("cafeItemList", cafeItemList);
		Map<Long, List<CafeProduct>> productMap = productService.getCafeProductMap();
		model.put("productMap", productMap);
		ElecCardBatch elecCardBatch = ticketService.getTicketDetail(id);
		model.put("elecCardBatch", elecCardBatch);
		if (id != null) {
			List<String> idList = ticketService.shopnames(id);
			model.put("idList", idList);
			List<Long> productList = ticketService.productids(id);
			model.put("productList", productList);
			List<Long> itemList = ticketService.caitmsids(id);
			model.put("itemList", itemList);
		}
		return "/admin/ticket/addTicket.vm";
	}
}

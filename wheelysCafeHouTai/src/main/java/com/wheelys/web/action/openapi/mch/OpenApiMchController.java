package com.wheelys.web.action.openapi.mch;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wheelys.Config;
import com.wheelys.api.vo.ResultCode;
import com.wheelys.util.BeanUtil;
import com.wheelys.util.DateUtil;
import com.wheelys.util.JsonUtils;
import com.wheelys.constant.OrderConstant;
import com.wheelys.model.CafeShop;
import com.wheelys.model.booking.Booking;
import com.wheelys.model.company.CompanyActivity;
import com.wheelys.model.merchant.MchOrder;
import com.wheelys.model.merchant.MchProduct;
import com.wheelys.model.merchant.MchProductItem;
import com.wheelys.model.orderrefund.RefundOrder;
import com.wheelys.model.report.ReportOrderDatePaymethod;
import com.wheelys.pay.AliWapPayUtil;
import com.wheelys.service.admin.BookingService;
import com.wheelys.service.admin.CompanyActityService;
import com.wheelys.service.admin.MchOrderService;
import com.wheelys.service.admin.MchProductService;
import com.wheelys.service.admin.NoticeService;
import com.wheelys.service.admin.OrderRefundService;
import com.wheelys.service.admin.ShopService;
import com.wheelys.service.admin.SupplierManagerService;
import com.wheelys.service.cafe.CafeShopService;
import com.wheelys.service.report.OrderReportService;
import com.wheelys.util.VmUtils;
import com.wheelys.util.WebUtils;
import com.wheelys.web.action.admin.vo.NoticeVo;
import com.wheelys.web.action.openapi.OpenApiBaseController;
import com.wheelys.web.action.openapi.vo.MchOrderDetailVo;
import com.wheelys.web.action.openapi.vo.MchOrderVo;
import com.wheelys.web.action.openapi.vo.OpneApiMchProductItemVo;
import com.wheelys.web.action.openapi.vo.OpneApiMchProductVo;
import com.wheelys.web.action.openapi.vo.RefundOrderVo;

@ResponseBody
@Controller
@RequestMapping("/opneapi/pos")
public class OpenApiMchController extends OpenApiBaseController {

	@Autowired
	@Qualifier("config")
	private Config config;
	@Autowired
	@Qualifier("shopService")
	private ShopService shopService;
	@Autowired
	@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;
	@Autowired
	@Qualifier("mchProductService")
	private MchProductService mchProductService;
	@Autowired
	@Qualifier("mchOrderService")
	private MchOrderService mchOrderService;
	@Autowired
	@Qualifier("orderReportService")
	private OrderReportService orderReportService;
	@Autowired
	@Qualifier("orderRefundService")
	private OrderRefundService orderRefundService;
	@Autowired
	@Qualifier("supplierManagerService")
	private SupplierManagerService supplierManagerService;
	@Autowired
	@Qualifier("operatorActityService")
	private CompanyActityService operatorActityService;
	@Autowired
	@Qualifier("noticeService")
	private NoticeService noticeService;
	@Autowired
	@Qualifier("bookingService")
	private BookingService bookingService;

	@RequestMapping("/shop/getProductList.xhtml")
	public String getProductList(Long shopid, String name, HttpServletRequest request) {
		dbLogger.warn(JsonUtils.writeMapToJson(WebUtils.getRequestMap(request)));
		CafeShop cafeShop = shopService.findCafeShop(shopid);
		List<MchProduct> mchProductList = mchProductService.mchProductlist(null, name, "Y", cafeShop.getSupplierid(), 0,
				500);
		Map<Long, MchProductItem> itemMap = mchProductService.mchProductItemMap();
		Map mchProductMap = BeanUtil.groupBeanList(mchProductList, "itemid");
		List<OpneApiMchProductItemVo> itemVoList = new ArrayList<OpneApiMchProductItemVo>();
		for (Long itemidKey : itemMap.keySet()) {
			List<OpneApiMchProductVo> productVoList = new ArrayList<OpneApiMchProductVo>();
			List<MchProduct> productList = (List<MchProduct>) mchProductMap.get(itemidKey);
			if (productList == null)
				continue;
			for (MchProduct mchProduct : productList) {
				OpneApiMchProductVo productVo = new OpneApiMchProductVo(mchProduct, config.getString("picPath"));
				productVoList.add(productVo);
			}
			MchProductItem mchProductItem = itemMap.get(itemidKey);
			OpneApiMchProductItemVo itemVo = new OpneApiMchProductItemVo(mchProductItem.getId(),
					mchProductItem.getName(), productVoList);
			itemVoList.add(itemVo);
		}
		return this.successJsonResult(itemVoList);
	}

	@RequestMapping("/shop/getItemList.xhtml")
	public String getItemList() {
		Map<Long, MchProductItem> itemMap = mchProductService.mchProductItemMap();
		List<OpneApiMchProductItemVo> itemVoList = new ArrayList<OpneApiMchProductItemVo>();
		for (Long itemidKey : itemMap.keySet()) {
			MchProductItem mchProductItem = itemMap.get(itemidKey);
			OpneApiMchProductItemVo itemVo = new OpneApiMchProductItemVo(mchProductItem.getId(),
					mchProductItem.getName(), null);
			itemVoList.add(itemVo);
		}
		return this.successJsonResult(itemVoList);
	}

	@RequestMapping("/shop/addMchOrder.xhtml")
	public String addMchOrder(HttpServletRequest request, Long shopid, String orderDtailJson, String mobile, String contacts, String isexpress) {
		dbLogger.warn(JsonUtils.writeMapToJson(WebUtils.getRequestMap(request)));
		String clientIp = WebUtils.getRemoteIp(request);
		List<MchOrderDetailVo> detailList = JsonUtils.readJsonToObjectList(MchOrderDetailVo.class, orderDtailJson);
		ResultCode<MchOrderVo> result = mchOrderService.addMchOrder(shopid, mobile, contacts, isexpress, detailList, clientIp);
		if (result.isSuccess()) {
			return this.successJsonResult(result.getRetval());
		} else {
			return this.errorJsonResult(result.getMsg());
		}
	}

	@RequestMapping("/shop/confirmMchOrder.xhtml")
	public String confirmMchOrder(HttpServletRequest request, Long shopid, String contacts, String mobile,
			String tradeno, String status, String remark, String isexpress) {
		dbLogger.warn(JsonUtils.writeMapToJson(WebUtils.getRequestMap(request)));
		String clientIp = WebUtils.getRemoteIp(request);
		ResultCode<MchOrderVo> result = mchOrderService.confirmMchOrder(shopid, tradeno, status, contacts, mobile, isexpress, remark, clientIp);
		if (result.isSuccess()) {
			return this.successJsonResult(result.getRetval());
		} else {
			return this.errorJsonResult(result.getMsg());
		}
	}

	@RequestMapping("/shop/getMchOrderList.xhtml")
	public String getMchOrderList(HttpServletRequest request, Integer pageno, Long shopid, 
			String status, Timestamp fromtime, Timestamp totime, String tradeno) {
		dbLogger.warn(JsonUtils.writeMapToJson(WebUtils.getRequestMap(request)));
		List<MchOrder> orderList = mchOrderService.getMchOrderList(shopid, status, fromtime, totime, tradeno, pageno);
		List<MchOrderVo> voList = new ArrayList<MchOrderVo>();
		for (MchOrder order : orderList) {
			MchOrderVo vo = new MchOrderVo(order);
			voList.add(vo);
		}
		return this.successJsonResult(voList);
	}

	@RequestMapping("/shop/getMchOrderDetail.xhtml")
	public String getMchOrderDetail(HttpServletRequest request, Long shopid, String tradeno) {
		dbLogger.warn(JsonUtils.writeMapToJson(WebUtils.getRequestMap(request)));
		MchOrderVo orderVo = mchOrderService.getMchOrderVoDetail(shopid, tradeno);
		if (StringUtils.equals(OrderConstant.STATUS_SEND, orderVo.getStatus())) {
			if (orderVo.getSendtime() != null) {
				Timestamp confimTime = DateUtil.addDay(orderVo.getSendtime(), 14);
				long difftime = confimTime.getTime() - DateUtil.getMillTimestamp().getTime();
				String diffdateStr = (difftime / DateUtil.m_day) + "天" + (difftime % DateUtil.m_day / DateUtil.m_hour)
						+ "时" + (difftime % DateUtil.m_hour / DateUtil.m_minute) + "分";
				orderVo.setConfimtime(diffdateStr);
			}
		}
		return this.successJsonResult(orderVo);
	}

	@RequestMapping("/upgrade.xhtml")
	public String upgrade() {
		Map map = new HashMap();
		map.put("version", "1.5.0");
		map.put("upgradeurl", config.getString("ossPath") + "android/v1.5.0.apk");
		map.put("description", "1.商户采购的支付宝支付功能\n 2.运营商活动申请功能\n 3.通知（后台可以给咖啡师发通知）\n 4.申请关店（店铺因意外可以申请关停和开店）");
		map.put("updatetime", DateUtil.getMillTimestamp());
		return this.successJsonResult(map);
	}

	@RequestMapping("/shop/todayOrderReport.xhtml")
	public String todayOrderReport(Long shopid) {
		ReportOrderDatePaymethod report = orderReportService.getTodayShopReport(shopid, new Date());
		Map data = new HashMap();
		data.put("quantity", report.getQuantity());
		data.put("ordercount", report.getOrdercount());
		data.put("netpaid", VmUtils.getAmount(report.getNetpaid()));
		return this.successJsonResult(data);
	}

	@RequestMapping("/shop/orderReportByDay.xhtml")
	public String orderReportByDay(Long shopid, Date date) {
		if (date == null) date = new Date();
		ReportOrderDatePaymethod report = orderReportService.getTodayShopReport(shopid, date);
		Map data = new HashMap();
		data.put("quantity", report.getQuantity());
		data.put("ordercount", report.getOrdercount());
		data.put("netpaid", VmUtils.getAmount(report.getNetpaid()));
		return this.successJsonResult(data);
	}

	@RequestMapping("/order/cafeOrderRefund.xhtml")
	public String orderRefund(HttpServletRequest request, String reason, String tradeno, Float refundamount) {
		dbLogger.warn(JsonUtils.writeMapToJson(WebUtils.getRequestMap(request)));
		int amount = (int) (refundamount * 100);
		
		ResultCode<RefundOrder> result = orderRefundService.addRefund(124L, null, reason, tradeno, amount);
		if (result.isSuccess()) {
			return this.successJsonResult(null);
		}
		return this.errorJsonResult(result.getMsg());
	}

	@RequestMapping("/notice/getNoticeList.xhtml")
	public String getNoticeList(Long shopid) {
		List<NoticeVo> noticeList = noticeService.voList(shopid);
		return this.successJsonResult(noticeList);
	}

	@RequestMapping("/order/refundOrderList.xhtml")
	public String getHistortyOrderList(HttpServletRequest request, Integer pageno, Long shopid, 
			Timestamp fromtime, Timestamp totime, String tradeno) {
		dbLogger.warn(JsonUtils.writeMapToJson(WebUtils.getRequestMap(request)));
		List<RefundOrderVo> orderList = orderRefundService.getOrderList(shopid, fromtime, totime, tradeno, pageno);
		return this.successJsonResult(orderList);
	}

	@RequestMapping("/shop/changeShopStatus.xhtml")
	public String changeShopStatus(HttpServletRequest request, Long shopid, String status, Timestamp closedowntime,String reason) {
		//open close closedown startbusiness cancel
		dbLogger.warn(JsonUtils.writeMapToJson(WebUtils.getRequestMap(request)));
		ResultCode result = shopService.changeShopStatus(shopid, status, closedowntime,reason);
		if(result.isSuccess()){
			Map data = new HashMap();
			data.put("status", status);
			CafeShop shop = cafeShopService.getShop(shopid);
			data.put("shopstatus", shop.getBooking());
			return this.successJsonResult(data);
		}
		return this.errorJsonResult(result.getMsg());
	}

	@RequestMapping("/shop/getDetail.xhtml")
	public String getDetail(Long shopid) {
		CafeShop shop = cafeShopService.getShop(shopid);
		Map data = new HashMap();
		data.put("shopstatus", shop.getBooking());
		Booking booking = bookingService.getCurBooking(shopid);
		if(booking != null){
			data.put("closedowntime", booking.getApplytime());
			data.put("reason", booking.getReason());
			if(StringUtils.equals(booking.getStatus(), "closedown") || StringUtils.equals(booking.getStatus(), "startbusiness")){
				data.put("status", booking.getStatus());
			}
		}
		return this.successJsonResult(data);
	}

	@RequestMapping("/order/getMchOrderPay.xhtml")
	public String getMchOrderPay(HttpServletRequest request, String tradeno) {
		dbLogger.warn(JsonUtils.writeMapToJson(WebUtils.getRequestMap(request)));
		Map map = new HashMap();
		MchOrder order = mchOrderService.getMchOrderDetail(tradeno);
		String payurl = AliWapPayUtil.getPrecreatePayUrl(order, config.getString("wpsPath"));
		map.put("payurl", payurl);
		return this.successJsonResult(map);
	}
	
	@RequestMapping("/operoter/operoterActity.xhtml")
	public String orderRefund(HttpServletRequest request, Long shopid, String operatorinfo) {
		dbLogger.warn(JsonUtils.writeMapToJson(WebUtils.getRequestMap(request)));
		ResultCode<CompanyActivity>result = operatorActityService.addoperator(shopid,operatorinfo);
		if(result.isSuccess()){
			return this.successJsonResult(null);
		}
		return this.errorJsonResult(result.getMsg());
	}
}

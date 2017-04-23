package com.wheelys.web.action.openapi.mobile;

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

import com.wheelys.Config;
import com.wheelys.api.vo.ResultCode;
import com.wheelys.util.BeanUtil;
import com.wheelys.constant.CafeOrderConstant;
import com.wheelys.constant.ElecCardConstant;
import com.wheelys.model.CafeShopProfile;
import com.wheelys.model.discount.DiscountActivity;
import com.wheelys.model.order.MemberAddress;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.cafe.CafeShopProfileService;
import com.wheelys.service.order.DiscountService;
import com.wheelys.service.order.MemberAddressService;
import com.wheelys.service.order.OrderService;
import com.wheelys.service.pay.ElecCardService;
import com.wheelys.util.VmUtils;
import com.wheelys.web.action.openapi.OpenApiBaseController;
import com.wheelys.web.action.wap.vo.ElecCardVo;
import com.wheelys.web.action.wap.vo.WheelysOrderDetailVo;
import com.wheelys.web.action.wap.vo.WheelysOrderVo;

@Controller
@RequestMapping("/openapi/mobile")
public class OpenApiHomeController extends OpenApiBaseController {
	
	@Autowired@Qualifier("elecCardService")
	private ElecCardService elecCardService;
	@Autowired@Qualifier("orderService")
	private OrderService orderService;
	@Autowired@Qualifier("config")
	private Config config;
	@Autowired@Qualifier("cafeShopProfileService")
	private CafeShopProfileService cafeShopProfileService;
	@Autowired@Qualifier("memberAddressService")
	private MemberAddressService memberAddressService;
	@Autowired@Qualifier("discountService")
	private DiscountService discountService;
	
	/**
	 * 优惠券列表
	 * @param pageNo
	 * @param maxnum
	 * @param model
	 * @return
	 */
	@RequestMapping("/home/cardList.xhtml")
	public String cardList(Integer pageNo, Integer maxnum, ModelMap model){
		if(maxnum == null) maxnum = 6;
		if(pageNo == null) pageNo = 0;
		WheelysMember member = this.getLogonMember(true);
		List<ElecCardVo> cardList = elecCardService.getElecCardVoList(member.getId(), null, ElecCardConstant.STATUS_SOLD, pageNo, maxnum);
		List<Map> result = BeanUtil.getBeanMapList(cardList, "id", "cardname", "begintime", "endtime", "description", "cardtype", "dhname", "amount", "minprice");
		return successJsonResult(model, result);
	}
	/**
	 * 历史优惠券列表
	 * @param pageNo
	 * @param maxnum
	 * @param model
	 * @return
	 */
	@RequestMapping("/home/cardListHistory.xhtml")
	public String cardListHistory(Integer pageNo, Integer maxnum, ModelMap model){
		if(maxnum == null) maxnum = 6;
		if(pageNo == null) pageNo = 0;
		WheelysMember member = this.getLogonMember(true);
		elecCardService.unlockElecCard(member.getId());
		List<ElecCardVo> cardList = elecCardService.getElecCardVoList(member.getId(), null, ElecCardConstant.STATUS_USED, pageNo, maxnum);
		List<Map> result = BeanUtil.getBeanMapList(cardList, "id", "cardname", "begintime", "endtime", "description", "cardtype", "dhname", "amount", "minprice");
		return successJsonResult(model, result);
	}
	/**
	 * 订单列表
	 * @param pageNo
	 * @param maxnum
	 * @param status
	 * @param model
	 * @return
	 */
	@RequestMapping("/home/getOrderList.xhtml")
	public String orderList(Integer pageNo, Integer maxnum, String status, ModelMap model){		
		if(maxnum == null) maxnum = 6;
		if(pageNo == null) pageNo = 0;
		WheelysMember member = this.getLogonMember(true);
		List<WheelysOrderVo> orderVoList = orderService.getOrderList(status, member.getId(), pageNo, maxnum);
		List<Map> result = new ArrayList<>();
		Map tempMap = null;
		for (WheelysOrderVo order : orderVoList) {
			tempMap = new HashMap<>();
			tempMap.put("id", order.getId());
			tempMap.put("tradeno", order.getTradeno());
			if(order.getShop() == null || StringUtils.isBlank(order.getShop().getShopimg())){
				tempMap.put("shopimg", "");	//默认图？
			}else{
				tempMap.put("shopimg", config.getString("mobilePicPath") + order.getShop().getShopimg());
			}
			tempMap.put("ordertitle", order.getOrdertitle());
			tempMap.put("detailList", BeanUtil.getBeanPropertyList(order.getDetailList(), "productname", true));
			tempMap.put("orderStatus", order.getOrderStatus());
			tempMap.put("netpaid", VmUtils.getAmount(order.getNetpaid()));
			tempMap.put("createtime", order.getCreatetime());
			result.add(tempMap);
		}
		return successJsonResult(model, result);
	}
	/**
	 * 订单详情
	 * @param tradeno
	 * @param model
	 * @return
	 */
	@RequestMapping("/home/orderDetail.xhtml")
	public String orderDetail(String tradeno, ModelMap model){
		WheelysMember member = this.getLogonMember(true);
		ResultCode<WheelysOrderVo> orderVoList = orderService.getWheelysOrderVo(tradeno, member.getId());
		if(!orderVoList.isSuccess()){
			return errorJsonResult(model, orderVoList.getMsg());
		}
		WheelysOrderVo order = orderVoList.getRetval();
		Map<String, Object> result = new HashMap<>();
		List<Map> detailList = new ArrayList<>();
		//商品信息
		for (WheelysOrderDetailVo detailVo : order.getDetailList()) {
			Map<String, Object> tempMap = new HashMap<>();
			tempMap.put("productname", detailVo.getProductname());
			tempMap.put("productename", detailVo.getProductename());
			tempMap.put("description", detailVo.getDescription());
			tempMap.put("discountnum", detailVo.getDiscountnum());
			tempMap.put("quantity", detailVo.getQuantity());
			tempMap.put("paidfee", VmUtils.getAmount(detailVo.getPaidfee()));
			tempMap.put("totalfee", VmUtils.getAmount(detailVo.getTotalfee()));
			detailList.add(tempMap);
		}
		result.put("detailList", detailList);
		//订单信息
		result.put("payfee", VmUtils.getAmount(order.getPayfee()));
		result.put("disreason", order.getDisreason());
		result.put("discount", VmUtils.getAmount(order.getDiscount()));
		result.put("takekey", order.getTakekey());
		result.put("category", order.getCategory());//配送方式 TAKE RESERVED TAKEAWAY
		if(order.getShop() != null && StringUtils.isNotBlank(order.getShop().getShopaddress())){
			result.put("shopaddress", order.getShop().getShopaddress());
		}
		result.put("taketime", order.getTaketime());
		if(StringUtils.equals(order.getCategory(), CafeOrderConstant.CATEGORY_TAKEAWAY)){
			CafeShopProfile shopProfile = cafeShopProfileService.getShopProfile(order.getShopid());
			MemberAddress memberAddress = memberAddressService.getMemberAddressByMemidAndShopid(order.getMemberid(), order.getShopid());
			result.put("detailaddress", memberAddress.getDetailaddress());
			result.put("delivermobile", shopProfile.getMobile());
		}
		if(order.getSdid() != null && order.getSdid() > 0){
			DiscountActivity discount = discountService.getDiscount(order.getSdid());
			if(discount != null){
				result.put("discounttype", discount.getType());
			}
		}
		result.put("refundStatus", order.getRefundStatus());
		return successJsonResult(model, result);
	}
}

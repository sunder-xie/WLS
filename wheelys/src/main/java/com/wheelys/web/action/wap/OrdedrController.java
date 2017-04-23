package com.wheelys.web.action.wap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.Config;
import com.wheelys.api.vo.ResultCode;
import com.wheelys.util.BeanUtil;
import com.wheelys.util.DateUtil;
import com.wheelys.constant.CafeOrderConstant;
import com.wheelys.constant.ElecCardConstant;
import com.wheelys.constant.PayConstant;
import com.wheelys.helper.ElecCardUtil;
import com.wheelys.helper.OrderContainer;
import com.wheelys.model.CafeKey;
import com.wheelys.model.CafeProduct;
import com.wheelys.model.CafeShopProfile;
import com.wheelys.model.CartProduct;
import com.wheelys.model.discount.DiscountActivity;
import com.wheelys.model.order.MemberAddress;
import com.wheelys.model.order.WheelysOrder;
import com.wheelys.model.user.OpenMember;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.MemberService;
import com.wheelys.service.cafe.CafeKeyService;
import com.wheelys.service.cafe.CafeProductService;
import com.wheelys.service.cafe.CafeShopProfileService;
import com.wheelys.service.cafe.CafeShopService;
import com.wheelys.service.cafe.ShoppingCartService;
import com.wheelys.service.cafe.WpsService;
import com.wheelys.service.order.DiscountService;
import com.wheelys.service.order.MemberAddressService;
import com.wheelys.service.order.OrderService;
import com.wheelys.service.pay.ElecCardService;
import com.wheelys.untrans.pay.AliWapPayService;
import com.wheelys.untrans.pay.WxMpPayService;
import com.wheelys.util.OAuthUtils;
import com.wheelys.util.WebUtils;
import com.wheelys.web.action.AnnotationController;
import com.wheelys.web.action.wap.vo.ElecCardVo;
import com.wheelys.web.action.wap.vo.WheelysOrderVo;

@Controller
public class OrdedrController extends AnnotationController{

	@Autowired@Qualifier("config")
	private Config config;
	@Autowired@Qualifier("memberService")
	private MemberService memberService;
	@Autowired@Qualifier("wpsService")
	private WpsService wpsService;
	@Autowired@Qualifier("orderService")
	private OrderService orderService;
	@Autowired@Qualifier("discountService")
	private DiscountService discountService;
	@Autowired@Qualifier("elecCardService")
	private ElecCardService elecCardService;
	@Autowired@Qualifier("shoppingCartService")
	private ShoppingCartService shoppingCartService;
	@Autowired@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;
	@Autowired@Qualifier("cafeProductService")
	private CafeProductService cafeProductService;
	@Autowired@Qualifier("wxMpPayService")
	private WxMpPayService wxMpPayService;
	@Autowired@Qualifier("aliWapPayService")
	private AliWapPayService aliWapPayService;
	@Autowired@Qualifier("cafeKeyService")
	private CafeKeyService cafeKeyService;
	@Autowired@Qualifier("cafeShopProfileService")
	private CafeShopProfileService cafeShopProfileService;
	@Autowired@Qualifier("memberAddressService")
	private MemberAddressService memberAddressService;
	
	@RequestMapping("/order/step1.xhtml")
	public String step1(ModelMap model, Long shopid, HttpServletRequest request, HttpServletResponse response){
		WheelysMember member = this.getLogonMember();
		String ua = request.getHeader("User-Agent");
		boolean isWx = true;
		if(!ua.contains("MicroMessenger")){
			isWx = false;
		}
		model.put("isWx", isWx);
		elecCardService.unlockElecCard(member.getId());
		String citycode = WebUtils.getAndSetCityCode(request, response);
		String ukey = "";
		String membername = member.getNickname();
		OpenMember openMember = memberService.getOpenMemberByMemberid(member.getId());
		if(openMember != null){
			ukey = openMember.getLoginname();
			membername = openMember.getNickname();
		}
		String memberKey = WebUtils.getCookieValue(request, "memberKey");
		List<CartProduct> cartProductVoList = shoppingCartService.getMemberCart(memberKey);
		ResultCode<OrderContainer> result = orderService.addOrder(cartProductVoList,member.getId(), null, membername, ukey, shopid, null, citycode, false);
		if(result.isSuccess()){
			model.put("discount", result.getRetval().getDiscount());
			model.put("cardVo", result.getRetval().getCard());
			model.put("container", result.getRetval());
			Boolean ifTakeoutSupport = cafeShopProfileService.getTakeawaystatus(shopid);
			Boolean ifReservedstatus = cafeShopProfileService.getReservedstatus(shopid);
			if(ifTakeoutSupport){
				MemberAddress memberAddress = memberAddressService.getMemberAddressByMemidAndShopid(member.getId(), shopid);
				model.put("memberAddress", memberAddress);
				List<String> exprAddrList = cafeShopProfileService.getExpressAddressByShopid(shopid);
				model.put("exprAddrList", exprAddrList);
			}
			model.put("ifReservedstatus", ifReservedstatus);
			model.put("ifTakeoutSupport", ifTakeoutSupport);
			return "/wps/order/confirmOrderComplete.vm";
		}
		return "/wps/erro.vm";
	}
	
	@RequestMapping("/order/couponPage.xhtml")
	public String couponPage(ModelMap model, Integer totalfee, Long shopid, HttpServletRequest request){
		String memberKey = WebUtils.getCookieValue(request, "memberKey");
		List<CartProduct> cartProductVoList = shoppingCartService.getMemberCart(memberKey);
		WheelysMember member = this.getLogonMember();
		List<ElecCardVo> cardList = elecCardService.getElecCardVoList(member.getId(), null, ElecCardConstant.STATUS_SOLD, 0, 150);
		List<ElecCardVo> cardYList = new ArrayList<ElecCardVo>();
		List<ElecCardVo> cardNList = new ArrayList<ElecCardVo>();
		List<CartProduct> productVoList = new ArrayList<CartProduct>();
		productVoList.addAll(cartProductVoList);
		Collections.sort(productVoList, new PropertyComparator("price", false, false));
		for (ElecCardVo cardVo : cardList) {
			String status = "N";
			for (CartProduct cartProduct : productVoList) {
				Integer cardtotalprice = ElecCardUtil.validPrice(shopid, cardVo.getShopid(), cardVo.getProductid(), productVoList);
				if(ElecCardUtil.validCard(shopid, cardVo.getShopid(), cardVo.getProductid(), cartProduct.getProductid(), 
				cardVo.getBegintime(), cardVo.getEndtime(), cardVo.getCardtype(),cardtotalprice, cardVo.getMinprice())){
					status = "Y";
					if(StringUtils.equals(cardVo.getCardtype(), ElecCardConstant.CARDTYPE_EXCHANGE)){
						cardVo.setUseamount(cartProduct.getPrice());
					}else{
						if(StringUtils.equals(cardVo.getAmountmark(), ElecCardConstant.MARK_FIXAMOUNT)){
							cardVo.setUseamount(cartProduct.getPrice() - cardVo.getAmount());
						}else{
							cardVo.setUseamount(cardVo.getAmount());
						}
					}
					break;
				}
			}
			cardVo.setStatus(status);
			if(StringUtils.equals(status, "Y")){
				cardYList.add(cardVo);
				if(StringUtils.isNotBlank(cardVo.getAnnexation())){
					List<Long> idList = BeanUtil.getIdList(cardVo.getAnnexation(), ",");
					if(!idList.isEmpty()){
						Long id = idList.get(0);
						CafeProduct product = this.cafeProductService.getCacheProduct(id);
						if(product != null)cardVo.setDesc("(另送"+product.getName()+")");
					}
				}
				continue;
			}
			cardNList.add(cardVo);
		}
		model.put("cardYList", cardYList);
		model.put("cardNList", cardNList);
		model.put("member", member);
		model.put("totalfee", totalfee);
		return "/wps/modul/couponPage.vm";
	}
	
	@RequestMapping("/order/confirm.xhtml")
	public String memberAddOrder(ModelMap model, Long shopid, Long cardid, String category, Integer reservedtime, Long addressid
			,HttpServletRequest request, HttpServletResponse response){
		String ua = request.getHeader("User-Agent");
		boolean isWx = true;
		if(!ua.contains("MicroMessenger")){
			isWx = false;
		}
		WheelysMember member = this.getLogonMember();
		String clientIp = WebUtils.getRemoteIp(request);
		String citycode = WebUtils.getAndSetCityCode(request, response);
		String ukey = "";
		String membername = member.getNickname();
		OpenMember openMember = memberService.getOpenMemberByMemberid(member.getId());
		if(openMember != null){
			ukey = openMember.getLoginname();
			membername = openMember.getNickname();
		}
		String memberKey = WebUtils.getCookieValue(request, "memberKey");
		List<CartProduct> cartProductVoList = shoppingCartService.getMemberCart(memberKey);
		ResultCode<OrderContainer> result = orderService.addOrder(cartProductVoList,member.getId(), member.getMobile(), membername, ukey, shopid, cardid, citycode, true);
		if(result.isSuccess()){
			shoppingCartService.reset(memberKey);
			WheelysOrder order = result.getRetval().getOrder();
			orderService.saveOrderCategory(order.getTradeno(), member.getId(), addressid, category, reservedtime);
			HashMap data = new HashMap();
			data.put("paystatus", order.getPaystatus());
			data.put("tradeno", order.getTradeno());
			if (StringUtils.equals(order.getPaystatus(), PayConstant.PAYSTATUS_PAID)) {
				return this.showJsonSuccess(model, data);
			}
			order.setValidtime(DateUtil.addDay(order.getValidtime(), 1));
			if(isWx){
				Map<String, String> payparams = wxMpPayService.getJsapiPayJsonParams(order , clientIp);
				data.put("payparams", payparams);
			}else{
				String wapPayUrl = aliWapPayService.getWapPayUrl(order, clientIp);
				data.put("wapPayUrl", wapPayUrl);
			}
			return this.showJsonSuccess(model, data);
		}
		return this.showJsonError(model, result.getMsg());
	}

	@RequestMapping("/shop/getOrderList.xhtml")
	public String orderList(String status, ModelMap model, HttpServletRequest request){		
		WheelysMember member = this.getLogonMember();
		if(member == null){
			String lastpage = config.getAbsPath()+"order/getOrderList.xhtml?status="+status;
			String oauthUrl = OAuthUtils.oauth2Url(request, lastpage, config.getAbsPath());
			return showRedirect(oauthUrl, model);
		}
		List<WheelysOrderVo> orderVoList = orderService.getOrderList(status,member.getId(), 0, 20);
		model.put("orderVoList", orderVoList);
		OpenMember openmember = memberService.getOpenMemberByMemberid(member.getId());
		model.put("member", member);
		if(openmember != null){
			model.put("member", openmember);
		}
		return "/wps/order/orderList.vm";
	}

	@RequestMapping("/order/getOrderList.xhtml")
	public String getOrderList(String status, ModelMap model){
		WheelysMember member = this.getLogonMember();
		List<WheelysOrderVo> orderVoList = orderService.getOrderList(status,member.getId(), 0, 20);
		model.put("orderVoList", orderVoList);
		OpenMember openmember = memberService.getOpenMemberByMemberid(member.getId());
		model.put("member", member);
		if(openmember != null){
			model.put("member", openmember);
		}
		return "/wps/order/orderList.vm";
	}

	@RequestMapping("/order/orderDetail.xhtml")
	public String orderDetail(String tradeno, ModelMap model){
		WheelysMember member = this.getLogonMember();
		ResultCode<WheelysOrderVo> orderVo = orderService.getWheelysOrderVo(tradeno, member.getId());
		if(orderVo.isSuccess()){
			WheelysOrderVo order = orderVo.getRetval();
			if(StringUtils.equals(order.getCategory(), CafeOrderConstant.CATEGORY_TAKEAWAY)){
				CafeShopProfile shopProfile = cafeShopProfileService.getShopProfile(order.getShopid());
				MemberAddress memberAddress = memberAddressService.getMemberAddressByMemidAndShopid(order.getMemberid(), order.getShopid());
				model.put("shopProfile", shopProfile);
				model.put("memberAddress", memberAddress);
			}
			if(order.getSdid() != null && order.getSdid() > 0){
				DiscountActivity discount = discountService.getDiscount(order.getSdid());
				model.put("discount", discount);
			}
			model.put("orderVo", orderVo.getRetval());
		}
		return "/wps/order/orderDetail.vm";
	}
	
	@RequestMapping("/order/payresult.xhtml")
	public String payresult(ModelMap model, String tradeno){
		WheelysMember member = this.getLogonMember();
		ResultCode<WheelysOrderVo> result = orderService.getWheelysOrderVo(tradeno,member.getId());		
		if(result.isSuccess()){
			Long keyid = result.getRetval().getKeyid();
			CafeKey cafeKey = cafeKeyService.getCafeKeyById(keyid);
			model.put("cafeKey", cafeKey);
			model.put("orderVo", result.getRetval());
		}
		return "/wps/order/payresult.vm";
	}
	
	@RequestMapping("/order/editMemberAddress.xhtml")
	public String editMemberAddress(ModelMap model, Long shopid, String username, String mobile, String detailaddress, String address){
		WheelysMember member = this.getLogonMember();
		detailaddress = StringUtils.trim(detailaddress);
		username = StringUtils.trim(username);
		MemberAddress memberAddress = memberAddressService.saveOrUpdateMemAddr(member.getId(), shopid,username,mobile,detailaddress,address);
		List<String> exprAddrList = cafeShopProfileService.getExpressAddressByShopid(shopid);
		model.put("exprAddrList", exprAddrList);
		model.put("memberAddress", memberAddress);
		return "/wps/modul/forOrderAddress.vm";
	}
	
	@RequestMapping("/order/setOrderCategory.xhtml")
	public String setCategoryReserved(ModelMap model, String category, Integer reservedtime, String tradeno, Long addressid){
		WheelysMember member = this.getLogonMember();
		orderService.saveOrderCategory(tradeno, member.getId(), addressid, category, reservedtime);
		return this.showJsonSuccess(model, null);
	}
	
}

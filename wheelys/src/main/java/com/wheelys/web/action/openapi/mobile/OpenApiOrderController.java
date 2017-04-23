package com.wheelys.web.action.openapi.mobile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.wheelys.util.JsonUtils;
import com.wheelys.constant.ElecCardConstant;
import com.wheelys.helper.ElecCardUtil;
import com.wheelys.helper.OrderContainer;
import com.wheelys.model.CafeProduct;
import com.wheelys.model.CartProduct;
import com.wheelys.model.order.MemberAddress;
import com.wheelys.model.order.WheelysOrder;
import com.wheelys.model.user.OpenMember;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.MemberService;
import com.wheelys.service.cafe.CafeKeyService;
import com.wheelys.service.cafe.CafeProductService;
import com.wheelys.service.cafe.CafeShopProfileService;
import com.wheelys.service.cafe.WpsService;
import com.wheelys.service.order.DiscountService;
import com.wheelys.service.order.MemberAddressService;
import com.wheelys.service.order.OrderService;
import com.wheelys.service.pay.ElecCardService;
import com.wheelys.untrans.pay.AliWapPayService;
import com.wheelys.untrans.pay.WxMpPayService;
import com.wheelys.web.action.openapi.OpenApiBaseController;
import com.wheelys.web.action.wap.vo.ElecCardVo;

@Controller
@RequestMapping("/openapi/mobile")
public class OpenApiOrderController extends OpenApiBaseController {
	
	@Autowired@Qualifier("config")
	private Config config;
	@Autowired@Qualifier("wpsService")
	private WpsService wpsService;
	@Autowired@Qualifier("memberService")
	private MemberService memberService;
	@Autowired@Qualifier("elecCardService")
	private ElecCardService elecCardService;
	@Autowired@Qualifier("cafeProductService")
	private CafeProductService cafeProductService;
	@Autowired@Qualifier("wxMpPayService")
	private WxMpPayService wxMpPayService;
	@Autowired@Qualifier("aliWapPayService")
	private AliWapPayService aliWapPayService;
	@Autowired@Qualifier("orderService")
	private OrderService orderService;
	@Autowired@Qualifier("discountService")
	private DiscountService discountService;
	@Autowired@Qualifier("cafeKeyService")
	private CafeKeyService cafeKeyService;
	@Autowired@Qualifier("cafeShopProfileService")
	private CafeShopProfileService cafeShopProfileService;
	@Autowired@Qualifier("memberAddressService")
	private MemberAddressService memberAddressService;

	@RequestMapping("/order/step1.xhtml")
	public String step1(Long shopid, String mobile, String productListJson, ModelMap model){
		WheelysMember member = this.getLogonMember(true);
		elecCardService.unlockElecCard(member.getId());
		List<CartProduct> cartProductVoList = this.getCartProductList(productListJson);
		ResultCode<OrderContainer> result = orderService.addAppOrder(cartProductVoList,member.getId(), mobile, null, null, shopid, null, null, false);
		if(result.isSuccess()){
			OrderContainer container = result.getRetval();
			Boolean ifTakeoutSupport = cafeShopProfileService.getTakeawaystatus(shopid);
			Boolean ifReservedstatus = cafeShopProfileService.getReservedstatus(shopid);
			if(ifTakeoutSupport){
				MemberAddress memberAddress = memberAddressService.getMemberAddressByMemidAndShopid(member.getId(), shopid);
				List<String> exprAddrList = cafeShopProfileService.getExpressAddressByShopid(shopid);
				container.setMemberAddress(memberAddress);
				container.setExprAddrList(exprAddrList);
			}
			container.setIfTakeoutSupport(ifTakeoutSupport);
			container.setIfReservedstatus(ifReservedstatus);
			return this.successJsonResult(model, container);
		}
		return this.errorJsonResult(model, result.getMsg());
	}
	
	private List<CartProduct> getCartProductList(String productListJson){
		List<CartProduct> cartProductList = JsonUtils.readJsonToObjectList(CartProduct.class, productListJson);
		for (CartProduct cartProduct : cartProductList) {
			CafeProduct product = cafeProductService.getCacheProduct(cartProduct.getProductid());
			cartProduct.setProduct(product);
			int bean = StringUtils.equals(cartProduct.getMap().get("bean"), "y") ? 3 : 0;
			cartProduct.setPrice(product.getPrice() + bean);
			cartProduct.setTotalfee(cartProduct.getQuantity() * cartProduct.getPrice());
		}
		return cartProductList;
	}
	
	@RequestMapping("/order/getCardList.xhtml")
	public String couponPage(Long shopid, String productListJson, ModelMap model){
		List<CartProduct> cartProductVoList = JsonUtils.readJsonToObjectList(CartProduct.class, productListJson);
		WheelysMember member = this.getLogonMember(true);
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
					cardVo.setUseamount(cartProduct.getPrice());
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
		Map map = new HashMap<>();
		map.put("cardYList", cardYList);
		map.put("cardNList", cardNList);
		map.put("member", member);
		return this.successJsonResult(model, map);
	}
	
	@RequestMapping("/order/confirm.xhtml")
	public String memberAddOrder(String citycode, String productListJson, String paymethod,
			Long shopid, Long cardid, String category, Integer reservedtime, Long addressid, ModelMap model){
		WheelysMember member = this.getLogonMember(true);
		String ukey = "";
		String membername = member.getNickname();
		OpenMember openMember = memberService.getOpenMemberByMemberid(member.getId());
		if(openMember != null){
			ukey = openMember.getLoginname();
			membername = openMember.getNickname();
		}
		List<CartProduct> cartProductVoList = JsonUtils.readJsonToObjectList(CartProduct.class, productListJson);
		ResultCode<OrderContainer> result = orderService.addOrder(cartProductVoList,member.getId(), member.getMobile(), membername, ukey, shopid, cardid, citycode, true);
		if(result.isSuccess()){
			WheelysOrder order = result.getRetval().getOrder();
			orderService.saveOrderCategory(order.getTradeno(), member.getId(), addressid, category, reservedtime);
			if(StringUtils.equals(paymethod, "")){
				
			}
		}
		return this.errorJsonResult(model, result.getMsg());
	}
	
}

package com.wheelys.web.action.openapi.pos;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.Config;
import com.wheelys.api.vo.ResultCode;
import com.wheelys.util.DateUtil;
import com.wheelys.util.JsonUtils;
import com.wheelys.util.StringUtil;
import com.wheelys.constant.CityConstant;
import com.wheelys.helper.OrderContainer;
import com.wheelys.model.CafeItem;
import com.wheelys.model.CafeProduct;
import com.wheelys.model.CafeShop;
import com.wheelys.model.ShopSeller;
import com.wheelys.service.cafe.CafeShopService;
import com.wheelys.service.cafe.WpsService;
import com.wheelys.service.pos.PosService;
import com.wheelys.util.WebUtils;
import com.wheelys.web.action.openapi.OpenApiBaseController;
import com.wheelys.web.action.openapi.vo.OpneApiCafeProductVo;
import com.wheelys.web.action.openapi.vo.PosMchUser;
import com.wheelys.web.action.openapi.vo.PosOrderDetailVo;
import com.wheelys.web.action.openapi.vo.PosOrderVo;
import com.wheelys.web.action.openapi.vo.ShopProductVo;

@Controller
@RequestMapping("/opneapi/pos")
public class OpenApiPosController extends OpenApiBaseController {

	@Autowired@Qualifier("config")
	private Config config;
	@Autowired@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;
	@Autowired@Qualifier("wpsService")
	private WpsService wpsService;
	@Autowired@Qualifier("posService")
	private PosService posService;

	@RequestMapping("/shop/getProductList.xhtml")
	public String getProductList(HttpServletRequest request, Long shopid, ModelMap model) {
		dbLogger.warn(JsonUtils.writeMapToJson(WebUtils.getRequestMap(request)));
		Map<CafeItem, List<CafeProduct>> cafeProductMap = wpsService.getShopCafeProductMap(shopid);
		List<ShopProductVo> voList = new ArrayList<ShopProductVo>();
		for (CafeItem item : cafeProductMap.keySet()) {
			List<OpneApiCafeProductVo> productVoList = new ArrayList<OpneApiCafeProductVo>();
			List<CafeProduct> productList = cafeProductMap.get(item);
			for (CafeProduct cafeProduct : productList) {
				OpneApiCafeProductVo productVo = new OpneApiCafeProductVo(cafeProduct, config.getString("picPath"));
				productVoList.add(productVo);
			}
			ShopProductVo vo = new ShopProductVo();
			vo.setItem(item);
			vo.setProductList(productVoList);
			voList.add(vo);
		}
		return this.successJsonResult(model, voList);
	}

	@RequestMapping("/shop/orderPay.xhtml")
	public String orderPay(HttpServletRequest request, Long shopid, String tradeno, String payseqno, ModelMap model) {
		dbLogger.warn(JsonUtils.writeMapToJson(WebUtils.getRequestMap(request)));
		ResultCode result = posService.posOrderPay(shopid,tradeno,payseqno);
		return this.successJsonResult(model, result.getRetval());
	}

	@RequestMapping("/shop/addOrder.xhtml")
	public String addOrder(HttpServletRequest request, Long shopid, String citycode, String orderDtailJson, ModelMap model) {
		dbLogger.warn(JsonUtils.writeMapToJson(WebUtils.getRequestMap(request)));
		String clientIp = WebUtils.getRemoteIp(request);
		List<PosOrderDetailVo> detailList = JsonUtils.readJsonToObjectList(PosOrderDetailVo.class, orderDtailJson);
		ResultCode<OrderContainer> result = posService.addPosOrder(shopid, citycode, detailList, clientIp);
		if(result.isSuccess()){
			return this.successJsonResult(model, result.getRetval());
		}else{
			return this.errorJsonResult(model, result.getMsg());
		}
	}

	@RequestMapping("/shop/changeOrderStatus.xhtml")
	public String changeOrderStatus(HttpServletRequest request, Long shopid, String status, String tradenolist, ModelMap model) {
		dbLogger.warn(JsonUtils.writeMapToJson(WebUtils.getRequestMap(request)));
		ResultCode result = posService.changeOrderStatus(shopid,status,tradenolist);
		return this.successJsonResult(model, result.getRetval());
	}

	@RequestMapping("/shop/changeShopStatus.xhtml")
	public String changeShopStatus(HttpServletRequest request, Long shopid, String status, ModelMap model) {
		dbLogger.warn(JsonUtils.writeMapToJson(WebUtils.getRequestMap(request)));
		ResultCode result = posService.changeShopStatus(shopid,status);
		return this.successJsonResult(model, result.getRetval());
	}

	@RequestMapping("/shop/getOrderList.xhtml")
	public String getOrderList(Long shopid, String status,Timestamp fromtime, ModelMap model) {
		List<PosOrderVo> orderList = posService.getPosOrderList(shopid,status,fromtime);
		return this.successJsonResult(model, orderList);
	}

	@RequestMapping("/shop/getHistortyOrderList.xhtml")
	public String getHistortyOrderList(HttpServletRequest request, Integer pageno,
			Long shopid, Timestamp fromtime,Timestamp totime, String tradeno, ModelMap model) {
		dbLogger.warn(JsonUtils.writeMapToJson(WebUtils.getRequestMap(request)));
		List<PosOrderVo> orderList = posService.getHistortyOrderList(shopid,fromtime,totime,tradeno,pageno);
		return this.successJsonResult(model, orderList);
	}

	@RequestMapping("/time.xhtml")
	public String login(ModelMap model){
		return this.successJsonResult(model, DateUtil.getMillTimestamp());
	}

	@RequestMapping("/login.xhtml")
	public String login(HttpServletRequest request, String mchid, String username, String password, ModelMap model) {
		dbLogger.warn(JsonUtils.writeMapToJson(WebUtils.getRequestMap(request)));
		CafeShop shop = cafeShopService.getShopByMchid(mchid);
		if(shop == null){
			return this.errorJsonResult(model, "商户不存在");
		}
		ResultCode<ShopSeller> result = posService.shopUserLogin(shop.getShopid(), username, password);
		if(!result.isSuccess()){
			return this.errorJsonResult(model, result.getMsg());
		}
		ShopSeller seller = result.getRetval();
		PosMchUser mchUser = new PosMchUser(StringUtil.md5(seller.getLoginname()+seller.getUserpwd()),shop.getShopid(),CityConstant.CITYCODE_SH);
		mchUser.setAddress(shop.getShopaddress());
		mchUser.setShopname(shop.getShopname());
		mchUser.setUsername(seller.getUsername());
		mchUser.setBooking(shop.getBooking());
		mchUser.setShopimg(config.getString("picPath")+shop.getShopimg());
		return this.successJsonResult(model, mchUser);
	}
	
}

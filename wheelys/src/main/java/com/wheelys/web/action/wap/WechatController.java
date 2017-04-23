package com.wheelys.web.action.wap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wheelys.Config;
import com.wheelys.constant.BannerConstant;
import com.wheelys.constant.CityConstant;
import com.wheelys.model.CafeItem;
import com.wheelys.model.CafeKey;
import com.wheelys.model.CafeProduct;
import com.wheelys.model.CafeShop;
import com.wheelys.model.CartProduct;
import com.wheelys.model.news.WheelysBanner;
import com.wheelys.model.order.WheelysOrder;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.MemberService;
import com.wheelys.service.cafe.CafeKeyService;
import com.wheelys.service.cafe.CafeShopService;
import com.wheelys.service.cafe.ShoppingCartService;
import com.wheelys.service.cafe.WpsService;
import com.wheelys.service.news.NewsService;
import com.wheelys.service.order.OrderService;
import com.wheelys.util.OAuthUtils;
import com.wheelys.util.VmUtils;
import com.wheelys.util.WebUtils;
import com.wheelys.web.action.AnnotationController;

@Controller
public class WechatController extends AnnotationController{

	@Autowired@Qualifier("config")
	private Config config;
	@Autowired@Qualifier("memberService")
	private MemberService memberService;
	@Autowired@Qualifier("newsService")
	private NewsService newsService;
	@Autowired@Qualifier("wpsService")
	private WpsService wpsService;
	@Autowired@Qualifier("orderService")
	private OrderService orderService;
	@Autowired@Qualifier("shoppingCartService")
	private ShoppingCartService shoppingCartService;
	@Autowired@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;
	@Autowired@Qualifier("cafeKeyService")
	private CafeKeyService cafeKeyService;
	
	@RequestMapping("/index.xhtml")
	public String wpsindex(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		return index(request, response, model);
	}
	
	@RequestMapping("/shop/index.xhtml")
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		List<WheelysBanner> bannerList = newsService.getBannerList(BannerConstant.TYPE_WECHAT_INDEX);
		model.put("bannerList", bannerList);
		String memberKey = WebUtils.getShopingCartMemberKey(request, response);
		shoppingCartService.reset(memberKey);
		return "/wps/index.vm";
	}

	private List<Long> memberidList = Arrays.asList(2L,1344L);
	
	@RequestMapping("/shop/queryList.xhtml")
	public String shopList(String lat, String lng, HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String citycode = WebUtils.getAndSetCityCode(request, response);
		List<CafeShop> shopList = wpsService.queryNearShopList(citycode, lat, lng);
		model.put("citymap", CityConstant.CITYMAP);
		WheelysMember member = this.getLogonMember();
		if(member != null){
			if(memberidList.contains(member.getId())){
				CafeShop shop = cafeShopService.getCacheShop(6L);
				shopList.add(shop);
			}
		}
		model.put("shopList", shopList);
		return "/wps/modul/shopList.vm";
	}
	
	@RequestMapping("/shop/shopDetail.xhtml")
	public String shopDetail(Long shopid, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		String memberKey = WebUtils.getShopingCartMemberKey(request, response);
		shoppingCartService.reset(memberKey);
		WheelysMember member = this.getLogonMember();
		response.setHeader("Cache-Control", "no-store");
		if(member == null){
			String lastpage = config.getAbsPath()+"shop/shopDetail.xhtml?shopid="+shopid+"&t="+System.currentTimeMillis();
			String oauthUrl = OAuthUtils.oauth2Url(request, lastpage, config.getAbsPath());
			return showRedirect(oauthUrl, model);
		}
		CafeShop shop = cafeShopService.getShop(shopid);
		model.put("shop", shop);
		Map<CafeItem, List<CafeProduct>> cafeProductMap = wpsService.getShopCafeProductMap(shopid);
		model.put("cafeProductMap", cafeProductMap);
		Map cartMap = shoppingCartService.getMemberCartMap("memberKey", null);
		model.put("cartMap", cartMap);
		List<WheelysBanner> bannerList = newsService.getBannerListByShop(shopid);
		model.put("bannerList", bannerList);
		return "/wps/shopDetail.vm";
	}

	@RequestMapping("/shop/getShoppingCart.xhtml")
	public String getShoppingCart(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String memberKey = WebUtils.getShopingCartMemberKey(request, response);
		List<CartProduct> cartProductVoList = shoppingCartService.getMemberCart(memberKey);
		model.put("cartProductVoList", cartProductVoList);
		return "/wps/modul/buyCart.vm";
	}

	@RequestMapping("/shop/shoppingCart.xhtml")
	public String shoppingCart(Long productid, String otherinfo, String key, ModelMap model, 
			String opttype, HttpServletRequest request, HttpServletResponse response, Integer quantity){
		String memberKey = WebUtils.getShopingCartMemberKey(request, response);
		CartProduct productVo = new CartProduct(memberKey, productid, quantity, otherinfo);
		productVo.setPkey(key);
		if(StringUtils.equals("reset", opttype)){
			shoppingCartService.reset(memberKey);
		}else if(StringUtils.equals("remove", opttype)){
			shoppingCartService.remove(memberKey, productVo);
		}else{
			if(quantity > 0){
				shoppingCartService.addProductToCart(memberKey, productVo);
			}else if(quantity < 0){
				shoppingCartService.removeProductToCart(memberKey, productVo, quantity);
			}
		}
		Map cartMap = shoppingCartService.getMemberCartMap(memberKey, productVo.getPkey());
		model.put("cartMap", cartMap);
		return this.showJsonSuccess(model, cartMap);
	}
	
	@RequestMapping("/shop/search.xhtml")
	public String search(ModelMap model, HttpServletRequest request){
		WheelysMember member = this.getLogonMember();
		if(member == null){
			String lastpage = config.getAbsPath()+"shop/search.xhtml?t="+System.currentTimeMillis();
			String oauthUrl = OAuthUtils.oauth2Url(request, lastpage, config.getAbsPath());
			return showRedirect(oauthUrl, model);
		}
		WheelysOrder order = orderService.getLastPaidOrder(member.getId());
		model.put("order", order);
		model.put("citymap", CityConstant.OPENCITYMAP);
		return "/wps/search.vm";
	}

	@RequestMapping("/setJsVersion.xhtml")
	@ResponseBody
	public String setJsVersion(String version,ModelMap model){
		VmUtils.setJsVersion(version);
		VmUtils.setVersion(version);
		return this.showJsonSuccess(model, VmUtils.getVersion());
	}

	@RequestMapping("/cafeKey.xhtml")
	public String testlog(ModelMap model){
		CafeKey key = cafeKeyService.getCafeKeyByShopid(1090L);
		dbLogger.warn("wx mpno, notify content: " + key.getName()+","+key.getImgurl());
		return this.showJsonSuccess(model, key);
	}
	
}

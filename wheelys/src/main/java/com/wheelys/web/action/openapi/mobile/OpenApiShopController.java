package com.wheelys.web.action.openapi.mobile;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.constant.BannerConstant;
import com.wheelys.constant.CityConstant;
import com.wheelys.model.CafeShop;
import com.wheelys.model.news.WheelysBanner;
import com.wheelys.service.cafe.CafeShopService;
import com.wheelys.service.cafe.WpsService;
import com.wheelys.service.news.NewsService;
import com.wheelys.web.action.openapi.OpenApiBaseController;
import com.wheelys.web.action.openapi.mobile.vo.OpenApiCafeItemVo;

@Controller
@RequestMapping("/openapi/mobile")
public class OpenApiShopController extends OpenApiBaseController {
	
	@Autowired@Qualifier("wpsService")
	private WpsService wpsService;
	@Autowired@Qualifier("newsService")
	private NewsService newsService;
	@Autowired@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;

	@RequestMapping("/index.xhtml")
	public String index(ModelMap model) {
		return this.successJsonResult(model, null);
	}
	
	@RequestMapping("/shop/getList.xhtml")
	public String getShopList(String lat, String lng, String citycode, ModelMap model) {
		List<CafeShop> shopList = wpsService.queryNearShopList(citycode, lat, lng);
		Map data = new LinkedHashMap();
		data.put("shopList", shopList);
		data.put("citymap", CityConstant.CITYMAP);
		return this.successJsonResult(model, data);
	}
	
	@RequestMapping("/shop/detail.xhtml")
	public String detail(Long shopid, ModelMap model) {
		CafeShop shop = cafeShopService.getShop(shopid);
		List<OpenApiCafeItemVo> itemVoList = wpsService.getShopCafeItemVoList(shopid);
		Map data = new LinkedHashMap();
		data.put("shop", shop);
		data.put("itemVoList", itemVoList);
		return this.successJsonResult(model, data);
	}
	
	@RequestMapping("/banner/getList.xhtml")
	public String getBannerList(Long shopid, String type, ModelMap model) {
		List<WheelysBanner> bannerList = null;
		if(shopid != null){
			bannerList = newsService.getBannerListByShop(shopid);
		}else if(BannerConstant.BANNERTYPE.contains(type)){
			bannerList = newsService.getBannerList(type);
		}
		return this.successJsonResult(model, bannerList);
	}
	
}

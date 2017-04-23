package com.wheelys.web.action.openapi.mobile;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.Config;
import com.wheelys.util.BeanUtil;
import com.wheelys.constant.BannerConstant;
import com.wheelys.model.news.WheelysBanner;
import com.wheelys.model.news.WheelysNews;
import com.wheelys.service.news.NewsService;
import com.wheelys.web.action.openapi.OpenApiBaseController;

@Controller
@RequestMapping("/openapi/mobile")
public class OpenApiDiscoveryController extends OpenApiBaseController{
	
	@Autowired@Qualifier("newsService")
	private NewsService newsService;
	@Autowired@Qualifier("config")
	private Config config;
	
	@RequestMapping("/discovery/bannerList.xhtml")
	public String bannerList(ModelMap model) {
		List<WheelysBanner> bannerList = newsService.getBannerList(BannerConstant.TYPE_NEWS_INDEX);
		List<Map> result = BeanUtil.getBeanMapList(bannerList, "tourl", "imageurl", "title", "id");
		for (Map map : result) {
			map.put("imageurl", config.getString("mobilePicPath") + map.get("imageurl"));
		}
		return successJsonResult(model, result);
	}
	
	@RequestMapping("/discovery/newsList.xhtml")
	public String newsList(String category, Integer pageNo, Integer maxnum, ModelMap model) {
		if(maxnum == null) maxnum = 6;
		if(pageNo == null) pageNo = 0;
		List<WheelysNews> newsList = newsService.getNewsList(category, pageNo, maxnum);
		List<Map> result = BeanUtil.getBeanMapList(newsList, "publishtime", "id", "title", "newspicture", "overview");
		for (Map map : result) {
			map.put("newspicture", config.getString("mobilePicPath") + map.get("newspicture"));
			map.put("tourl", config.getAbsPath() + "news/newsDetail.xhtml?id=" + map.get("id"));
		}
		return successJsonResult(model, result);
	}
}

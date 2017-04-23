package com.wheelys.web.action.wap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.untrans.CacheService;
import com.wheelys.web.util.PageUtil;
import com.wheelys.constant.BannerConstant;
import com.wheelys.constant.CacheConstant;
import com.wheelys.constant.CacheKey;
import com.wheelys.model.news.WheelysBanner;
import com.wheelys.model.news.WheelysNews;
import com.wheelys.service.news.NewsService;
import com.wheelys.untrans.WheelysRedisService;
import com.wheelys.web.action.AnnotationController;

@Controller
public class NewsController extends AnnotationController {

	@Autowired
	@Qualifier("newsService")
	private NewsService newsService;
	@Autowired
	@Qualifier("cacheService")
	private CacheService cacheService;
	@Autowired
	@Qualifier("WheelysRedisService")
	private WheelysRedisService WheelysRedisService;

	@RequestMapping("/news/newsList.xhtml")
	public String newsList(String category, Integer pageNo, ModelMap model) {
		int rowsPerPage = 10;
		if (pageNo == null)
			pageNo = 0;
		List<WheelysNews> newsList = newsService.getNewsList(category, pageNo, rowsPerPage);
		model.put("newsList", newsList);
		int count = newsService.getNewsListCount(category);
		PageUtil pageUtil = new PageUtil(count, rowsPerPage, pageNo, "news/newsList.xhtml");
		Map params = new HashMap();
		params.put("category", category);
		params.put("pageNo", pageNo);
		pageUtil.initPageInfo(params);
		model.put("pageUtil", pageUtil);
		List<WheelysBanner> bannerList = newsService.getBannerList(BannerConstant.TYPE_NEWS_INDEX);
		model.put("bannerList", bannerList);
		return "/wps/news/newsList.vm";
	}

	@RequestMapping("/news/newsDetail.xhtml")
	public String newsDetail(Long id, ModelMap model) {
		WheelysNews news = newsService.getNewsDetail(id);
		model.put("news", news);
		String content = newsService.getNewscontent(id);
		model.put("content", content);
		WheelysRedisService.saveInvokeQuantity(CacheConstant.REGION_TWENTYMIN, id, CacheKey.NEWS_READING_IDS,
				CacheKey.NEWS_READING_QUANTITY);
		return "/wps/news/newsDetail.vm";
	}

}

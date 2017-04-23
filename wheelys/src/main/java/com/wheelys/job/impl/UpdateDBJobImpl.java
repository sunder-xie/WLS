package com.wheelys.job.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.wheelys.job.JobService;
import com.wheelys.untrans.CacheService;
import com.wheelys.constant.CacheConstant;
import com.wheelys.constant.CacheKey;
import com.wheelys.job.UpdateDBJob;
import com.wheelys.model.news.WheelysNews;
import com.wheelys.service.news.NewsService;

public class UpdateDBJobImpl extends JobService implements UpdateDBJob {

	@Autowired
	@Qualifier("cacheService")
	private CacheService cacheService;
	@Autowired
	@Qualifier("newsService")
	private NewsService newsService;

	@Override
	public void updateNewsReadQuantity() {
		Object ids = cacheService.get(CacheConstant.REGION_TWENTYMIN, CacheKey.NEWS_READING_IDS);
		if (ids != null) {
			String[] idArray = ids.toString().split(",");
			for (String id : idArray) {
				long newId = Long.parseLong(id);
				Integer quantity = (Integer) cacheService.get(CacheConstant.REGION_TWENTYMIN,
						CacheKey.NEWS_READING_QUANTITY + id);
				if (quantity != null) {
					WheelysNews wheelysNews = newsService.getNewsDetail(newId);
					if (wheelysNews != null) {
						wheelysNews.setPageview(wheelysNews.getPageview() + quantity);
						newsService.updateWheelysNews(wheelysNews);
						dbLogger.warn("updateNewsReadQuantity,id:" + newId);
						cacheService.remove(CacheConstant.REGION_TWENTYMIN, CacheKey.NEWS_READING_QUANTITY + newId);
					}
				}
			}
			cacheService.remove(CacheConstant.REGION_TWENTYMIN, CacheKey.NEWS_READING_IDS);
		}
	}
}

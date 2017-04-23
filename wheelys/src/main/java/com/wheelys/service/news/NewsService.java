package com.wheelys.service.news;

import java.util.List;

import com.wheelys.model.news.WheelysBanner;
import com.wheelys.model.news.WheelysNews;

public interface NewsService {

	WheelysNews getNewsDetail(Long id);

	String getNewscontent(Long id);

	List<WheelysNews> getNewsList(String category, Integer pageNo, Integer maxnum);

	int getNewsListCount(String category);

	List<WheelysBanner> getBannerList(String type);

	List<WheelysBanner> getBannerListByShop(Long shopid);

	List<WheelysNews> findAllNews();

	void updateWheelysNews(WheelysNews wheelysNews);
}

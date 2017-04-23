package com.wheelys.service.admin;

import java.sql.Timestamp;
import java.util.List;

import com.wheelys.model.news.WheelysNews;

public interface NewsService {

	List<WheelysNews> getLatestNewsList();

	void updatePublishstatus(Long id);

	void deleteNews(Long id);

	WheelysNews getNewsById(Long id);

	void saveNews(String title, String newsimg, String writer, String overview, String content, String category, Timestamp appeartime);

	void updateNews(Long id, String newsimg, String title, String writer, String overview, String content, String category, Timestamp appeartime);

	List<WheelysNews> searchNewsList(String title, Timestamp fromdate, Timestamp todate, String category);

	int getNewsListCount(String title, Timestamp fromdate, Timestamp todate, String category);	

}

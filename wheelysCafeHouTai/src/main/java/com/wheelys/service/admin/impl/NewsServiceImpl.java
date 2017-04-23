package com.wheelys.service.admin.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.DateUtil;
import com.wheelys.model.news.WheelysNews;
import com.wheelys.model.news.WheelysNewsContent;
import com.wheelys.service.admin.NewsService;
import com.wheelys.util.VmUtils;

@Service("newsService")
public class NewsServiceImpl extends BaseServiceImpl implements NewsService {

	@Override
	public List<WheelysNews> getLatestNewsList() {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysNews.class);
		query.add(Restrictions.eq("delstatus", "N"));
		query.addOrder(Order.desc("createtime"));
		List<WheelysNews> newsList = baseDao.findByCriteria(query);
		return newsList;
	}

	@Override
	public void updatePublishstatus(Long id) {
		WheelysNews wheelysNews = baseDao.getObject(WheelysNews.class, id);
		if(StringUtils.equals(wheelysNews.getPublishstatus(), "Y")){
			wheelysNews.setPublishstatus("N");
			wheelysNews.setUpdatetime(DateUtil.getMillTimestamp());
		}else{
			wheelysNews.setPublishstatus("Y");
			wheelysNews.setUpdatetime(DateUtil.getMillTimestamp());
		}
		baseDao.saveObject(wheelysNews);
	}

	@Override
	public void deleteNews(Long id) {
		WheelysNews wheelysNews = baseDao.getObject(WheelysNews.class, id);
		wheelysNews.setDelstatus("Y");
		wheelysNews.setUpdatetime(DateUtil.getMillTimestamp());
		baseDao.saveObject(wheelysNews);
	}

	@Override
	public WheelysNews getNewsById(Long id) {
		WheelysNews news = baseDao.getObject(WheelysNews.class, id);
		return news;
	}

	@Override
	public void saveNews(String title, String newsimg, String writer, String overview, String content, String category,
			Timestamp publishtime) {
		title = StringUtils.trim(title);
		writer = StringUtils.trim(writer);
		category = StringUtils.trim(category);
		WheelysNews news = new WheelysNews(title, writer, overview, category, publishtime);
		news.setNewspicture(newsimg);
		news.setPublishtime(publishtime);
		baseDao.saveObject(news);
		WheelysNewsContent newsContent = new WheelysNewsContent(news);
		newsContent.setNewscontent(content);
		baseDao.saveObject(newsContent);
	}

	@Override
	public void updateNews(Long id, String newsimg, String title, String writer, String overview, String content, String category,
			Timestamp publishtime) {
		title = StringUtils.trim(title);
		writer = StringUtils.trim(writer);
		category = StringUtils.trim(category);
		WheelysNews news = baseDao.getObject(WheelysNews.class, id);
		news.setTitle(title);
		news.setWriter(writer);
		news.setOverview(overview);
		news.setCategory(category);
		if(StringUtils.isNotBlank(newsimg)){
			news.setNewspicture(newsimg);
		}
		news.setPublishtime(publishtime);
		news.setUpdatetime(DateUtil.getMillTimestamp());
		baseDao.saveObject(news);
		WheelysNewsContent newsContent = baseDao.getObject(WheelysNewsContent.class, id);
		newsContent.setNewscontent(content);
		baseDao.saveObject(newsContent);
	}

	@Override
	public List<WheelysNews> searchNewsList(String title, Timestamp fromdate, Timestamp todate, String category) {
		DetachedCriteria query = getNewListQuery(title, fromdate, todate, category);
		query.addOrder(Order.desc("createtime"));
		List<WheelysNews> newsList = baseDao.findByCriteria(query);
		return newsList;
	}

	@Override
	public int getNewsListCount(String title, Timestamp fromdate, Timestamp todate, String category) {
		DetachedCriteria query = getNewListQuery(title, fromdate, todate, category);
		query.setProjection(Projections.rowCount());
		List<Long> result = baseDao.findByCriteria(query);
		return result.get(0).intValue();
	}
	
	private DetachedCriteria getNewListQuery(String title, Timestamp fromdate, Timestamp todate, String category){
		DetachedCriteria query = DetachedCriteria.forClass(WheelysNews.class);
		if(VmUtils.isNotEmpty(title)){
			query.add(Restrictions.like("title", title, MatchMode.ANYWHERE));
		}
		
		if(VmUtils.isNotEmpObj(fromdate)){
			Timestamp fromTime = DateUtil.getBeginTimestamp(fromdate);
			query.add(Restrictions.ge("createtime", fromTime));
			if(VmUtils.isNotEmpObj(todate)){
				Timestamp toTime = DateUtil.getLastTimeOfDay(todate);
				if(fromTime.getTime() <= toTime.getTime()){
					query.add(Restrictions.le("createtime", toTime));
				}
			}
		}else if(VmUtils.isNotEmpObj(todate)){
			Timestamp toTime = DateUtil.getLastTimeOfDay(todate);
			query.add(Restrictions.le("createtime", toTime));
		}
		
		if(VmUtils.isNotBlank(category)){
			query.add(Restrictions.eq("category", category));
		}
		query.add(Restrictions.eq("delstatus", "N"));
		return query;
	}
}

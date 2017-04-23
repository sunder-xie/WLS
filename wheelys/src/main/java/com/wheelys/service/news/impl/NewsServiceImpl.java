package com.wheelys.service.news.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.constant.BannerConstant;
import com.wheelys.model.news.WheelysBanner;
import com.wheelys.model.news.WheelysNews;
import com.wheelys.model.news.WheelysNewsContent;
import com.wheelys.service.news.NewsService;

@Service("newsService")
public class NewsServiceImpl extends BaseServiceImpl implements NewsService {

	@Override
	public WheelysNews getNewsDetail(Long id) {
		WheelysNews wheelysNews = this.baseDao.getObject(WheelysNews.class, id);
		return wheelysNews;
	}

	@Override
	public String getNewscontent(Long id) {
		WheelysNewsContent content = this.baseDao.getObject(WheelysNewsContent.class, id);
		return content.getNewscontent();
	}

	@Override
	public List<WheelysNews> getNewsList(String category, Integer pageNo, Integer maxnum) {
		DetachedCriteria query = getNewsListQuery(category);
		List<WheelysNews> newsList = baseDao.findByCriteria(query, pageNo * maxnum, maxnum);
		return newsList;
	}

	private DetachedCriteria getNewsListQuery(String category) {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysNews.class);
		if (StringUtils.isNotBlank(category)) {
			query.add(Restrictions.eq("category", category));
		}
		query.add(Restrictions.eq("delstatus", "N"));
		query.add(Restrictions.eq("publishstatus", "Y"));
		query.addOrder(Order.desc("publishtime"));
		return query;
	}

	@Override
	public int getNewsListCount(String category) {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysNews.class);
		if (StringUtils.isNotBlank(category)) {
			query.add(Restrictions.eq("category", category));
		}
		query.setProjection(Projections.rowCount());
		List<Long> newsList = baseDao.findByCriteria(query);
		return newsList.get(0).intValue();
	}

	@Override
	public List<WheelysBanner> getBannerList(String type) {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysBanner.class);
		query.add(Restrictions.eq("showstatus", BannerConstant.SHOWSTATUS_YES));
		query.add(Restrictions.eq("delstatus", BannerConstant.DELSTATUS_NO));
		query.add(Restrictions.eq("type", type));
		query.addOrder(Order.asc("sn"));
		List<WheelysBanner> bannerList = baseDao.findByCriteria(query);
		return bannerList;
	}

	@Override
	public List<WheelysBanner> getBannerListByShop(Long shopid) {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysBanner.class);
		query.add(Restrictions.eq("showstatus", BannerConstant.SHOWSTATUS_YES));
		query.add(Restrictions.eq("delstatus", BannerConstant.DELSTATUS_NO));
		query.add(Restrictions.eq("shopid", shopid));
		query.addOrder(Order.asc("sn"));
		List<WheelysBanner> bannerList = baseDao.findByCriteria(query);
		return bannerList;
	}

	@Override
	public List<WheelysNews> findAllNews() {
		DetachedCriteria query = getNewsListQuery(null);
		return baseDao.findByCriteria(query);
	}

	@Override
	public void updateWheelysNews(WheelysNews wheelysNews) {
		baseDao.updateObject(wheelysNews);
	}

}

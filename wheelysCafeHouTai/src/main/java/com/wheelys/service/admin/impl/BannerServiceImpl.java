package com.wheelys.service.admin.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.DateUtil;
import com.wheelys.constant.BannerConstant;
import com.wheelys.model.banner.WheelysBanner;
import com.wheelys.service.admin.BannerService;

@Service("bannerService")
public class BannerServiceImpl extends BaseServiceImpl implements BannerService {

	@Override
	public List<WheelysBanner> getShopBannerList(Long shopid, Integer pageNo, int maxnum) {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysBanner.class);
		query.add(Restrictions.eq("shopid", shopid));
		query.add(Restrictions.eq("delstatus", BannerConstant.DELSTATUS_NO));
		query.add(Restrictions.eq("type", BannerConstant.TYPE_SHOP_INDEX));
		query.addOrder(Order.asc("sn"));
		query.addOrder(Order.desc("createtime"));
		int from = pageNo * maxnum;
		List<WheelysBanner> bannerList = baseDao.findByCriteria(query, from, maxnum);
		return bannerList;
	}
	
	@Override
	public List<WheelysBanner> getBannerList(Integer pageNo, String type, int maxnum) {
		if(type == null)type = BannerConstant.TYPE_NEWS_INDEX;
		DetachedCriteria query = DetachedCriteria.forClass(WheelysBanner.class);
		query.add(Restrictions.eq("delstatus", BannerConstant.DELSTATUS_NO));
		query.add(Restrictions.eq("type", type));
		query.addOrder(Order.asc("sn"));
		query.addOrder(Order.asc("createtime"));
		int from = pageNo * maxnum;
		List<WheelysBanner> wechatBannerList = baseDao.findByCriteria(query, from, maxnum);
		return wechatBannerList;
	}


	@Override
	public int findBannerCount(String type) {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysBanner.class);
		query.add(Restrictions.eq("delstatus", BannerConstant.DELSTATUS_NO));
		query.add(Restrictions.eq("type", type));
		query.setProjection(Projections.rowCount());
		List<Long> result = baseDao.findByCriteria(query);
		return result.get(0).intValue();
	}
	
	@Override
	public WheelysBanner updateShowstatus(Long id, Integer sn,String showstatus) {
		WheelysBanner banner = baseDao.getObject(WheelysBanner.class, id);
		if (banner != null) {
		   if(showstatus!=null){
			   if (StringUtils.equals(banner.getShowstatus(), BannerConstant.SHOWSTATUS_YES)) {
				   banner.setShowstatus(BannerConstant.SHOWSTATUS_NO);
				   banner.setUpdatetime(DateUtil.getCurFullTimestamp());
			    } else if (StringUtils.equals(banner.getShowstatus(), BannerConstant.SHOWSTATUS_NO)) {
				   banner.setShowstatus(BannerConstant.SHOWSTATUS_YES);
				   banner.setUpdatetime(DateUtil.getCurFullTimestamp());
			    }
		     }
			if (sn != null) {
				banner.setSn(sn);
			}
			baseDao.saveObject(banner);
		}
		
		return banner;
	}

	@Override
	public WheelysBanner deleteWechatBanner(Long id) {
		WheelysBanner banner = baseDao.getObject(WheelysBanner.class, id);
		banner.setDelstatus(BannerConstant.DELSTATUS_YES);
		banner.setUpdatetime(DateUtil.getCurFullTimestamp());
		baseDao.saveObject(banner);
		return banner;
	}

	@Override
	public WheelysBanner saveBanner(Long id, Long shopid, String title, String tourl, String imgurl, String type) {
		WheelysBanner banner = new WheelysBanner(imgurl);
		if (id != null) {
			banner = baseDao.getObject(WheelysBanner.class, id);
		}
		if (StringUtils.isNotBlank(imgurl)) {
			banner.setImageurl(imgurl);
		}
		banner.setShopid(shopid);
		banner.setTourl(tourl);
		banner.setType(type);
		if (id == null) {
		banner.setSn(99);
		}
		banner.setTitle(title);
		banner.setDelstatus(BannerConstant.SHOWSTATUS_NO);
		banner.setShowstatus(BannerConstant.SHOWSTATUS_YES);
		banner.setBegintime(DateUtil.getCurFullTimestamp());
		banner.setEndtime(DateUtil.getCurFullTimestamp());
		banner.setCreatetime(DateUtil.getCurFullTimestamp());
		return this.baseDao.saveObject(banner);
	}

	@Override
	public WheelysBanner getBanner(Long id) {
		WheelysBanner banner = baseDao.getObject(WheelysBanner.class, id);
		return banner;
	}

}

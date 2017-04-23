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
import com.wheelys.util.StringUtil;
import com.wheelys.model.CafeShop;
import com.wheelys.model.ShopSeller;
import com.wheelys.service.admin.SellerSerivce;

@Service("sellerService")
public class SellerServiceImpl extends BaseServiceImpl implements SellerSerivce {

	@Override
	public ShopSeller upAddShopSeller(Long id, Long shopid,String username, String loginname, String password, String role,String mobile) {
		ShopSeller seller = this.baseDao.getObject(ShopSeller.class, id);
	
		if (id == null) {
			seller = new ShopSeller(username);
		}
		if (id != null && StringUtils.isBlank(password)) {
			seller.setUserpwd(seller.getUserpwd());
			
		} else {
			password = StringUtil.md5(password);
			seller.setUserpwd(password);
		}
		
		seller.setUsername(username);
		seller.setLoginname(loginname);
		seller.setRole(role);
		seller.setShopid(shopid);
		seller.setMobile(mobile);
		seller.setStatus(0);
		seller.setStime(DateUtil.getMillTimestamp().getTime()/1000);
		this.baseDao.addObject(seller);
		return seller;
	}

	@Override
	public List<ShopSeller> showSellerList(Long shopid, Integer pageNo, int maxnum) {
		DetachedCriteria query = DetachedCriteria.forClass(ShopSeller.class);
		query.addOrder(Order.desc("id"));
		query.add(Restrictions.eq("shopid", shopid));
		query.add(Restrictions.eq("status", 0));
		int from = pageNo * maxnum;
		List<ShopSeller> showSellerList = baseDao.findByCriteria(query, from, maxnum);
		return showSellerList;

	}

	@Override
	public int findShopSellerCount(Long shopid) {
		DetachedCriteria query = DetachedCriteria.forClass(ShopSeller.class);
		query.add(Restrictions.eq("shopid", shopid));
		query.add(Restrictions.eq("status", 0));
		query.setProjection(Projections.rowCount());
		List<Long> result = baseDao.findByCriteria(query);
		return result.get(0).intValue();
	}

	@Override
	public boolean ShopSeller(Long shopid,String loginname) {
		DetachedCriteria query = DetachedCriteria.forClass(ShopSeller.class);
		query.add(Restrictions.eq("shopid", shopid));
		if (StringUtils.isNotBlank(loginname)) {
			query.add(Restrictions.eq("loginname", loginname));
		}
		query.setProjection(Projections.rowCount());
		List<Long> nameList = baseDao.findByCriteria(query);
		if (nameList.get(0) > 0) {
			return true;
		}
		return false;

	}

	@Override
	public CafeShop cafeShop(Long shopid) {
		CafeShop cafeShop = this.baseDao.getObject(CafeShop.class, shopid);
		return cafeShop;
	}

	@Override
	public void delShopSeller(Long id) {
		ShopSeller seller = baseDao.getObject(ShopSeller.class, id);
		seller.setStatus(1);	
		baseDao.saveObject(seller);

	}

	@Override
	public ShopSeller seller(Long id) {
		ShopSeller seller = this.baseDao.getObject(ShopSeller.class, id);
		return seller;
	}
}

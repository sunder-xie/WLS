package com.wheelys.service.cafe.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.Gcache;
import com.wheelys.constant.CityContant;
import com.wheelys.model.CafeShop;
import com.wheelys.service.cafe.CafeShopService;

@Service("cafeShopService")
public class CafeShopServiceImpl extends BaseServiceImpl implements CafeShopService {

	public final Gcache<Long, CafeShop> gcache = new Gcache<Long, CafeShop>(100);
	private Map<Long, String> shopidCityNameMap = new HashMap<Long, String>();

	@Override
	public List<CafeShop> getShopList(List<Long> idList) {
		List<CafeShop> shopList = new ArrayList<CafeShop>();
		for (Long id : idList) {
			CafeShop shop = getShop(id);
			if (shop != null) {
				shopList.add(shop);
			}
		}
		return shopList;
	}

	@Override
	public CafeShop getCacheShop(Long id) {
		CafeShop cafeShop = gcache.getIfPresent(id);
		if (cafeShop == null) {
			cafeShop = this.baseDao.getObject(CafeShop.class, id);
			if (cafeShop == null)
				return null;
			gcache.put(id, cafeShop);
		}
		return cafeShop;
	}

	@Override
	public CafeShop getShop(Long id) {
		CafeShop cafeShop = this.baseDao.getObject(CafeShop.class, id);
		return cafeShop;
	}

	@Override
	public CafeShop getShopByMchid(String mchid) {
		CafeShop cafeShop = this.baseDao.getObjectByUkey(CafeShop.class, "shopaccount", mchid);
		return cafeShop;
	}
	
	@Override
	public List<CafeShop> getOpenShopList(){
		return getOpenShopList(null);
	}

	@Override
	public List<CafeShop> getOpenShopList(String status) {
		DetachedCriteria query = DetachedCriteria.forClass(CafeShop.class);
		if(status == null){
			List<String> statusList = Arrays.asList("open", "close");
			query.add(Restrictions.in("booking", statusList));
		}else{
			query.add(Restrictions.eq("booking", status));
		}
		query.addOrder(Order.asc("shopid"));
		query.setProjection(Projections.property("shopid"));
		List<Long> shopidList = baseDao.findByCriteria(query);
		List<CafeShop> shopList = getShopList(shopidList);
		return shopList;
	}

	@Override
	public Map<Long, String> getShopidCityNameMap() {
		if (shopidCityNameMap.isEmpty()) {
			List<CafeShop> shopList = getOpenShopList(null);
			for (CafeShop shop : shopList) {
				shopidCityNameMap.put(shop.getShopid(), CityContant.CITYMAP.get(shop.getCitycode()));
			}
		}
		return shopidCityNameMap;
	}

	@Override
	public List<CafeShop> getCafeShopByCityCods(List<String> cityCodes) {
		DetachedCriteria query = DetachedCriteria.forClass(CafeShop.class);
		if (CollectionUtils.isNotEmpty(cityCodes)) {
			query.add(Restrictions.in("citycode", cityCodes));
		} else {
			return null;
		}
		return baseDao.findByCriteria(query);
	}

	@Override
	public void positiveClose() {
		List<CafeShop> shopList = getOpenShopList("open");
		for (CafeShop shop : shopList) {
			dbLogger.warn("强制关闭店铺："+shop.getShopname()+",ID:"+shop.getShopid()+",status:"+shop.getBooking());
			shop.setBooking("close");
			this.baseDao.saveObject(shop);
		}
	}

}

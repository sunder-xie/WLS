package com.wheelys.service.cafe.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.Gcache;
import com.wheelys.model.CafeShop;
import com.wheelys.service.cafe.CafeShopService;

@Service("cafeShopService")
public class CafeShopServiceImpl extends BaseServiceImpl implements CafeShopService {

	public final Gcache<Long,CafeShop> gcache = new Gcache<Long,CafeShop>(100);
	
	@Override
	public List<CafeShop> getShopList(List<Long> idList) {
		List<CafeShop> shopList = new ArrayList<CafeShop>();
		for (Long id : idList) {
			CafeShop shop = getShop(id);
			if(shop != null){
				shopList.add(shop);
			}
		}
		return shopList;
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
	public CafeShop getCacheShop(Long id) {
		CafeShop cafeShop = gcache.getIfPresent(id);
		if(cafeShop == null){
			cafeShop = this.baseDao.getObject(CafeShop.class, id);
			if(cafeShop == null)return null;
			gcache.put(id, cafeShop);
		}
		return cafeShop;
	}

}

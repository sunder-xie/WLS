package com.wheelys.service.cafe;

import java.util.List;
import java.util.Map;

import com.wheelys.model.CafeShop;

public interface CafeShopService {

	List<CafeShop> getShopList(List<Long> idList);

	CafeShop getShop(Long id);

	CafeShop getCacheShop(Long id);

	CafeShop getShopByMchid(String mchid);

	List<CafeShop> getOpenShopList(String status);

	List<CafeShop> getOpenShopList();

	Map<Long, String> getShopidCityNameMap();

	List<CafeShop> getCafeShopByCityCods(List<String> cityCodes);

	void positiveClose();
}

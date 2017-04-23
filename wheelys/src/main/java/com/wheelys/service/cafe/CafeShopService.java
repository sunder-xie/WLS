package com.wheelys.service.cafe;

import java.util.List;

import com.wheelys.model.CafeShop;

public interface CafeShopService {

	List<CafeShop> getShopList(List<Long> idList);

	CafeShop getShop(Long id);

	CafeShop getShopByMchid(String mchid);

	CafeShop getCacheShop(Long shopid);

}

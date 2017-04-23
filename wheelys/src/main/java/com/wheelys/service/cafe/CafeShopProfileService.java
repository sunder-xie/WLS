package com.wheelys.service.cafe;

import java.util.List;

import com.wheelys.model.CafeShopProfile;

public interface CafeShopProfileService {

	Boolean getTakeawaystatus(Long shopid);
	
	Boolean getReservedstatus(Long shopid);

	CafeShopProfile getShopProfile(Long shopid);
	
	List<String> getExpressAddressByShopid(Long shopid);
	
}

package com.wheelys.service.cafe;

import java.util.Map;

public interface CafeKeyService {
	
	String getCafeKeyByShopid(Long shopid);

	void saveKey(Map<String, String> map);
	
}

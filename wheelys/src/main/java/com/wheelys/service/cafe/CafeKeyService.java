package com.wheelys.service.cafe;

import com.wheelys.model.CafeKey;

public interface CafeKeyService {
	
	CafeKey getCafeKeyById(Long id);
	
	CafeKey getCafeKeyByShopid(Long shopid);
	
}

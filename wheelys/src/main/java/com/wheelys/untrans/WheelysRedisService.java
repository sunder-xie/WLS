package com.wheelys.untrans;

public interface WheelysRedisService {
	void saveInvokeQuantity(String regionName, Long id, String idKey, String quantityKey);
}

package com.wheelys.untrans.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wheelys.untrans.CacheService;
import com.wheelys.util.BeanUtil;
import com.wheelys.untrans.WheelysRedisService;

@Service("WheelysRedisService")
public class WheelysRedisServiceImpl implements WheelysRedisService {

	@Autowired
	@Qualifier("cacheService")
	private CacheService cacheService;

	/**
	 * 将某个对象被使用的次数放进缓存
	 * 
	 * @param regionName
	 * @param id
	 * @param key1
	 * @param key2
	 */
	public void saveInvokeQuantity(String regionName, Long id, String idKey, String quantityKey) {
		Object ids = cacheService.get(regionName, idKey);
		List<Long> idList = new ArrayList<>();
		if (ids == null) {
			idList.add(id);
		} else {
			idList = BeanUtil.getIdList(ids.toString(), ",");
			if (!idList.contains(id)) {
				idList.add(id);
			}
		}
		cacheService.set(regionName, idKey, StringUtils.join(idList, ","));
		cacheService.incr(regionName, quantityKey + id, 1, 1);
	}
}

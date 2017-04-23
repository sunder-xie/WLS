package com.wheelys.service.cafe.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.model.CafeKey;
import com.wheelys.service.cafe.CafeKeyService;

@Service("cafeKeyService")
public class CafeKeyServiceImpl extends BaseServiceImpl implements CafeKeyService,InitializingBean {
	
	private List<CafeKey> keyList = new ArrayList<CafeKey>();
	
	@Override
	public String getCafeKeyByShopid(Long shopid){
		int index = getIndex()%keyList.size();
		CafeKey cafeKey = keyList.get(index);
		return cafeKey.getName();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		keyList = this.baseDao.getObjectList(CafeKey.class, "id", true, 0, 500);
	}
	
	private int getIndex(){
		int result = new Random().nextInt(keyList.size()*2);
		return result+1; 
	}

	@Override
	public void saveKey(Map<String, String> map) {
		for (String value : map.values()) {
			String[] arr = StringUtils.split(value,":");
			CafeKey key = new CafeKey();
			key.setKeyimg(arr[0]);
			key.setName(arr[1]);
			this.baseDao.saveObject(key);
		}
	}

}

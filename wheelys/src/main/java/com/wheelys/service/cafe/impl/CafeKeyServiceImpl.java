package com.wheelys.service.cafe.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.model.CafeKey;
import com.wheelys.service.cafe.CafeKeyService;

@Service("cafeKeyService")
public class CafeKeyServiceImpl extends BaseServiceImpl implements CafeKeyService,InitializingBean {
	
	private List<CafeKey> keyList = new ArrayList<CafeKey>();
	private Map<Long, CafeKey> keyMap = new HashMap<Long, CafeKey>();

	@Override
	public CafeKey getCafeKeyById(Long id){
		CafeKey cafeKey = keyMap.get(id);
		if(cafeKey == null){
			cafeKey = this.baseDao.getObject(CafeKey.class, id);
			keyMap.put(id, cafeKey);
		}
		return cafeKey;
	}
	
	@Override
	public CafeKey getCafeKeyByShopid(Long shopid){
		int index = getIndex()%keyList.size();
		CafeKey cafeKey = keyList.get(index);
		return cafeKey;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		keyList = this.baseDao.getObjectList(CafeKey.class, "id", true, 0, 500);
		dbLogger.warn("keyList："+keyList.size());
	}
	
	private int getIndex(){
		int result = new Random().nextInt(keyList.size()*2);
		return result+1; 
	}

}

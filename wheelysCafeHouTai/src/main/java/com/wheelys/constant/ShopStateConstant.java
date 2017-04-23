package com.wheelys.constant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.UnmodifiableMap;

public abstract class ShopStateConstant {

	public static final String STATE_OPEN = "open";//开店
	public static final String STATE_COLOSE= "close";//打烊
	public static final String STATE_FS= "FS";//店铺永久关停
	public static final String STATE_COLOSEDOWN= "closedown";//关店
	
	public static final List<String> SHOWSTATE = Arrays.asList(ShopStateConstant.STATE_COLOSE,
			ShopStateConstant.STATE_COLOSEDOWN,ShopStateConstant.STATE_OPEN);
	
	public static Map<String,String> SHOPSTATEMAP = new HashMap<String, String>();
	
	static{
		Map map = new HashMap<String, String>();
		map.put(STATE_OPEN, "开店");
		map.put(STATE_COLOSE, "打烊");
		map.put(STATE_FS, "店铺永久关停");
		map.put(STATE_COLOSEDOWN, "关店");
		SHOPSTATEMAP = UnmodifiableMap.decorate(map);
	}

}

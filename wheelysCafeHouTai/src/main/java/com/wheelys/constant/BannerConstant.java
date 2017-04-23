package com.wheelys.constant;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.UnmodifiableMap;

public class BannerConstant {

	public static final String TYPE_WECHAT_INDEX = "WECHAT_INDEX";
	public static final String TYPE_NEWS_INDEX = "NEWS_INDEX";
	public static final String TYPE_SHOP_INDEX = "SHOP_INDEX";
	
	public static Map<String,String> TYPEMAP = new HashMap<String, String>();
	
	static{
		Map map = new HashMap<String, String>();
		map.put(TYPE_SHOP_INDEX, "店铺Banner");
		map.put(TYPE_WECHAT_INDEX, "首页");
		map.put(TYPE_NEWS_INDEX, "发现页");
		TYPEMAP = UnmodifiableMap.decorate(map);
	}
	
	public static final String DELSTATUS_YES = "Y";
	public static final String DELSTATUS_NO = "N";
	
	public static final String SHOWSTATUS_YES = "Y";
	public static final String SHOWSTATUS_NO = "N";

}

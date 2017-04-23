package com.wheelys.constant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.UnmodifiableMap;

public class BannerConstant {

	public static final String TYPE_WECHAT_INDEX = "WECHAT_INDEX";	//首页Banner
	public static final String TYPE_NEWS_INDEX = "NEWS_INDEX";		//发现Banner
	
	public static final List<String> BANNERTYPE = Arrays.asList(TYPE_WECHAT_INDEX, TYPE_NEWS_INDEX);
	
	public static Map<String,String> TYPEMAP = new HashMap<String, String>();
	
	static{
		Map map = new HashMap<String, String>();
		map.put(TYPE_WECHAT_INDEX, "微信首页");
		map.put(TYPE_NEWS_INDEX, "新闻首页");
		TYPEMAP = UnmodifiableMap.decorate(map);
	}
	
	public static final String DELSTATUS_YES = "Y";
	public static final String DELSTATUS_NO = "N";
	
	public static final String SHOWSTATUS_YES = "Y";
	public static final String SHOWSTATUS_NO = "N";

}

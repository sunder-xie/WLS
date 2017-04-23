package com.wheelys.constant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.UnmodifiableMap;


public class MobileDynamicCodeConstant {
	
	public static final String TAG_REGISTERCODE = "registerCode"; 	//获取手机注册
	public static final String TAG_DYNAMICCODE = "dynamicCode"; 	//快速登录动态码
	public static final String TAG_BINDMOBILE = "bindMobile"; 		//绑定手机号
	public static final String TAG_CHANGEMOBILE = "changeMobile";	//修改手机号
	
	public static final int VALID_MIN = 10;	//有效时间
	public static final int VALID_MAXCHECK = 20;	//最大验证次数
	//有效发送动态码tag集合
	public static final List<String> VALID_TAG_LIST = Arrays.asList(new String[] {TAG_REGISTERCODE, TAG_DYNAMICCODE, TAG_BINDMOBILE, TAG_CHANGEMOBILE});
	
	private static final Map<String, String> TEMPLATE_MAP;
	static {
		Map<String, String> tmp = new HashMap<String, String>();
		tmp.put(TAG_REGISTERCODE, "您的验证码为checkpass，有效时间10分钟");
		tmp.put(TAG_DYNAMICCODE, "您的验证码为checkpass，有效时间10分钟");
		tmp.put(TAG_BINDMOBILE, "您的验证码为checkpass，有效时间10分钟");
		tmp.put(TAG_CHANGEMOBILE, "您的验证码为checkpass，有效时间10分钟");
		TEMPLATE_MAP = UnmodifiableMap.decorate(tmp);
	}
	//默认短信
	public static final String DEFAULT_TEMPLATE = "";
	
	public static String getMsgTemplate(String tag) {
		if(TEMPLATE_MAP.containsKey(tag)) return TEMPLATE_MAP.get(tag);
		return DEFAULT_TEMPLATE;
	}
	
}

package com.wheelys.constant;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.UnmodifiableMap;

public class MchProductConstant {

	public static final long TYPE_ONEMIN  = 1;
	public static final long TYPE_TWOMIN = 2;
	public static Map<Long,String> TYPEMAP = new HashMap<Long, String>();
	
	static{
		Map map = new HashMap<Long, String>();
		map.put(1, "圣唐商品管理");
		map.put(2, "香季商品管理");
		TYPEMAP = UnmodifiableMap.decorate(map);
	}
	
	public static final String DELSTATUS_YES = "Y";
	public static final String DELSTATUS_NO = "N";
	
	public static final String SHOWSTATUS_YES = "Y";
	public static final String SHOWSTATUS_NO = "N";

}

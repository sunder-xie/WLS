package com.wheelys.constant;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.UnmodifiableMap;

public abstract class ElecCardConstant {

	public static final String STATUS_NEW = "N";	//待售
	public static final String STATUS_SOLD = "Y";	//售出
	public static final String STATUS_DISCARD = "D";//废弃
	public static final String STATUS_USED = "U";   //使用过
	public static final String STATUS_LOCK = "L";   //冻结使用, Y------>L

	public static final String CARDTYPE_1 = "1";//抵值券优惠券
	public static final String CARDTYPE_2 = "2";//兑换券

	public static final String JSTYPPE_WHEELYS="WHEELYS";//公司单独承担
	public static final String JSTYPPE_WHEELYS_PARTNER="WHEELYS_PARTNER";//公司和运营商共同承担
	public static final String JSTYPPE_PARTNER="PARTNER";//运营商单独承担

	public static final String MARK_FIXAMOUNT="FIXAMOUNT";//特价金额
	public static final String MARK_DISCOUNTAMOUNT="DISCOUNTAMOUNT";//抵扣金额
	
	public static Map<String,String> MARKAMOUNTMAP = new HashMap<String, String>();
	static{
		Map map = new HashMap<String, String>();
		map.put(MARK_FIXAMOUNT, "特价金额(元)");
		map.put(MARK_DISCOUNTAMOUNT, "抵扣金额(元)");
		MARKAMOUNTMAP = UnmodifiableMap.decorate(map);
	}
	
	public static Map<String,String> JSTYPEMAP = new HashMap<String, String>();
	static{
		Map map = new HashMap<String, String>();
		map.put(JSTYPPE_WHEELYS,"公司单独承担");
		map.put(JSTYPPE_WHEELYS_PARTNER,"公司和运营商共同承担");
		map.put(JSTYPPE_PARTNER,"运营商单独承担");
		JSTYPEMAP = UnmodifiableMap.decorate(map);
	}
	
	public static Map<String,String> CARDTYPEMAP = new HashMap<String, String>();
	
	static{
		Map map = new HashMap<String, String>();
		map.put(CARDTYPE_1,"抵值优惠券");
		map.put(CARDTYPE_2,"兑换券");
		CARDTYPEMAP = UnmodifiableMap.decorate(map);
	}
}

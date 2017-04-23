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

	public static final String CARDTYPE_DEDUCTION = "1";//抵值券优惠券
	public static final String CARDTYPE_EXCHANGE = "2";//兑换券
	
	public static final String MARK_FIXAMOUNT="FIXAMOUNT";//特价金额
	public static final String MARK_DISCOUNTAMOUNT="DISCOUNTAMOUNT";//抵扣金额
	
	public static Map<String,String> CARDTYPEMAP = new HashMap<String, String>();
	
	static{
		Map map = new HashMap<String, String>();
		map.put(CARDTYPE_DEDUCTION,"抵值优惠券");
		map.put(CARDTYPE_EXCHANGE,"兑换券");
		CARDTYPEMAP = UnmodifiableMap.decorate(map);
	}
}

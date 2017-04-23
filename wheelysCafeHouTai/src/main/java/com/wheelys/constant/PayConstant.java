package com.wheelys.constant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.UnmodifiableMap;

public abstract class PayConstant {

	public static final List<String> ALLPAYMETHOD = Arrays.asList(PayConstant.PAYMETHOD_WEIXINH5,
			PayConstant.PAYMETHOD_ALIH5);

	public static final String GATEWAYCODE_WEIXIN = "WEIXIN"; // 微信支付网关
	public static final String GATEWAYCODE_POS = "POS"; // POS支付网关
	public static final String PAYMETHOD_UNKNOWN = "unknown"; // 未知
	public static final String PAYMETHOD_ALIH5 = "ALIH5"; // 支付宝支付
	public static final String PAYMETHOD_WEIXINH5 = "WEIXINH5"; // 微信H5
	public static final String PAYMETHOD_POSPAY = "POSPAY"; // POS机支付
	public static final String PAYMETHOD_CARD = "CARDPAY"; // 卡券支付

	public static Map<String,String> PAYMAP = new HashMap<String, String>();
	static{
		Map map = new HashMap<String, String>();
		map.put(PAYMETHOD_UNKNOWN, "未知");
		map.put(PAYMETHOD_ALIH5, "支付宝支付");
		map.put(PAYMETHOD_WEIXINH5, "微信支付");
		map.put(PAYMETHOD_POSPAY, "POS机支付");
		map.put(PAYMETHOD_CARD, "卡券支付");
		PAYMAP = UnmodifiableMap.decorate(map);
	}
	
}

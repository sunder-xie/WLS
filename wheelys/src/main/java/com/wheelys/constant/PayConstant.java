package com.wheelys.constant;

import java.util.Arrays;
import java.util.List;

public abstract class PayConstant {
	
	public static final List<String> ALLPAYMETHOD = Arrays.asList(PayConstant.PAYMETHOD_WEIXINH5,PayConstant.PAYMETHOD_POSPAY);

	public static final String PAYSTATUS_NEW = "new";		//新订单
	public static final String PAYSTATUS_PAID = "paid";		//已支付

	public static final String GATEWAYCODE_WEIXIN = "WEIXIN";			//微信支付网关
	public static final String GATEWAYCODE_ALIPAY = "ALIPAY";			//支付宝支付网关
	public static final String GATEWAYCODE_POS = "POS";			//POS支付网关
	public static final String GATEWAYCODE_CARD = "CARD";			//CARD支付网关

	public static final String PAYMETHOD_UNKNOWN = "unknown";			//未知
	public static final String PAYMETHOD_ALIH5 = "ALIH5";			//支付宝支付
	public static final String PAYMETHOD_WEIXINH5 = "WEIXINH5";		//微信H5
	public static final String PAYMETHOD_ALIAPP = "ALIAPP";			//支付宝支付
	public static final String PAYMETHOD_WEIXINAPP = "WEIXINAPP";	//微信支付
	public static final String PAYMETHOD_POSPAY = "POSPAY";			//POS机支付
	public static final String PAYMETHOD_CARD = "CARDPAY";			//卡券支付
	
}

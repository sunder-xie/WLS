package com.wheelys.pay.config;

/**
 * 微信支付配置
 */
public class WeiXinPayConfigure {
	
	public static String APPID = "wx4beff2537ff35c6c";
	
	public static String MCH_ID = "1405603202";
	
	public static String MCH_KEY = "29782422897dd7f6e1beb82ee477561b";

	public static String APPSECRET = "d352ae72fb7cbedef06c569712f62fc6";
	
	public static String PREPAYURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	public static String QUERYURL = "https://api.mch.weixin.qq.com/pay/orderquery";
	
	public static String NOTIFYURL = "notify/wxmppay.xhtml";
	
	public static String MICROPAYURL = "https://api.mch.weixin.qq.com/pay/micropay";

}

package com.wheelys.pay.config;

/**
 * 微信支付配置
 */
public interface WeiXinPayConfigure {
	
	String APPID = "wx4beff2537ff35c6c";
	
	String MCH_ID = "1405603202";
	
	String MCH_KEY = "29782422897dd7f6e1beb82ee477561b";

	String APPSECRET = "d352ae72fb7cbedef06c569712f62fc6";
	
	String PREPAYURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	String QUERYURL = "https://api.mch.weixin.qq.com/pay/orderquery";
	
	String REFUNDURL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	
	String NOTIFYURL = "notify/wxmppay.xhtml";
	
	String MICROPAYURL = "https://api.mch.weixin.qq.com/pay/micropay";

}

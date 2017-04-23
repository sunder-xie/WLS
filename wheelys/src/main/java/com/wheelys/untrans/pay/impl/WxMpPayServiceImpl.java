package com.wheelys.untrans.pay.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.model.order.CardCouponsOrder;
import com.wheelys.model.order.WheelysOrder;
import com.wheelys.pay.WeiXinJsPayUtil;
import com.wheelys.pay.config.WeiXinPayConfigure;
import com.wheelys.untrans.pay.AbstractPayService;
import com.wheelys.untrans.pay.WxMpPayService;

@Service("wxMpPayService")
public class WxMpPayServiceImpl extends AbstractPayService implements WxMpPayService {

	@Value("${WXMP.APPID}")
	private String APPID;
	@Value("${WXMP.MCH_ID}")
	private String MCH_ID;
	@Value("${WXMP.MCH_KEY}")
	private String MCH_KEY;
	@Value("${WXMP.APPSECRET}")
	private String APPSECRET;
	@Value("${WXMP.PREPAYURL}")
	private String PREPAYURL;
	@Value("${WXMP.QUERYURL}")
	private String QUERYURL;
	@Value("${WXMP.NOTIFYURL}")
	private String NOTIFYURL;
	@Value("${WXMP.MICROPAYURL}")
	private String MICROPAYURL;
	
	@Override
	public Map<String, String> getJsapiPayJsonParams(WheelysOrder order, String clientIp) {
		Map<String, String> payParams = WeiXinJsPayUtil.getJsapiPayParams(order, clientIp, config.getAbsPath());
		return payParams;
	}

	@Override
	public Map<String, String> getJsapiPayJsonParams(CardCouponsOrder order, String clientIp) {
		Map<String, String> payParams = WeiXinJsPayUtil.getJsapiPayParams(order.getTradeno(), order.getPayfee(), order.getCreatetime(), order.getValidtime(), order.getUkey(), null, clientIp, config.getAbsPath());
		return payParams;
	}

	@Override
	public ResultCode<Map<String, String>> queryPayInfoForNewMer(String tradeno) {
		return WeiXinJsPayUtil.queryPayInfoForNewMer(tradeno);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		WeiXinPayConfigure.APPID =  APPID;
		WeiXinPayConfigure.MCH_ID = MCH_ID;
		WeiXinPayConfigure.MCH_KEY = MCH_KEY;
		WeiXinPayConfigure.APPSECRET = APPSECRET;
		WeiXinPayConfigure.PREPAYURL = PREPAYURL;
		WeiXinPayConfigure.QUERYURL = QUERYURL;
		WeiXinPayConfigure.NOTIFYURL = NOTIFYURL;
		WeiXinPayConfigure.MICROPAYURL = MICROPAYURL;
	}

}

package com.wheelys.untrans.pay;

import java.util.Map;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.model.order.CardCouponsOrder;
import com.wheelys.model.order.WheelysOrder;

public interface WxMpPayService {
	
	Map<String, String> getJsapiPayJsonParams(WheelysOrder order, String clientIp);
	
	ResultCode<Map<String, String>> queryPayInfoForNewMer(String tradeno);

	Map<String, String> getJsapiPayJsonParams(CardCouponsOrder order, String clientIp);
	
}

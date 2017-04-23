package com.wheelys.untrans.pay;

import java.util.Map;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.model.order.WheelysOrder;

public interface AliWapPayService {

	String getWapPayUrl(WheelysOrder order, String clientIp);
	
	ResultCode<Map<String, String>> processOrder(String tradeno);
	
}

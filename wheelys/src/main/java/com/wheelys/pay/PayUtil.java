package com.wheelys.pay;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.wheelys.util.JsonUtils;
import com.wheelys.model.order.WheelysOrder;

public class PayUtil {
	
	public static String getPayUrl(HttpServletRequest request, WheelysOrder order, String absPath, String clientIp){
    	String ua = request.getHeader("User-Agent");
        String payUrl = "";
        if (ua.contains("MicroMessenger")) {
        	Map<String, String> payParams = WeiXinJsPayUtil.getJsapiPayParams(order , clientIp, absPath);
        	payUrl = JsonUtils.writeMapToJson(payParams);
        }else{
        	payUrl = AliWapPayUtil.getWapPayUrl(order, clientIp, absPath);
        }
        return payUrl;
	}

}

package com.wheelys.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.util.DateUtil;
import com.wheelys.util.WheelysLogger;
import com.wheelys.util.HttpResult;
import com.wheelys.util.HttpUtils;
import com.wheelys.util.JsonUtils;
import com.wheelys.util.WebLogger;
import com.wheelys.model.order.WheelysOrder;
import com.wheelys.pay.config.WeiXinPayConfigure;

public class WeiXinSendUtils {

	protected final static transient WheelysLogger dbLogger = WebLogger.getLogger(WeiXinSendUtils.class);
	public final static String GETTOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
	public final static String URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
	
	public static void sendMsg(WheelysOrder order){
		try {
			if(StringUtils.isBlank(order.getUkey())){
				return;
			}
			Map msgMap = new HashMap();
			msgMap.put("touser", order.getUkey());
			msgMap.put("template_id", "sZrfTXa3t-rBXPQihg0sGADAccCvqfLzfQOcwy3qJng");
			Map data = new HashMap();
			msgMap.put("data", data);
			Map valueMap = new HashMap();
			valueMap.put("value", "恭喜你购买成功！");
			data.put("first", valueMap);
			Map valueMap1 = new HashMap();
			valueMap1.put("value", VmUtils.getAmount(order.getNetpaid()));
			data.put("keyword1", valueMap1);
			Map valueMap2 = new HashMap();
			valueMap2.put("value", order.getQuantity());
			data.put("keyword2", valueMap2);
			Map valueMap3 = new HashMap();
			valueMap3.put("value", DateUtil.formatTimestamp(order.getPaidtime()));
			data.put("keyword3", valueMap3);
			Map valueMap4 = new HashMap();
			valueMap4.put("value", order.getTradeno());
			data.put("keyword4", valueMap4);
			Map valueMap5 = new HashMap();
			valueMap5.put("value", order.getOrdertitle());
			data.put("keyword5", valueMap5);
			Map valueMap6 = new HashMap();
			valueMap6.put("value", "取杯口令："+order.getTakekey());
			data.put("remark", valueMap6);
			String body = JsonUtils.writeMapToJson(msgMap);
			HttpResult result = HttpUtils.postBodyAsString(URL+getBaseToken().getRetval(), body);
			if(!result.isSuccess()){
				dbLogger.error("sendMsg error : "+ result.getResponse());
			}
		} catch (Exception e) {
			dbLogger.error("sendMsg error : "+ order.getId());
		}
	}
	
	private static ResultCode<String> getBaseToken() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", WeiXinPayConfigure.APPID);
		params.put("secret", WeiXinPayConfigure.APPSECRET);
		params.put("grant_type", "client_credential");
		HttpResult result = HttpUtils.getUrlAsString(GETTOKEN_URL, params);
		if (result.isSuccess()) {
			String response = result.getResponse();
			Map<String, Object> map = JsonUtils.readJsonToMap(response);
			Object obj = map.get("errmsg");
			String errmsg = (obj == null ? null : obj+"");
			if(StringUtils.isNotBlank(errmsg) && !StringUtils.equalsIgnoreCase(errmsg, "ok")){
				return ResultCode.getFailure(""+map.get("errcode"), errmsg);
			}
			Map<String, Object> res = JsonUtils.readJsonToMap(response);
			return ResultCode.getSuccessReturn(res.get("access_token")+"");
		}
		return ResultCode.getFailure(result.getMsg());
	}

}

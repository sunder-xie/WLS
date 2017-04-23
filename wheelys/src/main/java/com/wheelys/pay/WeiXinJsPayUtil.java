package com.wheelys.pay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.util.DateUtil;
import com.wheelys.util.WheelysLogger;
import com.wheelys.util.HttpResult;
import com.wheelys.util.HttpUtils;
import com.wheelys.util.JsonUtils;
import com.wheelys.util.StringUtil;
import com.wheelys.util.WebLogger;
import com.wheelys.model.order.WheelysOrder;
import com.wheelys.pay.config.WeiXinPayConfigure;
import com.wheelys.util.WebUtils;

public class WeiXinJsPayUtil {
	
	private static final transient WheelysLogger dbLogger = WebLogger.getLogger(WeiXinJsPayUtil.class);

	public static Map<String, String> getJsapiPayParams(String tradeno, Integer payfee, Timestamp createtime,
			Timestamp validtime, String ukey, Long shopid, String clientIp, String absPath) {
		Map<String, String> prepayParams = new HashMap<String, String>();
		prepayParams.put("appid", WeiXinPayConfigure.APPID);// 公众账号ID
		prepayParams.put("mch_id", WeiXinPayConfigure.MCH_ID);// 商户号
		prepayParams.put("nonce_str", StringUtil.getRandomString(32));// 随机字符串
		prepayParams.put("body", "wheelys咖啡");// 商品描述
		prepayParams.put("out_trade_no", tradeno);// 商户订单号
		prepayParams.put("fee_type", "CNY");// 货币类型 
		prepayParams.put("total_fee", payfee + "");// 总金额
		prepayParams.put("spbill_create_ip", clientIp);// 终端IP
		prepayParams.put("time_start", DateUtil.format(createtime, "yyyyMMddHHmmss"));// 交易起始时间
		prepayParams.put("time_expire",DateUtil.format(validtime, "yyyyMMddHHmmss"));// 交易结束时间
		prepayParams.put("notify_url", absPath + WeiXinPayConfigure.NOTIFYURL);// 通知地址
		prepayParams.put("trade_type", "JSAPI");// 交易类型
		prepayParams.put("openid", ukey);// 用户标识
		prepayParams.put("device_info", shopid+"");// device_info
		String sign = getSign(prepayParams, WeiXinPayConfigure.MCH_KEY);
		prepayParams.put("sign", sign);
		String postBody = toXml(prepayParams);
		HttpResult httpResult = HttpUtils.postBodyAsString(WeiXinPayConfigure.PREPAYURL, postBody);
		Map<String, String> payParams = new HashMap<String, String>();
		if(httpResult.isSuccess()){		
			String response = httpResult.getResponse();
			Map<String, String> retParams = xml2Map(response);				
			if(StringUtils.equals(retParams.get("return_code"), "SUCCESS")){
				String retSign = retParams.remove("sign");
				String retMysign = WeiXinJsPayUtil.getSign(retParams, WeiXinPayConfigure.MCH_KEY);
				if(!StringUtils.equals(retSign, retMysign)){
					dbLogger.error(tradeno + " sign check error:" + response);
					return payParams;
				}else{
					payParams.put("appId", WeiXinPayConfigure.APPID);
					payParams.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
					payParams.put("nonceStr", StringUtil.getRandomString(32));
					payParams.put("package", "prepay_id="+retParams.get("prepay_id"));
					payParams.put("signType", "MD5");
					String paysign = getSign(payParams, WeiXinPayConfigure.MCH_KEY);
					payParams.put("paySign", paysign);
				}				
			}else{
				dbLogger.warn(tradeno + ": return_code:" + retParams.get("return_code") + "; return_msg:" + retParams.get("return_msg"));	
			}
		}else{
			dbLogger.warn(tradeno + " submitBody error:" + httpResult.getMsg());
		}
		return payParams;
	}

	public static Map<String, String> getJsapiPayParams(WheelysOrder order, String clientIp, String absPath) {
		Map<String, String> prepayParams = new HashMap<String, String>();
		prepayParams.put("appid", WeiXinPayConfigure.APPID);// 公众账号ID
		prepayParams.put("mch_id", WeiXinPayConfigure.MCH_ID);// 商户号
		prepayParams.put("nonce_str", StringUtil.getRandomString(32));// 随机字符串
		prepayParams.put("body", "wheelys咖啡");// 商品描述
		prepayParams.put("out_trade_no", order.getTradeno());// 商户订单号
		prepayParams.put("fee_type", "CNY");// 货币类型 
		prepayParams.put("total_fee", order.getPayfee() + "");// 总金额
		prepayParams.put("spbill_create_ip", clientIp);// 终端IP
		prepayParams.put("time_start", DateUtil.format(order.getCreatetime(), "yyyyMMddHHmmss"));// 交易起始时间
		prepayParams.put("time_expire",DateUtil.format(order.getValidtime(), "yyyyMMddHHmmss"));// 交易结束时间
		prepayParams.put("notify_url", absPath + WeiXinPayConfigure.NOTIFYURL);// 通知地址
		prepayParams.put("trade_type", "JSAPI");// 交易类型
		prepayParams.put("openid", order.getUkey());// 用户标识
		prepayParams.put("device_info", order.getShopid()+"");// device_info
		String sign = getSign(prepayParams, WeiXinPayConfigure.MCH_KEY);
		prepayParams.put("sign", sign);
		String postBody = toXml(prepayParams);
		HttpResult httpResult = HttpUtils.postBodyAsString(WeiXinPayConfigure.PREPAYURL, postBody);
		Map<String, String> payParams = new HashMap<String, String>();
		if(httpResult.isSuccess()){		
			String response = httpResult.getResponse();
			Map<String, String> retParams = xml2Map(response);				
			if(StringUtils.equals(retParams.get("return_code"), "SUCCESS")){
				String retSign = retParams.remove("sign");
				String retMysign = WeiXinJsPayUtil.getSign(retParams, WeiXinPayConfigure.MCH_KEY);
				if(!StringUtils.equals(retSign, retMysign)){
					dbLogger.error(order.getTradeno() + " sign check error:" + response);
					return payParams;
				}else{
					payParams.put("appId", WeiXinPayConfigure.APPID);
					payParams.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
					payParams.put("nonceStr", StringUtil.getRandomString(32));
					payParams.put("package", "prepay_id="+retParams.get("prepay_id"));
					payParams.put("signType", "MD5");
					String paysign = getSign(payParams, WeiXinPayConfigure.MCH_KEY);
					payParams.put("paySign", paysign);
				}	
			}else{
				dbLogger.warn(order.getTradeno() + ": return_code:" + retParams.get("return_code") + "; return_msg:" + retParams.get("return_msg"));	
			}
		}else{
			dbLogger.warn(order.getTradeno() + " submitBody error:" + httpResult.getMsg());
		}
		return payParams;
	}

	public static ResultCode<Map<String, String>> queryPayInfoForNewMer(String tradeno) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", WeiXinPayConfigure.APPID);// 公众账号ID
		params.put("mch_id", WeiXinPayConfigure.MCH_ID);// 商户号
		params.put("out_trade_no", tradeno);
		params.put("nonce_str", StringUtil.getRandomString(32));
		String sign = getSign(params, WeiXinPayConfigure.MCH_KEY);
		params.put("sign", sign);

		String postBody = toXml(params);
		HttpResult httpResult = HttpUtils.postBodyAsString(WeiXinPayConfigure.QUERYURL, postBody);
		if (httpResult.isSuccess()) {
			String response = httpResult.getResponse();
			Map<String, String> retParams = xml2Map(response);
			if (StringUtils.equals(retParams.get("result_code"), "SUCCESS")) {
				String returnSign = retParams.remove("sign");
				String myCheckSign = getSign(retParams, WeiXinPayConfigure.MCH_KEY);
				if (StringUtils.equals(returnSign, myCheckSign)) {
					if (StringUtils.equals(retParams.get("trade_state"), "SUCCESS")) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("paidamount", retParams.get("total_fee"));
						map.put("payseqno", retParams.get("transaction_id"));
						map.put("ukey", retParams.get("openid"));
						map.put("appid", retParams.get("appid"));
						map.put("mchid", retParams.get("mch_id"));
						map.put("otherinfo", JsonUtils.writeMapToJson(retParams));
						return ResultCode.getSuccessReturn(map);
					} else {
						dbLogger.warn("订单" + tradeno + "查询失败：" + JsonUtils.writeMapToJson(retParams));
						return ResultCode.getFailure("订单" + tradeno + "查询失败");
					}
				} else {
					dbLogger.warn("签名验证失败！");
					return ResultCode.getFailure("check sign error");
				}
			}else{
				dbLogger.warn("订单" + tradeno + "查询失败：" + JsonUtils.writeMapToJson(retParams));
				return ResultCode.getFailure("订单" + tradeno + "查询失败");
			}
		}else {
			dbLogger.warn(tradeno + "查询失败:httpStatus="+httpResult.getStatus()+",httpMsg=" + httpResult.getMsg());
			return ResultCode.getFailure("查询失败:httpStatus="+httpResult.getStatus()+",httpMsg=" + httpResult.getMsg());
		}
	}

	public static boolean checkSign(Map<String, String> params, String sign){
		String mysign = getSign(params, WeiXinPayConfigure.MCH_KEY);
		if(StringUtils.equals(sign, mysign)){
			return true;
		}
		return false;
	}

	private static String getSign(Map<String, String> params, String partnerKey) {
		String content = WebUtils.joinParams(params, true);
		content = content + "&key=" + partnerKey;
		return StringUtil.md5(content, "utf-8").toUpperCase();
	}

	public static String toXml(Map<String, String> params){
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for( Map.Entry<String,String> entry : params.entrySet()){
			sb.append("<" + entry.getKey() + ">");
			sb.append("<![CDATA[" + entry.getValue() + "]]>");
			sb.append("</" + entry.getKey() + ">");
		}
		sb.append("</xml>");
		return sb.toString();
	}
	
	public static Map<String, String> xml2Map(String infoXML) {  
	     Document document;  
	     Map<String, String> map = new HashMap<String, String>();  
	     try {  
	         document = DocumentHelper.parseText(infoXML);  
	         Element root = document.getRootElement();  
	         Iterator it = root.elements().iterator();  
	         while (it.hasNext()) {  
	             Element info = (Element) it.next();  
	             map.put(info.getName(), info.getText());  
	             Iterator itc = info.elements().iterator();  
	             while (itc.hasNext()) {  
	                 Element infoc = (Element) itc.next();  
	                 map.put(infoc.getName(), infoc.getText());  
	             }  
	         }  
	     } catch (DocumentException e) {  
	     }  
	     return map;  
	 }
	
	public static String getPostBody(HttpServletRequest request){
		String encode = "utf-8" ;
		BufferedReader in = null;
		String result = "";
		try{
			in = new BufferedReader(new InputStreamReader(request.getInputStream(), encode));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}			
		}catch(Exception e){
			dbLogger.error(e, 5);
		}finally{
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					dbLogger.error(e, 5);
				}
			}
		}		
		return result;
	}
	
}

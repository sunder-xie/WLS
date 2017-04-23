package com.wheelys.pay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import com.wheelys.util.WheelysLogger;
import com.wheelys.util.HttpResult;
import com.wheelys.util.HttpUtils;
import com.wheelys.util.StringUtil;
import com.wheelys.util.WebLogger;
import com.wheelys.model.order.WheelysOrder;
import com.wheelys.pay.config.WeiXinPayConfigure;
import com.wheelys.util.WebUtils;

public class WeiXinMicroPayUtil {
	
	private static final transient WheelysLogger dbLogger = WebLogger.getLogger(WeiXinMicroPayUtil.class);

	public static ResultCode getJsapiPayParams(WheelysOrder order,String authCode, String clientIp) {
		Map<String, String> prepayParams = new HashMap<String, String>();
		prepayParams.put("appid", WeiXinPayConfigure.APPID);// 公众账号ID
		prepayParams.put("mch_id", WeiXinPayConfigure.MCH_ID);// 商户号
		prepayParams.put("nonce_str", StringUtil.getRandomString(32));// 随机字符串
		prepayParams.put("body", "wheelys咖啡");// 商品描述
		prepayParams.put("out_trade_no", order.getTradeno());// 商户订单号
		prepayParams.put("total_fee", order.getPayfee() + "");// 总金额
		prepayParams.put("spbill_create_ip", clientIp);// 终端IP
		prepayParams.put("auth_code", authCode);// auth_code
		prepayParams.put("device_info", order.getShopid()+"");// device_info
		String sign = getSign(prepayParams, WeiXinPayConfigure.MCH_KEY);
		prepayParams.put("sign", sign);
		String postBody = toXml(prepayParams);
		HttpResult httpResult = HttpUtils.postBodyAsString(WeiXinPayConfigure.MICROPAYURL, postBody);
		if(httpResult.isSuccess()){		
			String response = httpResult.getResponse();
			Map<String, String> retParams = xml2Map(response);				
			if(StringUtils.equals(retParams.get("return_code"), "SUCCESS")){
				String retSign = retParams.remove("sign");
				String retMysign = WeiXinMicroPayUtil.getSign(retParams, WeiXinPayConfigure.MCH_KEY);
				if(!StringUtils.equals(retSign, retMysign)){
					dbLogger.error(order.getTradeno() + " sign check error:" + response);
					return ResultCode.getFailure("sign check error");
				}
				if(StringUtils.equals(retParams.get("result_code"), "SUCCESS")){
					return ResultCode.SUCCESS;
				}
				dbLogger.warn(order.getTradeno() + "err_code:" + retParams.get("err_code") + "; err_code_des:" + retParams.get("err_code_des"));
				return ResultCode.getFailure("err_code:" + retParams.get("err_code") + "; err_code_des:" + retParams.get("err_code_des"));
			}else{
				dbLogger.warn(order.getTradeno() + ": return_code:" + retParams.get("return_code") + "; return_msg:" + retParams.get("return_msg"));
				return ResultCode.getFailure("return_code:" + retParams.get("return_code") + "; return_msg:" + retParams.get("return_msg"));
			}
		}else{
			dbLogger.warn(order.getTradeno() + " submitBody error:" + httpResult.getMsg());
		}
		return ResultCode.getFailure("http msg:" +httpResult.getMsg());
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

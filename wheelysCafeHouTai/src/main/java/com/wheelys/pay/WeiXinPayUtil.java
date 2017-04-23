package com.wheelys.pay;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.wheelys.support.ErrorCode;
import com.wheelys.util.WheelysLogger;
import com.wheelys.util.StringUtil;
import com.wheelys.util.WebLogger;
import com.wheelys.model.pay.WheelysOrder;
import com.wheelys.pay.config.WeiXinPayConfigure;
import com.wheelys.util.WebUtils;

public class WeiXinPayUtil {
	
	private static final transient WheelysLogger dbLogger = WebLogger.getLogger(WeiXinPayUtil.class);

	public final static Map<String, SSLConnectionSocketFactory> sslConnfactorys = new HashMap<String, SSLConnectionSocketFactory>();
	
	/**
	 * 
	 * @param order
	 * @param refundamount
	 * @param refundno
	 * @return
	 */
	public static ErrorCode<Map<String, String>> refund(WheelysOrder order, Integer refundamount, String refundno) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", WeiXinPayConfigure.APPID);
		params.put("mch_id", WeiXinPayConfigure.MCH_ID);
		params.put("nonce_str", StringUtil.getRandomString(32));
		params.put("transaction_id", order.getPayseqno());
		params.put("out_trade_no", order.getTradeno());
		params.put("out_refund_no", refundno);
		params.put("total_fee", String.valueOf(order.getNetpaid()));
		params.put("refund_fee", String.valueOf(refundamount));
		params.put("op_user_id", WeiXinPayConfigure.MCH_ID);
		String sign = getSign(params, WeiXinPayConfigure.MCH_KEY);
		params.put("sign", sign);
		String postBody = toXml(params);
		ErrorCode<String> code = postBodyAsStringWithCert(WeiXinPayConfigure.REFUNDURL, postBody, WeiXinPayConfigure.MCH_ID);
		if (!code.isSuccess()) {
			dbLogger.error("wx refund error,errmsg=" + code.getMsg());
			return ErrorCode.getFailure(code.getMsg());
		}
	
		Map<String, String> retParams = xml2Map(code.getRetval());
		if (StringUtils.equals(retParams.get("return_code"), "SUCCESS")) {
			String returnSign = retParams.remove("sign");
			String myCheckSign = getSign(retParams, WeiXinPayConfigure.MCH_KEY);
			if (StringUtils.equals(returnSign, myCheckSign)) {
				return ErrorCode.getSuccessReturn(retParams);
			} else {
				dbLogger.error("wx refund checkSign error,response=" + code.getRetval());
				return ErrorCode.getFailure("checkSign error");
			}
		} else {
			dbLogger.error("wx refund fail,return_code=" + retParams.get("return_code") + ",return_msg=" + retParams.get("return_msg"));
			return ErrorCode.getFailure(retParams.get("return_code"), retParams.get("return_msg"));
		}
	}
	
	public static ErrorCode<String> postBodyAsStringWithCert(String url, String postBody, String merchantNo) {
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		ErrorCode<String> code = null;
		try {
			String charset = "UTF-8";
			httpclient = HttpClients.custom().setSSLSocketFactory(sslConnfactorys.get(merchantNo)).build();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new StringEntity(postBody, charset));
			response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String responseText = EntityUtils.toString(entity, charset);
			code = ErrorCode.getSuccessReturn(URLDecoder.decode(responseText, charset));
		} catch (Exception e) {
			dbLogger.error("wx refund request error,errmsg=" + e.getMessage());
			code = ErrorCode.getFailure(e.getMessage());
		} finally {
			if(httpclient!=null)try {httpclient.close();} catch (IOException e) {}
			if(response!=null)try {response.close();} catch (IOException e) {}
		}
		return code;
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
	
}


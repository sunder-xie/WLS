package com.wheelys.pay;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.support.ErrorCode;
import com.wheelys.util.DateUtil;
import com.wheelys.util.WheelysLogger;
import com.wheelys.util.HttpResult;
import com.wheelys.util.HttpUtils;
import com.wheelys.util.JsonUtils;
import com.wheelys.util.WebLogger;
import com.wheelys.model.merchant.MchOrder;
import com.wheelys.model.pay.WheelysOrder;
import com.wheelys.pay.config.AliPayConfigure;
import com.wheelys.util.VmUtils;
import com.wheelys.util.WebUtils;

public class AliWapPayUtil {
	
	private static final transient WheelysLogger dbLogger = WebLogger.getLogger(AliWapPayUtil.class);
	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";
	public static final String CHARSET = "utf-8";
	
	public static ErrorCode<Map<String, String>> refund(WheelysOrder order, Integer refundamount, String refundno) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("app_id", AliPayConfigure.APPID);
		params.put("method", "alipay.trade.refund");
		params.put("format", "JSON");
		params.put("charset", CHARSET);
		params.put("sign_type", "RSA");
		params.put("timestamp", DateUtil.format(DateUtil.getMillTimestamp(), "yyyy-MM-dd HH:mm:ss"));
		params.put("version", "1.0");
		Map<String, String> bizMap = new HashMap<String, String>();
		bizMap.put("out_trade_no", order.getTradeno());	//wheelys 订单号
		bizMap.put("refund_amount", VmUtils.getAmount(refundamount));	//退款金额
		bizMap.put("out_request_no", refundno);	//退款金额
		params.put("biz_content", JsonUtils.writeMapToJson(bizMap));
		String sign = getSign(params, AliPayConfigure.PRIVATEKEY);
		params.put("sign", sign);
		HttpResult result = HttpUtils.postUrlAsString(AliPayConfigure.GATEWAY, params);
		if(result.isSuccess()){
			Map respMap = JsonUtils.readJsonToMap(result.getResponse());
			String retsign = respMap.get("sign")+"";
			Map retParams = (Map) respMap.get("alipay_trade_refund_response");
			if(!checkRsaSign(JsonUtils.writeMapToJson(retParams), retsign, AliPayConfigure.PUBLICKEY, CHARSET)){
				dbLogger.error("trade_no" + order.getId() + ",验签错误：" + JsonUtils.writeMapToJson(retParams));
				return ErrorCode.getFailure("验签错误："+result.getResponse());
			}
			String code = retParams.get("code")+"";
			String msg = retParams.get("msg")+"";
			if(StringUtils.equals(code, "10000") && StringUtils.equals(msg, "Success")){
				return ErrorCode.getSuccessReturn(retParams);
			}else{
				dbLogger.error("alipay refund checkSign error,response=" + result.getResponse());
				return ErrorCode.getFailure("checkSign error");
			}
		}else{
			dbLogger.error("alipay refund error,errmsg=" + result.getMsg());
			return ErrorCode.getFailure(result.getMsg());
		}
		
	}
	
	public static ResultCode<Map<String, String>> queryPayInfo(String trade_no) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("app_id", AliPayConfigure.APPID);
		params.put("method", AliPayConfigure.QUERYURL);
		params.put("charset", CHARSET);
		params.put("sign_type", "RSA");
		params.put("timestamp", DateUtil.format(DateUtil.getMillTimestamp(), "yyyy-MM-dd HH:mm:ss"));
		params.put("version", "1.0");
		Map<String, String> bizMap = new HashMap<String, String>();
		//bizMap.put("trade_no", trade_no); 支付宝订单号
		bizMap.put("out_trade_no", trade_no);	//wheelys 订单号
		params.put("biz_content", JsonUtils.writeMapToJson(bizMap));
		String sign = getSign(params, AliPayConfigure.PRIVATEKEY);
		params.put("sign", sign);
		HttpResult result = HttpUtils.postUrlAsString(AliPayConfigure.GATEWAY, params);
		if(result.isSuccess()){
			Map respMap = JsonUtils.readJsonToMap(result.getResponse());
			String retsign = respMap.get("sign")+"";
			Map retParams = (Map) respMap.get("alipay_trade_query_response");
			if(!checkRsaSign(JsonUtils.writeMapToJson(retParams), retsign, AliPayConfigure.PUBLICKEY, CHARSET)){
				dbLogger.warn("trade_no" + trade_no + "查询失败：" + JsonUtils.writeMapToJson(retParams));
				return ResultCode.getFailure("验签错误："+result.getResponse());
			}
			String code = retParams.get("code")+"";
			String msg = retParams.get("msg")+"";
			String status = retParams.get("trade_status")+"";
			if(StringUtils.equals(code, "10000") && StringUtils.equals(msg, "Success") 
				&& StringUtils.equals(status, "TRADE_SUCCESS")){
				Map<String, String> map = new HashMap<String, String>();
				map.put("tradeno", retParams.get("out_trade_no")+"");
				map.put("payseqno", retParams.get("trade_no")+"");
				map.put("paidamount", retParams.get("total_amount")+"");
				map.put("ukey", retParams.get("open_id")+"");
				map.put("otherinfo", JsonUtils.writeMapToJson(retParams));
				return ResultCode.getSuccessReturn(map);
			}
		}
		dbLogger.warn("queryPayInfo error:"+result.getResponse());
		return ResultCode.getFailure("反查失败！trade_no:"+trade_no);
	}
	
	public static void main(String[] args) {
		String trade_no = "A161212200401204";
		ResultCode<Map<String, String>> resultCode = queryPayInfo(trade_no);
		System.out.println(JsonUtils.writeObjectToJson(resultCode));
		/*MchOrder order = new MchOrder();
		order.setCreatetime(DateUtil.getMillTimestamp());
		order.setTradeno("A161212200401204");
		order.setPayfee(100);
		order.setShopid(6L);
		System.out.println(getPrecreatePayUrl(order, "http://www.wheelyschina.com/wheelyscafe/"));*/
	}
	
	public static String getPrecreatePayUrl(MchOrder order, String absPath){
		Map<String, String> payParams = new HashMap<String, String>();
		payParams.put("app_id", AliPayConfigure.APPID);
		payParams.put("method", AliPayConfigure.PRECREATEURL);
		payParams.put("charset", CHARSET);
		payParams.put("sign_type", "RSA");
		payParams.put("timestamp", DateUtil.format(order.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
		payParams.put("version", "1.0");
		payParams.put("notify_url", absPath+AliPayConfigure.NOTIFYURL);
		Map<String, String> bizMap = new HashMap<String, String>();
		bizMap.put("subject", "wheelys咖啡");
		bizMap.put("out_trade_no", order.getTradeno());
		bizMap.put("total_amount", VmUtils.getAmount(order.getPayfee()));
		bizMap.put("store_id", order.getShopid()+"");
		payParams.put("biz_content", JsonUtils.writeMapToJson(bizMap));
		String sign = getSign(payParams, AliPayConfigure.PRIVATEKEY);
		payParams.put("sign", sign);
		HttpResult result = HttpUtils.postUrlAsString(AliPayConfigure.GATEWAY, payParams);
		System.out.println(result.getResponse());
		Map respMap = JsonUtils.readJsonToMap(result.getResponse());
		Map precreateMap = (Map) respMap.get("alipay_trade_precreate_response");
		String precreatePayUrl = precreateMap.get("qr_code")+"";
		if(StringUtils.isBlank(precreatePayUrl)){
			dbLogger.warn("trade_no" + order.getTradeno() + "getPrecreatePayUrl：" + result.getResponse());
		}
		return precreatePayUrl;
	}
	
	public static boolean checkRsaSign(Map retParams, String sign){
		retParams.remove("sign");
		retParams.remove("sign_type");
		String content = WebUtils.joinParams(retParams,true);
		return checkRsaSign(content, sign, AliPayConfigure.PUBLICKEY, CHARSET);
	}

	private static String getSign(Map<String, String> params, String partnerKey) {
		String content = WebUtils.joinParams(params, true);
		return rsaSign(content, partnerKey, CHARSET);
	}

	private static boolean checkRsaSign(String content, String sign, String pubKey, String encoding) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decodeBase64(pubKey);
			PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
			signature.initVerify(publicKey);
			signature.update(content.getBytes(encoding));
			return signature.verify(Base64.decodeBase64(sign));
		} catch (Exception e) {
			dbLogger.warn("校验签名出错", e);
		}
		return false;
	}
	

	private static String rsaSign(String content, String privateKey, String encoding) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = privateKey.getBytes(encoding);
			encodedKey = Base64.decodeBase64(encodedKey);
			PrivateKey priKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
			Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
			signature.initSign(priKey);
			signature.update(content.getBytes(encoding));
			byte[] signed = signature.sign();
			return new String(Base64.encodeBase64(signed));
		} catch (Exception e) {
			dbLogger.error("", e);
			return "";
		}
	}
	
}

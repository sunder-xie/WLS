package com.wheelys.util;

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
import com.wheelys.util.BeanUtil;
import com.wheelys.util.DateUtil;
import com.wheelys.util.WheelysLogger;
import com.wheelys.util.HttpResult;
import com.wheelys.util.HttpUtils;
import com.wheelys.util.JsonUtils;
import com.wheelys.util.WebLogger;
import com.wheelys.json.AliPayUserVo;
import com.wheelys.pay.config.AliPayConfigure;

public class AliOAuthUtils {
	private static final transient WheelysLogger dbLogger = WebLogger.getLogger(AliOAuthUtils.class);

	public static final String CHARSET = "utf-8";
	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";
	private static final String AUTHORIZATION_CODE = "authorization_code";
	private static final String RSA = "RSA";
	private static final String OAUTH_TOKEN = "alipay.system.oauth.token";
	//private static final String INFO_SHARE = "alipay.user.info.share";
	public static final String LOGIN_URI = "dynamicCodeLogin.xhtml";

	/**
	 * @param code
	 *            授权成功支付宝返回auth_code
	 * @return 获取access_token
	 */
	private static ResultCode<Map<String, String>> getAccessTokenByCode(String code) {
		Map<String, String> params = new HashMap<>();
		params.put("code", code);
		params.put("app_id", AliPayConfigure.APPID);
		params.put("charset", CHARSET);
		params.put("sign_type", RSA);
		params.put("grant_type", AUTHORIZATION_CODE);
		params.put("method", OAUTH_TOKEN);
		params.put("timestamp", DateUtil.format(DateUtil.getMillTimestamp(), "yyyy-MM-dd HH:mm:ss"));
		params.put("version", "1.0");
		String sign = getSign(params, AliPayConfigure.PRIVATEKEY);
		params.put("sign", sign);

		HttpResult result = HttpUtils.postUrlAsString(AliPayConfigure.GATEWAY, params);
		String response = result.getResponse();

		if (!result.isSuccess()) {
			dbLogger.warn(result.getResponse() + "," + result.getMsg());
			return ResultCode.getFailure(result.getMsg());
		}
		Map<String, Object> map = JsonUtils.readJsonToMap(response);
		Map userInfoMap = (Map) map.get("alipay_system_oauth_token_response");
		
		if (userInfoMap == null || userInfoMap.size() <= 0) {
			dbLogger.warn("获取令牌失败 : " + result.getResponse() + "," + result.getMsg());
			userInfoMap = new HashMap();
		}
		if (!checkRsaSign(JsonUtils.writeMapToJson(userInfoMap).replace("/", "\\/"), map.get("sign") + "",
				AliPayConfigure.PUBLICKEY, CHARSET)) {
			// 验签不过
			dbLogger.warn(result.getResponse() + "," + result.getMsg());
			return ResultCode.getFailure("签名验证不通过" + result.getMsg());
		}
		if (map.containsKey("error_response")) {
			return ResultCode.getFailure(map.get("error_response") + "");
		}
		return ResultCode.getSuccessReturn(BeanUtil.getSimpleStringMap(map));
	}

	/**
	 * @param code
	 *            授权成功支付宝返回auth_code
	 * @return 支付宝返回用户信息封装对象
	 */
	public static ResultCode<AliPayUserVo> getUserInfoByCode(String code) {
		ResultCode<Map<String, String>> resultCode = getAccessTokenByCode(code);
		if (!resultCode.isSuccess()) {
			return ResultCode.getFailure(resultCode.getMsg());
		}
		Map<String, String> params = resultCode.getRetval();

		String param = params.get("alipay_system_oauth_token_response");
		
		//Map map = JsonUtils.readJsonToMap(JsonUtils.writeObjectToJson(param));
		
		if (param != null && StringUtils.isNotEmpty(param) && param.length() > 2)
			param = param.substring(1, param.length() - 1);
//		String access_token = "";
		String user_id = "";
		if (param != null && StringUtils.isNotEmpty(param)) {
			String[] paramsArr = StringUtils.split(param, ",");
			for (int i = 0; i < paramsArr.length; i++) {
//				if (paramsArr[i].trim().startsWith("access_token")) {
//					access_token = StringUtils.split(paramsArr[i], "=")[1];
//				}
				if (paramsArr[i].trim().startsWith("user_id")) {
					user_id = StringUtils.split(paramsArr[i], "=")[1];
				}
			}
		}
//		Map<String, String> requestParam = new HashMap<>();
//		requestParam.put("sign_type", RSA);
//		requestParam.put("method", INFO_SHARE);
//		requestParam.put("app_id", AliPayConfigure.APPID);
//		requestParam.put("timestamp", DateUtil.format(DateUtil.getMillTimestamp(), "yyyy-MM-dd HH:mm:ss"));
//		requestParam.put("version", "1.0");
//		requestParam.put("charset", CHARSET);
//		requestParam.put("auth_token", access_token);
//		Map<String, String> bizMap = new HashMap<String, String>();
//		bizMap.put("user_id", user_id);
//		requestParam.put("biz_content", JsonUtils.writeMapToJson(bizMap));
//
//		String sign = getSign(requestParam, AliPayConfigure.PRIVATEKEY);
//		requestParam.put("sign", sign);
//		
//		HttpResult result = HttpUtils.postUrlAsString(AliPayConfigure.GATEWAY, requestParam);
//		String response = result.getResponse();
//
//		if (!result.isSuccess()) {
//			dbLogger.warn(result.getResponse() + "," + result.getMsg());
//			return ResultCode.getFailure(result.getMsg());
//		}
//
//		Map resp = JsonUtils.readJsonToMap(response);
//		Map userInfoMap = (Map) resp.get("alipay_user_info_share_response");
//		
//		if (userInfoMap == null || userInfoMap.size() <= 0) {
//			dbLogger.warn("获取头像和昵称失败 : " + result.getResponse() + "," + result.getMsg());
//			userInfoMap = new HashMap();
//		}
//		if (!checkRsaSign(JsonUtils.writeMapToJson(userInfoMap).replace("/", "\\/"), resp.get("sign") + "",
//				AliPayConfigure.PUBLICKEY, CHARSET)) {
//			// 验签不过
//			dbLogger.warn("获取头像和昵称失败 : " + result.getResponse() + "," + result.getMsg());
//		}
//
//		if (!StringUtils.equals("10000", userInfoMap.get("code") + "")) {
//			dbLogger.warn(result.getResponse() + "," + result.getMsg());
//			return ResultCode.getFailure(result.getResponse() + "," + result.getMsg() + "," + userInfoMap.get("msg") + "");
//		}

		AliPayUserVo user = new AliPayUserVo();
		user.setUser_id(user_id);
		return ResultCode.getSuccessReturn(user);
	}
	private static String getSign(Map<String, String> params, String partnerKey) {
		String content = WebUtils.joinParams(params, true);
		return rsaSign(content, partnerKey, CHARSET);
	}

	/**
	 * 生成签名
	 * 
	 * @param content
	 * @param privateKey
	 * @param encoding
	 * @return
	 */
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

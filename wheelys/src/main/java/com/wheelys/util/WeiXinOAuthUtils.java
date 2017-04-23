package com.wheelys.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.json.WeixinUserVo;
import com.wheelys.pay.config.WeiXinPayConfigure;

public class WeiXinOAuthUtils {
	
	private static final transient WheelysLogger dbLogger = WebLogger.getLogger(WeiXinOAuthUtils.class);
	
	private static final String ACCESSTOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
	private final static String GETTOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
	private final static String USERINFO_URL_APP = "https://api.weixin.qq.com/sns/userinfo";
	private final static String USERINFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info";
    
    private static ResultCode<Map<String, String>> getAccessTokenByCode(String code) {
		Map<String, String> params = new HashMap<>();
		params.put("grant_type", "authorization_code");
		params.put("appid", WeiXinPayConfigure.APPID);
		params.put("secret", WeiXinPayConfigure.APPSECRET);
		params.put("code", code);
		HttpResult result = HttpUtils.getUrlAsString(ACCESSTOKEN_URL, params);
		String response = result.getResponse();
		if(!result.isSuccess()){
			dbLogger.warn(result.getResponse() + "," + result.getMsg());
			return ResultCode.getFailure(result.getMsg());
		}
		Map<String, Object> map = JsonUtils.readJsonToMap(response);
		if(map.containsKey("errmsg")){
			return ResultCode.getFailure(map.get("errmsg")+"");
		}
		return ResultCode.getSuccessReturn(BeanUtil.getSimpleStringMap(map));
	}
	
	public static ResultCode<WeixinUserVo> getUserInfoByCode(String code){
		ResultCode<Map<String, String>> resultCode = WeiXinOAuthUtils.getAccessTokenByCode(code);
		if(!resultCode.isSuccess()){
			return ResultCode.getFailure(resultCode.getMsg());
		}
		Map<String, String> params = resultCode.getRetval();
		String unionid = params.get("unionid"); 
		String scope = params.get("scope");
		if (StringUtils.equals("snsapi_userinfo", scope)) {
			HttpResult result = HttpUtils.getUrlAsString(USERINFO_URL_APP, params);
			String response = result.getResponse();
			Map<String, Object> map = JsonUtils.readJsonToMap(response);
			if(map.containsKey("errmsg")){
				dbLogger.warn(result.getResponse() + "," + result.getMsg());
				return ResultCode.getFailure(map.get("errmsg")+"");
			}
			WeixinUserVo user = JsonUtils.readJsonToObject(WeixinUserVo.class, response);
			if (user == null) {
				dbLogger.warn("getUserInfoByCode_response_userisnull:" + result.getResponse());
			}
			if (user!=null && StringUtils.isNotBlank(unionid) && StringUtils.isBlank(user.getUnionid())) {
				user.setUnionid(unionid);
			}
			return ResultCode.getSuccessReturn(user);
		} else {
			String openid = params.get("openid");
			ResultCode<WeixinUserVo> weixinUserVo = WeiXinOAuthUtils.getUserInfoOpenid(openid);
			return weixinUserVo;
		}
	}

	public static ResultCode<WeixinUserVo> getUserInfoOpenid(String openid) {
		ResultCode<String> resCode = getBaseToken();
		if(!resCode.isSuccess()){
			return ResultCode.getFailure(resCode.getMsg());
		}
		Map<String, String> params = new HashMap<>();
		params.put("access_token", resCode.getRetval());
		params.put("openid", openid);
		HttpResult result = HttpUtils.getUrlAsString(USERINFO_URL, params);
		String response = result.getResponse();
		Map<String, Object> map = JsonUtils.readJsonToMap(response);
		if(map.containsKey("errmsg")){
			dbLogger.warn(params+ "," + result.getResponse() + "," + result.getMsg());
			return ResultCode.getFailure(map.get("errmsg")+"");
		}
		WeixinUserVo user = JsonUtils.readJsonToObject(WeixinUserVo.class, response);
		return ResultCode.getSuccessReturn(user);
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

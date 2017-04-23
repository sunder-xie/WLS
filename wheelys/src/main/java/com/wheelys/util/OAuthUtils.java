package com.wheelys.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.wheelys.pay.config.AliPayConfigure;
import com.wheelys.pay.config.WeiXinPayConfigure;

public class OAuthUtils {
	
	private static final String WEIXIN_MOBILE_OAUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";
	private static final String WEIXIN_REDIRECT_URI = "auth/weixin.xhtml";
    private static final String LOGIN_URI = "dynamicCodeLogin.xhtml";

	private static final String REDIRECT_URI = "auth/alipay.xhtml";
	private static final String PUBLIC_APP_AUTHORIZE_URL = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm";
	private static final String SCOPE = "auth_base";//取值:auth_base(唯一不需要授权,只获取userid)、 auth_user、auth_zhima、auth_ecard

    
    public static String oauth2Url(HttpServletRequest request, String lastpage, String absPath) {
    	String ua = request.getHeader("User-Agent");
        if(StringUtils.isBlank(ua)){
        	return "";
        }
        if (StringUtils.isBlank(lastpage)) {
        	lastpage = request.getHeader("Referer");
        }
        String authUrl = "";
        if (ua.contains("MicroMessenger")) {
        	authUrl = WEIXIN_MOBILE_OAUTH_URL +
                    "?appid=" + WeiXinPayConfigure.APPID +
                    "&state=addorder" + 
                    "&response_type=code&scope=snsapi_base" +
                    "&redirect_uri=" + VmUtils.encodeStr(absPath+WEIXIN_REDIRECT_URI + "?lastpage=" + lastpage, "utf-8");
        } else if (ua.contains("AliApp")) {
			authUrl = PUBLIC_APP_AUTHORIZE_URL + "?app_id=" + AliPayConfigure.APPID + "&scope=" + SCOPE
					+ "&redirect_uri=" + VmUtils.encodeStr(absPath + REDIRECT_URI + "?lastpage=" + lastpage, "utf-8");
        }else{
        	authUrl = absPath+LOGIN_URI + "?TARGETURL=" + lastpage;
        }
        return authUrl;
    }
    
}

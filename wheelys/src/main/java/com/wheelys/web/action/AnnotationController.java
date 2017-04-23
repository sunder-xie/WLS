package com.wheelys.web.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.support.ErrorCode;
import com.wheelys.support.GewaCaptchaService;
import com.wheelys.util.WheelysLogger;
import com.wheelys.util.JsonUtils;
import com.wheelys.util.WebLogger;
import com.wheelys.web.util.LoginUtils;
import com.wheelys.web.util.ReqLogUtil;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.WheelysLoginService;
import com.wheelys.util.WebUtils;
import com.wheelys.web.support.token.MemberEncodeAuthenticationToken;

public abstract class AnnotationController {

	protected final transient WheelysLogger dbLogger = WebLogger.getLogger(getClass());

	@Autowired
	@Qualifier("wheelysLoginService")
	private WheelysLoginService wheelysLoginService;
	@Autowired
	@Qualifier("captchaService")
	private GewaCaptchaService captchaService;
	
	protected HttpServletRequest getRequest() {
		ServletRequestAttributes holder = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (holder != null) {
			HttpServletRequest request = holder.getRequest();
			if (request != null) {
				return request;
			}
		}
		return null;
	}

	protected HttpServletResponse getResponse() {
		ServletRequestAttributes holder = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (holder != null) {
			HttpServletResponse response = holder.getResponse();
			if (response != null) {
				return response;
			}
		}
		return null;
	}
	
	protected String showWapMsg(ModelMap model, String msg){
		model.put("msg", msg);
		return "wapTip.vm";
	}
	
	protected final String showRedirect(String path, ModelMap model){
		String redirect = path;
		if(StringUtils.startsWith(path, "/")) redirect = path.substring(1);
		StringBuilder targetUrl = new StringBuilder(redirect);
		if(!StringUtils.startsWith(path, "http")){
			WebUtils.appendQueryProperties(targetUrl, model, "utf-8");
		}
		model.put("redirectUrl", targetUrl.toString());
		return "tempRedirect.vm";
	}

	protected final String showJsonSuccess(ModelMap model, Object data) {
		Map dataMap = new HashMap();
		dataMap.put("success", true);
		dataMap.put("data", data);
		model.put("retuslt", JsonUtils.writeMapToJson(dataMap));
		return "common/json.vm";
	}

	protected final String showJsonError(ModelMap model, String msg){
		Map jsonMap = new HashMap();
		jsonMap.put("success", false);
		jsonMap.put("msg", msg);
		model.put("retuslt", JsonUtils.writeMapToJson(jsonMap));
		return "common/json.vm";
	}

	protected final WheelysMember getLogonMember() {
		WheelysMember member = null;//getLogonMemberFromAuth();
		//if (member == null) {
			HttpServletRequest request = getRequest();
			String ip = WebUtils.getRemoteIp(request);
			String sessid = LoginUtils.getSessid(request);
			ResultCode<MemberEncodeAuthenticationToken> result = wheelysLoginService.getLogonMemberByMemberEncodeAndIp(sessid, ip);
			if (result.isSuccess()) {
				member = result.getRetval().getMember();
			}
		//}
		return member;
	}

	/*private final WheelysMember getLogonMemberFromAuth() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null)
			return null;
		if (auth.isAuthenticated() && auth instanceof MemberEncodeAuthenticationToken) {// 登录
			return ((MemberEncodeAuthenticationToken) auth).getMember();
		}
		return null;
	}*/

	public void setLoginKey2Cookie(HttpServletRequest request, HttpServletResponse response, MemberEncodeAuthenticationToken token) {
		try {
			int duration =  60 * 60 * 24 * 30;
			/*if(StringUtils.isNotBlank(request.getParameter("rememberMe")) ){
				duration = 60 * 60 * 24 * 30;
			}*/
			setLogonSessid(token.getMemberEncode(),response, duration);
			LoginUtils.setLogonTrace(token.getMember().getId(), request, response);
		} catch (Exception e) {
			dbLogger.warn(e, 20);
		}
	}

	private void setLogonSessid(String sessid, HttpServletResponse response, int duration){
		Cookie cookie = new Cookie(LoginUtils.SESS_COOKIE_NAME, sessid);
		cookie.setMaxAge(duration);
		cookie.setPath("/");
		cookie.setSecure(false);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}
	
	protected boolean validateCaptcha(String captchaId, String captcha, String ip) {
		boolean validCaptcha = false;
		if (StringUtils.isNotBlank(captcha)) {
			captcha = StringUtils.lowerCase(captcha);
			ErrorCode<Map<String, String>> code = captchaService.validateResponseForID(captchaId, captcha, ip);
			if (code.isSuccess()) {
				validCaptcha = true;
			} else {
				ReqLogUtil.addLogInfo(code.getRetval());
			}
		}
		return validCaptcha;
	}
}

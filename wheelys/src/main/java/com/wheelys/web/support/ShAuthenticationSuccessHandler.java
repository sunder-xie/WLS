package com.wheelys.web.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;

import com.wheelys.util.BaseWebUtils;
import com.wheelys.util.BeanUtil;
import com.wheelys.web.support.GewaAuthenticationSuccessHandler;
import com.wheelys.web.util.LoginUtils;
import com.wheelys.web.support.token.MemberEncodeAuthenticationToken;

public class ShAuthenticationSuccessHandler extends GewaAuthenticationSuccessHandler {
	@Override
	public String saveAuthentication(String ip, int duration, boolean rememberMe, Authentication authentication){
		if(authentication instanceof MemberEncodeAuthenticationToken){
			return ((MemberEncodeAuthenticationToken)authentication).getMemberEncode();
		}else{
			return super.saveAuthentication(ip, duration, rememberMe, authentication);
		}
	}
	/**
	 * 认证成功后处理cookie、cache
	 * @param request
	 * @param response
	 * @param authentication
	 */
	@Override
	public void processSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		if(authentication.isAuthenticated()){
			String ip = BaseWebUtils.getRemoteIp(request);
			boolean rememberMe = StringUtils.isNotBlank(request.getParameter(REMEMBERME_PARAM));
			int duration = defaultDuration;
			if(enableRememberMe && rememberMe){
				duration = rememberMeSeconds;
			}
			
			String sessid = saveAuthentication(ip, duration, rememberMe, authentication);
			
			setLogonSessid(sessid, response, duration);
			Object user = authentication.getPrincipal();
			LoginUtils.setLogonTrace((Long) BeanUtil.get(user, "id"), request, response);
		}
	}
}
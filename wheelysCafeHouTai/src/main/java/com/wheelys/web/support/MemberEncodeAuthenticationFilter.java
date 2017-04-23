package com.wheelys.web.support;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.support.ErrorCode;
import com.wheelys.support.GewaCaptchaService;
import com.wheelys.util.BaseWebUtils;
import com.wheelys.web.util.LoginUtils;
import com.wheelys.web.util.ReqLogUtil;
import com.wheelys.service.WheelysLoginService;
import com.wheelys.util.WebUtils;
import com.wheelys.web.support.token.MemberEncodeAuthenticationToken;

public class MemberEncodeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private String logonTypeParamName = "ptn";
	private boolean enableCaptcha = true;

	@Autowired@Qualifier("wheelysLoginService")
	private WheelysLoginService wheelysLoginService;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		if (enableCaptcha) {
			boolean validCaptcha = validateCaptcha(request.getParameter("captchaId"), request.getParameter("captcha"),
					BaseWebUtils.getRemoteIp(request));
			if (!validCaptcha){
				throw new InvalidCookieException("验证码不正确！");
			}
		}
		String ptn = request.getParameter(logonTypeParamName);
		if (StringUtils.isBlank(ptn)){
			ptn = "member";
		}
		if (!request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		String username = obtainUsername(request);
		String password = obtainPassword(request);
		
		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}

		username = username.trim();
		String remoteIp = WebUtils.getRemoteIp(request);
		ResultCode<MemberEncodeAuthenticationToken> result = wheelysLoginService.doLogin4MemberEncodeAndSave(username, password, remoteIp);
		if(result.isSuccess()){
			return result.getRetval();
		}else{
			if(StringUtils.equals(LoginUtils.ERROR_REJECTED, result.getErrcode())){
				throw new DisabledException(result.getMsg());
			}else if(StringUtils.equals(LoginUtils.ERROR_PASSORNAME, result.getErrcode()) || 
					StringUtils.equals(LoginUtils.ERROR_PASSWORD, result.getErrcode())){
				throw new BadCredentialsException(result.getMsg());
			}
			throw new AuthenticationServiceException(result.getMsg());
		}
	}

	protected boolean validateCaptcha(String captchaId, String captcha, String ip) {
		boolean validCaptcha = false;
		if (StringUtils.isNotBlank(captcha)) {
			String lowerCaseCaptcha = StringUtils.lowerCase(captcha);
			WebApplicationContext ctx = WebApplicationContextUtils
					.getRequiredWebApplicationContext(this.getServletContext());
			GewaCaptchaService captchaService = ctx.getBean(GewaCaptchaService.class);
			ErrorCode<Map<String, String>> code = captchaService.validateResponseForID(captchaId, lowerCaseCaptcha, ip);
			if(code.isSuccess()){
				validCaptcha = true;
			}else{
				ReqLogUtil.addLogInfo(code.getRetval());
			}
		}
		return validCaptcha;
	}

	public void setEnableCaptcha(boolean enableCaptcha) {
		this.enableCaptcha = enableCaptcha;
	}
}

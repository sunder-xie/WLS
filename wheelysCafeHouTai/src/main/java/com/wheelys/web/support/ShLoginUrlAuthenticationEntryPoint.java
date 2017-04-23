package com.wheelys.web.support;

import javax.servlet.http.HttpServletRequest;

import com.wheelys.web.support.GewaLoginUrlAuthenticationEntryPoint;

public class ShLoginUrlAuthenticationEntryPoint extends GewaLoginUrlAuthenticationEntryPoint{
	public ShLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
	}

	@Override
	protected boolean isEnableSSO(HttpServletRequest request){
		return enableSSO && request.getRequestURI().startsWith("/admin/");
	}
}

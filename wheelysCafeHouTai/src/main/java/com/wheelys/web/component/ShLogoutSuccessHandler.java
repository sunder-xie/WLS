package com.wheelys.web.component;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;

import com.wheelys.util.BaseWebUtils;
import com.wheelys.web.support.GewaUrlLogoutSuccessHandler;
import com.wheelys.web.util.LoginUtils;
import com.wheelys.service.WheelysLoginService;

public class ShLogoutSuccessHandler extends GewaUrlLogoutSuccessHandler{
	
	@Autowired@Qualifier("wheelysLoginService")
	private WheelysLoginService wheelysLoginService;
	
	@Override
	public void doLogout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException{
		String ukey = BaseWebUtils.getCookieValue(request, LoginUtils.SESS_COOKIE_NAME);
		String ip = BaseWebUtils.getRemoteIp(request);
		if(LoginUtils.isValidSessid(ukey)){
			wheelysLoginService.doLogout(ukey, ip);
		}
		LoginUtils.removeLogonUkey(response);
		super.handle(request, response, authentication);
	}
}

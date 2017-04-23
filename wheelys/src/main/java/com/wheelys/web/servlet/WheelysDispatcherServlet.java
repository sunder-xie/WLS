package com.wheelys.web.servlet;

import com.wheelys.Config;
import com.wheelys.web.support.GewaDispatcherServlet;

public class WheelysDispatcherServlet extends GewaDispatcherServlet{
	private static final long serialVersionUID = 2372494027032166134L;

	@Override
	public String getContextConfigLocation() {
		String ip = Config.getServerIp();
		if(Config.isPreEnv() || ip.startsWith("192.168.")){
			return super.getContextConfigLocation();
		}
		return "classpath:spring/action-servlet-base.xml,classpath:spring/action-servlet-web.xml";
	}

}

package com.wheelys.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.ContextLoaderListener;

import com.wheelys.Config;
import com.wheelys.util.WheelysLogger;
import com.wheelys.util.WebLogger;

public class StartupListener extends ContextLoaderListener implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		System.setProperty("dubbo.application.logger","slf4j");
		//根据环境加载配置文件
		String SPRING_CONFIG_KEY = "contextConfigLocation";
		String[] remoteConfig = new String[]{
				"classpath:spring/property.remote.xml",
				"classpath:spring/config.remote.xml",
				"classpath:spring/appContext-*.xml",
				"classpath:spring/redis.remote.xml",
				"classpath:spring/task.remote.xml"
		};
		String[] testConfig = new String[]{
				"classpath:spring/property.test.xml",
				"classpath:spring/config.test.xml",
				"classpath:spring/appContext-*.xml",
				"classpath:spring/redis.test.xml",
				"classpath:spring/task.local.xml"
		};
		
		String SPRING_CONFIG_VALUE = "";
		
		WheelysLogger logger = WebLogger.getLogger(StartupListener.class);
		if(StringUtils.startsWithIgnoreCase(Config.getHostname(), "iZuf6")){//其他机器
			System.setProperty(Config.SYSTEMID + "-GEWACONFIG", "REMOTE");
			SPRING_CONFIG_VALUE = StringUtils.join(remoteConfig, ",");
			logger.warn("Config Using REMOTE:" + SPRING_CONFIG_VALUE);
		}else{
			logger.warn("Config GEWACONFIG:"+ Config.getHostname()+",--" + StringUtils.startsWith(Config.getHostname(), "iZuf6"));
			System.setProperty(Config.SYSTEMID + "-GEWACONFIG", "LOCAL");
			if(Config.isTestEnv()){
				SPRING_CONFIG_VALUE = StringUtils.join(testConfig, ",");
			}else{
				SPRING_CONFIG_VALUE = StringUtils.join(testConfig, ",");
			}
		}
		ServletContext context = event.getServletContext();
		context.setInitParameter(SPRING_CONFIG_KEY, SPRING_CONFIG_VALUE);
		super.contextInitialized(event);
	}
	
}

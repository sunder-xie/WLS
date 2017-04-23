package com.wheelys.web.listener;

import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.ContextLoaderListener;

import com.wheelys.Config;
import com.wheelys.util.WheelysLogger;
import com.wheelys.util.WebLogger;
import com.wheelys.util.AppKeyUtil;

public class StartupListener extends ContextLoaderListener implements ServletContextListener {
	
	private List<String> HOSTLIST = Arrays.asList("iZuf6i4mxt5qb6dnf8htbmZ","iZuf635fwy8k6sqzyv151cZ","iZuf6c2qeenlv92ye1i55pZ","iZuf6g8dzrhn5akai3tjs7Z");
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		System.setProperty("dubbo.application.logger","slf4j");
		//根据环境加载配置文件
		String SPRING_CONFIG_KEY = "contextConfigLocation";
		String[] remoteConfig = new String[]{
				"classpath:spring/property.remote.xml",
				"classpath:spring/config.remote.xml",
				"classpath:spring/redis.remote.xml",
				"classpath:spring/appContext-*.xml",
				"classpath:spring/task.remote.xml"
		};
		String[] testConfig = new String[]{
				"classpath:spring/property.test.xml",
				"classpath:spring/config.test.xml",
				"classpath:spring/redis.test.xml",
				"classpath:spring/appContext-*.xml",
				"classpath:spring/task.local.xml"
		};
		
		String SPRING_CONFIG_VALUE = "";
		
		WheelysLogger logger = WebLogger.getLogger(StartupListener.class);
		
		String appkeyProperty = "com/wheelys/openapi.remote.properties";
		if(HOSTLIST.contains(Config.getHostname())){//其他机器
			System.setProperty(Config.SYSTEMID + "-WHEELYSCONFIG", "REMOTE");
			SPRING_CONFIG_VALUE = StringUtils.join(remoteConfig, ",");
			logger.warn("Config Using REMOTE:" + SPRING_CONFIG_VALUE);
		}else{
			System.setProperty(Config.SYSTEMID + "-WHEELYSCONFIG", "LOCAL");
			if(Config.isTestEnv()){
				SPRING_CONFIG_VALUE = StringUtils.join(testConfig, ",");
			}else{
				SPRING_CONFIG_VALUE = StringUtils.join(testConfig, ",");
			}
			appkeyProperty = "com/wheelys/openapi.test.properties";
		}
		logger.warn("AppKeyUtil Using :" + appkeyProperty);
		AppKeyUtil.init(appkeyProperty);
		ServletContext context = event.getServletContext();
		context.setInitParameter(SPRING_CONFIG_KEY, SPRING_CONFIG_VALUE);
		super.contextInitialized(event);
	}
}

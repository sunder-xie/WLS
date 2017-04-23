package com.wheelys.untrans.pay;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.wheelys.Config;
import com.wheelys.util.WheelysLogger;
import com.wheelys.util.WebLogger;

public abstract class AbstractPayService implements InitializingBean {

	protected final transient WheelysLogger dbLogger = WebLogger.getLogger(getClass());

	@Autowired@Qualifier("config")
	protected Config config;
	
	protected Map<String, String> configMap = new HashMap<String, String>();
	
}

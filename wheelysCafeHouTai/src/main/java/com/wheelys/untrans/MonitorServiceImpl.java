package com.wheelys.untrans;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.wheelys.untrans.monitor.MonitorEntry;
import com.wheelys.untrans.monitor.impl.AbstractMonitorService;

public class MonitorServiceImpl extends AbstractMonitorService implements InitializingBean{

	@Override
	public void addMonitorEntry(String datatype, Map<String, String> entry) {
		
	}

	@Override
	public void addMonitorEntry(MonitorEntry entry) {
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

}

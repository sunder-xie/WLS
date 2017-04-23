package com.wheelys.untrans;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wheelys.Config;
import com.wheelys.untrans.CacheConfigure;
import com.wheelys.util.JsonUtils;

@Service("cacheConfigureService")
public class CacheConfigureServiceImpl implements CacheConfigure {
	
	@Autowired@Qualifier("config")
	private Config config;

	@Override
	public Map<String, String> getRegionVersion() {
		return JsonUtils.readJsonToMap("");
	}

	@Override
	public Map<String, String> getServiceCachePre() {
		return JsonUtils.readJsonToMap("");
	}
}

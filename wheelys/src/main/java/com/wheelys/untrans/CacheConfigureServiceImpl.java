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
		String content = "{\"oneMin\":\"1v2\",\"tenMin\":\"10v2\",\"halfHour\":\"30v2\",\"oneHour\":\"60v2\",\"twoHour\":\"2hv2\",\"halfDay\":\"12hv2\",\"loginKey\":\"lkv2\",\"loginAuth\":\"lav2\"}";
		return JsonUtils.readJsonToMap(content);
	}

	@Override
	public Map<String, String> getServiceCachePre() {
		return JsonUtils.readJsonToMap("");
	}
}

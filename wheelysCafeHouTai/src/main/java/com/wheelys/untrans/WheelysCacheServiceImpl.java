package com.wheelys.untrans;

import java.util.Collection;
import java.util.Map;

import com.wheelys.support.CachePair;
import com.wheelys.untrans.impl.AbstractCacheService;
import com.wheelys.util.Gcache;

public class WheelysCacheServiceImpl extends AbstractCacheService {

	public final Gcache<String,Object> gcache = new Gcache<String,Object>(500);

	@Override
	public Map<String, Object> getBulk(String regionName, Collection<String> keys) {
		return null;
	}

	@Override
	public Object get(String regionName, String key) {
		return gcache.getIfPresent(key);
	}

	@Override
	public void set(String regionName, String key, Object value, int timeoutSecond) {
		gcache.put(key, value);
	}

	@Override
	public int incrementAndGet(String regionName, String key, int by, int def) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CachePair getCachePair(String regionName, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setCachePair(String regionName, String key, long version, Object value, int expSeconds) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void add(String regionName, String key, Object value, int expSeconds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int decrAndGet(String regionName, String key, int by, int def) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int incrementAndGet(String regionName, String key, int by, int def, int exp) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void set(String regionName, String key, Object value) {
		gcache.put(key, value);
	}

	@Override
	public void remove(String regionName, String key) {
		gcache.invalidate(key);
	}

	@Override
	public boolean isLocal() {
		// TODO Auto-generated method stub
		return false;
	}

}

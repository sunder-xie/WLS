package com.wheelys.untrans.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.untrans.CacheService;
import com.wheelys.untrans.monitor.MonitorService;
import com.wheelys.util.DateUtil;
import com.wheelys.constant.CacheConstant;
import com.wheelys.untrans.UntransService;

@Service("untransService")
public class UntransServiceImpl implements UntransService {

	@Autowired
	@Qualifier("cacheService")
	private CacheService cacheService;
	@Autowired
	@Qualifier("monitorService")
	private MonitorService monitorService;

	@Override
	public ResultCode<String> updateLoginLimitInCache(String key) {
		Integer maxnum = getMaxNumber();
		Long time = (Long) cacheService.get(CacheConstant.REGION_HALFDAY, "TIME" + key);
		Integer count = 1;
		Long cur = System.currentTimeMillis();
		if (time != null) {
			Long rc = (time + DateUtil.m_minute * 10);
			if (rc > cur) {
				count = (Integer) cacheService.get(CacheConstant.REGION_HALFDAY, "LIMIT" + key);
				if (count == null)
					count = 1;
				else
					count++;
			} else {
				cacheService.set(CacheConstant.REGION_HALFDAY, "TIME" + key, cur);
			}
		} else {
			cacheService.set(CacheConstant.REGION_HALFDAY, "TIME" + key, cur);
		}
		cacheService.set(CacheConstant.REGION_HALFDAY, "LIMIT" + key, count);
		if (count > maxnum) {
			return ResultCode.getFailure("loginLimitedCount=" + count);
		}
		return ResultCode.getSuccess("");
	}

	@Override
	public ResultCode<String> checkLoginLimitNum(String key) {
		Integer maxnum = getMaxNumber();
		Long time = (Long) cacheService.get(CacheConstant.REGION_HALFDAY, "TIME" + key);
		Long cur = System.currentTimeMillis();
		if (time != null) {
			Long rc = (time + DateUtil.m_minute * 10);
			if (rc > cur) {
				Integer count = (Integer) cacheService.get(CacheConstant.REGION_HALFDAY, "LIMIT" + key);
				if (count != null && count > maxnum) {
					return ResultCode.getFailure("登录失败次数受限，请稍后再试！");
				}
			}
		}
		return ResultCode.SUCCESS;
	}

	private Integer getMaxNumber() {
		return 20;
	}
}

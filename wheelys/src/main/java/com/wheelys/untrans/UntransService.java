package com.wheelys.untrans;

import com.wheelys.api.vo.ResultCode;

public interface UntransService {

	ResultCode checkLoginLimitNum(String remoteIp);

	ResultCode<String> updateLoginLimitInCache(String remoteIp);

}

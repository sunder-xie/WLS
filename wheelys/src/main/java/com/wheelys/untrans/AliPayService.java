package com.wheelys.untrans;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.model.user.WheelysMember;

public interface AliPayService {


	/**
	 * @param code
	 * @param remoteIp
	 * @return 根据用户id创建用户
	 */
	public ResultCode<WheelysMember> createMemberByCodeOnlyUserId(String code, String remoteIp);

}

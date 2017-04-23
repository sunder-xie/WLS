package com.wheelys.untrans;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.model.user.WheelysMember;

public interface WeiXinService {

	ResultCode<WheelysMember> createMemberByCodeOnlyOpenid(String code, String remoteIp);

}

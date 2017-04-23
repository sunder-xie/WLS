package com.wheelys.service;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.model.acl.User;
import com.wheelys.web.support.token.MemberEncodeAuthenticationToken;

public interface WheelysLoginService {

	ResultCode<MemberEncodeAuthenticationToken> getLogonMemberByMemberEncodeAndIp(String sessid, String remoteIp);

	ResultCode<MemberEncodeAuthenticationToken> doLogin4MemberEncodeAndSave(String username, String password,
			String remoteIp);

	void updateMemberAuth(String encodeOrSessid, User member);

	void doLogout(String sessid, String ip);

}

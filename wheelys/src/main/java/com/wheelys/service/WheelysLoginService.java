package com.wheelys.service;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.web.support.token.MemberEncodeAuthenticationToken;

public interface WheelysLoginService {

	ResultCode<MemberEncodeAuthenticationToken> getLogonMemberByMemberEncodeAndIp(String sessid, String remoteIp);

	ResultCode<MemberEncodeAuthenticationToken> doLogin4MemberEncodeAndSave(String username, String password,
			String remoteIp);

	void updateMemberAuth(String encodeOrSessid, WheelysMember member);

	void doLogout(String sessid, String ip);

}

package com.wheelys.service;

import com.wheelys.support.ErrorCode;
import com.wheelys.model.user.OpenMember;
import com.wheelys.model.user.WheelysMember;

public interface MemberRegService {

	WheelysMember createMember(String source, String loginname, String ip);
	
	OpenMember createOpenMember(WheelysMember member,String source,String loginname, String headimgurl,String nickname,String unionid);
	
	/**
	 * 动态码登录或则注册
	 * @param mobile
	 * @param checkpass
	 * @param source
	 * @param ip
	 * @return
	 */
	ErrorCode<WheelysMember> dynamicCodeLoginOrRegister(String mobile, String checkpass, String source, String ip);

}

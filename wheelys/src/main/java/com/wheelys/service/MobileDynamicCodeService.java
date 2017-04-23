package com.wheelys.service;

import com.wheelys.support.ErrorCode;

public interface MobileDynamicCodeService {
	/**
	 * 未登录前的验证码
	 * @param tag
	 * @param mobile
	 * @param memberid
	 * @param ip
	 * @return
	 */
	ErrorCode sendDynamicCode(String tag, String mobile, Long memberid, String ip);
	/**
	 * 未注册（或未登录前）的验证码绑定
	 * @param tag
	 * @param mobile
	 * @param checkpass
	 * @param memberid
	 * @return
	 */
	ErrorCode checkDynamicCode(String tag, String mobile, Long memberid, String checkpass);
	
}

package com.wheelys.service;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.helper.EncodeData;
import com.wheelys.model.user.OpenMember;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.web.support.token.MemberEncodeAuthenticationToken;

public interface MemberService {
	
	ResultCode<MemberEncodeAuthenticationToken> doLogin4MemberEncode(String username, String password, String remoteIp, String appkey, String srcsystem);
	
	ResultCode<MemberEncodeAuthenticationToken> doLoginByMemberEncode(String memberEncode, String remoteIp, String srcsystem);

	OpenMember getOpenMemberByLoginname(String sourceWeixin, String unionid);
	
	OpenMember getOpenMemberByMemberid(Long memberid);

	ResultCode<MemberEncodeAuthenticationToken> doLoginByOpenMember(WheelysMember member, String remoteIp, String srcsystem);

	void updateOpenMemberInfo(Long memberid, String nickname, String headimg);

	WheelysMember getWheelysMemberByMemberId(Long memberid);
	
	/**
	 * 绑定手机号
	 * @param memberid
	 * @param bindMobile
	 * @param checkpass
	 * @param remoteIp
	 * @return
	 */
	ResultCode<WheelysMember> bindMobile(Long memberid, String bindMobile, String checkpass, String remoteIp);
	/**
	 * 修改绑定手机号
	 * @param memberid
	 * @param bindMobile
	 * @param checkpass
	 * @param remoteIp
	 * @return
	 */
	ResultCode<WheelysMember> changeMobile(Long memberid, String bindMobile, String checkpass, String remoteIp);
	/**
	 * 根据手机号获取用户
	 * @param mobile
	 * @return
	 */
	WheelysMember getWheelysMemberByMobile(String mobile);
	/**
	 * 修改用户信息更新登录状态
	 * @param encodeOrSessid
	 * @param member
	 */
	void updateMemberAuth(String encodeOrSessid, WheelysMember member);

	/**
	 * 根据Membercode获取用户
	 * @param memberencode
	 * @return
	 */
	WheelysMember getWheelysMemberByMemberencode(String memberencode);
	/**
	 * 根据memberEncode得到用户登录信息
	 * @param memberEncode
	 * @param doValidate
	 * @return
	 */
	EncodeData getMemberEncodeData(String memberEncode, boolean doValidate);

}

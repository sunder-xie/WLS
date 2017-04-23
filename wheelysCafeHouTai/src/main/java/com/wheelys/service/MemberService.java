package com.wheelys.service;

import java.util.Date;
import java.util.List;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.model.acl.User;
import com.wheelys.web.action.admin.vo.WheelysMemberVo;
import com.wheelys.web.support.token.MemberEncodeAuthenticationToken;

public interface MemberService {

	ResultCode<MemberEncodeAuthenticationToken> doLogin4MemberEncode(String username, String password, String remoteIp,
			String appkey, String srcsystem);

	ResultCode<MemberEncodeAuthenticationToken> doLoginByMemberEncode(String memberEncode, String remoteIp,
			String srcsystem);

	ResultCode<MemberEncodeAuthenticationToken> doLoginByOpenMember(User member, String remoteIp, String srcsystem);

	/**
	 * 获取所有会员信息(包括这些会员最新的一笔订单的信息)
	 * 
	 * @param pageNo
	 * @param rowPerPage
	 * @return pageCount和list的map
	 */
	List<WheelysMemberVo> findMemberWithLatestOrder(Integer pageNo, int rowPerPage, String region, String cityCode,
			Long shopId, String phone, Date paidTimeBegin, Date paidTimeEnd);

}

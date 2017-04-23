package com.wheelys.untrans.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.cons.MemberConstant;
import com.wheelys.json.AliPayUserVo;
import com.wheelys.model.user.OpenMember;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.MemberRegService;
import com.wheelys.service.MemberService;
import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.untrans.AliPayService;
import com.wheelys.util.AliOAuthUtils;
import com.wheelys.util.WebUtils;

@Service("aliPayService")
public class AliPayServiceImpl extends BaseServiceImpl implements AliPayService {
	@Autowired
	@Qualifier("memberService")
	private MemberService memberService;
	@Autowired
	@Qualifier("memberRegService")
	private MemberRegService memberRegService;

	@Override
	public ResultCode<WheelysMember> createMemberByCodeOnlyUserId(String code, String remoteIp) {
		ResultCode<AliPayUserVo> resultCode = AliOAuthUtils.getUserInfoByCode(code);
		if (!resultCode.isSuccess()) {
			return ResultCode.getFailure(resultCode.getMsg());
		}
		WheelysMember member = createOpenmemberByAliPayUserVo(resultCode.getRetval(), remoteIp);
		return ResultCode.getSuccessReturn(member);

	}

	private WheelysMember createOpenmemberByAliPayUserVo(AliPayUserVo user, String remoteIp) {
		String source = MemberConstant.SOURCE_ALIPAY;
		String loginname = user.getUser_id();
		OpenMember openMember = memberService.getOpenMemberByLoginname(source, loginname);
		WheelysMember member = null;
		String nickname = WebUtils.removeFourChar(user.getNick_name());
		if (openMember == null) {
			member = memberRegService.createMember(source, loginname, remoteIp);
			openMember = memberRegService.createOpenMember(member, source, loginname, user.getAvatar(), nickname,
					user.getUser_id());
			dbLogger.warn("userid:" + openMember.getLoginname() + ",nickname:" + openMember.getNickname());
		} else {
			member = baseDao.getObject(WheelysMember.class, openMember.getMemberid());
			memberService.updateOpenMemberInfo(member.getId(), nickname, user.getAvatar());
		}
		return member;
	}

}

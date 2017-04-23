package com.wheelys.untrans.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.cons.MemberConstant;
import com.wheelys.util.JsonUtils;
import com.wheelys.json.WeixinUserVo;
import com.wheelys.model.user.OpenMember;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.MemberRegService;
import com.wheelys.service.MemberService;
import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.untrans.WeiXinService;
import com.wheelys.util.WebUtils;
import com.wheelys.util.WeiXinOAuthUtils;

@Service("weiXinService")
public class WeiXinServiceImpl extends BaseServiceImpl implements WeiXinService {

	@Autowired@Qualifier("memberService")
	private MemberService memberService;
	@Autowired@Qualifier("memberRegService")
	private MemberRegService memberRegService;
	
	@Override
	public ResultCode<WheelysMember> createMemberByCodeOnlyOpenid(String code, String remoteIp) {
		ResultCode<WeixinUserVo> resultCode = WeiXinOAuthUtils.getUserInfoByCode(code);
		if(!resultCode.isSuccess()){
			return ResultCode.getFailure(resultCode.getMsg());
		}
		WheelysMember member = createOpenmemberByWeixinUserVo(resultCode.getRetval(), remoteIp);
		return ResultCode.getSuccessReturn(member);
	}
	
	private WheelysMember createOpenmemberByWeixinUserVo(WeixinUserVo user, String remoteIp){
		String source = MemberConstant.SOURCE_WEIXIN;
		String loginname = StringUtils.isNotBlank(user.getOpenid()) ? user.getOpenid() : user.getUnionid();
		dbLogger.warn("openid:"+JsonUtils.writeObjectToJson(user));
		OpenMember openMember = memberService.getOpenMemberByLoginname(source, loginname);
		WheelysMember member = null;
		String nickname = WebUtils.removeFourChar(user.getNickname());
		if(openMember==null){
			member = memberRegService.createMember(source, loginname, remoteIp);
			openMember = memberRegService.createOpenMember(member, source, loginname,user.getHeadimgurl(),nickname,user.getUnionid());
			dbLogger.warn("openid:"+openMember.getLoginname()+",nickname:"+openMember.getNickname());
		}else{
			member = baseDao.getObject(WheelysMember.class, openMember.getMemberid());
			memberService.updateOpenMemberInfo(member.getId(), nickname,user.getHeadimgurl());
		}
		return member;
	}

}

package com.wheelys.web.component;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.model.acl.User;
import com.wheelys.support.ErrorCode;
import com.wheelys.util.BeanUtil;
import com.wheelys.web.component.LoginServiceImpl;
import com.wheelys.service.WheelysLoginService;
import com.wheelys.util.WebUtils;
import com.wheelys.web.support.token.MemberEncodeAuthenticationToken;

public class ShLoginServiceImpl extends LoginServiceImpl {
	
	@Autowired@Qualifier("wheelysLoginService")
	private WheelysLoginService wheelysLoginService;
	
	@Override
	public ErrorCode<Map> autoLogin(HttpServletRequest request, HttpServletResponse response, String username, String password) {
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password))
			
			return ErrorCode.getFailure("用户名密码必填！");
		String remoteIp = WebUtils.getRemoteIp(request);
		ResultCode<MemberEncodeAuthenticationToken> result = wheelysLoginService.doLogin4MemberEncodeAndSave(username, password, remoteIp);
		Map errorMap = new HashMap();
		if(result.isSuccess()){
			User member = result.getRetval().getMember();
			successHandler.processSessid(request, response, member.getId(), result.getRetval().getMemberEncode());

			Map jsonMap = BeanUtil.getBeanMapWithKey(member, "nickname", "id", "mobile");
			if(StringUtils.isNotBlank((String) jsonMap.get("mobile"))){
				jsonMap.put("isMobile", true);
			}else {
				jsonMap.put("isMobile", false);
			}
			return ErrorCode.getSuccessReturn(jsonMap);
		}else{
			errorMap.put("j_username", result.getMsg());
			return ErrorCode.getFailureReturn(errorMap);
		}
	}
	
	@Override
	public Authentication loadAuthentication(String remoteIp, String sessid){
		ResultCode<MemberEncodeAuthenticationToken> result = wheelysLoginService.getLogonMemberByMemberEncodeAndIp(sessid, remoteIp);
		if(result.isSuccess()){
			return result.getRetval();
		}
		return null;
	}

}

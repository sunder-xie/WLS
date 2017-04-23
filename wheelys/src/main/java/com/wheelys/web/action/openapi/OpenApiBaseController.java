package com.wheelys.web.action.openapi;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.cons.ApiConstant;
import com.wheelys.util.WheelysLogger;
import com.wheelys.util.JsonDirectOut;
import com.wheelys.util.WebLogger;
import com.wheelys.web.support.GewaVelocityView;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.WheelysLoginService;
import com.wheelys.util.WebUtils;
import com.wheelys.web.filter.OpenApiAuth;
import com.wheelys.web.support.token.MemberEncodeAuthenticationToken;

public class OpenApiBaseController {

	protected final transient WheelysLogger dbLogger = WebLogger.getLogger(getClass());
	
	@Autowired@Qualifier("wheelysLoginService")
	private WheelysLoginService wheelysLoginService;
	
	protected String successJsonResult(ModelMap model, Object data) {
		ResultCode result = ResultCode.getSuccessReturn(data);
		model.put(GewaVelocityView.RENDER_JSON, "true");
		model.put(GewaVelocityView.DIRECT_OUTPUT, new JsonDirectOut(result));
		return "common/directOut.vm";
	}

	protected String errorJsonResult(ModelMap model, String msg) {
		ResultCode result = ResultCode.getFailure(msg);
		model.put(GewaVelocityView.RENDER_JSON, "true");
		model.put(GewaVelocityView.DIRECT_OUTPUT, new JsonDirectOut(result));
		return "common/directOut.vm";
	}

	protected OpenApiAuth getOpenApiAuth(){
		HttpServletRequest request = getRequest();
		return (OpenApiAuth) request.getAttribute(ApiConstant.OPENAPI_AUTH_KEY);
	}
	
	protected WheelysMember getLogonMember(boolean needLogon){
		HttpServletRequest request = getRequest();
		ResultCode<MemberEncodeAuthenticationToken> result = (ResultCode<MemberEncodeAuthenticationToken>) request.getAttribute(OpenApiAuth.MEMBERVO_KEY);
		if(result==null){
			OpenApiAuth auth = getOpenApiAuth();
			if(auth != null && StringUtils.isNotBlank(auth.getMemberencode())){
				result = wheelysLoginService.getLogonMemberByMemberEncodeAndIp(auth.getMemberencode(), auth.getRemoteip());
			}else{
				result = ResultCode.getFailure("用户未登录！");
			}
		}
		if(result.isSuccess()){
			return result.getRetval().getMember();
		}else{
			if(needLogon){
				dbLogger.warn("用户未登录：" + WebUtils.getParamStr(request, true));
			}
			return null;
		}
	}
	
	protected HttpServletRequest getRequest(){
		ServletRequestAttributes holder = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		if(holder!=null){
			HttpServletRequest request = holder.getRequest();
			if(request!=null){
				return request;
			}
		}
		return null;
	}
}

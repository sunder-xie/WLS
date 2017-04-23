package com.wheelys.web.filter;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.filter.OncePerRequestFilter;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.commons.api.ApiSysParamConstants;
import com.wheelys.commons.sign.Sign;
import com.wheelys.cons.ApiConstant;
import com.wheelys.util.DateUtil;
import com.wheelys.util.WheelysLogger;
import com.wheelys.util.JsonUtils;
import com.wheelys.util.WebLogger;
import com.wheelys.service.WheelysLoginService;
import com.wheelys.util.AppKeyUtil;
import com.wheelys.util.WebUtils;
import com.wheelys.web.support.token.MemberEncodeAuthenticationToken;

public class OpenApiMobileAuthenticationFilter extends OncePerRequestFilter  {
	
	private WheelysLogger dbLogger = WebLogger.getLogger(getClass());
	
	private final int VALIDTIME = 10 * 60 * 1000;
	
	private final static String MUST_MEMBER_URI_ORDER = "/openapi/mobile/order/";
	private final static String MUST_MEMBER_URI_HOME = "/openapi/mobile/home/";
	private final static String MUST_MEMBER_URI_PAY = "/openapi/mobile/pay/";
	
	@Autowired@Qualifier("wheelysLoginService")
	private WheelysLoginService wheelysLoginService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		Timestamp time = DateUtil.parseTimestamp(request.getParameter(ApiSysParamConstants.TIMESTAMP));
		if(time == null){
			filterChain.doFilter(request, response);
			//ApiFilterHelper.writeErrorResponse(response, ApiConstant.CODE_PARTNER_NOT_EXISTS, "无效");
			return;
		}
		long diff = System.currentTimeMillis() - time.getTime();
		if(diff < -VALIDTIME || diff > VALIDTIME){
			dbLogger.error("diff：" + diff + "   params：" + JsonUtils.writeObjectToJson(request.getParameterMap()));
			ApiFilterHelper.writeErrorResponse(response, ApiConstant.CODE_PARTNER_NOT_EXISTS, "已过期");
			return;
		}
		String apptype = request.getParameter("apptype");
		if(StringUtils.isBlank(apptype)){
			ApiFilterHelper.writeErrorResponse(response, ApiConstant.CODE_PARTNER_NOT_EXISTS, "用户错误");
			return;
		}
		String appkey = request.getParameter(ApiSysParamConstants.APPKEY);
		if(StringUtils.isBlank(appkey)){
			ApiFilterHelper.writeErrorResponse(response, ApiConstant.CODE_PARTNER_NOT_EXISTS, "用户不存在");
			return;
		}
		String appversion = request.getParameter("appversion");
		if(StringUtils.isBlank(appversion)){
			ApiFilterHelper.writeErrorResponse(response, ApiConstant.CODE_PARTNER_NOT_EXISTS, "版本不能为空");
			return;
		}
		String remoteip = WebUtils.getRemoteIp(request);
		if(StringUtils.isBlank(remoteip)){
			ApiFilterHelper.writeErrorResponse(response, ApiConstant.CODE_PARTNER_NORIGHTS, "非法调用");
			return;
		}
		String sign = request.getParameter(ApiSysParamConstants.SIGN);
		String privateKey = AppKeyUtil.getPrivateKey(appkey);
		//签名校验
		String signData = Sign.signMD5(ApiFilterHelper.getTreeMap(request), privateKey);
		if (!StringUtils.equalsIgnoreCase(sign, signData)) {
			ApiFilterHelper.writeErrorResponse(response, ApiConstant.CODE_PARTNER_NORIGHTS, "校验签名错误");
			return;
		}
		String memberencode = request.getParameter("memberencode");
		//登录必须
		if(mustMember(request.getRequestURI())){
			if(StringUtils.isBlank(memberencode)){
				ApiFilterHelper.writeErrorResponse(response, ApiConstant.CODE_NOTLOGIN, "请先登录");
				return;
			}
			ResultCode<MemberEncodeAuthenticationToken> result = wheelysLoginService.getLogonMemberByMemberEncodeAndIp(memberencode, remoteip);
			if(result.isSuccess()){
				request.setAttribute(OpenApiAuth.MEMBERVO_KEY, result);
			}else{
				ApiFilterHelper.writeErrorResponse(response, ApiConstant.CODE_PARTNER_NORIGHTS, "请先登录.");
				return;
			}
		}
		try{
			request.setAttribute(ApiConstant.OPENAPI_AUTH_KEY, new OpenApiAuth(apptype, appkey, appversion, remoteip, memberencode));
		}catch(Exception e) {
			dbLogger.error(e, 15);
		}
		filterChain.doFilter(request, response);
	}
	
	private boolean mustMember(String uri){
		return uri.contains(MUST_MEMBER_URI_ORDER) || uri.contains(MUST_MEMBER_URI_HOME) || uri.contains(MUST_MEMBER_URI_PAY);
	}
	
}

package com.wheelys.web.action.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.Config;
import com.wheelys.api.vo.ResultCode;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.MemberService;
import com.wheelys.untrans.AliPayService;
import com.wheelys.untrans.WeiXinService;
import com.wheelys.util.WebUtils;
import com.wheelys.web.action.AnnotationController;
import com.wheelys.web.support.token.MemberEncodeAuthenticationToken;

@Controller
public class AppAuthController extends AnnotationController {

	@Autowired
	@Qualifier("config")
	private Config config;
	@Autowired
	@Qualifier("weiXinService")
	private WeiXinService weiXinService;
	@Autowired
	@Qualifier("memberService")
	private MemberService memberService;
	@Autowired
	@Qualifier("aliPayService")
	private AliPayService aliPayService;
	/**
	 * @param request
	 * @param state
	 * @param code
	 * @param model
	 * @param response
	 * @param lastpage
	 * @return
	 */
	
	@RequestMapping("/auth/alipay.xhtml")
	public String alipayAuth(HttpServletRequest request, String state, String auth_code, ModelMap model,
			HttpServletResponse response, String lastpage) {
		if (StringUtils.isBlank(auth_code)) {
			return showWapMsg(model, "code is null, state:" + state);
		}
		String remoteIp = WebUtils.getRemoteIp(request);
		ResultCode<WheelysMember> memberCode = aliPayService.createMemberByCodeOnlyUserId(auth_code, remoteIp);
		if (!memberCode.isSuccess()) {
			return showWapMsg(model, memberCode.getMsg());
		}
		WheelysMember member = memberCode.getRetval();
		ResultCode<MemberEncodeAuthenticationToken> loginResult = memberService.doLoginByOpenMember(member, remoteIp,
				"aliPayAuth");
		if (!loginResult.isSuccess()) {
			return showWapMsg(model, memberCode.getMsg());
		}
		setLoginKey2Cookie(request, response, loginResult.getRetval());
		response.setHeader("Cache-Control", "no-store");
		 if (StringUtils.isBlank(lastpage)) {
	        	lastpage = request.getHeader("Referer");
	        }
		return showRedirect(lastpage, model);
	}

	/**
	 * 
	 * @param request
	 * @param code
	 * @param model
	 * @param response
	 * @param lastpage
	 * @return
	 */
	@RequestMapping("/auth/weixin.xhtml")
	public String wxAuth(HttpServletRequest request, String state, String code, ModelMap model,
			HttpServletResponse response, String lastpage) {
		if (StringUtils.isBlank(code)) {
			return showWapMsg(model, "code is null, state:" + state);
		}
		String remoteIp = WebUtils.getRemoteIp(request);
		ResultCode<WheelysMember> memberCode = weiXinService.createMemberByCodeOnlyOpenid(code, remoteIp);
		if (!memberCode.isSuccess()) {
			return showWapMsg(model, memberCode.getMsg());
		}
		WheelysMember member = memberCode.getRetval();
		ResultCode<MemberEncodeAuthenticationToken> loginResult = memberService.doLoginByOpenMember(member, remoteIp,
				"wxAuth");
		if (!loginResult.isSuccess()) {
			return showWapMsg(model, memberCode.getMsg());
		}
		setLoginKey2Cookie(request, response, loginResult.getRetval());
		response.setHeader("Cache-Control", "no-store");
		return showRedirect(lastpage, model);
	}
	
	@RequestMapping("/auth/alipay/test.xhtml")
	public String test(){
		return "redirect:https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=2016113003642153&scope=auth_user&redirect_uri=http://www.wheelyschina.com/wheelyscafe/auth/alipay.xhtml";
	}
}
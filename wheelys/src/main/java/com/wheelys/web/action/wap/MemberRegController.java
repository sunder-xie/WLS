package com.wheelys.web.action.wap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.Config;
import com.wheelys.api.vo.ResultCode;
import com.wheelys.cons.MemberConstant;
import com.wheelys.constant.MobileDynamicCodeConstant;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.MemberRegService;
import com.wheelys.service.MemberService;
import com.wheelys.service.MobileDynamicCodeService;
import com.wheelys.support.ErrorCode;
import com.wheelys.util.OAuthUtils;
import com.wheelys.util.WebUtils;
import com.wheelys.web.action.AnnotationController;
import com.wheelys.web.support.token.MemberEncodeAuthenticationToken;
import com.wheelys.web.util.LoginUtils;
import com.wheelys.web.util.ReqLogUtil;

@Controller
public class MemberRegController extends AnnotationController {

	@Autowired
	@Qualifier("memberRegService")
	private MemberRegService memberRegService;
	@Autowired
	@Qualifier("mobileDynamicCodeService")
	private MobileDynamicCodeService mobileDynamicCodeService;
	@Autowired
	@Qualifier("memberService")
	private MemberService memberService;
	@Autowired
	@Qualifier("config")
	private Config config;

	@RequestMapping("/dynamicCodeLogin.xhtml")
	public String login() {
		return "/iphoneLogin.vm";
	}

	/**
	 * 验证图片验证码后，发送手机验证码
	 * 
	 * @param username
	 *            手机号
	 * @param piccode
	 *            图形验证码
	 */
	@RequestMapping("/ajax/common/sendDynamicCode.xhtml")
	public String sendDynamicCodeWithCaptchaId(HttpServletRequest request, String phoneCode, String captchaId, String picCode, ModelMap model) {
		String ip = WebUtils.getRemoteIp(request);
		Boolean isValid = this.validateCaptcha(captchaId, picCode, ip);
		if (!isValid) {
			return showJsonError(model, "您输入的图片验证码有误，请重新输入");
		}
		ErrorCode result = mobileDynamicCodeService.sendDynamicCode(MobileDynamicCodeConstant.TAG_DYNAMICCODE, phoneCode,
				null, ip);
		if (result.isSuccess()) {
			ReqLogUtil.addLogInfo("sendCode_success", "Y");
			return showJsonSuccess(model, "验证码发送成功");
		} else {
			ReqLogUtil.addLogInfo(result.getErrcode(), result.getMsg());
			return showJsonError(model, result.getMsg());
		}
	}

	/**
	 * 快速登录
	 * @param request
	 * @param response
	 * @param username 手机号
	 * @param message 手机验证码
	 * @param model
	 * @return
	 */
	@RequestMapping("/ajax/common/asynchPhoneLogin.xhtml")
	public String asynchLogin(HttpServletRequest request, HttpServletResponse response, String phoneCode, String message,
			ModelMap model) {
		String ip = WebUtils.getRemoteIp(request);
		ErrorCode<WheelysMember> result = memberRegService.dynamicCodeLoginOrRegister(phoneCode, message,
				MemberConstant.REGISTER_CODE, ip);
		if (result.isSuccess()) {
			ResultCode<MemberEncodeAuthenticationToken> loginResult = memberService
					.doLoginByOpenMember(result.getRetval(), ip, "dynamicCode");
			if (!loginResult.isSuccess()) {
				return showWapMsg(model, loginResult.getMsg());
			}
			setLoginKey2Cookie(request, response, loginResult.getRetval());
			response.setHeader("Cache-Control", "no-store");
			return showJsonSuccess(model, null);
		} else {
			return showJsonError(model, result.getMsg());
		}
	}

	@RequestMapping("/bindPhone.xhtml")
	public String bindPhone(HttpServletRequest request, ModelMap model) {
		WheelysMember member = this.getLogonMember();
		if (member == null) {
			String lastpage = config.getAbsPath() + "member/center.xhtml";
			String oauthUrl = OAuthUtils.oauth2Url(request, lastpage, config.getAbsPath());
			return showRedirect(oauthUrl, model);
		}
		if(member.isBindMobile()){
			return showWapMsg(model, "您已绑定手机号");
		}
		return "/wps/home/bindPhone.vm";
	}

	/**
	 * 绑定手机号时发送验证码
	 * 
	 * @param username
	 *            手机号
	 */
	@RequestMapping("/ajax/common/sendPhoneBindCode.xhtml")
	public String sendPhoneBindCode(HttpServletRequest request, String phoneCode,
			ModelMap model) {
		String ip = WebUtils.getRemoteIp(request);
		WheelysMember member = this.getLogonMember();
		if (member == null) {
			String lastpage = config.getAbsPath() + "member/center.xhtml";
			String oauthUrl = OAuthUtils.oauth2Url(request, lastpage, config.getAbsPath());
			return showRedirect(oauthUrl, model);
		}
		if (member.isBindMobile()) {
			return showJsonError(model, "您已绑定手机号");
		}
		ErrorCode result = mobileDynamicCodeService.sendDynamicCode(MobileDynamicCodeConstant.TAG_BINDMOBILE, phoneCode, member.getId(), ip);
		if (result.isSuccess()) {
			ReqLogUtil.addLogInfo("sendCode_success", "Y");
			return showJsonSuccess(model, "验证码发送成功");
		} else {
			ReqLogUtil.addLogInfo(result.getErrcode(), result.getMsg());
			return showJsonError(model, result.getMsg());
		}
	}

	/**
	 * 绑定手机号
	 * 
	 * @param username
	 *            手机号
	 * @param message
	 *            手机验证码
	 */
	@RequestMapping("/ajax/common/bindPhone.xhtml")
	public String bingPhone(HttpServletRequest request, String phoneCode, String message,
			ModelMap model) {
		String ip = WebUtils.getRemoteIp(request);
		WheelysMember member = this.getLogonMember();
		if (member == null) {
			String lastpage = config.getAbsPath() + "member/center.xhtml";
			String oauthUrl = OAuthUtils.oauth2Url(request, lastpage, config.getAbsPath());
			return showRedirect(oauthUrl, model);
		}
		if (member.isBindMobile()) {
			return showJsonError(model, "您已绑定手机号");
		}
		ResultCode<WheelysMember> resultCode = memberService.bindMobile(member.getId(), phoneCode, message, ip);
		if (resultCode.isSuccess()) {
			ReqLogUtil.addLogInfo("bindPhone_success", "Y");
			memberService.updateMemberAuth(LoginUtils.getSessid(request), resultCode.getRetval());
			return showJsonSuccess(model, null);
		} else {
			ReqLogUtil.addLogInfo(resultCode.getErrcode(), resultCode.getMsg());
			return showJsonError(model, resultCode.getMsg());
		}
	}
}

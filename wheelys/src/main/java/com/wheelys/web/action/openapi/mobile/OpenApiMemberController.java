package com.wheelys.web.action.openapi.mobile;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.cons.MemberConstant;
import com.wheelys.constant.MobileDynamicCodeConstant;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.MemberRegService;
import com.wheelys.service.MemberService;
import com.wheelys.service.MobileDynamicCodeService;
import com.wheelys.support.ErrorCode;
import com.wheelys.util.JsonUtils;
import com.wheelys.util.WebUtils;
import com.wheelys.web.action.openapi.OpenApiBaseController;
import com.wheelys.web.support.token.MemberEncodeAuthenticationToken;
import com.wheelys.web.util.ReqLogUtil;

@Controller
@RequestMapping("/openapi/mobile")
public class OpenApiMemberController extends OpenApiBaseController {
	
	@Autowired@Qualifier("mobileDynamicCodeService")
	private MobileDynamicCodeService mobileDynamicCodeService;
	@Autowired@Qualifier("memberRegService")
	private MemberRegService memberRegService;
	@Autowired@Qualifier("memberService")
	private MemberService memberService;
	
	/**
	 * 获取手机验证码
	 * @param request
	 * @param mobile
	 * @return
	 */
	@RequestMapping("/member/sendDynamicCode.xhtml")
	public String sendDynamicCode(HttpServletRequest request, String mobile, ModelMap model) {
		String ip = WebUtils.getRemoteIp(request);
		ErrorCode result = mobileDynamicCodeService.sendDynamicCode(MobileDynamicCodeConstant.TAG_DYNAMICCODE, mobile, null, ip);
		if (result.isSuccess()) {
			ReqLogUtil.addLogInfo("sendCode_success", "Y");
			return successJsonResult(model, "验证码发送成功");
		} else {
			ReqLogUtil.addLogInfo(result.getErrcode(), result.getMsg());
			return errorJsonResult(model, result.getMsg());
		}
	}

	/**
	 * 快速登录
	 * @param request
	 * @param mobile
	 * @param code
	 * @return
	 */
	@RequestMapping("/member/asynchPhoneLogin.xhtml")
	public String asynchLogin(HttpServletRequest request, String mobile, String code, ModelMap model) {
		String ip = WebUtils.getRemoteIp(request);
		ErrorCode<WheelysMember> result = memberRegService.dynamicCodeLoginOrRegister(mobile, code, MemberConstant.REGISTER_CODE, ip);
		if (result.isSuccess()) {
			ResultCode<MemberEncodeAuthenticationToken> loginResult = memberService.doLoginByOpenMember(result.getRetval(), ip, "dynamicCode");
			if (!loginResult.isSuccess()) {
				return errorJsonResult(model, loginResult.getMsg());
			}
			return successJsonResult(model, loginResult.getRetval().getMemberEncode());
		} else {
			return errorJsonResult(model, result.getMsg());
		}
	}
	
	@RequestMapping("/member/getMemberInfo.xhtml")
	public String getMemberInfo(ModelMap model){
		WheelysMember member = getLogonMember(false);
		dbLogger.warn(JsonUtils.writeObjectToJson(member));
		return successJsonResult(model, member);
	}
}

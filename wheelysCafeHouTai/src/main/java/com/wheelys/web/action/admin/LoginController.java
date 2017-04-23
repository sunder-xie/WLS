package com.wheelys.web.action.admin;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.support.ErrorCode;
import com.wheelys.support.GewaCaptchaService;
import com.wheelys.web.component.LoginService;
import com.wheelys.web.util.ReqLogUtil;
import com.wheelys.util.WebUtils;
import com.wheelys.web.action.AnnotationController;

@Controller
public class LoginController extends AnnotationController {
	
	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	@Autowired@Qualifier("captchaService")
	private GewaCaptchaService captchaService;
	
	@RequestMapping("/login.xhtml")
	public String login() {
		return "/admin/login_new.vm";
	}
	
	@RequestMapping("/logout.xhtml")
	public String logout(){
		return "/admin/login_new.vm";
	}
	
	@RequestMapping("/ajax/common/asynchLogin.xhtml")
	public String asynchLogin(HttpServletRequest request, HttpServletResponse response, String username, String password, String captchaId, String captcha, ModelMap model) {
		String ip = WebUtils.getRemoteIp(request);
		Boolean isValid = this.validateCaptcha(captchaId, captcha, ip);
		if(!isValid){
			return showJsonError(model, "验证码错误！");
		}
		ErrorCode<Map> result = loginService.autoLogin(request, response,username, password);
		if (result.isSuccess()) {
			ReqLogUtil.addLogInfo("login_success", "Y");
			return showJsonSuccess(model, result.getRetval());
		} else {
			return showJsonError(model, result.getRetval().get("j_username")+"");
		}
	}

	public boolean validateCaptcha(String captchaId, String captcha, String ip) {
		boolean validCaptcha = false;
		if(StringUtils.isNotBlank(captcha)){
			captcha = StringUtils.lowerCase(captcha);
			ErrorCode<Map<String, String>> code = captchaService.validateResponseForID(captchaId, captcha, ip);
			if(code.isSuccess()){
				validCaptcha = true;
			}else{
				ReqLogUtil.addLogInfo(code.getRetval());
			}
		}
		return validCaptcha;
	}

}

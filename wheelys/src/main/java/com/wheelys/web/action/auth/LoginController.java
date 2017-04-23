package com.wheelys.web.action.auth;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.support.ErrorCode;
import com.wheelys.web.component.LoginService;
import com.wheelys.web.util.ReqLogUtil;
import com.wheelys.web.action.AnnotationController;

@Controller
public class LoginController extends AnnotationController {

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;


	@RequestMapping("/login.xhtml")
	public String login(){
		return "/login.vm";
	}
	
	@RequestMapping("/logout.xhtml")
	public String logout(){
		return "/login.vm";
	}
	
	@RequestMapping("/ajax/common/asynchLogin.xhtml")
	public String asynchLogin(HttpServletRequest request, HttpServletResponse response, String username, String password, ModelMap model) {
		ErrorCode<Map> result = loginService.autoLogin(request, response, username, password);
		if (result.isSuccess()) {
			ReqLogUtil.addLogInfo("login_success", "Y");
			return showJsonSuccess(model, result.getRetval());
		} else {
			return showJsonError(model, result.getRetval().get("j_username")+"");
		}
	}
	
}

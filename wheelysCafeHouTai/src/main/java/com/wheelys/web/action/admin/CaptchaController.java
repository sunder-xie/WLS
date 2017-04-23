package com.wheelys.web.action.admin;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.patchca.PatchcaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wheelys.Config;
import com.wheelys.support.GewaCaptchaService;
import com.wheelys.util.GewaIpConfig;
import com.wheelys.util.VmUtils;
import com.wheelys.util.WebUtils;
import com.wheelys.web.action.AnnotationController;

@Controller
public class CaptchaController extends AnnotationController{
	@Autowired@Qualifier("config")
	private Config config;
	@Autowired@Qualifier("captchaService")
	private GewaCaptchaService captchaService;
	
	@RequestMapping("/getCaptchaId.xhtml")
	public String getCaptchaId(HttpServletRequest request, ModelMap model){
		String captchaId = VmUtils.getRandomCaptchaId();
		boolean isAjax = WebUtils.isAjaxRequest(request);
		if(!isAjax){
			captchaId = StringUtils.reverse(captchaId.substring(0, 16)) + captchaId.substring(16);
			dbLogger.error("GetInvalidCaptchaID:" + WebUtils.getRemoteIp(request));
		}
		return showJsonSuccess(model, captchaId);
	}

	@RequestMapping("/captcha.xhtml")
	public void showPicture(HttpServletResponse response, HttpServletRequest request, String captchaId) throws Exception {
		captchaId = StringUtils.substring(captchaId, 0, 100);
		if(!VmUtils.isValidCaptchaId(captchaId)) {
			captchaId = "err" + captchaId;
			dbLogger.error("UseInvalidCaptchaID:" + WebUtils.getRemoteIp(request));
		}
		try{
			BufferedImage challenge = captchaService.getCaptchaImage(captchaId);
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/jpeg");
			ServletOutputStream os = response.getOutputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PatchcaUtils.printImage(bos, challenge);
			os.write(bos.toByteArray());
			os.flush();
			os.close();
		}catch(Exception e){
			dbLogger.error(e, 100);
		}
	}
	@RequestMapping("/get_captcha_value.xhtml")
	@ResponseBody
	public String getCaptchaValue(String cpid, /*String username, String md5pass, */HttpServletRequest request){
		dbLogger.warn(WebUtils.getParamStr(request, false));
		String ip = WebUtils.getRemoteIp(request);
		if(GewaIpConfig.isOfficeIp(ip) || GewaIpConfig.isDevServer()){
			return captchaService.getCaptchaValue(cpid);
		}else{
			return "error";
		}
		
	}
}
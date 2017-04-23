package com.wheelys.web.action.pay;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.util.JsonUtils;
import com.wheelys.util.WebUtils;
import com.wheelys.web.action.AnnotationController;


@Controller
public class WapPayRedirectController extends AnnotationController{

	@RequestMapping("/wapPayRedirect.xhtml")
	public String wapPayRedirect(HttpServletRequest request, Boolean pause, ModelMap model) throws Exception {
		try {
			Map<String, String> params = WebUtils.getRequestMap(request);
			String payData = params.get("PAY_DATA_");
			if(StringUtils.isBlank(payData))return show404(model, "对不起，请求错误！");
			
			Map<String, String> rsMap = JsonUtils.readJsonToMap(new String(Base64.decodeBase64(payData),"UTF-8"));
			Map submitParams = JsonUtils.readJsonToMap(rsMap.get("submitParams"));
			model.put("method", rsMap.get("httpMethod"));
			model.put("submitParams", submitParams);
			model.put("paramsNames", submitParams.keySet());
			model.put("payUrl", rsMap.get("payurl"));
			
			model.put("pause", pause);
			return "tempSubmitForm3.vm";
		} catch (Exception e) {
			dbLogger.error("", e);
		}
		return show404(model, "对不起，出错了！");
	}
	
	protected final String show404(ModelMap model, String msg){
		model.put("msg", msg);
		return "404.vm";
	}
}

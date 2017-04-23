package com.wheelys.web.action.pay.notify;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.util.JsonUtils;
import com.wheelys.pay.AliWapPayUtil;
import com.wheelys.untrans.pay.AliWapPayService;
import com.wheelys.util.WebUtils;
import com.wheelys.web.action.AnnotationController;

@Controller
public class AliWapPayController extends AnnotationController {
	
	@Autowired@Qualifier("aliWapPayService")
	private AliWapPayService aliWapPayService;

	@RequestMapping("/return/aliwappay.xhtml")
	public String aliwappay(String out_trade_no, String sign, HttpServletRequest request,
			ModelMap model) {
		Map<String, String> params = WebUtils.getRequestMap(request);
		dbLogger.warn("return2:" + JsonUtils.writeMapToJson(params));
		dbLogger.warn("return支付宝Wap" + AliWapPayUtil.checkRsaSign(params, sign));
		model.put("tradeno", out_trade_no);
		String tradeno = out_trade_no;
		boolean check = AliWapPayUtil.checkRsaSign(params, sign);
		if(check){
			ResultCode<Map<String, String>> result = aliWapPayService.processOrder(tradeno);
			if(!result.isSuccess()){
				dbLogger.error("payreturn失败,订单号：" + tradeno+",msg"+result.getMsg());
			}
		}
		model.put("tradeno", tradeno);
		return showRedirect("order/payresult.xhtml", model);
	}

	@RequestMapping("/notify/aliwappay.xhtml")
	@ResponseBody
	public String aliwappay(HttpServletRequest request, String out_trade_no, String sign, String trade_no) {
		Map<String, String> params = WebUtils.getRequestMap(request);
		dbLogger.warn(trade_no + " aliwappay :" + JsonUtils.writeMapToJson(params));

		String tradeno = out_trade_no;
		String scrSign = params.remove("sign");
		if (StringUtils.isBlank(scrSign)) {
			dbLogger.error("非法参数提交");
			return "fail:Invalid params";
		}

		Map<String, String> returnParams = new HashMap<String, String>();
		boolean check = AliWapPayUtil.checkRsaSign(params, sign);
		if (check) {
			dbLogger.warn("签名成功, 订单号:" + tradeno);
			if (StringUtils.equals(params.get("trade_status"), "TRADE_SUCCESS")) {
				ResultCode<Map<String, String>> result = aliWapPayService.processOrder(tradeno);
				if (result.isSuccess()) {
					return "success";
				}
				returnParams = result.getRetval();
			} else {
				returnParams.put("return_msg", "校验错误");
			}
			return "success";
		} else {
			dbLogger.error("签名校验失败,订单号：" + tradeno);
			returnParams.put("return_msg", "签名失败");
		}
		returnParams.put("return_code", "FAIL");
		return "fail";
	}

	@RequestMapping("/test/aliwappay.xhtml")
	@ResponseBody
	public String aliwappay(String tradeno) {
		ResultCode<Map<String, String>> result = aliWapPayService.processOrder(tradeno);
		return JsonUtils.writeMapToJson(result.getRetval());
	}
}

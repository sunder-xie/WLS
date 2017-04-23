package com.wheelys.web.action.openapi.mobile;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.Config;
import com.wheelys.api.vo.ResultCode;
import com.wheelys.util.JsonUtils;
import com.wheelys.model.order.WheelysOrder;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.pay.AliWapPayUtil;
import com.wheelys.service.order.OrderService;
import com.wheelys.untrans.pay.AliWapPayService;
import com.wheelys.util.WebUtils;
import com.wheelys.web.action.openapi.OpenApiBaseController;

@Controller
@RequestMapping("/openapi/mobile")
public class OpenApiPayController extends OpenApiBaseController {
	
	@Autowired@Qualifier("orderService")
	private OrderService orderService;
	@Autowired@Qualifier("config")
	private Config config;
	@Autowired@Qualifier("aliWapPayService")
	private AliWapPayService aliWapPayService;

	/**
	 * 支付宝支付参数
	 * @param tradeno
	 * @param model
	 * @return
	 */
	@RequestMapping("/pay/getAlipayParams.xhtml")
	public String getAlipayParams(String tradeno, ModelMap model){
		WheelysMember member = this.getLogonMember(true);
		WheelysOrder order = orderService.getOrderByTradeNo(tradeno);
		if(order == null || !order.getMemberid().equals(member.getId())){
			return this.errorJsonResult(model, "订单错误！");
		}
		String paramsStr = AliWapPayUtil.getAppPayUrl(order, getOpenApiAuth().getRemoteip(), config.getAbsPath());
		dbLogger.warn("getAlipayParams：" + paramsStr);
		return successJsonResult(model, paramsStr);
	}
	/**
	 * 支付宝支付同步通知
	 * @param memo
	 * @param result
	 * @param resultStatus
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/pay/returnAlipay.xhtml")
	public String returnAlipay(String memo, String result, String resultStatus, HttpServletRequest request, ModelMap model){
		Map<String, String> params = WebUtils.getRequestMap(request);
		dbLogger.warn("returnAlipay支付宝app:" + JsonUtils.writeMapToJson(params));
		if(StringUtils.equals(resultStatus, "9000")){
			Map resultMap = JsonUtils.readJsonToMap(result);
			Map payresponseMap = (Map) resultMap.get("alipay_trade_app_pay_response");
			String sign = resultMap.get("sign") + "";
			boolean check = AliWapPayUtil.checkRsaSign(payresponseMap, sign);
			if(check){
				String tradeno = payresponseMap.get("out_trade_no") + "";
				ResultCode<Map<String, String>> resultCode = aliWapPayService.processOrder(tradeno);
				if(!resultCode.isSuccess()){
					dbLogger.error("payreturn失败,订单号：" + tradeno+",msg"+resultCode.getMsg());
				}
			}
		}
		return successJsonResult(model, memo);
	}
}

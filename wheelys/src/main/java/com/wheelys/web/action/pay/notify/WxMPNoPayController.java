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
import com.wheelys.constant.PayConstant;
import com.wheelys.model.order.CardCouponsOrder;
import com.wheelys.model.order.WheelysOrder;
import com.wheelys.pay.WeiXinJsPayUtil;
import com.wheelys.service.PaymentService;
import com.wheelys.util.WeiXinSendUtils;
import com.wheelys.web.action.AnnotationController;

@Controller
public class WxMPNoPayController extends AnnotationController {
	
	@Autowired@Qualifier("paymentService")
	private PaymentService paymentService;
	
	@RequestMapping("/return/wxmppay.xhtml")
	public String payreturn(String tradeno, ModelMap model){
		if(StringUtils.isBlank(tradeno)){
			dbLogger.error("非法参数提交");
			return "fail:Invalid params";
		}
		ResultCode<Map<String, String>> result = processOrder(tradeno);
		if(!result.isSuccess()){
			dbLogger.error("payreturn失败,订单号：" + tradeno+",msg"+result.getMsg());
		}
		model.put("tradeno", tradeno);
		return showRedirect("order/payresult.xhtml", model);
	}
	
	@RequestMapping("/notify/wxmppay.xhtml")
	@ResponseBody
	public String wxmppay(HttpServletRequest request){
		String xml = WeiXinJsPayUtil.getPostBody(request);
		dbLogger.warn("wx mpno, notify content: " + xml);
		if(StringUtils.isBlank(xml)){
			return "fail:Invalid params";
		}
		
		Map<String, String> params = WeiXinJsPayUtil.xml2Map(xml);
		String tradeNo = params.get("out_trade_no");
		String scrSign = params.remove("sign");
		String merchantNo = params.get("mch_id");

		if(StringUtils.isBlank(merchantNo) || StringUtils.isBlank(scrSign)){
			dbLogger.error("非法参数提交");
			return "fail:Invalid params";
		}

		Map<String, String> returnParams = new HashMap<String, String>();
		boolean check = WeiXinJsPayUtil.checkSign(params, scrSign);
		if(check){
			dbLogger.warn("签名成功, 订单号:" + tradeNo);			
			if(StringUtils.equals(params.get("result_code"), "SUCCESS")){
				ResultCode<Map<String, String>> result = processOrder(tradeNo);
				if(result.isSuccess()){
					return WeiXinJsPayUtil.toXml(result.getRetval());
				}
				returnParams = result.getRetval();
			}else{
				returnParams.put("return_msg", "校验错误");
			}
		}else{
			dbLogger.error("签名校验失败,订单号：" + tradeNo);
			returnParams.put("return_msg", "签名失败");
		}
		returnParams.put("return_code", "FAIL");
		return WeiXinJsPayUtil.toXml(returnParams);
	}
	
	private ResultCode<Map<String, String>> processOrder(String tradeno){
		Map<String, String> returnParams = new HashMap<String, String>();
		ResultCode<Map<String,String>> queryResult = WeiXinJsPayUtil.queryPayInfoForNewMer(tradeno);
		if(queryResult.isSuccess()){
			try{
				Map<String, String> params = queryResult.getRetval();
				int payfee = Integer.valueOf(params.get("paidamount"));
				String payseqno = params.get("payseqno");
				String mchid = params.get("mchid");
				Map<String, String> otherInfoMap = new HashMap<String, String>();
				otherInfoMap.put("openid", params.get("openid"));
				otherInfoMap.put("appid", params.get("appid"));
				otherInfoMap.put("payseqno", payseqno);
				otherInfoMap.put("payfee", params.get("paidamount"));
				otherInfoMap.put("tradeno", tradeno);
				otherInfoMap.put("paytype", PayConstant.PAYMETHOD_WEIXINH5);
				otherInfoMap.put("mchid", mchid);
				//咖啡订单
				if(StringUtils.startsWith(tradeno, "A")){
					ResultCode<WheelysOrder> result = paymentService.netPayOrder(tradeno,payseqno,payfee,PayConstant.GATEWAYCODE_WEIXIN,mchid,PayConstant.PAYMETHOD_WEIXINH5,otherInfoMap);
					if(result.isSuccess()) { 
						WeiXinSendUtils.sendMsg(result.getRetval());
					}else{
						dbLogger.warn(result.getMsg());
					}
				}else{
					ResultCode<CardCouponsOrder> result = paymentService.netPayCardOrder(tradeno,payseqno,payfee,PayConstant.GATEWAYCODE_WEIXIN,mchid,PayConstant.PAYMETHOD_WEIXINH5,otherInfoMap);
					if(!result.isSuccess()){
						dbLogger.warn(result.getMsg());
					}
				}
				returnParams.put("return_code", "SUCCESS");
				returnParams.put("return_msg", "OK");
				return ResultCode.getSuccessReturn(returnParams);
			}catch(Exception e){
				//TODO:通知客服！！！！
				dbLogger.warn("订单付款调用失败，尽快处理,订单号：" +  tradeno, e);
				returnParams.put("return_msg", "处理异常");
			}			
		}else{
			returnParams.put("return_msg", "查询校验错误");
		}
		return ResultCode.getFailureReturn(returnParams);
	}
	
}

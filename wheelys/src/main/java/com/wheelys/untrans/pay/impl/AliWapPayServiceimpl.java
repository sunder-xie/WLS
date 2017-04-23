package com.wheelys.untrans.pay.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.constant.PayConstant;
import com.wheelys.model.order.MchOrder;
import com.wheelys.model.order.WheelysOrder;
import com.wheelys.pay.AliWapPayUtil;
import com.wheelys.pay.config.AliPayConfigure;
import com.wheelys.service.PaymentService;
import com.wheelys.untrans.pay.AbstractPayService;
import com.wheelys.untrans.pay.AliWapPayService;

@Service("aliWapPayService")
public class AliWapPayServiceimpl extends AbstractPayService implements AliWapPayService {

	@Value("${ALIWAP.APPID}")
	private String APPID;
	@Value("${ALIWAP.SELLERID}")
	private String SELLERID;
	@Value("${ALIWAP.PUBLICKEY}")
	private String PUBLICKEY;
	@Value("${ALIWAP.PRIVATEKEY}")
	private String PRIVATEKEY;
	@Value("${ALIWAP.GATEWAY}")
	private String GATEWAY;
	@Value("${ALIWAP.PAYURL}")
	private String PAYURL;
	@Value("${ALIWAP.QUERYURL}")
	private String QUERYURL;
	@Value("${ALIWAP.NOTIFYURL}")
	private String NOTIFYURL;
	@Value("${ALIWAP.RETURNURL}")
	private String RETURNURL;
	
	@Autowired@Qualifier("paymentService")
	private PaymentService paymentService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		configMap.put(AliPayConfigure.APPID, APPID);
		configMap.put(AliPayConfigure.SELLERID, SELLERID);
		configMap.put(AliPayConfigure.PUBLICKEY, PUBLICKEY);
		configMap.put(AliPayConfigure.PRIVATEKEY, PRIVATEKEY);
		configMap.put(AliPayConfigure.GATEWAY, GATEWAY);
		configMap.put(AliPayConfigure.PAYURL, PAYURL);
		configMap.put(AliPayConfigure.NOTIFYURL, NOTIFYURL);
		configMap.put(AliPayConfigure.QUERYURL, QUERYURL);
		configMap.put(AliPayConfigure.NOTIFYURL, NOTIFYURL);
		configMap.put(AliPayConfigure.RETURNURL, RETURNURL);
	}

	@Override
	public String getWapPayUrl(WheelysOrder order, String clientIp) {
		return AliWapPayUtil.getWapPayUrl(order, clientIp, config.getAbsPath());
	}

	@Override
	public  ResultCode<Map<String, String>> processOrder(String tradeno) {
		Map<String, String> returnParams = new HashMap<String, String>();
		ResultCode<Map<String, String>> queryResult = AliWapPayUtil.queryPayInfo(tradeno);
		if (queryResult.isSuccess()) {
			try {
				Map<String, String> params = queryResult.getRetval();
				int payfee = (int) (Double.valueOf(params.get("paidamount")) * 100);
				String payseqno = params.get("payseqno");
				String mchid = params.get("mchid");
				Map<String, String> otherInfoMap = new HashMap<String, String>();
				otherInfoMap.put("appid", params.get("app_id"));
				otherInfoMap.put("payseqno", payseqno);
				otherInfoMap.put("payfee", payfee+"");
				otherInfoMap.put("tradeno", tradeno);
				otherInfoMap.put("paytype", PayConstant.PAYMETHOD_ALIH5);
				otherInfoMap.put("mchid", mchid);
				if(StringUtils.startsWith(tradeno, "A")){
					ResultCode<WheelysOrder> result = paymentService.netPayOrder(tradeno,payseqno,payfee,PayConstant.GATEWAYCODE_ALIPAY,mchid,PayConstant.PAYMETHOD_ALIH5,otherInfoMap);
					if(result.isSuccess()) {
						dbLogger.warn(result.getMsg());
					}
				}else{
					ResultCode<MchOrder> result = paymentService.payMchOrder(tradeno, payseqno, payfee, PayConstant.GATEWAYCODE_ALIPAY, mchid, PayConstant.PAYMETHOD_ALIH5, otherInfoMap);
					if (!result.isSuccess()) {
						dbLogger.warn(result.getMsg());
					}
				}
				returnParams.put("return_code", "SUCCESS");
				returnParams.put("return_msg", "OK");
				return ResultCode.getSuccessReturn(returnParams);
			} catch (Exception e) {
				// TODO:通知客服！！！！
				dbLogger.warn("订单付款调用失败，尽快处理,订单号：" + tradeno, e);
				returnParams.put("return_msg", "处理异常");
			}
		} else {
			returnParams.put("return_msg", "查询校验错误");
		}
		return ResultCode.getFailureReturn(returnParams);
	}
}

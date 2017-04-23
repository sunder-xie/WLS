package com.wheelys.service;

import java.util.Map;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.model.order.CardCouponsOrder;
import com.wheelys.model.order.MchOrder;
import com.wheelys.model.order.WheelysOrder;

public interface PaymentService {

	ResultCode<WheelysOrder> netPayOrder(String tradeno, String payseqno, int paidamount, String gatewaycode, String mchid, String paymethod, Map<String, String> otherInfoMap);

	ResultCode<WheelysOrder> payZeroOrder(WheelysOrder order);

	ResultCode<CardCouponsOrder> netPayCardOrder(String tradeno, String payseqno, int paidamount, String gatewaycode, String mchid, String paymethod, Map<String, String> otherInfoMap);
	
	ResultCode<MchOrder> payMchOrder(String tradeno, String payseqno, int paidamount, String gatewaycode, String mchid, String paymethod, Map<String, String> otherInfoMap);
	
}

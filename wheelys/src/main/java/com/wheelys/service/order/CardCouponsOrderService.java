package com.wheelys.service.order;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.model.order.CardCouponsOrder;
import com.wheelys.model.user.WheelysMember;

public interface CardCouponsOrderService {

	ResultCode<CardCouponsOrder> createValentineOrder(WheelysMember member, Long shopid, String ukey, String citycode, String datetype, String ordertitle, String type);

	CardCouponsOrder getOrder(String tradeno);

}

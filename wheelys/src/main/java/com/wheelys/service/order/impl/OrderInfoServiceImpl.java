package com.wheelys.service.order.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.DateUtil;
import com.wheelys.model.order.MemberOrderInfo;
import com.wheelys.model.order.WheelysOrder;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.MemberService;
import com.wheelys.service.order.OrderInfoService;
import com.wheelys.service.order.OrderService;
import com.wheelys.util.VmUtils;

@Service("orderInfoService")
public class OrderInfoServiceImpl extends BaseServiceImpl implements OrderInfoService {
	
	@Autowired@Qualifier("orderService")
	private OrderService orderService;
	
	@Autowired@Qualifier("memberService")
	private MemberService memberService;
	
	@Override
	public void saveOrUpdateOrderInfo(String tradeNo, String pamentWay) {
		WheelysOrder order = orderService.getOrderByTradeNo(tradeNo);
		Long memberid = order.getMemberid();
		MemberOrderInfo orderInfo = this.getOrderInfoByMemberId(memberid);
		if(VmUtils.isEmpObj(orderInfo)){
			// 为空，用户第一次下单
			WheelysMember wheelysMember = memberService.getWheelysMemberByMemberId(memberid);
			orderInfo = new MemberOrderInfo(wheelysMember,order);
			this.baseDao.saveObject(orderInfo);
		}else{
			// 不为空，更新部分字段
			orderInfo.setLastordertime(order.getPaidtime());
			orderInfo.setOrdernum(orderInfo.getOrdernum() + 1);
			orderInfo.setTotalnetpaid(orderInfo.getTotalnetpaid()+order.getNetpaid());
			orderInfo.setLastshopid(order.getShopid());
			orderInfo.setLasttradeno(order.getTradeno());
			orderInfo.setUpdatetime(DateUtil.getMillTimestamp());
			this.baseDao.updateObject(orderInfo);
		}
	}

	@Override
	public MemberOrderInfo getOrderInfoByMemberId(Long memberid) {
		return this.baseDao.getObject(MemberOrderInfo.class, memberid);
	}
	
}

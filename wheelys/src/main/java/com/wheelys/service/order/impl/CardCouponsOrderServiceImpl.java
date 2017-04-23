package com.wheelys.service.order.impl;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.DateUtil;
import com.wheelys.model.order.CardCouponsOrder;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.order.CardCouponsOrderService;

@Service("cardCouponsOrderService")
public class CardCouponsOrderServiceImpl extends BaseServiceImpl implements CardCouponsOrderService {

	@Override
	public ResultCode<CardCouponsOrder> createValentineOrder(WheelysMember member, Long shopid, String ukey, String citycode, String datetype, String ordertitle, String type) {
		CardCouponsOrder order = new CardCouponsOrder(member.getId(),member.getNickname(), ukey, citycode);
		order.setPayfee(6500);
		order.setTradeno(getTicketTradeNo());
		order.setOrdertitle(ordertitle);
		order.setQuantity(5);
		order.setMobile(member.getMobile());
		order.setShopid(shopid);
		order.setEbatchid("249");//{"249":"5",}
		order.setOtherinfo(datetype);
		this.baseDao.saveObject(order);
		return ResultCode.getSuccessReturn(order);
	}
	
	private String getTicketTradeNo() {
		String s = "C" + DateUtil.format(new Date(), "yyMMddHHmm");
		long num = getIndex()%100000;
		s += StringUtils.leftPad("" + num, 5, '0'); // 订单号
		return s;
	}
	
	private int getIndex(){
		int result = new Random().nextInt(9000);
		return result+1; 
	}

	@Override
	public CardCouponsOrder getOrder(String tradeno) {
		DetachedCriteria query = DetachedCriteria.forClass(CardCouponsOrder.class);
		query.add(Restrictions.eq("tradeno", tradeno));
		List<CardCouponsOrder> resultList = baseDao.findByCriteria(query);
		return resultList.isEmpty() ? null : resultList.get(0);
	}

}

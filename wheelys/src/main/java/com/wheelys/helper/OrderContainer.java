package com.wheelys.helper;

import java.util.List;

import com.wheelys.model.CafeShop;
import com.wheelys.model.discount.DiscountActivity;
import com.wheelys.model.order.MemberAddress;
import com.wheelys.model.order.WheelysOrder;
import com.wheelys.model.order.WheelysOrderDetail;
import com.wheelys.web.action.wap.vo.ElecCardVo;

public class OrderContainer {

	private CafeShop shop;
	private ElecCardVo card;
	private DiscountActivity discount;
	private WheelysOrder order;
	private MemberAddress memberAddress;
	private List<String> exprAddrList;
	private Boolean ifTakeoutSupport;
	private Boolean ifReservedstatus;
	private List<WheelysOrderDetail> detailList;

	public ElecCardVo getCard() {
		return card;
	}

	public void setCard(ElecCardVo card) {
		this.card = card;
	}

	public DiscountActivity getDiscount() {
		return discount;
	}

	public void setDiscount(DiscountActivity discount) {
		this.discount = discount;
	}

	public CafeShop getShop() {
		return shop;
	}

	public void setShop(CafeShop shop) {
		this.shop = shop;
	}

	public WheelysOrder getOrder() {
		return order;
	}

	public void setOrder(WheelysOrder order) {
		this.order = order;
	}

	public MemberAddress getMemberAddress() {
		return memberAddress;
	}

	public void setMemberAddress(MemberAddress memberAddress) {
		this.memberAddress = memberAddress;
	}

	public List<String> getExprAddrList() {
		return exprAddrList;
	}

	public void setExprAddrList(List<String> exprAddrList) {
		this.exprAddrList = exprAddrList;
	}

	public Boolean getIfTakeoutSupport() {
		return ifTakeoutSupport;
	}

	public void setIfTakeoutSupport(Boolean ifTakeoutSupport) {
		this.ifTakeoutSupport = ifTakeoutSupport;
	}

	public Boolean getIfReservedstatus() {
		return ifReservedstatus;
	}

	public void setIfReservedstatus(Boolean ifReservedstatus) {
		this.ifReservedstatus = ifReservedstatus;
	}

	public List<WheelysOrderDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<WheelysOrderDetail> detailList) {
		this.detailList = detailList;
	}

}

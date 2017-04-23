package com.wheelys.web.action.admin.vo;

import java.sql.Timestamp;

import com.wheelys.model.order.CardCouponsOrder;

public class CardCouponsOrderVo {

	private String shopname; // 店铺名称
	private String tradeno;	 // 订单编号
	private String nickname;//用户名    OpenMember类
	private String mobile;//手机号  WheelysMember类
	private Timestamp paidtime;//购买时间
	private Integer payfee;//付款金额
	private String otherinfo;//领花时间

	public CardCouponsOrderVo() {
		super();
	}
	public CardCouponsOrderVo(CardCouponsOrder card) {
		this.tradeno=card.getTradeno();
		this.paidtime=card.getPaidtime();
		this.payfee=card.getPayfee();
		this.otherinfo=card.getOtherinfo();
	}
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getTradeno() {
		return tradeno;
	}
	public void setTradeno(String tradeno) {
		this.tradeno = tradeno;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Timestamp getPaidtime() {
		return paidtime;
	}
	public void setPaidtime(Timestamp paidtime) {
		this.paidtime = paidtime;
	}
	public Integer getPayfee() {
		return payfee;
	}
	public void setPayfee(Integer payfee) {
		this.payfee = payfee;
	}
	public String getOtherinfo() {
		return otherinfo;
	}
	public void setOtherinfo(String otherinfo) {
		this.otherinfo = otherinfo;
	}
	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

}

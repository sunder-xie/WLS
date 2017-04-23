package com.wheelys.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.model.BaseObject;
import com.wheelys.util.DateUtil;
import com.wheelys.model.pay.WheelysOrder;
import com.wheelys.model.user.WheelysMember;

public class MemberOrderInfo extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2543059485039171624L;
	private Long memberid; 				// memberid
	private Timestamp regstertime; 		// 用户注册时间
	private String tradeno;				// 首单订单号
	private Timestamp firstordertime; 	// 用户第一次下单时间
	private Long firstshopid; 			// 第一次下单店铺
	private Long lastshopid;		// 最后一次下单店铺
	private String lasttradeno;			// 最后一次下单订单号
	private Timestamp lastordertime;	// 最后一次下单时间
	private Integer totalnetpaid;		// 总共支付金额
	private Integer ordernum; 			// 用户订单总数
	private Timestamp createtime; 		// 创建时间
	private Timestamp updatetime; 		// 更新时间
	
	public MemberOrderInfo(){}

	public MemberOrderInfo(WheelysMember member, WheelysOrder order) {
		this.memberid = member.getId();
		this.regstertime = member.getAddtime();
		this.tradeno = order.getTradeno();
		this.firstordertime = order.getPaidtime();
		this.firstshopid = order.getShopid();
		this.lastordertime = this.firstordertime;
		this.ordernum = 1;
		this.createtime = DateUtil.getMillTimestamp();
		this.updatetime = this.createtime;
	}

	public Long getMemberid() {
		return memberid;
	}

	public void setMemberid(Long memberid) {
		this.memberid = memberid;
	}

	public Timestamp getRegstertime() {
		return regstertime;
	}

	public void setRegstertime(Timestamp regstertime) {
		this.regstertime = regstertime;
	}

	public String getTradeno() {
		return tradeno;
	}

	public void setTradeno(String tradeno) {
		this.tradeno = tradeno;
	}

	public Timestamp getFirstordertime() {
		return firstordertime;
	}

	public void setFirstordertime(Timestamp firstordertime) {
		this.firstordertime = firstordertime;
	}

	public Long getFirstshopid() {
		return firstshopid;
	}

	public void setFirstshopid(Long firstshopid) {
		this.firstshopid = firstshopid;
	}

	public Timestamp getLastordertime() {
		return lastordertime;
	}

	public void setLastordertime(Timestamp lastordertime) {
		this.lastordertime = lastordertime;
	}

	public Integer getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(Integer ordernum) {
		this.ordernum = ordernum;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	public Long getLastshopid() {
		return lastshopid;
	}

	public void setLastshopid(Long lastshopid) {
		this.lastshopid = lastshopid;
	}

	public String getLasttradeno() {
		return lasttradeno;
	}

	public void setLasttradeno(String lasttradeno) {
		this.lasttradeno = lasttradeno;
	}

	public Integer getTotalnetpaid() {
		return totalnetpaid;
	}

	public void setTotalnetpaid(Integer totalnetpaid) {
		this.totalnetpaid = totalnetpaid;
	}

	@Override
	public Serializable realId() {
		return memberid;
	}

}

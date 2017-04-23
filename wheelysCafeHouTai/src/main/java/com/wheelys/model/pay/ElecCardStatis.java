package com.wheelys.model.pay;

import java.io.Serializable;

import com.wheelys.model.BaseObject;

public class ElecCardStatis extends BaseObject {
	
	private static final long serialVersionUID = 375454650779947L;
	private Long id;	//ID
	private Integer cardcount; // 优惠券总数
	private Integer lqcount; // 领取数
	private Integer lqmoney; // 领取金额
	private Integer usecount; // 使用数
	private Integer usemoney; // 使用金额
	private Integer gqcount; // 过期数
	
	public ElecCardStatis(){
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getCardcount() {
		return cardcount;
	}
	public void setCardcount(Integer cardcount) {
		this.cardcount = cardcount;
	}
	public Integer getLqcount() {
		return lqcount;
	}
	public void setLqcount(Integer lqcount) {
		this.lqcount = lqcount;
	}
	public Integer getLqmoney() {
		return lqmoney;
	}
	public void setLqmoney(Integer lqmoney) {
		this.lqmoney = lqmoney;
	}
	public Integer getUsecount() {
		return usecount;
	}
	public void setUsecount(Integer usecount) {
		this.usecount = usecount;
	}
	public Integer getUsemoney() {
		return usemoney;
	}
	public void setUsemoney(Integer usemoney) {
		this.usemoney = usemoney;
	}
	public Integer getGqcount() {
		return gqcount;
	}
	public void setGqcount(Integer gqcount) {
		this.gqcount = gqcount;
	}
	public Serializable realId() {
		return id;
	}
}

package com.wheelys.web.action.admin.ticket.vo;

import com.wheelys.model.pay.ElecCard;

public class ElecCardBatchVo {
	private Integer lqcount; // 领取数
	private Integer lqmoney; // 领取金额
	private Integer usecount; // 使用数
	private Integer usemoney; // 使用金额
	private Integer gqcount; // 过期数
	private String status; // 状态
	
	
	public ElecCardBatchVo(ElecCard card) {
		super();
		this.lqcount=0;
		this.usecount=0;
		this.gqcount=0;
		this.lqmoney=0;
		this.usemoney=0;
		this.status=card.getStatus();
		
		
	}
	public ElecCardBatchVo() {
		super();
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	
}

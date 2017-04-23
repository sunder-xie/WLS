package com.wheelys.web.action.report.vo.summaryVo;

import java.io.Serializable;
import java.util.Date;

import com.wheelys.model.BaseObject;
import com.wheelys.util.DateUtil;

public class ReportSummaryActive extends BaseObject {

	/**
	 * 市场活动summary表统计
	 */
	private static final long serialVersionUID = -6144430818836104514L;

	@Override
	public Serializable realId() {
		return null;
	}
	private Integer shopcount;//直营店营业店铺数量
	private Integer subnetpaid; // 每家店当月实付金额总计
	private Integer subquantity; //每家店当月杯数总计
	private Integer avquantity;//当月店均日均杯数
	private Integer avnetpaid;//当月店均日均每杯价格
	private Date date;
	
	public ReportSummaryActive(Date day) {
		super();
		this.date=DateUtil.getLastTimeOfDay(day);
		this.shopcount=0;
		this.subnetpaid=0;
		this.avquantity=0;
		this.avnetpaid=0;
		this.subquantity=0;
	}
	public ReportSummaryActive() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Integer getSubquantity() {
		return subquantity;
	}
	public void setSubquantity(Integer subquantity) {
		this.subquantity = subquantity;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getShopcount() {
		return shopcount;
	}
	public void setShopcount(Integer shopcount) {
		this.shopcount = shopcount;
	}
	public Integer getSubnetpaid() {
		return subnetpaid;
	}
	public void setSubnetpaid(Integer subnetpaid) {
		this.subnetpaid = subnetpaid;
	}
	public Integer getAvquantity() {
		return avquantity;
	}
	public void setAvquantity(Integer avquantity) {
		this.avquantity = avquantity;
	}
	public Integer getAvnetpaid() {
		return avnetpaid;
	}
	public void setAvnetpaid(Integer avnetpaid) {
		this.avnetpaid = avnetpaid;
	}
	
}

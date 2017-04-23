package com.wheelys.web.action.report.vo;

import java.io.Serializable;
import com.wheelys.model.BaseObject;
import com.wheelys.util.DateUtil;
import com.wheelys.model.report.ReportOrderDatePaymethod;
public class ReportBackSellProductVo extends BaseObject {

	/**
	 * 月份统计后半段
	 */
	private static final long serialVersionUID = -694588182224557360L;
	
	private String date; // 经营时间
	private Long shopid; // 店铺id
	private String shopname;// 店铺名字
	private Integer subtotalfee; // 总金额
	private Integer subquantity; // 总杯数
	private String sellmethod;	//销售方式
	

	public Serializable realId() {
		return null;
	}
	public ReportBackSellProductVo() {
		super();
	}
	
	public ReportBackSellProductVo(ReportOrderDatePaymethod reportpaymethod) {
		super();
		this.date=DateUtil.formatDate(reportpaymethod.getDate());
		this.shopid=reportpaymethod.getShopid();
		this.shopname=reportpaymethod.getShopname();
		this.subtotalfee=0;
		this.subquantity=0;
	}
	
	public String getSellmethod() {
		return sellmethod;
	}
	public void setSellmethod(String sellmethod) {
		this.sellmethod = sellmethod;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Long getShopid() {
		return shopid;
	}
	public void setShopid(Long shopid) {
		this.shopid = shopid;
	}
	public String getShopname() {
		return shopname;
	}
	public void setShopname(String shopname) {
		this.shopname = shopname;
	}
	public Integer getSubtotalfee() {
		return subtotalfee;
	}
	public void setSubtotalfee(Integer subtotalfee) {
		this.subtotalfee = subtotalfee;
	}
	public Integer getSubquantity() {
		return subquantity;
	}
	public void setSubquantity(Integer subquantity) {
		this.subquantity = subquantity;
	}
	
	
}

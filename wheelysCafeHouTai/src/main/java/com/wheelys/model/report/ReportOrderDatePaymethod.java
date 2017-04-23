package com.wheelys.model.report;

import java.io.Serializable;
import java.util.Date;

import com.wheelys.model.BaseObject;
import com.wheelys.model.CafeShop;

public class ReportOrderDatePaymethod extends BaseObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8782269919411021466L;
	private Long id;// id
	private Long shopid; // 店铺id
	private Date date; // 查询时间
	private String shopname;// 店铺名字
	private String paymethod; // 支付方式
	private Integer totalfee; // 原价金额
	private Integer netpaid; // 实付金额
	private Integer quantity; // 杯数
	private Integer ordercount; // 多少单
	private Integer discount; // 优惠金额
	private String ukey; // 时间_shopid_paymethod
	private String citycode;// 城市code
	private Integer paybrokerage;// 支付扣点
	private Integer companybrokerage;// 公司扣点
	private Double companyrate;// 公司扣点比率
	private Double payrate;// 支付扣点比率
	private Integer settlementamount;// 应结算金额
	private String sellmethod;//销售方式
	
	public ReportOrderDatePaymethod(){}

	public ReportOrderDatePaymethod(CafeShop cafeShop) {
		this.shopid = cafeShop.getShopid();
		this.shopname = cafeShop.getShopname();
		this.quantity = 0;
		this.netpaid = 0;
		this.ordercount = 0;
		this.totalfee = 0;
		this.discount = 0;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getShopid() {
		return shopid;
	}

	public void setShopid(Long shopid) {
		this.shopid = shopid;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getPaymethod() {
		return paymethod;
	}

	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}

	public Integer getTotalfee() {
		return totalfee;
	}

	public void setTotalfee(Integer totalfee) {
		this.totalfee = totalfee;
	}

	public Integer getNetpaid() {
		return netpaid;
	}

	public void setNetpaid(Integer netpaid) {
		this.netpaid = netpaid;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	public Integer getOrdercount() {
		return ordercount;
	}

	public void setOrdercount(Integer ordercount) {
		this.ordercount = ordercount;
	}

	public String getUkey() {
		return ukey;
	}

	public void setUkey(String ukey) {
		this.ukey = ukey;
	}

	@Override
	public Serializable realId() {
		return id;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public Integer getPaybrokerage() {
		return paybrokerage;
	}

	public void setPaybrokerage(Integer paybrokerage) {
		this.paybrokerage = paybrokerage;
	}

	public Integer getCompanybrokerage() {
		return companybrokerage;
	}

	public void setCompanybrokerage(Integer companybrokerage) {
		this.companybrokerage = companybrokerage;
	}

	public Double getCompanyrate() {
		return companyrate;
	}

	public void setCompanyrate(Double companyrate) {
		this.companyrate = companyrate;
	}

	public Double getPayrate() {
		return payrate;
	}

	public void setPayrate(Double payrate) {
		this.payrate = payrate;
	}

	public Integer getSettlementamount() {
		return settlementamount;
	}

	public void setSettlementamount(Integer settlementamount) {
		this.settlementamount = settlementamount;
	}

	public String getSellmethod() {
		return sellmethod;
	}

	public void setSellmethod(String sellmethod) {
		this.sellmethod = sellmethod;
	}

}

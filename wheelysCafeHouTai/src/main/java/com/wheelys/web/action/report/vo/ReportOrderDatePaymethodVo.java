package com.wheelys.web.action.report.vo;

public class ReportOrderDatePaymethodVo {

	private Long shopid;
	private String shopname;// 店铺名字
	private String paymethod; // 支付方式
	private Integer quantity; // 杯数
	private Integer totalfee; // 总金额
	private Integer discount; // 优惠金额
	private Integer netpaid; // 实际金额
	private Integer ordercount; // 实际金额
	private String date; // 日期
	private String citycode;// 城市code
	private Integer paybrokerage;// 支付扣点
	private Double payrate;// 支付扣点比率
	private Integer companybrokerage;// 公司扣点
	private Double companyrate;// 公司扣点比率
	private Integer settlementamount;// 应结算金额

	public ReportOrderDatePaymethodVo() {
		super();
	}

	public ReportOrderDatePaymethodVo(String date) {
		super();
		this.date = date;
		this.paybrokerage=0;
		this.payrate=0d;
		this.companybrokerage=0;
		this.companyrate=0d;
		this.settlementamount=0;
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

	public String getPaymethod() {
		return paymethod;
	}

	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getTotalfee() {
		return totalfee;
	}

	public void setTotalfee(Integer totalfee) {
		this.totalfee = totalfee;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	public Integer getNetpaid() {
		return netpaid;
	}

	public void setNetpaid(Integer netpaid) {
		this.netpaid = netpaid;
	}

	public Integer getOrdercount() {
		return ordercount;
	}

	public void setOrdercount(Integer ordercount) {
		this.ordercount = ordercount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public Integer getAvgprice() {
		if (totalfee != null && totalfee > 0 && quantity != null && quantity > 0) {
			return totalfee / quantity;
		}
		return 0;
	}

}

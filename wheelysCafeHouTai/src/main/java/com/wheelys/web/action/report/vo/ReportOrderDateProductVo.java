package com.wheelys.web.action.report.vo;

import com.wheelys.util.DateUtil;
import com.wheelys.model.report.ReportOrderDateProduct;

public class ReportOrderDateProductVo {

	private Long productid;	//ID
	private String productname;// 店铺名字
	private String shopname;// 店铺名字
	private String date; // 查询时间
	private Integer price; // 金额
	private Integer bomcost; // 成本
	private Integer quantity; // 杯数
	private Integer totalfee; // 总金额
	private Integer discount; // 优惠金额
	private Integer paidfee; // 实际金额
	private Integer discountnum; // 优惠杯数

	public ReportOrderDateProductVo() {
		super();
	}

	public ReportOrderDateProductVo(ReportOrderDateProduct reportOrderDateProduct) {
		this.productid = reportOrderDateProduct.getProductid();
		this.productname = reportOrderDateProduct.getProductname();
		this.shopname = reportOrderDateProduct.getShopname();
		this.date = DateUtil.formatDate(reportOrderDateProduct.getDate());
		this.price = reportOrderDateProduct.getPrice();
		this.bomcost = 0;
		this.quantity = 0;
		this.totalfee = 0;
		this.discount = 0;
		this.paidfee = 0;
		this.discountnum = 0;
	}

	public Long getProductid() {
		return productid;
	}

	public void setProductid(Long productid) {
		this.productid = productid;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getBomcost() {
		return bomcost;
	}

	public void setBomcost(Integer bomcost) {
		this.bomcost = bomcost;
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

	public Integer getPaidfee() {
		return paidfee;
	}

	public void setPaidfee(Integer paidfee) {
		this.paidfee = paidfee;
	}

	public Integer getDiscountnum() {
		return discountnum;
	}

	public void setDiscountnum(Integer discountnum) {
		this.discountnum = discountnum;
	}

}

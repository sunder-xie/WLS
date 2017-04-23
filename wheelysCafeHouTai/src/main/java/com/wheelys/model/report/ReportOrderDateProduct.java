package com.wheelys.model.report;

import java.io.Serializable;
import java.util.Date;

import com.wheelys.model.BaseObject;

public class ReportOrderDateProduct extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7694978470817773716L;
	private Long id;//id
	private Long productid;	//品项ID
	private Long shopid; // 店铺id
	private String productname;// 店铺名字
	private String shopname;// 店铺名字
	private Date date; // 查询时间
	private Integer price; // 总金额
	private Integer bomcost; // 成本
	private Integer quantity; // 杯数
	private Integer totalfee; // 总金额
	private Integer discount; //优惠金额
	private Integer paidfee; // 实际金额
	private Integer discountnum; // 优惠杯数
	private String ukey;	//shopid+productid+date
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductid() {
		return productid;
	}

	public void setProductid(Long productid) {
		this.productid = productid;
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

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

}

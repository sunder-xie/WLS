package com.wheelys.model.merchant;

import java.io.Serializable;

import com.wheelys.model.BaseObject;

public class MchOrderDetail extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6157919988429329501L;
	private Long id; // 主键
	private Long orderid; // 订单ID
	private Long productid; // 商品ID
	private Integer price; // 单价
	private String productname; // 商品名称
	private String productimg; // 图片
	private Integer quantity; // 数量
	private Integer totalfee; // 总价
	private Integer paidfee; // 需要支付的金额
	private Integer discount; // 折扣
	private String unit; // 单位
	private String description;// 商品描述

	public MchOrderDetail() {
	}

	public MchOrderDetail(MchProduct mchProduct, Integer quantity) {
		this.productid = mchProduct.getId();
		this.productname = mchProduct.getName();
		this.productimg = mchProduct.getImgurl();
		this.unit = mchProduct.getUnit();
		this.description = mchProduct.getDescription();
		this.quantity = quantity;
		this.price = mchProduct.getPrice();
		this.totalfee = this.quantity * this.price;
		this.paidfee = this.totalfee;
		this.discount = 0;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public Long getProductid() {
		return productid;
	}

	public void setProductid(Long productid) {
		this.productid = productid;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getProductimg() {
		return productimg;
	}

	public void setProductimg(String productimg) {
		this.productimg = productimg;
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

	public Integer getPaidfee() {
		return paidfee;
	}

	public void setPaidfee(Integer paidfee) {
		this.paidfee = paidfee;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Serializable realId() {
		return id;
	}

}

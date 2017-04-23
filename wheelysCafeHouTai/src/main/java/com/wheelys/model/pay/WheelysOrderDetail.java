package com.wheelys.model.pay;

import java.io.Serializable;

import com.wheelys.model.BaseObject;
import com.wheelys.model.CafeProduct;

public class WheelysOrderDetail extends BaseObject {
	
	private static final long serialVersionUID = -5949883901674406982L;
	
	private Long id;			//主键
	private Long orderid;	//订单ID
	private Long productid;	//商品ID
	private Integer price;		//单价
	private String productname;//商品名称
	private String productename;//商品名称
	private String productimg;	//图片
	private String description;//{冷1热2默认0}
	private Integer quantity;	//数量
	private Integer totalfee;	//总价
	private Integer paidfee;	//需要支付的金额
	private Integer discount;	//折扣
	private Integer discountnum;//折扣杯数
	
	public WheelysOrderDetail(){}
	
	public WheelysOrderDetail(CafeProduct product, Integer quantity, String description, Integer price){
		this.productid = product.getId();
		this.productname = product.getName();
		this.productename = product.getEnname();
		this.productimg = product.getImg();
		this.price = product.getPrice();
		if(this.price+3 == price){
			this.price = price;
		}
		this.quantity = quantity;
		this.totalfee = this.price * this.quantity * 100;
		this.discount = 0;
		this.discountnum = 0;
		this.paidfee = this.totalfee;
		this.description = description;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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

	public String getProductename() {
		return productename;
	}

	public void setProductename(String productename) {
		this.productename = productename;
	}

	public String getProductimg() {
		return productimg;
	}

	public void setProductimg(String productimg) {
		this.productimg = productimg;
	}

	public Integer getTotalfee() {
		return totalfee;
	}

	public void setTotalfee(Integer totalfee) {
		this.totalfee = totalfee;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	@Override
	public Serializable realId() {
		return id;
	}
}

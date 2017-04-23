
package com.wheelys.web.action.openapi.vo;

import com.wheelys.model.order.WheelysOrderDetail;
import com.wheelys.util.VmUtils;

public class OpenApiOrderDetailVo {

	private Long id; // 主键
	private Long orderid; // 订单ID
	private Long productid; // 商品ID
	private Integer price; // 单价
	private String productname;// 商品名称
	private String productename;// 商品名称
	private String productimg; // 图片
	private String description;// {冷1热2默认0}
	private Integer quantity; // 数量
	private String totalfee; // 总价
	private String paidfee; // 需要支付的金额
	private String discount; // 折扣
	private Integer discountnum;// 折扣杯数
	
	public OpenApiOrderDetailVo() {
		super();
	}

	public OpenApiOrderDetailVo(WheelysOrderDetail detail, String picpath) {
		super();
		this.id = detail.getId();
		this.orderid = detail.getOrderid();
		this.productid = detail.getProductid();
		this.price = detail.getPrice();
		this.productname = detail.getProductname();
		this.productename = detail.getProductename();
		this.productimg = picpath+detail.getProductimg();
		this.description = detail.getDescription();
		this.quantity = detail.getQuantity();
		this.totalfee = VmUtils.getAmount(detail.getTotalfee());
		this.paidfee = VmUtils.getAmount(detail.getPaidfee());
		this.discount = VmUtils.getAmount(detail.getDiscount());
		this.discountnum = detail.getDiscountnum();
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getTotalfee() {
		return totalfee;
	}

	public void setTotalfee(String totalfee) {
		this.totalfee = totalfee;
	}

	public String getPaidfee() {
		return paidfee;
	}

	public void setPaidfee(String paidfee) {
		this.paidfee = paidfee;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public Integer getDiscountnum() {
		return discountnum;
	}

	public void setDiscountnum(Integer discountnum) {
		this.discountnum = discountnum;
	}

}

package com.wheelys.web.action.openapi.vo;

import com.wheelys.model.merchant.MchOrderDetail;
import com.wheelys.model.merchant.MchProduct;
import com.wheelys.util.VmUtils;

public class MchOrderDetailVo {

	private Long productid; // 商品ID
	private Double price; // 单价
	private String productname; // 商品名称
	private String productimg; // 图片
	private String unit; // 单位
	private String description;// 商品描述
	private Integer quantity; // 数量
	private Double totalfee; // 总价
	private Double paidfee; // 需要支付的金额
	private Double discount; // 折扣
	
	
	public MchOrderDetailVo() {
		super();
	}

	public MchOrderDetailVo(MchOrderDetail detail,MchProduct mchProduct) {
		this.productid = detail.getProductid();
		this.productname = detail.getProductname();
		this.productimg = detail.getProductimg();
		this.quantity = detail.getQuantity();
		this.price = VmUtils.getDoubleAmount(detail.getPrice());
		this.totalfee = VmUtils.getDoubleAmount(detail.getTotalfee());
		this.paidfee = VmUtils.getDoubleAmount(detail.getPaidfee());
		this.discount = VmUtils.getDoubleAmount(detail.getDiscount());
		if(mchProduct != null){
			this.unit = mchProduct.getUnit();
			this.description = mchProduct.getDescription();
		}
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

	public Long getProductid() {
		return productid;
	}

	public void setProductid(Long productid) {
		this.productid = productid;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
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

	public Double getTotalfee() {
		return totalfee;
	}

	public void setTotalfee(Double totalfee) {
		this.totalfee = totalfee;
	}

	public Double getPaidfee() {
		return paidfee;
	}

	public void setPaidfee(Double paidfee) {
		this.paidfee = paidfee;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}


}

package com.wheelys.model;

import java.io.Serializable;

import com.wheelys.model.BaseObject;

public class ProductShop extends BaseObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1650743432870938782L;
	private Long psid;// 主键ID,
	private Long psshopid;// 店铺ID,
	private Long psprodid;// 商品ID,
	private Integer psorder;// 排序顺序,
	private Integer display;// 1商品上架 0 下架,
	private Float discount;// 折扣,
	private String delstatus;//删除状态
	
	public ProductShop() {}

	public ProductShop(Long psshopid, Long psprodid) {
		this.psshopid = psshopid;
		this.psprodid = psprodid;
		this.psorder=99;
		this.display=1;
		this.discount=0.0f;
		this.delstatus = "N";
	}

	public String getDelstatus() {
		return delstatus;
	}

	public void setDelstatus(String delstatus) {
		this.delstatus = delstatus;
	}

	public Long getPsid() {
		return psid;
	}

	public void setPsid(Long psid) {
		this.psid = psid;
	}

	public Long getPsshopid() {
		return psshopid;
	}

	public void setPsshopid(Long psshopid) {
		this.psshopid = psshopid;
	}

	public Long getPsprodid() {
		return psprodid;
	}

	public void setPsprodid(Long psprodid) {
		this.psprodid = psprodid;
	}

	public Integer getPsorder() {
		return psorder;
	}

	public void setPsorder(Integer psorder) {
		this.psorder = psorder;
	}

	public Integer getDisplay() {
		return display;
	}

	public void setDisplay(Integer display) {
		this.display = display;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	@Override
	public Serializable realId() {
		return psid;
	}

}

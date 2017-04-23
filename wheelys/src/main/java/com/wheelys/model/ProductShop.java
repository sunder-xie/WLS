package com.wheelys.model;

import java.io.Serializable;

import com.wheelys.model.BaseObject;

public class ProductShop extends BaseObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1650743432870938782L;
	private Long id;// 主键ID,
	private Long shopid;// 店铺ID,
	private Long productid;// 商品ID,
	private Integer psorder;// 排序顺序,
	private Integer display;// 1商品上架 0 下架,
	private String delstatus;// 删除状态

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

	public Long getProductid() {
		return productid;
	}

	public void setProductid(Long productid) {
		this.productid = productid;
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

	public String getDelstatus() {
		return delstatus;
	}

	public void setDelstatus(String delstatus) {
		this.delstatus = delstatus;
	}

	@Override
	public Serializable realId() {
		return id;
	}

}

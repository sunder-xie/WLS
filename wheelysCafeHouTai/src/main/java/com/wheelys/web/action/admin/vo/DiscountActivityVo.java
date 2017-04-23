package com.wheelys.web.action.admin.vo;

import com.wheelys.model.discount.DiscountActivity;

public class DiscountActivityVo {
	private DiscountActivity discountActity;
	private String shopname;// 店铺名字

	public DiscountActivityVo() {

	} 
	
	public DiscountActivityVo(DiscountActivity discountActity, String shopname) {
		super();
		this.discountActity = discountActity;
		this.shopname = shopname;
	}

	public DiscountActivity getDiscountActity() {
		return discountActity;
	}

	public void setDiscountActity(DiscountActivity discountActity) {
		this.discountActity = discountActity;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}
	
}

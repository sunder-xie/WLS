package com.wheelys.web.action.admin.vo;

import com.wheelys.model.CafeProduct;
import com.wheelys.model.ProductShop;

public class ProductShopVo {

	private Long itemid;
	private ProductShop productShop;
	private CafeProduct cafeProduct;

	public ProductShopVo(ProductShop productShop, CafeProduct cafeProduct) {
		super();
		this.itemid = cafeProduct.getItemid();
		this.productShop = productShop;
		this.cafeProduct = cafeProduct;
	}

	public Long getItemid() {
		return itemid;
	}

	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}

	public ProductShop getProductShop() {
		return productShop;
	}

	public void setProductShop(ProductShop productShop) {
		this.productShop = productShop;
	}

	public CafeProduct getCafeProduct() {
		return cafeProduct;
	}

	public void setCafeProduct(CafeProduct cafeProduct) {
		this.cafeProduct = cafeProduct;
	}

}

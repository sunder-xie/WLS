package com.wheelys.web.action.openapi.vo;

import java.util.List;

import com.wheelys.model.CafeItem;

public class ShopProductVo {

	private CafeItem item;
	private List<OpneApiCafeProductVo> productList;

	public CafeItem getItem() {
		return item;
	}

	public void setItem(CafeItem item) {
		this.item = item;
	}

	public List<OpneApiCafeProductVo> getProductList() {
		return productList;
	}

	public void setProductList(List<OpneApiCafeProductVo> productList) {
		this.productList = productList;
	}

}

package com.wheelys.web.action.openapi.vo;

import java.util.List;

public class OpneApiMchProductItemVo {

	private Long id; // id
	private String name; // 商品名
	private List<OpneApiMchProductVo> productList;
	
	public OpneApiMchProductItemVo(Long id, String name, List<OpneApiMchProductVo> productList) {
		this.id = id;
		this.name = name;
		this.productList = productList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<OpneApiMchProductVo> getProductList() {
		return productList;
	}

	public void setProductList(List<OpneApiMchProductVo> productList) {
		this.productList = productList;
	}

}

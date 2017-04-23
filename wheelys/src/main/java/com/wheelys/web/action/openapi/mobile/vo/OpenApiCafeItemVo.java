package com.wheelys.web.action.openapi.mobile.vo;

import java.util.List;

import com.wheelys.util.BeanUtil;
import com.wheelys.model.CafeItem;

public class OpenApiCafeItemVo {

	private Long id;
	private String name;
	private List<OpenApiCafeProductVo> productList;

	public OpenApiCafeItemVo(CafeItem item, List<OpenApiCafeProductVo> productVoList) {
		BeanUtil.copyProperties(this, item);
		this.productList = productVoList;
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

	public List<OpenApiCafeProductVo> getProductList() {
		return productList;
	}

	public void setProductList(List<OpenApiCafeProductVo> productList) {
		this.productList = productList;
	}

}

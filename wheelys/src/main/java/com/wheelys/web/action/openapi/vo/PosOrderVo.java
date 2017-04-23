package com.wheelys.web.action.openapi.vo;

import java.util.List;

public class PosOrderVo {

	private OpenApiOrderVo order;
	private OpenApiMemberAddressVo address;
	private List<OpenApiOrderDetailVo> detailList;

	public OpenApiOrderVo getOrder() {
		return order;
	}

	public void setOrder(OpenApiOrderVo order) {
		this.order = order;
	}

	public OpenApiMemberAddressVo getAddress() {
		return address;
	}

	public void setAddress(OpenApiMemberAddressVo address) {
		this.address = address;
	}

	public List<OpenApiOrderDetailVo> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<OpenApiOrderDetailVo> detailList) {
		this.detailList = detailList;
	}

}

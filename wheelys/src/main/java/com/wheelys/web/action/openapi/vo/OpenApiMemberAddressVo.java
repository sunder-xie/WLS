package com.wheelys.web.action.openapi.vo;

import com.wheelys.model.order.MemberAddress;

public class OpenApiMemberAddressVo {

	private String username; // 姓名
	private String mobile; // 联系电话
	private String address; // 地址
	private String detailaddress;// 详细地址
	
	public OpenApiMemberAddressVo(MemberAddress memberAddress) {
		super();
		this.username = memberAddress.getUsername();
		this.mobile = memberAddress.getMobile();
		this.address = memberAddress.getAddress();
		this.detailaddress = memberAddress.getDetailaddress();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDetailaddress() {
		return detailaddress;
	}

	public void setDetailaddress(String detailaddress) {
		this.detailaddress = detailaddress;
	}

}

package com.wheelys.web.action.openapi.vo;

public class PosMchUser {

	private String userencode;
	private Long shopid;
	private String citycode;
	private String shopimg;
	private String username;
	private String shopname;
	private String address;
	private String status;
	private String booking; //是否开放预订
	
	public PosMchUser() {
		super();
	}

	public PosMchUser(String userencode, Long shopid, String citycode) {
		super();
		this.userencode = userencode;
		this.shopid = shopid;
		this.citycode = citycode;
		this.username = "test1";
		this.shopname = "test2";
		this.address = "test3";
	}

	public String getBooking() {
		return booking;
	}

	public void setBooking(String booking) {
		this.booking = booking;
	}

	public String getUserencode() {
		return userencode;
	}

	public void setUserencode(String userencode) {
		this.userencode = userencode;
	}

	public Long getShopid() {
		return shopid;
	}

	public void setShopid(Long shopid) {
		this.shopid = shopid;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getShopimg() {
		return shopimg;
	}

	public void setShopimg(String shopimg) {
		this.shopimg = shopimg;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}

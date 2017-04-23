package com.wheelys.web.action.admin.vo;

public class CafeShopVo {
	private Long shopid; // 主键
	private String shopaccount; // 账号
	private String shopname; // 店铺名称
	private String citycode; // 城市Code
	private String contacts; // 联系人
	private String shoptelephone; // 商家电话
	private String shoptime; // 商家营业时间
	private String operatorName;// 运营商名称
	private String booking;//开关店
	private String esname;
	

	public Long getShopid() {
		return shopid;
	}

	public void setShopid(Long shopid) {
		this.shopid = shopid;
	}

	public String getShopaccount() {
		return shopaccount;
	}

	public void setShopaccount(String shopaccount) {
		this.shopaccount = shopaccount;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getShoptelephone() {
		return shoptelephone;
	}

	public void setShoptelephone(String shoptelephone) {
		this.shoptelephone = shoptelephone;
	}

	public String getShoptime() {
		return shoptime;
	}

	public void setShoptime(String shoptime) {
		this.shoptime = shoptime;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getEsname() {
		return esname;
	}

	public void setEsname(String esname) {
		this.esname = esname;
	}

	public String getBooking() {
		return booking;
	}

	public void setBooking(String booking) {
		this.booking = booking;
	}
	
}

package com.wheelys.web.action.admin.vo;

import com.wheelys.model.citymanage.CityManage;

public class CityManageVo {
	private CityManage city;
	private String companyname;
	private int shopname;

	public CityManageVo(CityManage city, String companyname, int shopname) {
		super();
		this.city = city;
		this.companyname = companyname;
		this.shopname = shopname;
	}

	public CityManage getCity() {
		return city;
	}

	public void setCity(CityManage city) {
		this.city = city;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public int getShopname() {
		return shopname;
	}

	public void setShopname(int shopname) {
		this.shopname = shopname;
	}

}

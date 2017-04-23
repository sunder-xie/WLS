package com.wheelys.model.company;

import java.io.Serializable;

import com.wheelys.model.BaseObject;

public class Company extends BaseObject {
	/**
		 * 
		 */
	private static final long serialVersionUID = -8934242740891783897L;
	private Long id;// 运营商id
	private String name;// 运营商名字
	private String contants;// 联系人
	private String telephone;// 手机
	private String citycode; // 城市Code
	private String shopnumber;// 店铺id
	private String delstatus;// 删除状态默认为N，删除为Y
	private String password;// 密码
	private String account;//运营商账号

	public Company() {

	}

	public Company(String name, String contants, String telephone,
			String citycode,String account) {
		this.name = name;
		this.contants = contants;
		this.telephone = telephone;
		this.citycode = citycode;
		this.delstatus = "N";
		this.account=account;
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

	public String getContants() {
		return contants;
	}

	public void setContants(String contants) {
		this.contants = contants;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getShopnumber() {
		return shopnumber;
	}

	public void setShopnumber(String shopnumber) {
		this.shopnumber = shopnumber;
	}

	public String getDelstatus() {
		return delstatus;
	}

	public void setDelstatus(String delstatus) {
		this.delstatus = delstatus;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Override
	public Serializable realId() {
		// TODO Auto-generated method stub
		return null;
	}

}

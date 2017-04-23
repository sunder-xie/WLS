package com.wheelys.model.company;

import java.io.Serializable;

import com.wheelys.model.BaseObject;

public class CompanyActivity extends BaseObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3121856257644127753L;
	/**
	 * 
	 */

	private Long id;
	private Long shopid;// 店铺id
	private String shopname;//店铺名字
	private Long companyid;// 运营商id
	private String companyname;//运营商名字
	private String companyinfo;// 活动详细
	private String contants;//联系人
	private String telephone;//电话
	private String companystatus;// 处理进度 N代表处理过 Y代表未处理，默认为未处理

	public CompanyActivity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CompanyActivity(Long shopid, String companyinfo) {
		this.shopid = shopid;
		this.companyinfo = companyinfo;
		this.companystatus = "Y";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getShopid() {
		return shopid;
	}

	public void setShopid(Long shopid) {
		this.shopid = shopid;
	}

	public Long getCompanyid() {
		return companyid;
	}

	public void setCompanyid(Long companyid) {
		this.companyid = companyid;
	}

	public String getCompanyinfo() {
		return companyinfo;
	}

	public void setCompanyinfo(String companyinfo) {
		this.companyinfo = companyinfo;
	}

	public String getCompanystatus() {
		return companystatus;
	}

	public void setCompanystatus(String companystatus) {
		this.companystatus = companystatus;
	}
	
	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
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

	@Override
	public Serializable realId() {
		// TODO Auto-generated method stub
		return null;
	}

}

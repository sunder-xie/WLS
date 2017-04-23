package com.wheelys.web.action.admin.vo;

import com.wheelys.model.company.Company;
import com.wheelys.model.company.CompanyActivity;

public class CompanyActityVo {

	private Company operator;
	private CompanyActivity operatorActity;
	private String shopname;
	private String contants;
	private String telephone;
	public CompanyActityVo() {
		
	}

	public CompanyActityVo( CompanyActivity operatorActity,String name,String contants,String telephone,Company operator) {
		this.operatorActity = operatorActity;
		this.operator = operator;
		this.shopname = name;
		this.contants=contants;
		this.telephone=telephone;
	}

	
	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public Company getOperator() {
		return operator;
	}

	public void setOperator(Company operator) {
		this.operator = operator;
	}

	public CompanyActivity getOperatorActity() {
		return operatorActity;
	}

	public void setOperatorActity(CompanyActivity operatorActity) {
		this.operatorActity = operatorActity;
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
	
}

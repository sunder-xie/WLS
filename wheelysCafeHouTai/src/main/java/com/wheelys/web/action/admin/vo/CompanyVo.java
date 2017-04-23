package com.wheelys.web.action.admin.vo;

import com.wheelys.model.company.Company;

public class CompanyVo {
	private Company operator;
	private Integer count;

	public CompanyVo() {
		
	}

	public CompanyVo(Company operator, Integer count) {
		this.operator = operator;
		this.count = count;
	}

	public Company getOperator() {
		return operator;
	}

	public void setOperator(Company operator) {
		this.operator = operator;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}

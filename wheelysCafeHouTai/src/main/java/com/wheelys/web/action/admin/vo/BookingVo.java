package com.wheelys.web.action.admin.vo;

import java.sql.Timestamp;

public class BookingVo {
	private Long id;// 编号
	private String esname;// 店铺名字
	private String companyname;// 运营商名字
	private String contant;// 联系人
	private String telephone;// 电话
	private String type;// 类型
	private Timestamp applytime;// 申请时间
	private String status;// 状态

	public BookingVo() {

	}

	public BookingVo(Long id, String esname, String companyname, String contant, String telephone, String type,
			Timestamp applytime, String status) {
		this.id = id;
		this.esname = esname;
		this.companyname = companyname;
		this.contant = contant;
		this.telephone = telephone;
		this.type = type;
		this.applytime = applytime;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEsname() {
		return esname;
	}

	public void setEsname(String esname) {
		this.esname = esname;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getContant() {
		return contant;
	}

	public void setContant(String contant) {
		this.contant = contant;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getApplytime() {
		return applytime;
	}

	public void setApplytime(Timestamp applytime) {
		this.applytime = applytime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}

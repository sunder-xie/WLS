package com.wheelys.model;

import java.io.Serializable;

import com.wheelys.model.BaseObject;

public class ShopSeller extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6564899524278512605L;
	private Long id;
	private Long shopid; // 商家id
	private String loginname; // 员工账号
	private String userpwd; // 员工密码
	private String username; // 员工姓名
	private Integer status; // 员工状态

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

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getUserpwd() {
		return userpwd;
	}

	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public Serializable realId() {
		return id;
	}

}

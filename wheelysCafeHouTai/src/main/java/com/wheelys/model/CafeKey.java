package com.wheelys.model;

import java.io.Serializable;

import com.wheelys.model.BaseObject;

public class CafeKey extends BaseObject {

	private static final long serialVersionUID = -4697872180942409268L;
	private Long id;// 主键
	private String name;// 口令
	private String keyimg;//图片


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

	public String getKeyimg() {
		return keyimg;
	}

	public void setKeyimg(String keyimg) {
		this.keyimg = keyimg;
	}


	@Override
	public Serializable realId() {
		return id;
	}

}

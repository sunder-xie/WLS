package com.wheelys.model.citymanage;

import java.io.Serializable;

import com.wheelys.model.BaseObject;

public class CityCode extends BaseObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3619198543319307173L;
	private String citycode;// 城市code;
	private String name;// 名字

	public CityCode() {
		
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Serializable realId() {
		// TODO Auto-generated method stub
		return null;
	}

}

package com.wheelys.model.merchant;

import java.io.Serializable;

import com.wheelys.model.BaseObject;

public class MchCityExpress extends BaseObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3785919663672972197L;
	private String id;
	private String citycode;
	private String cityname;
	private Integer expressfee;
	private Integer orderfee;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public Integer getExpressfee() {
		return expressfee;
	}

	public void setExpressfee(Integer expressfee) {
		this.expressfee = expressfee;
	}

	public Integer getOrderfee() {
		return orderfee;
	}

	public void setOrderfee(Integer orderfee) {
		this.orderfee = orderfee;
	}

	@Override
	public Serializable realId() {
		return id;
	}

}

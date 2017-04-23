package com.wheelys.model.citymanage;

import java.io.Serializable;

import com.wheelys.model.BaseObject;

public class CityManage extends BaseObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3699759395532997458L;
	private Long id;// 城市id
	private String citycode;// 城市编号
	private String cityname;// 城市名字
	private String companyids;// 运营商id
	private String shopids;// 店铺数
	private String delstatus;// 删除状态N代表未删除
	private String region;// 城市大区
	private Integer sn; // 序号
	private String status;// 状态/正常:normal /推荐recommend

	public CityManage() {

	}

	public CityManage(Long id, String citycode, String cityname) {
		this.id = id;
		this.citycode = citycode;
		this.cityname = cityname;
		this.delstatus = "N";
		this.sn = 99;
		this.status = "normal";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getCompanyids() {
		return companyids;
	}

	public void setCompanyids(String companyids) {
		this.companyids = companyids;
	}

	public String getShopids() {
		return shopids;
	}

	public void setShopids(String shopids) {
		this.shopids = shopids;
	}

	public String getDelstatus() {
		return delstatus;
	}

	public void setDelstatus(String delstatus) {
		this.delstatus = delstatus;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public Serializable realId() {
		// TODO Auto-generated method stub
		return null;
	}

}

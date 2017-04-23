package com.wheelys.model;

import java.io.Serializable;

import org.apache.commons.codec.binary.StringUtils;

import com.wheelys.model.BaseObject;

public class CafeShop extends BaseObject implements LatLngObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4437096184795412131L;
	private Long shopid; // 主键
	private String shopname; // 店铺名称
	private String shopaddress; // 店铺地址
	private String shopimg; // 店铺缩略图
	private String shoplon; // 经度
	private String shoplat; // 纬度
	private String shopaccount; // 账号
	private String shoptime; // 商家营业时间
	private String shoptelephone; // 商家电话
	private String shopintroduce;// 店铺介绍
	private String shopfeature; // 店铺特色
	private String citycode; // 城市Code
	private String booking; //是否开放预订
	private Long distance;	//距离
	private String esname;//内部简称

	public Long getShopid() {
		return shopid;
	}

	public void setShopid(Long shopid) {
		this.shopid = shopid;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getShopaddress() {
		return shopaddress;
	}

	public void setShopaddress(String shopaddress) {
		this.shopaddress = shopaddress;
	}

	public String getShopimg() {
		return shopimg;
	}

	public void setShopimg(String shopimg) {
		this.shopimg = shopimg;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getShoplon() {
		return shoplon;
	}

	public void setShoplon(String shoplon) {
		this.shoplon = shoplon;
	}

	public String getShoplat() {
		return shoplat;
	}

	public void setShoplat(String shoplat) {
		this.shoplat = shoplat;
	}

	public String getShopaccount() {
		return shopaccount;
	}

	public void setShopaccount(String shopaccount) {
		this.shopaccount = shopaccount;
	}

	public String getShoptime() {
		return shoptime;
	}

	public void setShoptime(String shoptime) {
		this.shoptime = shoptime;
	}

	public String getShoptelephone() {
		return shoptelephone;
	}

	public void setShoptelephone(String shoptelephone) {
		this.shoptelephone = shoptelephone;
	}

	public String getShopintroduce() {
		return shopintroduce;
	}

	public void setShopintroduce(String shopintroduce) {
		this.shopintroduce = shopintroduce;
	}

	public String getShopfeature() {
		return shopfeature;
	}

	public void setShopfeature(String shopfeature) {
		this.shopfeature = shopfeature;
	}
	
	public String getEsname() {
		return esname;
	}

	public void setEsname(String esname) {
		this.esname = esname;
	}

	@Override
	public Serializable realId() {
		return shopid;
	}

	@Override
	public String getLat() {
		return shoplat;
	}

	@Override
	public String getLng() {
		return shoplon;
	}

	public Long getDistance() {
		return distance;
	}

	public String getBooking() {
		return booking;
	}

	public void setBooking(String booking) {
		this.booking = booking;
	}

	public void setDistance(Long distance) {
		this.distance = distance;
	}
	
	public Boolean isOpen(){
		return StringUtils.equals(booking, "open") || this.shopid == 6L;
	}

	@Override
	public String getSortKey() {
		return CafeShop.class.getName()+"_"+this.realId();
	}

}

package com.wheelys.web.action.admin.vo;

import java.sql.Timestamp;

public class WheelysMemberVo {

	private Long id;
	private String mobile;
	private Timestamp addtime;
	private String cityCode; // 最后一笔订单所在城市
	private String openNickName; // 第三方昵称：微信/支付宝
	private Integer totalOrderQuantity; // 总订单数
	private Integer totalFee; // 总消费额
	private String latestConsumerShop; // 最后一笔消费店铺
	private Timestamp latestConsumerTime; // 最后一笔消费时间
	private String latestTradeno; //最新一笔订单的编号

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Timestamp getAddtime() {
		return addtime;
	}

	public void setAddtime(Timestamp addtime) {
		this.addtime = addtime;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getOpenNickName() {
		return openNickName;
	}

	public void setOpenNickName(String openNickName) {
		this.openNickName = openNickName;
	}

	public Integer getTotalOrderQuantity() {
		return totalOrderQuantity;
	}

	public void setTotalOrderQuantity(Integer totalOrderQuantity) {
		this.totalOrderQuantity = totalOrderQuantity;
	}

	public Integer getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}

	public String getLastConsumerShop() {
		return latestConsumerShop;
	}

	public void setLastConsumerShop(String lastConsumerShop) {
		this.latestConsumerShop = lastConsumerShop;
	}

	public Timestamp getLastConsumerTime() {
		return latestConsumerTime;
	}

	public void setLastConsumerTime(Timestamp lastConsumerTime) {
		this.latestConsumerTime = lastConsumerTime;
	}

	public String getLatestTradeno() {
		return latestTradeno;
	}

	public void setLatestTradeno(String latestTradeno) {
		this.latestTradeno = latestTradeno;
	}
}

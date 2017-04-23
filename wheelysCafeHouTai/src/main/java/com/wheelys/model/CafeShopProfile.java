package com.wheelys.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.model.BaseObject;
import com.wheelys.util.DateUtil;

/**
 * 店铺外送预定信息配置
 * 
 * @author dell
 *
 */
public class CafeShopProfile extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1238894476685916649L;
	private Long shopid; // 店铺ID
	private String name; // 店铺名
	private String mobile; // 外送手机
	private String takeawaystatus; // 外送状态
	private String reservedstatus; // 预定状态
	private String delstatus; // 逻辑删除状态
	private Timestamp createtime; // 创建时间
	private Timestamp updatetime; // 更新时间
	private String timeslot;// 时间段1

	public CafeShopProfile() {
	}

	public CafeShopProfile(CafeShop shop) {
		this.shopid = shop.getShopid();
		this.name = shop.getShopname();
		this.takeawaystatus = "N";
		this.reservedstatus = "N";
		this.delstatus = "N";
		this.createtime = DateUtil.getMillTimestamp();
		this.updatetime = this.createtime;
	}

	public Long getShopid() {
		return shopid;
	}

	public void setShopid(Long shopid) {
		this.shopid = shopid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTakeawaystatus() {
		return takeawaystatus;
	}

	public void setTakeawaystatus(String takeawaystatus) {
		this.takeawaystatus = takeawaystatus;
	}

	public String getReservedstatus() {
		return reservedstatus;
	}

	public void setReservedstatus(String reservedstatus) {
		this.reservedstatus = reservedstatus;
	}

	public String getDelstatus() {
		return delstatus;
	}

	public void setDelstatus(String delstatus) {
		this.delstatus = delstatus;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	
	public String getTimeslot() {
		return timeslot;
	}

	public void setTimeslot(String timeslot) {
		this.timeslot = timeslot;
	}

	@Override
	public Serializable realId() {
		return shopid;
	}

}

package com.wheelys.model.booking;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.model.BaseObject;
import com.wheelys.util.DateUtil;

public class Booking extends BaseObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8478724775188153259L;
	private Long id;
	private Long shopid;// 店铺id
	private String reason;// 理由
	private Timestamp applytime;// 申请时间
	private String status;// 状态
	private Timestamp passtime;// 审核通过时间
	private Long companyid;// 运营商id
	private String contant;// 联系人
	private String telephone;// 电话
	private String type;// 类型
	private Timestamp createtime;// 数据创建时间
	private Timestamp updatetime;// 数据修改时间

	public Booking() {

	}

	public Booking(Long shopid, String reason, Timestamp applytime, String status) {
		this.shopid = shopid;
		this.reason = reason;
		this.applytime = applytime;
		this.status = status;
		this.createtime = DateUtil.getMillTimestamp();

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getPasstime() {
		return passtime;
	}

	public void setPasstime(Timestamp passtime) {
		this.passtime = passtime;
	}

	public Long getCompanyid() {
		return companyid;
	}

	public void setCompanyid(Long companyid) {
		this.companyid = companyid;
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getApplytime() {
		return applytime;
	}

	public void setApplytime(Timestamp applytime) {
		this.applytime = applytime;
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

	@Override
	public Serializable realId() {
		// TODO Auto-generated method stub
		return null;
	}

}

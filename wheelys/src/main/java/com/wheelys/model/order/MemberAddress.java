package com.wheelys.model.order;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.model.BaseObject;

/**
 * 用户外送地址
 * @author liufeng
 *
 */
public class MemberAddress extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 678743136531105244L;
	private Long id; // id
	private Long memberid; // 用户ID
	private Long shopid; // 店铺ID
	private String username; // 姓名
	private String mobile; // 联系电话
	private String address; // 地址
	private String detailaddress;// 详细地址
	private String type; // 类型（Y/N）Y默认使用
	private Timestamp createtime; // 创建时间
	private Timestamp updatetime; // 修改时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMemberid() {
		return memberid;
	}

	public void setMemberid(Long memberid) {
		this.memberid = memberid;
	}

	public Long getShopid() {
		return shopid;
	}

	public void setShopid(Long shopid) {
		this.shopid = shopid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDetailaddress() {
		return detailaddress;
	}

	public void setDetailaddress(String detailaddress) {
		this.detailaddress = detailaddress;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
		return id;
	}

}

package com.wheelys.model.user;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.model.BaseObject;

public class MobileDynamicCode extends BaseObject {
	
	private static final long serialVersionUID = 1648055411052069956L;
	
	private String ukey;			//tag + mobile
	private String tag;				//类型
	private String mobile;			//手机号
	private String checkpass;		//验证码
	private String lastip;			//ip
	private Timestamp validtime;	//有效时间
	private Integer sendcount;		//发送次数
	private Integer checkcount;		//当前使用次数，重发时复位
	private Integer totalcheck;		//总使用次数
	private Long memberid;			//用户ID
	private Integer version;

	public MobileDynamicCode(){}
	
	public MobileDynamicCode(String tag, String mobile, String checkpass, String lastip) {
		this.ukey = tag+mobile;
		this.tag = tag;
		this.version = 0;
		this.checkcount = 0;
		this.sendcount = 0;
		this.totalcheck = 0;
		this.mobile = mobile;
		this.checkpass = checkpass;
		this.lastip = lastip;
	}
	
	@Override
	public Serializable realId() {
		return ukey;
	}

	public String getUkey() {
		return ukey;
	}

	public void setUkey(String ukey) {
		this.ukey = ukey;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCheckpass() {
		return checkpass;
	}

	public void setCheckpass(String checkpass) {
		this.checkpass = checkpass;
	}

	public String getLastip() {
		return lastip;
	}

	public void setLastip(String lastip) {
		this.lastip = lastip;
	}

	public Timestamp getValidtime() {
		return validtime;
	}

	public void setValidtime(Timestamp validtime) {
		this.validtime = validtime;
	}

	public Integer getSendcount() {
		return sendcount;
	}

	public void setSendcount(Integer sendcount) {
		this.sendcount = sendcount;
	}

	public Integer getCheckcount() {
		return checkcount;
	}

	public void setCheckcount(Integer checkcount) {
		this.checkcount = checkcount;
	}

	public Integer getTotalcheck() {
		return totalcheck;
	}

	public void setTotalcheck(Integer totalcheck) {
		this.totalcheck = totalcheck;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Long getMemberid() {
		return memberid;
	}

	public void setMemberid(Long memberid) {
		this.memberid = memberid;
	}

}

package com.wheelys.model;

import java.io.Serializable;

import com.wheelys.model.BaseObject;

public class Pcemail extends BaseObject {

	private static final long serialVersionUID = -8389250934762521104L;

	private Integer eid; // id
	private String eemail; // 名字
	private Integer etime;// 时间

	public Pcemail() {}

	public Pcemail(Integer eid, String eemail, Integer etime) {

		this.eid = eid;
		this.eemail = eemail;
		this.etime = etime;
	}

	public Integer getEid() {
		return eid;
	}

	public void setEid(Integer eid) {
		this.eid = eid;
	}

	public String getEemail() {
		return eemail;
	}

	public void setEemail(String eemail) {
		this.eemail = eemail;
	}

	public Integer getEtime() {
		return etime;
	}

	public void setEtime(Integer etime) {
		this.etime = etime;
	}

	public Serializable realId() {
		// TODO Auto-generated method stub
		return eid;
	}

}

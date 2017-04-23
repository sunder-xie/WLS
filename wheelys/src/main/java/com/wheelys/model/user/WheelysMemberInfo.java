package com.wheelys.model.user;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.model.BaseObject;

public class WheelysMemberInfo extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4331122120769274543L;
	private Long memberid;
	private Integer version; // 版本
	private String nickname; // 与Member中相同
	private String realname;
	private String sex; // 性别
	private String citycode;
	private String source; // 注册来源(member,shop,weixin,alipay)
	private Long inviteid; // 邀请人ID
	private Integer pointvalue; // 积分总值
	private Timestamp createtime;
	private Timestamp updatetime;
	private String ip; // 注册IP
	
	public WheelysMemberInfo(){}
	
	public WheelysMemberInfo(WheelysMember member, String ip){
		this.memberid = member.getId();
		this.nickname = member.getNickname();
		this.createtime = member.getAddtime();
		this.updatetime = this.createtime;
		this.ip = ip;
		this.pointvalue = 0;
		this.inviteid = 0L;
	}

	public Long getMemberid() {
		return memberid;
	}

	public void setMemberid(Long memberid) {
		this.memberid = memberid;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Long getInviteid() {
		return inviteid;
	}

	public void setInviteid(Long inviteid) {
		this.inviteid = inviteid;
	}

	public Integer getPointvalue() {
		return pointvalue;
	}

	public void setPointvalue(Integer pointvalue) {
		this.pointvalue = pointvalue;
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public Serializable realId() {
		return memberid;
	}
}

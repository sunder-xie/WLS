package com.wheelys.model.pay;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.model.BaseObject;

public class SMSRecord extends BaseObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8717301746298424510L;
	private Long id;
	private String tradeno; // 订单号
	private String contact; // 手机号码
	private String content;	//短信内容
	private Timestamp sendtime;	//计划发送时间
	private Timestamp realtime;	//实际发送时间
	private Timestamp validtime;//有效时间
	private String status;	//状态
	private String smstype;	//短信类型
	private Integer sendnum;//发送次数
	private String seqno;	//发送序列号
	private String channel;	//发送渠道
	private Long memberid;	//用户ID
	private Long relatedid; // 关联ID
	private String tag;	//关联类型
	
	public SMSRecord() {}
	
	public SMSRecord(String tradeno, String contact, String content, 
			Timestamp sendtime, Timestamp validtime) {
		this.tradeno = tradeno;
		this.contact = contact;
		this.content = content;
		this.sendtime = sendtime;
		this.validtime = validtime;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTradeno() {
		return tradeno;
	}

	public void setTradeno(String tradeno) {
		this.tradeno = tradeno;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getSendtime() {
		return sendtime;
	}

	public void setSendtime(Timestamp sendtime) {
		this.sendtime = sendtime;
	}

	public Timestamp getRealtime() {
		return realtime;
	}

	public void setRealtime(Timestamp realtime) {
		this.realtime = realtime;
	}

	public Timestamp getValidtime() {
		return validtime;
	}

	public void setValidtime(Timestamp validtime) {
		this.validtime = validtime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSmstype() {
		return smstype;
	}

	public void setSmstype(String smstype) {
		this.smstype = smstype;
	}

	public Integer getSendnum() {
		return sendnum;
	}

	public void setSendnum(Integer sendnum) {
		this.sendnum = sendnum;
	}

	public String getSeqno() {
		return seqno;
	}

	public void setSeqno(String seqno) {
		this.seqno = seqno;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Long getMemberid() {
		return memberid;
	}

	public void setMemberid(Long memberid) {
		this.memberid = memberid;
	}

	public Long getRelatedid() {
		return relatedid;
	}

	public void setRelatedid(Long relatedid) {
		this.relatedid = relatedid;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public Serializable realId() {
		// TODO Auto-generated method stub
		return null;
	}
}

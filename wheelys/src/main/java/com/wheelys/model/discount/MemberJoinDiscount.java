package com.wheelys.model.discount;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.wheelys.model.BaseObject;

public class MemberJoinDiscount extends BaseObject {

	private static final long serialVersionUID = 6248026290050768613L;
	private Long id;		//ID
	private Long memberid; // 用户ID
	private Long discountid; // 用户ID
	private Timestamp addtime; // 有效时间
	private Timestamp validtime; // 有效时间

	public MemberJoinDiscount() {
	}

	public MemberJoinDiscount(Long memberid, Long discountid) {
		LocalDateTime nowtime = LocalDateTime.now();
		this.addtime = Timestamp.valueOf(nowtime);
		this.validtime = Timestamp.valueOf(nowtime.plusMinutes(10)); // 有效时间10分钟
		this.memberid = memberid;
		this.discountid = discountid;
	}

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

	public Long getDiscountid() {
		return discountid;
	}

	public void setDiscountid(Long discountid) {
		this.discountid = discountid;
	}

	public Timestamp getAddtime() {
		return addtime;
	}

	public void setAddtime(Timestamp addtime) {
		this.addtime = addtime;
	}

	public Timestamp getValidtime() {
		return validtime;
	}

	public void setValidtime(Timestamp validtime) {
		this.validtime = validtime;
	}

	@Override
	public Serializable realId() {
		return id;
	}
}

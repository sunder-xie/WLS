package com.wheelys.helper;

import com.wheelys.model.acl.User;

public class EncodeData {

	private long memberid;
	private long validMonth;
	private String passvalid;// 密码验证结果：U 未做验证 Y 验证通过 N 验证未通过
	private User member;

	public EncodeData(long memberid, long validMonth, String passvalid, User member) {
		this.memberid = memberid;
		this.validMonth = validMonth;
		this.passvalid = passvalid;
		this.member = member;
	}

	public String getPassvalid() {
		return passvalid;
	}

	public User getMember() {
		return member;
	}

	public long getMemberid() {
		return memberid;
	}

	public long getValidMonth() {
		return validMonth;
	}
}

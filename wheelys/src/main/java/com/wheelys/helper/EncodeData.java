package com.wheelys.helper;

import com.wheelys.model.user.WheelysMember;

public class EncodeData {

	private long memberid;
	private long validMonth;
	private String passvalid;// 密码验证结果：U 未做验证 Y 验证通过 N 验证未通过
	private WheelysMember member;

	public EncodeData(long memberid, long validMonth, String passvalid, WheelysMember member) {
		this.memberid = memberid;
		this.validMonth = validMonth;
		this.passvalid = passvalid;
		this.member = member;
	}

	public String getPassvalid() {
		return passvalid;
	}

	public WheelysMember getMember() {
		return member;
	}

	public long getMemberid() {
		return memberid;
	}

	public long getValidMonth() {
		return validMonth;
	}
}

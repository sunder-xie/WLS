package com.wheelys.web.support.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import com.wheelys.model.user.WheelysMember;

public class MemberEncodeAuthenticationToken extends AbstractAuthenticationToken {
	private static final long serialVersionUID = -3802313724481182163L;
	private String memberEncode;
	private WheelysMember member;
	public MemberEncodeAuthenticationToken(WheelysMember member, String memberEncode) {
		super(member.getAuthorities());
		this.memberEncode = memberEncode;
		this.member = member;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return member;
	}
	@Override
	public void eraseCredentials() {
	}

	public String getMemberEncode() {
		return memberEncode;
	}

	public void setMemberEncode(String memberEncode) {
		this.memberEncode = memberEncode;
	}

	public WheelysMember getMember() {
		return member;
	}
}

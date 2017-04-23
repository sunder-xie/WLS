package com.wheelys.web.support;

import org.springframework.security.core.Authentication;

import com.wheelys.web.support.auth.DefaultSessidGenerator;
import com.wheelys.web.support.auth.SessidGenerator;
import com.wheelys.web.support.token.MemberEncodeAuthenticationToken;

public class ShSessidGenerator implements SessidGenerator{
	public static final String COOPER_USER_PRE = "CPU2";
	private SessidGenerator defaultGenerator = new DefaultSessidGenerator();

	@Override
	public String generateSessid(Authentication auth) {
		if(auth instanceof MemberEncodeAuthenticationToken){
			return ((MemberEncodeAuthenticationToken)auth).getMemberEncode();
		}
		return defaultGenerator.generateSessid(auth);
	}
}

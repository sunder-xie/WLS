package com.wheelys.web.support;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import com.wheelys.util.StringUtil;

public class GewaUpgradePasswordEncoder extends Md5PasswordEncoder{
	private String prikey;
	public void setPrikey(String prikey) {
		this.prikey = prikey;
	}
	
	@Override
	public String encodePassword(String rawPass, Object salt) {
		return StringUtil.md5(StringUtil.md5(rawPass)+prikey);
	}

	@Override
	public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
		String md5 = StringUtil.md5(rawPass);
		if(StringUtils.equals(encPass, md5)){
			return true;
		}
		String md5t = StringUtil.md5(md5+prikey);
		return StringUtils.equals(encPass, md5t);
	}
	
}

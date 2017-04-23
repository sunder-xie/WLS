package com.wheelys.helper;

import org.apache.commons.lang.StringUtils;

public class AccountHelper {
	public static boolean isSimplePwd(String pwd){
		return StringUtils.isBlank(pwd) || "123456".equals(pwd) || StringUtils.length(pwd) < 6 ;
	}
	public static String getNicknameByMobile(String mobile){
		String start = StringUtils.substring(mobile, 0, 3);
		String nickname = start + "xxxx" + StringUtils.substring(mobile, 7);
		return nickname;
	}
	public static String getCardnum(String cardNumber){
		int length = StringUtils.length(cardNumber);
		String nickname = StringUtils.substring(cardNumber, 0, length-7) + "****" + StringUtils.substring(cardNumber, length-3);
		return nickname;
	}
}

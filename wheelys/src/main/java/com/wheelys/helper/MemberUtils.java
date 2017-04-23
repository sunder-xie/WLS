package com.wheelys.helper;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.wheelys.util.DateUtil;
import com.wheelys.util.PKCoderUtil;
import com.wheelys.util.StringUtil;
import com.wheelys.model.user.WheelysMember;

public class MemberUtils {

	public static boolean isMemberEncode(String memberEncode, String checkPass){
		String sessid = memberEncode;
		if(StringUtils.isBlank(sessid)) return false;
		if(StringUtils.contains(sessid, ",")){
			String[] encodes = sessid.split(",");
			if(StringUtils.equals(encodes[0], encodes[1])){
				sessid = encodes[0];
			}
		}
		String[] pair = sessid.split("@");
		if(pair.length > 1){
			if(StringUtil.md5(pair[0] + checkPass).substring(0, 8).equalsIgnoreCase(pair[1])){
				return true;
			}
		}
		return false;
	}
	
	public static String generateMemberEncode(WheelysMember member, String checkPass){
		String time = DateUtil.format(DateUtils.addMonths(new Date(System.currentTimeMillis()), 1), "yyyyMM");
		String pass = StringUtil.getRandomString(3) + StringUtil.md5(member.getPassword() + checkPass).substring(8, 16);
		
		String memberEncode = pass + member.getId() + time;
		memberEncode = PKCoderUtil.encryptString(memberEncode, checkPass);
		memberEncode += "@" + StringUtil.md5(memberEncode + checkPass).substring(0, 8);
		return memberEncode;
	}
}

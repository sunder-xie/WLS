package com.wheelys.web.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.wheelys.model.acl.User;
import com.wheelys.util.DateUtil;

public class MemberUpdaterTrigger {
	private static Map<Long/*memberid*/, Long/*time*/> memberMap = new ConcurrentHashMap<Long, Long>();

	public static boolean isNeedRefresh(User member) {
		if(memberMap.isEmpty()) return false;
		Long validtime = memberMap.get(member.getId());
		if(validtime!=null && validtime< System.currentTimeMillis()){
			memberMap.remove(member.getId());
		}
		return validtime!=null;
	}
	public static void addMemberId(Long memberid){
		memberMap.put(memberid, System.currentTimeMillis() + DateUtil.m_minute*5);
	}
}


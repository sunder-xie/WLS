package com.wheelys.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.wheelys.util.DateUtil;
import com.wheelys.util.JsonUtils;
import com.wheelys.util.StringUtil;
import com.wheelys.util.VmBaseUtil;

/**
 * 每个项目都有自己独立的实现
 */
public class VmUtils extends VmBaseUtil {

	private static String version = DateUtil.format(DateUtil.getMillTimestamp(), "yyyyMMddHH") + "T";
	private static String baidu_ak = "FdIoD0Fw77XzxtygwA4Lb4CIN5kn3aks";

	public final static String getAk() {
		return baidu_ak;
	}

	public final static String getVersion() {
		return version;
	}
	
	public final static boolean isNotEmpty(String str){
		return StringUtils.isNotEmpty(str);
	}

	public final static String setVersion(String v) {
		return version = v;
	}

	public final static String getDistance(Long distance) {
		if (distance == null || distance == 0)
			return "0";
		return (distance / 1000) + "." + (distance % 1000 / 100);
	}

	public final static String getAmount(Integer amount) {
		if (amount == null || amount == 0)
			return "0";
		if (amount % 100 == 0) {
			return String.valueOf(amount / 100);
		} else {
			return String.valueOf(amount / 100.0);
		}
	}

	public final static Double getDoubleAmount(Number amount) {
		if (amount == null)
			return 0D;
		return amount.intValue() / 100.0D;
	}

	public final static String getPercent(Integer num1, Integer num2) {
		try {
			if (num1 == null || num2 == null)
				return "0%";
			return (num1 * 10000L / num2 * 1 / 100D) + "%";
		} catch (Exception e) {
			return "0%";
		}
	}

	public static boolean isValidCaptchaId(String captchaId) {
		if (StringUtils.length(captchaId) != 24)
			return false;
		return StringUtil.md5(StringUtils.substring(captchaId, 0, 16) + "sk#8Kr", 8)
				.equals(StringUtils.substring(captchaId, 16));
	}

	public static String getRandomCaptchaId() {
		String s = StringUtil.getRandomString(16);
		s += StringUtil.md5(s + "sk#8Kr", 8);
		return s;
	}

	public final static Map readJsonToMap(String json) {
		return JsonUtils.readJsonToMap(json);
	}

	
	 public static List<Long> getIntersection(List<Long> list1,
	            List<Long> list2) {
	        List<Long> result = new ArrayList<Long>();
	        for (Long integer : list2) {//遍历list1
	            if (list1.contains(integer)) {//如果存在这个数
	                result.add(integer);//放进一个list里面，这个list就是交集
	            }
	        }
	        return result;
	   }
	 
	/**
	 * 
	 * @param number
	 * @return判断固定电话
	 */
	public static boolean isfixedTelephone(String number) {
		return StringUtil.regMatch(number, "^[\\d\\-]+$", true);
	}

	/**
	 * 
	 * @param number
	 * @return判断输入是否是英文和数字
	 */
	public static boolean isEnglistAndNumber(String name) {
		return StringUtil.regMatch(name, "^[A-Za-z0-9\\-]+$", false);
	}

	/**
	 * 
	 * @param number
	 * @return判断手机号
	 */
	public static boolean isMobile(String name) {
		return StringUtil.regMatch(name, "^0?1[3|4|5|8|7][0-9]\\d{8}$", false);
	}

	/**
	 * 
	 * @param value
	 * @return判断字符长度
	 */
	public static int isLength(String value) {
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		/* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
		for (int i = 0; i < value.length(); i++) {
			/* 获取一个字符 */
			String temp = value.substring(i, i + 1);
			/* 判断是否为中文字符 */
			if (temp.matches(chinese)) {
				/* 中文字符长度为2 */
				valueLength += 2;
			} else {
				/* 其他字符长度为1 */
				valueLength += 1;
			}
		}
		return valueLength;
	}

	/**
	 * 
	 * @param number
	 * @return判断是否为数字和小数点
	 */
	public static boolean isNumberAndlittle(String name) {
		return StringUtil.regMatch(name, "/\\d+(\\.\\d{0,1})?/)||[''])[0]", false);
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkNum(String str) {
		return str.matches("[0-9]");
		// [2-5][0-9]可表示20-59，60则单独处理
	}
}
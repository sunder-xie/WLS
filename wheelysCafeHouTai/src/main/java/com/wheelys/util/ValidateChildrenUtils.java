package com.wheelys.util;

import com.wheelys.util.StringUtil;
import com.wheelys.util.ValidateUtil;

/**
 * 每个项目都有自己独立的实现
 */
public class ValidateChildrenUtils extends ValidateUtil {
	 /**
	  * 
	  * @param number
	  * @return判断固定电话
	  */
	 public static boolean isfixedTelephone(String number){
			return StringUtil.regMatch(number, "^[\\d\\-]+$", true);
		}
	 /**
	  * 
	  * @param number
	  * @return判断输入是否是英文和数字
	  */
	 public static boolean isEnglistAndNumber(String name){
			return StringUtil.regMatch(name, "^[A-Za-z0-9\\-]+$", false);
		}
	 /**
	  * 
	  * @param number
	  * @return判断手机号
	  */
	 public static boolean isMobile(String name){
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
	 public static boolean isNumberAndlittle(String name){
			return StringUtil.regMatch(name, "/\\d+(\\.\\d{0,1})?/)||[''])[0]", false);
		}
	 /**
	  * 
	  * @param str
	  * @return
	  */
	 public static boolean checkNum(String str){
	        return str.matches("[0-9]");
	        // [2-5][0-9]可表示20-59，60则单独处理
	    }
}
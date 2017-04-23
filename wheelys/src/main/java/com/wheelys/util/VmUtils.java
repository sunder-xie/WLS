package com.wheelys.util;

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
	
	private static String version = DateUtil.format(DateUtil.getMillTimestamp(), "yyyyMMddHH")+"T";
	private static String baidu_ak = "FdIoD0Fw77XzxtygwA4Lb4CIN5kn3aks";
	
	public final static String getAk(){
		return baidu_ak;
	}

	public final static String getVersion(){
		return version;
	}
	
	public final static String setVersion(String v){
		return version = v;
	}
	
	public final static String writeObjectToJson(Object object){
		return JsonUtils.writeObjectToJson(object);
	}
	
	public final static String getDistance(Long distance){
		if(distance == null || distance == 0)return "0";
		return (distance/1000) + "." + (distance%1000/100);
	}
	
	public final static String getAmount(Integer amount){
		if(amount == null || amount == 0)return "0";
		if(amount%100 == 0){
			return String.valueOf(amount/100);
		}else{
			return String.valueOf(amount/100.0);
		}
	}

	public final static String getPercent(Integer num1, Integer num2){
		try {
			if(num1 == null || num2 == null)return "0%";
			return (num1*10000L/num2*1/100D) + "%";
		} catch (Exception e) {
			return "0%";
		}
	}
	
	public final static Map readJsonToMap(String json){
		return JsonUtils.readJsonToMap(json);
	}

	public static boolean isValidCaptchaId(String captchaId) {
		if(StringUtils.length(captchaId) != 24) return false;
		return StringUtil.md5(StringUtils.substring(captchaId, 0, 16) + "sk#8Kr", 8).equals(StringUtils.substring(captchaId, 16));
	}
	
	public static String getRandomCaptchaId() {
		String s = StringUtil.getRandomString(16) ;
		s += StringUtil.md5(s + "sk#8Kr", 8);
		return s;
	}

	public static String getDescription(String description) {
		Map<String, String> map = JsonUtils.readJsonToMap(description);
		String des = "";
		if(StringUtils.equals(map.get("hot"), "hot") || StringUtils.equals(map.get("hot"), "y")){
			des = "热";
		}
		if(StringUtils.equals(map.get("bean"), "y")){
			if(StringUtils.isNotBlank(des)) des += ".";
			des += "特浓";
		}
		if(StringUtils.equals(map.get("milk"), "y")){
			if(StringUtils.isNotBlank(des)) des += ".";
			des += "加奶";
		}
		if(StringUtils.isNotBlank(des)) des = "( " +des+ " )";
		return des;
	}

}
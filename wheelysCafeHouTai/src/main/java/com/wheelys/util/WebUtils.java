package com.wheelys.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

import com.wheelys.util.BaseWebUtils;
import com.wheelys.constant.CityContant;

public class WebUtils extends BaseWebUtils {

	public static String getAndSetCityCode(HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = getCookie(request, "citycode");
		String citycode = null;
		if (cookie != null) {
			citycode = cookie.getValue();
			if (isValidCitycode(citycode)){
				return citycode;
			}
		}
		citycode = CityContant.CITYCODE_SH;
		WebUtils.addCookie(response, "citycode", citycode, "/", 60 * 60 * 24 * 7);
		return citycode;
	}
	
	public static boolean isValidCitycode(String citycode) {
		return CityContant.ALLCITY.contains(citycode);
	}
	
	public static String getCitycodeByIp(String ip){
		if(StringUtils.isBlank(ip)){
			return null;
		}
		String citycode = findCitycodeByIp();
		if(StringUtils.isBlank(citycode)){
			citycode = CityContant.CITYCODE_SH;
		}
		return citycode;
	}
	
	//没有默认值，特殊情况下使用
	public static String findCitycodeByIp(){
		return CityContant.CITYCODE_SH;
	}
	
	public static void appendQueryProperties(StringBuilder targetUrl, LinkedHashMap<String, Object> model, String encoding) {
		boolean first = (targetUrl.indexOf("?") < 0);
		for (Map.Entry<String, Object> entry : queryProperties(model).entrySet()) {
			Object rawValue = entry.getValue();
			Iterator valueIter = null;
			if (rawValue != null && rawValue.getClass().isArray()) {
				valueIter = Arrays.asList(ObjectUtils.toObjectArray(rawValue)).iterator();
			}
			else if (rawValue instanceof Collection) {
				valueIter = ((Collection) rawValue).iterator();
			}
			else {
				valueIter = Collections.singleton(rawValue).iterator();
			}
			while (valueIter.hasNext()) {
				Object value = valueIter.next();
				if (first) {
					targetUrl.append('?');
					first = false;
				}
				else {
					targetUrl.append('&');
				}
				String encodedKey = urlEncode(entry.getKey(), encoding);
				String encodedValue = (value != null ? urlEncode(value.toString(), encoding) : "");
				targetUrl.append(encodedKey).append('=').append(encodedValue);
			}
		}
	}
	
	private static Map<String, Object> queryProperties(Map<String, Object> model) {
		Map<String, Object> result = new LinkedHashMap<>();
		for (Map.Entry<String, Object> entry : model.entrySet()) {
			if (isEligibleProperty(entry.getValue())) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	

	private static boolean isEligibleProperty(Object value) {
		if (value == null) return false;
		if (isEligibleValue(value)) return true;

		if (value.getClass().isArray()) {
			int length = Array.getLength(value);
			if (length == 0) {
				return false;
			}
			for (int i = 0; i < length; i++) {
				Object element = Array.get(value, i);
				if (!isEligibleValue(element)) {
					return false;
				}
			}
			return true;
		}

		if (value instanceof Collection) {
			Collection coll = (Collection) value;
			if (coll.isEmpty()) {
				return false;
			}
			for (Object element : coll) {
				if (!isEligibleValue(element)) {
					return false;
				}
			}
			return true;
		}

		return false;
	}
	private static String urlEncode(String input, String charsetName) {
		try {
			return (input != null ? URLEncoder.encode(input, charsetName) : null);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	private static boolean isEligibleValue(Object value) {
		return (value != null && BeanUtils.isSimpleValueType(value.getClass()));
	}
}

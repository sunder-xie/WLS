package com.wheelys.web.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.util.JsonUtils;

public class ApiFilterHelper {
	
	public static void writeErrorResponse(HttpServletResponse res, String code, String message){
		res.setContentType("text/json; charset=UTF-8");
		try {
			PrintWriter writer = res.getWriter();
			writer.write(JsonUtils.writeObjectToJson(ResultCode.getFailure(code, message)));
			writer.close();
		} catch (IOException e) {
		}
	}
	
	public static TreeMap<String, String> getTreeMap(HttpServletRequest request) {
		Map<String, String[]> requestParams = request.getParameterMap();
		TreeMap<String, String> params = new TreeMap<String, String>();
		for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
			String[] values = entry.getValue();
			StringBuilder vb = new StringBuilder();
			for (String v : values) {
				vb.append(v + ",");
			}
			params.put(entry.getKey(),StringUtils.removeEnd(vb.toString(), ","));
		}
		return params;
	}
}

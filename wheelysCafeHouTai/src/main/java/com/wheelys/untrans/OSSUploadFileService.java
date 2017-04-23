package com.wheelys.untrans;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface OSSUploadFileService {
	
	Map<String, String> uploadImg(HttpServletRequest request, Class clazz);
	
}

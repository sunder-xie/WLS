package com.wheelys.untrans.impl;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.aliyun.oss.OSSClient;
import com.wheelys.util.WheelysLogger;
import com.wheelys.util.StringUtil;
import com.wheelys.util.WebLogger;
import com.wheelys.untrans.OSSUploadFileService;

@Service("ossUploadFileService")
public class OSSUploadFileServiceImpl implements OSSUploadFileService {

	protected final transient WheelysLogger dbLogger = WebLogger.getLogger(getClass());
	
	@Value("${aliyun.oss.bucket}")
	private String bucketName;
	@Autowired@Qualifier("multipartResolver")
	private CommonsMultipartResolver multipartResolver;
	@Autowired@Qualifier("ossClient")
	private OSSClient ossClient;
	
	private static List<String> extList = Arrays.asList("jpg","gif","png","jpeg");
	
	@Override
	public Map<String, String> uploadImg(HttpServletRequest request, Class clazz){
		if (ServletFileUpload.isMultipartContent(request)) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			return getUploadFile(multipartRequest, clazz);
		}
		return new HashMap<String, String>();
	}
	
	private Map<String, String> getUploadFile(MultipartHttpServletRequest multipartRequest, Class clazz) {
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		Map<String, String> filenameMap = new HashMap<String, String>();
		if(fileMap.isEmpty()) return filenameMap;
		try {
			for (String fieldName : fileMap.keySet()) {
				MultipartFile orginalFile = fileMap.get(fieldName);
				String ofilename = StringUtils.lowerCase(orginalFile.getOriginalFilename()).replace(" ", "");
				String extname = getFilenameExtension(ofilename);
				if(!extList.contains(extname)){continue;}
				InputStream input = orginalFile.getInputStream();
				String key = "img/"+clazz.getSimpleName().toLowerCase()+"/"+StringUtil.getUID() + "." + extname;
				ossClient.putObject(bucketName, key, input);
				filenameMap.put(fieldName, StringUtils.substring(key, 4));
			}
		} catch (Exception e) {
			dbLogger.warn("upload img exception "+clazz.getSimpleName().toLowerCase());
		}
		return filenameMap;
	}
	
	public static void main(String[] args) {
		System.out.println(StringUtils.substring("img/srtesrt", 4));
	}
	
	public static String getFilenameExtension(String path) {
		if (path == null) {
			return null;
		}
		int sepIndex = path.lastIndexOf('.');
		return (sepIndex != -1 ? path.substring(sepIndex + 1) : null);
	}
}

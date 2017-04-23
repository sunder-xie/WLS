package com.wheelys.service.admin;

import java.util.List;
import java.util.Map;

import com.wheelys.model.acl.WebModule;

public interface CommonService {
	/**
	 * 
	 * @return拿一级菜单
	 */
	List<WebModule> webList(Long userid);
	
	/**
	 * 拿二级菜单
	 */
	List<WebModule>showWebList(String menucode,Long userid);
	
	/**
	 * 
	 * @param shopid
	 * @return展示一二级cai'd
	 */
	Map<WebModule, List<WebModule>> showWebModuleMap(Long userid);
	
	
}

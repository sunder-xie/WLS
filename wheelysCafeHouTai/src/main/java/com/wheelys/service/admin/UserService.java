package com.wheelys.service.admin;

import java.util.List;
import java.util.Map;

import com.wheelys.model.acl.User;
import com.wheelys.model.acl.WebModule;

public interface UserService {
	/**
	 * 
	 * @param user新增员工
	 */
	void addUser(User user);
	
	/**
	 * 
	 * @return员工展示
	 */
	List<User>showStaffList(Integer pageNo, int rowsPerPage);
	/**
	 * 
	 * @param name
	 * @return查询总条数
	 */
	int findStaffMangeCount();
	/**
	 * 
	 * @param id
	 * @return返回用户
	 */
	User user(Long id);
	
	/**
	 * 接收并判断用户名是否存在
	 */
	boolean username(String username,Long userid,String oldPassword);
	/**
	 * 
	 * @param user物理删除后
	 */
	void delstate(User user);
	/**
	 * 
	 * @return拿一级菜单
	 */
	List<WebModule> webMdouleList();
	
	/**
	 * 拿二级菜单
	 */
	List<WebModule>showList(String menucode);
	/**
	 * 
	 * @param shopid
	 * @return展示一二级cai'd
	 */
	Map<WebModule, List<WebModule>> getWebModuleMap();
	/**
	 * 
	 * @param id
	 * @return
	 */
	List<String>webList(Long userid);
	/**
	 * 
	 * @param id
	 * @param delstatus修改权限
	 */
	
	void UpdateWebModule(Long id,String arr,Long userid, String status);
	/**
	 * 拿到所有urlList
	 * @return
	 */
	List<String>urlList();
	/**
	 * 用户url二级菜单
	 * @param userid
	 * @return
	 */
	List<String>userUrl(Long userid);
	/**
	 * 用户url一级菜单
	 * @param userid
	 * @return
	 */
	List<String>oneUrl(Long userid);
}

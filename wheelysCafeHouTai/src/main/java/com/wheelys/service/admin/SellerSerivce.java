package com.wheelys.service.admin;

import java.util.List;

import com.wheelys.model.CafeShop;
import com.wheelys.model.ShopSeller;

public interface SellerSerivce {
	/**
	 * 
	 * @param user新增和修改员工
	 */
	ShopSeller upAddShopSeller(Long id,Long shopid,String username,String loginname,String password,String role,String mobile);
	
	/**
	 * 
	 * @return员工展示
	 */
	List<ShopSeller>showSellerList(Long shopid,Integer pageNo, int rowsPerPage);
	/**
	 * 
	 * @param name
	 * @return查询总条数
	 */
	int findShopSellerCount(Long shopid);
	/**
	 * 
	 * @param id
	 * @return返回店铺名
	 */
	CafeShop cafeShop(Long shopid);
	
	/**
	 * 接收并判断账号是否存在
	 */
	boolean ShopSeller(Long shopid,String loginname);
	
	/**
	 * 逻辑删除
	 */
	void delShopSeller(Long id);
	
	/**
	 * 查询员工
	 * 
	 */
	ShopSeller seller(Long id);
}

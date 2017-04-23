package com.wheelys.service.admin;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.wheelys.model.merchant.MchOrder;
import com.wheelys.model.merchant.MchOrderDetail;
import com.wheelys.model.merchant.MchProduct;
import com.wheelys.model.merchant.MchProductItem;

public interface MchProductService {

	/**
	 * 
	 * @return查看mchproduct字段
	 */
	List<MchProduct> mchProductlist(Long itemid, String name, String status,Long supplierid, Integer pageNo, int maxnum);

	/**
	 * 
	 * @return查看mchproductItem字段
	 */
	Map<Long, MchProductItem> mchProductItemMap();

	/**
	 * 
	 * @param 添加
	 */
	void addmchproduct(MchProduct mchproduct);

	/**
	 * 
	 * 通过id查找
	 */
	MchProduct findmchproduct(Long id);

	/**
	 * 
	 * @param 修改
	 */
	void updateMchproduct(MchProduct upmcproduct);

	/**
	 * 
	 * @param id删除
	 */
	void delMch(Long id,Integer sn,String delstatus);

	/**
	 * 
	 * @return查询订单
	 */
	List<MchOrder> findMchorder(String tradeno, String mchname, Long supplierid, String status,String username,Timestamp time1, Timestamp time2,
			Integer pageNo, int maxnum);
	/**
	 * 
	 * @param name
	 * @return查询订单总条数
	 */
	int findOrderCount(String tradeno, String mchname, String username,String status,Long supplierid, Timestamp time1, Timestamp time2);

	/**
	 * 
	 * @return订单详情
	 */
	List<MchOrderDetail> findMchDetail();
	/**
	 * 
	 * @param id
	 * @param status
	 *            上下架
	 */
	void status(Long mchid, String status);

	/**
	 * 
	 * 订单详细页面展示
	 */
	List<MchOrderDetail> orderList(Long id);

	MchOrder mchorder(Long id);

	/**
	 * 
	 * @param name
	 * @return查询商品总条数
	 */
	int findListCount(Long itemid, String name,Long supplierid);

	/**
	 * 接收并判断用户名是否存在
	 */
	boolean shopuser(String name,Long supplierid,Long id);
	
	void updateOrderStatus(Long orderid, String status, String expressInfo);
	List<MchOrder>modList(String tradeno, String mchname, Long supplierid, String status,String username,Timestamp time1, Timestamp time2,
			Integer pageNo, int maxnum);

	
}

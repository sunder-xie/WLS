package com.wheelys.service.admin;

import java.util.List;
import java.util.Map;

import com.wheelys.model.CafeItem;
import com.wheelys.model.CafeProduct;
import com.wheelys.model.ProductShop;
import com.wheelys.web.action.admin.vo.ProductShopVo;

public interface ProductService {
	/**
	 * 
	 * @return商品表展示
	 */
	List<CafeProduct> showcafeList(String prodname, String itemid, Integer pageNo, int rowsPerPage);

	/*
	 * 商品表总条数
	 */
	int findListCount(String prodname, String itemid);

	/**
	 * @param psshopid
	 * @param 根据类别id拿商品
	 * @param itemid
	 * @return
	 */
	Map<Long, CafeItem> getCafeMap();

	/**
	 * 
	 * @param id
	 * @return根据商品id查询
	 */
	CafeProduct cafeproduct(Long id);

	/**
	 * 接收并判断商品名和英文名是否存在
	 */
	boolean productName(String prodname, String prodenname, Long shopid);

	/**
	 * 
	 * @param shopid
	 * @return根据id拿商品
	 */
	Map<CafeItem, List<ProductShopVo>> getShopCafeProductMap(Long shopid);

	/*
	 * 
	 */
	List<ProductShopVo> getCafeProductListByShop(Long shopid);

	/*
	 * 
	 */
	List<Long> getProductIdList(Long shopid);

	/*
	 * 
	 */
	void upProductShop(Long psid, Long shopid, String delstatus, Long prodid);

	/**
	 * @param psshopid
	 * @param 拿所有类别
	 * @param itemid
	 * @return
	 */
	List<CafeItem> getCafeItemList();

	/**
	 * @param 存储咖啡
	 * @param itemid
	 * @return
	 */

	void addProduct(CafeProduct cafeProduct);

	/**
	 * 
	 * @param psshopid
	 * @param name
	 * @param itemid
	 * @return
	 */
	void updateProcut(Long psid, Integer psorder, Integer display);

	/**
	 * @param psshopid
	 * @param 根据类别id拿商品
	 * @param itemid
	 * @return
	 */
	Map<Long, List<CafeProduct>> getCafeProductMap();

	/**
	 * 
	 * @param shopid
	 * @param arr
	 * @param productidList新增
	 */
	void addProductShop(Long shopid, String arr, String productidList);

	/**
	 * 
	 * @param prodid
	 * @return
	 */
	List<ProductShop> product(Long prodid);

	String ca(Long prodid);

	void delProduct(Long id, Integer display);
	
}

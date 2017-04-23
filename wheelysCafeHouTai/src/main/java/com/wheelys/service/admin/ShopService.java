package com.wheelys.service.admin;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.model.CafeItem;
import com.wheelys.model.CafeRecruit;
import com.wheelys.model.CafeShop;
import com.wheelys.model.CafeShopProfile;
import com.wheelys.model.ShopExpressAddress;
import com.wheelys.model.banner.WheelysBanner;
import com.wheelys.web.action.admin.vo.CafeShopVo;
import com.wheelys.web.action.admin.vo.ProductShopVo;

public interface ShopService {

	/**
	 * 
	 * @param cafeShop
	 */
	void saveCafeShop(CafeShop cafeShop);// 添加新的商家 1

	/**
	 * 
	 * @param name
	 * @param rowsPerPage
	 * @param pageNo
	 * @return分页
	 */
	List<CafeShopVo> findlist(String name, Long operatorid, String cityCode, Integer pageNo,
			int rowsPerPage);

	/**
	 * 
	 * @param psshopid
	 * @param name
	 * @param itemid
	 * @return
	 */
	List<ProductShopVo> findlist2(Long psshopid, String name, Long itemid);

	/**
	 * 
	 * @param psshopid
	 * @param name
	 * @param itemid
	 * @return
	 */
	CafeShop findshop(Long shopid);

	/**
	 * 
	 * @param psshopid
	 * @param 修改前查看店铺
	 * @param itemid
	 * @return
	 */
	CafeShop findCafeShop(Long shopid);

	/**
	 * 
	 * @param name
	 * @return查询总条数
	 */
	int findListCount(String name, Long operatorid, String citycode);
	int findCloseListCount(String name, Long operatorid, String citycode);
	
	/**
	 * 接收并判断用户名是否存在
	 */
	boolean shopuser(String shopname, Long shopid);

	/**
	 * 根据shopid查找cafeShopProfile
	 * 
	 * @param shopid
	 * @return
	 */
	CafeShopProfile findShopProfileByShopid(Long shopid);

	/**
	 * 
	 * @param shopid
	 * @param mobile
	 * @param takeawaystatus
	 * @param reservedstatus
	 */
	void saveOrUpdateShopProfile(Long shopid, String mobile, String takeawaystatus, String reservedstatus,
			String create);

	/**
	 * 根据shopid查找对应的配送地址
	 * 
	 * @param shopid
	 * @return
	 */
	List<ShopExpressAddress> findExpressAddressByShopid(Long shopid);

	/**
	 * 添加一条外送地址信息
	 * 
	 * @param shopid
	 * @param address
	 */
	Long addExprAddr(Long shopid, String address);

	/**
	 * 根据地址删除一条外送信息
	 * 
	 * @param id
	 * @return
	 */
	Boolean deleteExprAddrByAddr(Long id);

	/**
	 * 
	 */
	List<CafeRecruit> recruit(String name, Integer pageNo, int rowsPerPage);

	/**
	 * 
	 * @param name
	 * @return查询总条数
	 */
	int findCafeRecruitCount(String name);

	/**
	 * 
	 * @param id
	 * @return详情查询
	 */
	CafeRecruit caferecruit(Long id);

	/**
	 * 
	 * @param shopid
	 * @return展示中的banner图
	 */
	List<WheelysBanner> showBannerImgList(Long shopid);

	/**
	 * 
	 * @param shopid
	 * @return根据id拿商品
	 */
	Map<CafeItem, List<ProductShopVo>> getShopCafeProductMap(Long shopid);

	ResultCode changeShopStatus(Long shopid, String status, Timestamp closedowntime, String reason);

	/**
	 * 查询所有店铺
	 * 
	 * @return
	 */
	List<CafeShop> cafeShopList();
	/**
	 * 接收并判断简称是否存在
	 */
	boolean shopesname(String esname, Long shopid);
	/**
	 * 删除
	 * @param id
	 */
	 void delshop(Long shopid);

	void shopclose(Long shopid);
	
	List<CafeShopVo> findCloseList(String name,String citycode, Long operatorid,Integer pageNo,int maxnum);
}

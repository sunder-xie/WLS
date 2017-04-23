package com.wheelys.service.admin;

import java.util.List;
import com.wheelys.model.banner.WheelysBanner;

public interface BannerService {
	/**
	 * 展示banner列表
	 * @param pageNo
	 * @param type
	 * @param rowsPerPage
	 * @return
	 */
	List<WheelysBanner> getBannerList(Integer pageNo, String type, int rowsPerPage);
	/**
	 * 修改banner状态
	 * @param id
	 * @param sn
	 * @param showstatus
	 * @return
	 */
	WheelysBanner updateShowstatus(Long id, Integer sn,String showstatus);
	/**
	 * 删除banner
	 * @param id
	 * @return
	 */
	WheelysBanner deleteWechatBanner(Long id);
	/**
	 * 保存banner
	 * @param id
	 * @param shopid
	 * @param title
	 * @param tourl
	 * @param imageurl
	 * @param type
	 * @return
	 */
	WheelysBanner saveBanner(Long id, Long shopid, String title, String tourl, String imageurl, String type);
	/**
	 * 根据id拿banner
	 * @param id
	 * @return
	 */
	WheelysBanner getBanner(Long id);
	/**
	 * 拿到分页总条数
	 * @param type
	 * @return
	 */
	int findBannerCount(String type);
	/**
	 * 展示banner列表
	 * @param shopid
	 * @param pageNo
	 * @param rowsPerPage
	 * @return
	 */
	List<WheelysBanner> getShopBannerList(Long shopid, Integer pageNo, int rowsPerPage);
	
}

package com.wheelys.service.order;

import com.wheelys.model.order.MemberAddress;

public interface MemberAddressService {

	/**
	 * 根据店铺ID和用户ID查询外送地址
	 * @param memberid
	 * @param shopid
	 * @return
	 */
	MemberAddress getMemberAddressByMemidAndShopid(Long memberid, Long shopid);

	/**
	 * 新增/修改用户外送地址
	 * @param memberid
	 * @param shopid
	 * @param username
	 * @param mobile
	 * @param detailaddress
	 * @param address
	 * @return
	 */
	MemberAddress saveOrUpdateMemAddr(Long memberid, Long shopid, String username, String mobile, String detailaddress,
			String address) ;

}

package com.wheelys.service.order;

import com.wheelys.model.order.MemberOrderInfo;

public interface OrderInfoService {

	/**
	 * 保持或更新 MemberOrderInfo
	 * @param tradeNo
	 * @param pamentType
	 */
	void saveOrUpdateOrderInfo(String tradeNo, String pamentType);

	/**
	 * 根据用户id查找 MemberOrderInfo
	 * @param memberid
	 * @return
	 */
	MemberOrderInfo getOrderInfoByMemberId(Long memberid);

}

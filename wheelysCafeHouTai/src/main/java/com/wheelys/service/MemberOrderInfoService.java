package com.wheelys.service;

import java.util.Date;
import java.util.List;

import com.wheelys.model.MemberOrderInfo;

public interface MemberOrderInfoService {

	List<MemberOrderInfo> getNewOrderMemberByShopidAndDay(Long shopid, Date day);

	List<MemberOrderInfo> getOldOrderMemberByShopidAndDay(Long shopid, Date day);

	List<MemberOrderInfo> getMemberInfoByIds(List<Long> memberids);

	/**
	 * 获取用户最新订单信息
	 * 
	 * @param pageNo
	 * @param rowPerPage
	 * @param region
	 *            大区
	 * @param cityCode
	 *            城市
	 * @param shopId
	 *            店铺
	 * @param phone
	 *            手机
	 * @param createTimeBegin
	 *            创建时间
	 * @param createTimeEnd
	 *            创建时间
	 * @return
	 */
	List<MemberOrderInfo> getMemberLatestOrderInfo(Integer pageNo, int rowPerPage, String region, String cityCode,
			Long shopId, String phone, Date createTimeBegin, Date createTimeEnd);

	// 获取用户最新订单信息的数据量
	int getMemberLatestOrderInfoCount(String region, String cityCode, Long shopId, String phone, Date createTimeBegin,
			Date createTimeEnd);
}

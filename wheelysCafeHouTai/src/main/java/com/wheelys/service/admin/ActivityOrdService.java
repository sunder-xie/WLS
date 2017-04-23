package com.wheelys.service.admin;

import java.util.Date;
import java.util.List;

import com.wheelys.web.action.admin.vo.CardCouponsOrderVo;

public interface ActivityOrdService {

	/**
	 * 获取活动订单列表
	 * @param pageNo
	 * @param mobile
	 * @param rowsPerPage
	 * @param shopid 店铺id
	 * @param begin
	 * @param end
	 * @return
	 */
	List<CardCouponsOrderVo> findordvoList(Integer pageNo, String mobile, int rowsPerPage, Long shopid, Date begin,
			Date end);

	int findCount(String mobile, Long shopid, Date begin, Date end);

}

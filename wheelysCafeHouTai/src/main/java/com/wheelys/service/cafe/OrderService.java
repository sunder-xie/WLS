package com.wheelys.service.cafe;

import java.util.Date;
import java.util.List;

import com.wheelys.model.pay.WheelysOrder;
import com.wheelys.web.action.openapi.vo.WheelysOerderVo;

public interface OrderService {

	List<Long> getOrderedMemberids(Long shopid, Date day);

	WheelysOrder getOrder(String tradeno);

	/**
	 * 获取订单列表
	 * 
	 * @param region
	 *            大区
	 * @param cityCode
	 *            城市
	 * @param shopId
	 *            店铺
	 * @param status
	 *            订单状态
	 * @param tradeno
	 *            订单id
	 * @param createTimeBegin
	 *            创建时间
	 * @param createTimeEnd
	 *            创建时间
	 * @param paymethod
	 *            支付方式
	 * @param category
	 *            配送方式
	 */
	List<WheelysOerderVo> findOrderList(Integer pageNo, int rowsPerPage, String region, String cityCode, Long shopId,
			String status, String tradeno, Date createTimeBegin, Date createTimeEnd, String paymethod, String category,
			String elecCardBatchId);

	/**
	 * 获取订单列表的count
	 */
	int findOrderListCount(String region, String cityCode, Long shopId, String status, String tradeno,
			Date createTimeBegin, Date createTimeEnd, String paymethod, String category, String elecCardBatchId);

	/**
	 * 根据tradno查询订单
	 * 
	 * @param tradeno
	 * @return
	 */
	WheelysOerderVo findOrderByTradeno(String tradeno);

}

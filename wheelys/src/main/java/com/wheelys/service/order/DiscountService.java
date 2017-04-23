package com.wheelys.service.order;

import java.sql.Timestamp;
import java.util.List;

import com.wheelys.support.concurrent.AtomicCounter;
import com.wheelys.model.CartProduct;
import com.wheelys.model.discount.DiscountActivity;
import com.wheelys.model.order.WheelysOrder;
import com.wheelys.model.order.WheelysOrderDetail;

public interface DiscountService {
	
	/**
	 * 设置订单优惠
	 * @param order
	 * @param detailList
	 * @param cartProductList
	 */
	void setOrderDiscount(WheelysOrder order, List<WheelysOrderDetail> detailList, List<CartProduct> cartProductList);

	/**
	 * 根据ID查询优惠活动
	 * @param id
	 * @return
	 */
	DiscountActivity getDiscount(Long id);

	/**
	 * 更新优惠活动信息
	 * @param discountid
	 * @param status
	 * @param shopids
	 * @param begintime
	 * @param endtime
	 */
	void updateDiscount(Long discountid, String status, String shopids, Timestamp begintime, Timestamp endtime);
	
	/**
	 * 计数器
	 * @param discountid	活动id
	 * @return
	 */
	AtomicCounter getDiscountCounter(Long discountid);

}

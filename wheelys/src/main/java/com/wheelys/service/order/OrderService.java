package com.wheelys.service.order;

import java.util.List;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.helper.OrderContainer;
import com.wheelys.model.CartProduct;
import com.wheelys.model.order.WheelysOrder;
import com.wheelys.model.order.WheelysOrderDetail;
import com.wheelys.web.action.wap.vo.ElecCardVo;
import com.wheelys.web.action.wap.vo.WheelysOrderVo;

public interface OrderService {
	
	/**
	 * 用户下单接口
	 * @param cartProductVoList 购物车
	 * @param memberid
	 * @param membername
	 * @param ukey
	 * @param shopid
	 * @param cardid
	 * @param citycode
	 * @param isSave 
	 * @return
	 */
	ResultCode<OrderContainer> addOrder(List<CartProduct> cartProductVoList, Long memberid, String mobile, String membername, String ukey, Long shopid, Long cardid, String citycode, boolean isSave);

	/**
	 * 用户查询订单列表
	 * @param status
	 * @param memberid
	 * @return
	 */
	List<WheelysOrderVo> getOrderList(String status, Long memberid, int pageNo, int maxnum);
	
	/**
	 * 用户查询订单详情
	 * @param tradeno
	 * @param memberid
	 * @return
	 */
	ResultCode<WheelysOrderVo> getWheelysOrderVo(String tradeno, Long memberid);

	/**
	 * 根据订单查询订单
	 * @param tradeNo
	 * @return
	 */
	WheelysOrder getOrderByTradeNo(String tradeNo);

	/**
	 * 保存订单外送和预约信息
	 * @param tradeno
	 * @param memberid
	 * @param addressid
	 * @param category
	 * @param reservedtime
	 * @return
	 */
	ResultCode saveOrderCategory(String tradeno, Long memberid, Long addressid,String category, Integer reservedtime);

	/**
	 * 查询用户默认使用的优惠券
	 * @param order
	 * @param detailList
	 * @param cartProductVoList
	 * @return
	 */
	ElecCardVo useDefaultCard(WheelysOrder order, List<WheelysOrderDetail> detailList, List<CartProduct> cartProductVoList);

	void saveOntherInfo(String tradeno, Long memberid, String otherinfo);

	WheelysOrder getLastPaidOrder(Long memberid);

	ResultCode<OrderContainer> addAppOrder(List<CartProduct> cartProductVoList,Long memberid, String mobile,
			String membername, String ukey, Long shopid, Long cardid, String citycode, boolean isSave);

}

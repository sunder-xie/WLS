package com.wheelys.service.admin;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.wheelys.api.vo.ResultCode;
import com.wheelys.model.acl.User;
import com.wheelys.model.orderrefund.RefundOrder;
import com.wheelys.web.action.openapi.vo.RefundOrderVo;
import com.wheelys.web.action.openapi.vo.WheelysOerderVo;

public interface OrderRefundService {
	/**
	 * 退款订单展示
	 * 
	 * @param pageNo
	 * @param maxnum
	 * @return
	 */
	List<RefundOrder> showRefundList(Integer pageNo, int maxnum);

	/**
	 * 返回查询总条数
	 * 
	 * @return
	 */
	int findListCount();

	/**
	 * 接口调用添加
	 * 
	 * @param userid
	 * @param refunduserid
	 * @param refundinfo
	 * @param tradeno
	 * @param refundamount
	 * @return
	 */
	ResultCode<RefundOrder> addRefund(Long userid, Long refunduserid, String refundinfo, String tradeno,
			Integer refundamount);

	/**
	 * 更新状态
	 * 
	 * @param id
	 * @param status
	 * @param userid
	 * @param nickname
	 * @param expressInfo
	 * @param refundinfo
	 */
	void updateRefundStatus(Long id, String status, Long userid, String nickname, String expressInfo,
			String refundinfo);

	/**
	 * 根据id返回退款对象
	 * 
	 * @param id
	 * @return
	 */
	RefundOrder showRefundinfo(Long id);

	/**
	 * 根据订单号查询
	 * 
	 * @param tradeno
	 * @return
	 */
	WheelysOerderVo getOrder(String tradeno);

	/**
	 * 根据退款订单查询
	 * 
	 * @param shopid
	 * @param fromtime
	 * @param totime
	 * @param tradeno
	 * @param pageno
	 * @return
	 */
	List<RefundOrderVo> getOrderList(Long shopid, Timestamp fromtime, Timestamp totime, String tradeno, Integer pageno);

	User user(Long id);

	/**
	 * 根据时间查询店铺退款订单
	 * 
	 * @param begin
	 * @param end
	 * @param shopid
	 * @param pageNo
	 * @param rowsPerPage
	 * @return
	 */
	List<RefundOrder> showRefundListByTime(Date begin, Date end, Long shopid, Integer pageNo, int rowsPerPage);

	/**
	 * 根据店铺和时间段查询总条数
	 * 
	 * @param begin
	 * @param end
	 * @param shopid
	 * @return
	 */
	int findListCount(Date begin, Date end, Long shopid);

	/**
	 * 根据时间段查询制定店铺退单列表
	 * 
	 * @param begin
	 * @param end
	 * @param shopid
	 * @return
	 */
	List<RefundOrder> showRefundListById(Date begin, Date end, Long shopid);
}

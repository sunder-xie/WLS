package com.wheelys.service.admin;

import java.sql.Timestamp;
import java.util.List;
import com.wheelys.api.vo.ResultCode;
import com.wheelys.model.merchant.MchOrder;
import com.wheelys.web.action.openapi.vo.MchOrderDetailVo;
import com.wheelys.web.action.openapi.vo.MchOrderVo;

public interface MchOrderService {
	/**
	 * 展示VO订单列表
	 * @param shopid
	 * @param mobile
	 * @param contacts
	 * @param isexpress
	 * @param detailList
	 * @param clientIp
	 * @return
	 */
	ResultCode<MchOrderVo> addMchOrder(Long shopid, String mobile, String contacts, String isexpress, List<MchOrderDetailVo> detailList,
			String clientIp);
	/**
	 * 展示订单列表
	 * @param shopid
	 * @param status
	 * @param fromtime
	 * @param totime
	 * @param tradeno
	 * @param pageno
	 * @return
	 */
	List<MchOrder> getMchOrderList(Long shopid, String status, Timestamp fromtime, Timestamp totime, String tradeno,
			Integer pageno);
	/**
	 * 添加订单
	 * @param shopid
	 * @param tradeno
	 * @param status
	 * @param contacts
	 * @param mobile
	 * @param isexpress
	 * @param remark
	 * @param clientIp
	 * @return
	 */
	ResultCode<MchOrderVo> confirmMchOrder(Long shopid, String tradeno, String status, String contacts, String mobile, String isexpress, String remark, String clientIp);
	/**
	 * 返回订单详细vo
	 * @param shopid
	 * @param tradeno
	 * @return
	 */
	MchOrderVo getMchOrderVoDetail(Long shopid, String tradeno);
	/**
	 * 返回订单详细
	 * @param tradeno
	 * @return
	 */
	MchOrder getMchOrderDetail(String tradeno);

}

package com.wheelys.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.UnmodifiableMap;

public class OrderConstant {

	public static final String ORDER_PRICATEGORY_CAFE = "CAFE"; // CAFE模块

	public static final String STATUS_NEW = "new"; // 新订单
	public static final String STATUS_NEW_CONFIRM = "new_confirm"; // 已确认下单
	public static final String STATUS_ACCEPT = "accept"; // 已接受
	public static final String STATUS_SEND = "send"; // 已发货
	public static final String STATUS_FINISH = "finish"; // 已完成
	public static final String STATUS_CANCEL = "cancel"; // 已取消
	public static final String PAYSTATUS_NEW = "new"; // 新订单
	public static final String PAYSTATUS_PAID = "paid"; // 付完款

	public static final String ORDER_PRICATEGORY_REFUND = "REFUND"; // 退款模块

	public static final String REFUNDSTATUS_PENDING_AUDIT = "pending_audit"; // 待审核
	public static final String REFUNDSTATUS_ALREADY_PASSED = "already_passed"; // 通过
	public static final String REFUNDSTATUS_NO_PASSED = "no_passed"; // 未通过
	public static final String REFUNDSTATUS_FINISH = "refund_finish"; // 退款成功
	public static final String REFUNDSTATUS_CANCEL = "cancel"; // 取消
	public static final String REFUNDSTATUS_REFUND_FAILED = "refund_ failed"; // 退款失败

	public static List<String> refundStatus = new ArrayList<String>();

	public static Map<String, String> ORDER_STATUS_MAP = new HashMap<String, String>();

	static {
		refundStatus.add(REFUNDSTATUS_PENDING_AUDIT);
		refundStatus.add(REFUNDSTATUS_ALREADY_PASSED);
		refundStatus.add(REFUNDSTATUS_NO_PASSED);
		refundStatus.add(REFUNDSTATUS_FINISH);
		refundStatus.add(REFUNDSTATUS_CANCEL);
		refundStatus.add(REFUNDSTATUS_REFUND_FAILED);

		Map map = new HashMap<String, String>();
		map.put(STATUS_NEW, "新订单");
		map.put(STATUS_NEW_CONFIRM, "已确认下单");
		map.put(STATUS_ACCEPT, "已接受");
		map.put(STATUS_SEND, "已发货");
		map.put(STATUS_FINISH, "已完成");
		map.put(STATUS_CANCEL, "已取消");
		map.put(PAYSTATUS_NEW, "新订单");
		map.put(PAYSTATUS_PAID, "付完款");
		ORDER_STATUS_MAP = UnmodifiableMap.decorate(map);
	}
}

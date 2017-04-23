package com.wheelys.constant;

public class CafeOrderConstant {

	public static final String ORDER_PRICATEGORY_CAFE = "CAFE";		//CAFE模块	

	public static final String STATUS_NEW = "new";						//新订单
	public static final String STATUS_NEW_CONFIRM = "new_confirm";		//新订单，确认去付款
	public static final String STATUS_PAID = "paid";					//付完款，订单未必是成交的
	public static final String STATUS_FINISH = "finish";				//成交的
	public static final String STATUS_PAID_FAILURE = "paid_failure";	//付完款，订单有错误
	public static final String STATUS_PAID_SPECIAL = "paid_special";	//付完款，订单有不成功，也不退款，做了特殊处理，比如补偿券
	public static final String STATUS_PAID_SUCCESS = "paid_success";	//付款后，订单成交
	public static final String STATUS_PAID_RETURN = "paid_return";		//付完款，订单取消退款到余额

	public static final String CATEGORY_TAKE = "TAKE";			//自取
	public static final String CATEGORY_TAKEAWAY = "TAKEAWAY";	//外卖
	public static final String CATEGORY_RESERVED  = "RESERVED";	//预约

	public static final String ORDER_PRICATEGORY_REFUND = "REFUND"; // 退款模块

	public static final String REFUNDSTATUS_PENDING_AUDIT = "pending_audit"; // 待审核
	public static final String REFUNDSTATUS_ALREADY_PASSED = "already_passed"; // 通过
	public static final String REFUNDSTATUS_NO_PASSED = "no_passed"; // 未通过
	public static final String REFUNDSTATUS_FINISH = "refund_finish"; // 退款成功
	public static final String REFUNDSTATUS_CANCEL = "cancel"; // 取消
	public static final String REFUNDSTATUS_REFUND_FAILED = "refund_ failed"; // 退款失败


}

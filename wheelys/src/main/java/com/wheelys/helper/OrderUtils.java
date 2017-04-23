package com.wheelys.helper;

import com.wheelys.model.order.CardCouponsOrder;
import com.wheelys.model.order.MchOrder;
import com.wheelys.model.order.WheelysOrder;

public class OrderUtils {

	public static boolean isNetPaid(WheelysOrder order){
		return order.getNetpaid() > 0;
	}

	public static boolean isNetPaid(MchOrder order){
		return order.getNetpaid() > 0;
	}
	
	public static boolean isNetPaid(CardCouponsOrder order){
		return order.getNetpaid() > 0;
	}
	
}

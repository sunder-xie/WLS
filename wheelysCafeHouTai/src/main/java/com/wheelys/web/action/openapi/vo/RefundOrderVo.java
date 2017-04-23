package com.wheelys.web.action.openapi.vo;

import java.util.List;

import com.wheelys.model.orderrefund.RefundOrder;
import com.wheelys.model.pay.WheelysOrderDetail;
import com.wheelys.util.VmUtils;

public class RefundOrderVo {

	private WheelysOerderVo order;

	private List<WheelysOrderDetail> detailList;

	public RefundOrderVo(RefundOrder refund, List<WheelysOrderDetail> detailList, String takekey, String disreason, Integer discount) {
		this.order = new WheelysOerderVo();
		order.setTradeno(refund.getTradeno());
		order.setMembername(refund.getMembername());
		order.setCreatetime(refund.getCreatetime());
		order.setStatus(refund.getStatus());
		order.setRefundamount(VmUtils.getAmount(refund.getRefundamount()));
		order.setCategory(refund.getCategory());
		order.setTakekey(takekey);
		order.setNetpaid(VmUtils.getAmount(refund.getNetpaid()));
		order.setRefundfaildlinfo(refund.getRefundfaildlinfo());
		order.setDisreason(disreason);
		order.setDiscount(discount);
		this.detailList = detailList;
	}

	public List<WheelysOrderDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<WheelysOrderDetail> detailList) {
		this.detailList = detailList;
	}

	public WheelysOerderVo getOrder() {
		return order;
	}

	public void setOrder(WheelysOerderVo order) {
		this.order = order;
	}

}

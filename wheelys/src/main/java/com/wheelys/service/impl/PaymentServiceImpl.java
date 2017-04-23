package com.wheelys.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.BeanUtil;
import com.wheelys.util.DateUtil;
import com.wheelys.util.JsonUtils;
import com.wheelys.constant.CafeOrderConstant;
import com.wheelys.constant.PayConstant;
import com.wheelys.helper.OrderUtils;
import com.wheelys.model.order.CardCouponsOrder;
import com.wheelys.model.order.MchOrder;
import com.wheelys.model.order.WheelysOrder;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.PaymentService;
import com.wheelys.service.cafe.WpsService;
import com.wheelys.service.order.OrderInfoService;
import com.wheelys.service.pay.ElecCardService;

@Service("paymentService")
public class PaymentServiceImpl extends BaseServiceImpl implements PaymentService {

	@Autowired@Qualifier("wpsService")
	private WpsService wpsService;
	@Autowired@Qualifier("elecCardService")
	private ElecCardService elecCardService;
	@Autowired@Qualifier("orderInfoService")
	private OrderInfoService orderInfoService;
	
	@Override
	public ResultCode<WheelysOrder> netPayOrder(String tradeNo, String payseqno, int paidamount, String gatewaycode, String mchid, String paymethod, Map<String, String> otherInfoMap) {
		// 在此记录支付通知记录
		savePayRecord(tradeNo, payseqno, paidamount, otherInfoMap);

		WheelysOrder order = baseDao.getObjectByUkey(WheelysOrder.class, "tradeno", tradeNo);
		if (order == null) {
			String msg = "notify order 订单不存在：" + tradeNo;
			dbLogger.warn(msg);
			return ResultCode.getFailure(msg);
		}
		if (!OrderUtils.isNetPaid(order)) {
			if(order.getCardid() != null){
				ResultCode result = elecCardService.useElecCard(order);
				if(!result.isSuccess()){
					return ResultCode.getFailure("优惠券异常!cardid:"+order.getCardid()+",tradeNo:"+tradeNo+",payseqno:"+payseqno);
				}
				order.setDisreason("优惠券");
			}
			/*********************
			 * 1、更改付款状态 2、更改订单状态 3、增加用户积分 4、订单有效期加半年(为了查询方便)
			 *****************************/
			String status = order.getStatus();
			Timestamp curTime = new Timestamp(System.currentTimeMillis());
			if (StringUtils.isNotBlank(payseqno)) {
				order.setPayseqno(payseqno);
			}
			order.setNetpaid(paidamount);
			order.setUpdatetime(curTime);
			order.setPaidtime(curTime);
			order.setStatus(CafeOrderConstant.STATUS_PAID); // 先设置成此状态，第二步设成paid_success
			order.setPaystatus(CafeOrderConstant.STATUS_PAID);
			order.setPaymethod(paymethod);
			order.setGatewayCode(gatewaycode);
			order.setMerchantCode(mchid);
			this.setTaketime(order);
			baseDao.updateObject(order);
			
			if(order.getSdid() != null){//修改特价活动卖出数量
				try {
					String sql = "update DISCOUNTACTIVITY set ALLOWADDNUM = ALLOWADDNUM + 1 where id = ?";
					jdbcTemplate.update(sql, order.getSdid());
				} catch (Exception e) {
					dbLogger.error("修改特价活动使用数量出错", e);
				}
			}
			sendNotify(order.getTradeno(), order.getPaymethod());
			dbLogger.warn(order.getTradeno() + "," + payseqno + "," + status + "-->" + order.getStatus());
			return ResultCode.getSuccessReturn(order);
		} else {
			if(!StringUtils.equals(order.getPayseqno(), payseqno)){
				savePayRepeatTrade(tradeNo, payseqno, paidamount, otherInfoMap);
				return ResultCode.getFailure(tradeNo+",订单重复支付!"+order.getPayseqno()+":"+payseqno);
			}
			return ResultCode.getFailure("重复调用订单支付!"+tradeNo+":"+payseqno);
		}
	}
	
	private void sendNotify(String tradeno, String paymethod) {
		try {
			orderInfoService.saveOrUpdateOrderInfo(tradeno,paymethod);
		} catch (Exception e) {
			dbLogger.warn("用户订单信息更新失败：" +  tradeno);
		}
	}

	private void setTaketime(WheelysOrder order){
		try {
			if(!StringUtils.equals(order.getCategory(), CafeOrderConstant.CATEGORY_RESERVED)){
				order.setTaketime(order.getPaidtime());
			}else{
				Map map = JsonUtils.readJsonToMap(order.getOtherinfo());
				Integer reservedtime = (Integer) map.get("reservedtime");
				if(reservedtime == null){
					reservedtime = 0;
				}
				order.setTaketime(DateUtil.addMinute(order.getPaidtime(), reservedtime));
			}
		} catch (Exception e) {
			dbLogger.warn("setTaketime error!"+order.getTradeno()+":"+order.getPayseqno());
		}
	}

	private void savePayRepeatTrade(String tradeNo, String payseqno, int paidamount, Map<String, String> otherInfoMap) {
		dbLogger.warn("savePayRepeatTrade," + tradeNo+":"+payseqno+","+paidamount+","+JsonUtils.writeMapToJson(otherInfoMap));
	}

	private void savePayRecord(String tradeNo, String payseqno, int paidamount, Map<String, String> otherInfoMap) {
		dbLogger.warn("savePayRecord," + tradeNo+":"+payseqno+","+paidamount+","+JsonUtils.writeMapToJson(otherInfoMap));
	}

	@Override
	public ResultCode<WheelysOrder> payZeroOrder(WheelysOrder order){
		if(order.getQuantity() == 1 && order.getPayfee() == 0){
			if (StringUtils.equals(order.getPaystatus(), PayConstant.PAYSTATUS_NEW)) {
				/*********************
				 * 1、更改付款状态 2、更改订单状态 3、增加用户积分 4、订单有效期加半年(为了查询方便)
				 *****************************/
				String status = order.getStatus();
				Timestamp curTime = new Timestamp(System.currentTimeMillis());
				order.setUpdatetime(curTime);
				order.setPaidtime(curTime);
				order.setStatus(CafeOrderConstant.STATUS_PAID); // 先设置成此状态，第二步设成paid_success
				order.setPaystatus(PayConstant.PAYSTATUS_PAID);
				order.setPaymethod(PayConstant.PAYMETHOD_CARD);
				order.setGatewayCode(PayConstant.GATEWAYCODE_CARD);
				this.setTaketime(order);
				ResultCode result = elecCardService.useElecCard(order);
				if(result.isSuccess()){
					order.setDisreason("优惠券");
					baseDao.updateObject(order);
					dbLogger.warn("tradeno:"+order.getTradeno() + "shopid:"+order.getShopid()+",status:" + status + "-->" + order.getStatus());
					return ResultCode.getSuccessReturn(order);
				}
				return result;
			}
		}
		return ResultCode.getFailure("优惠券支付异常,tradeno:"+order.getTradeno());
	}

	@Override
	public ResultCode<CardCouponsOrder> netPayCardOrder(String tradeno, String payseqno, int paidamount, String gatewaycode, String mchid, String paymethod, Map<String, String> otherInfoMap) {
		// 在此记录支付通知记录
		savePayRecord(tradeno, payseqno, paidamount, otherInfoMap);

		CardCouponsOrder order = baseDao.getObjectByUkey(CardCouponsOrder.class, "tradeno", tradeno);
		if (order == null) {
			String msg = "notify order 订单不存在：" + tradeno;
			dbLogger.warn(msg);
			return ResultCode.getFailure(msg);
		}
		if (!OrderUtils.isNetPaid(order)) {
			/*********************
			 * 1、更改付款状态 2、更改订单状态 3、增加用户积分 4、订单有效期加半年(为了查询方便)
			 *****************************/
			String status = order.getStatus();
			Timestamp curTime = new Timestamp(System.currentTimeMillis());
			if (StringUtils.isNotBlank(payseqno)) {
				order.setPayseqno(payseqno);
			}
			order.setNetpaid(paidamount);
			order.setUpdatetime(curTime);
			order.setPaidtime(curTime);
			order.setStatus(CafeOrderConstant.STATUS_PAID); // 先设置成此状态，第二步设成paid_success
			order.setPaystatus(CafeOrderConstant.STATUS_PAID);
			order.setPaymethod(paymethod);
			order.setGatewayCode(gatewaycode);
			order.setMerchantCode(mchid);
			baseDao.updateObject(order);
			try {
				WheelysMember member = this.baseDao.getObject(WheelysMember.class, order.getMemberid());
				List<Long> idList = BeanUtil.getIdList(order.getEbatchid(), ",");
				elecCardService.bindElecCardByEbatchid(member, null, null, idList.get(0), order.getQuantity(), order.getId()+"");
			} catch (Exception e) {
				order.setStatus(CafeOrderConstant.STATUS_PAID_FAILURE);
				baseDao.updateObject(order);
				return ResultCode.getFailure(tradeno+",绑定劵异常!"+order.getPayseqno()+":"+payseqno);
			}
			dbLogger.warn(order.getTradeno() + "," + payseqno + "," + status + "-->" + order.getStatus());
			return ResultCode.getSuccessReturn(order);
		} else {
			if(!StringUtils.equals(order.getPayseqno(), payseqno)){
				savePayRepeatTrade(tradeno, payseqno, paidamount, otherInfoMap);
				return ResultCode.getFailure(tradeno+",订单重复支付!"+order.getPayseqno()+":"+payseqno);
			}
			return ResultCode.getFailure("重复调用订单支付!"+tradeno+":"+payseqno);
		}
	}

	@Override
	public ResultCode<MchOrder> payMchOrder(String tradeno, String payseqno, int paidamount, String gatewaycode,
			String mchid, String paymethod, Map<String, String> otherInfoMap) {
		// 在此记录支付通知记录
		savePayRecord(tradeno, payseqno, paidamount, otherInfoMap);

		MchOrder order = baseDao.getObjectByUkey(MchOrder.class, "tradeno", tradeno);
		if (order == null) {
			String msg = "notify order 订单不存在：" + tradeno;
			dbLogger.warn(msg);
			return ResultCode.getFailure(msg);
		}
		if (!OrderUtils.isNetPaid(order)) {
			/*********************
			 * 1、更改付款状态 2、更改订单状态 3、增加用户积分 4、订单有效期加半年(为了查询方便)
			 *****************************/
			String status = order.getStatus();
			Timestamp curTime = new Timestamp(System.currentTimeMillis());
			if (StringUtils.isNotBlank(payseqno)) {
				order.setPayseqno(payseqno);
			}
			order.setNetpaid(paidamount);
			order.setUpdatetime(curTime);
			order.setPaidtime(curTime);
			order.setStatus(CafeOrderConstant.STATUS_PAID); // 先设置成此状态，第二步设成paid_success
			order.setPaystatus(CafeOrderConstant.STATUS_PAID);
			order.setPaymethod(paymethod);
			order.setGatewayCode(gatewaycode);
			order.setMerchantCode(mchid);
			baseDao.updateObject(order);
			dbLogger.warn(order.getTradeno() + "," + payseqno + "," + status + "-->" + order.getStatus());
			return ResultCode.getSuccessReturn(order);
		} else {
			if(!StringUtils.equals(order.getPayseqno(), payseqno)){
				savePayRepeatTrade(tradeno, payseqno, paidamount, otherInfoMap);
				return ResultCode.getFailure(tradeno+",订单重复支付!"+order.getPayseqno()+":"+payseqno);
			}
			return ResultCode.getFailure("重复调用订单支付!"+tradeno+":"+payseqno);
		}
	}

}

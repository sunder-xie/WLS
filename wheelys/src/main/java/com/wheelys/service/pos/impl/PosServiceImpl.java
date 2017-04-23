package com.wheelys.service.pos.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wheelys.Config;
import com.wheelys.api.vo.ResultCode;
import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.BeanUtil;
import com.wheelys.util.DateUtil;
import com.wheelys.util.JsonUtils;
import com.wheelys.constant.CafeOrderConstant;
import com.wheelys.constant.PayConstant;
import com.wheelys.helper.OrderContainer;
import com.wheelys.model.CafeKey;
import com.wheelys.model.CafeProduct;
import com.wheelys.model.CafeShop;
import com.wheelys.model.ShopSeller;
import com.wheelys.model.order.MemberAddress;
import com.wheelys.model.order.WheelysOrder;
import com.wheelys.model.order.WheelysOrderDetail;
import com.wheelys.service.cafe.CafeKeyService;
import com.wheelys.service.cafe.CafeProductService;
import com.wheelys.service.cafe.CafeShopService;
import com.wheelys.service.pos.PosService;
import com.wheelys.web.action.openapi.vo.OpenApiMemberAddressVo;
import com.wheelys.web.action.openapi.vo.OpenApiOrderDetailVo;
import com.wheelys.web.action.openapi.vo.OpenApiOrderVo;
import com.wheelys.web.action.openapi.vo.PosOrderDetailVo;
import com.wheelys.web.action.openapi.vo.PosOrderVo;

@Service("posService")
public class PosServiceImpl extends BaseServiceImpl implements PosService {

	@Autowired@Qualifier("config")
	private Config config;
	@Autowired@Qualifier("cafeProductService")
	private CafeProductService cafeProductService;
	@Autowired@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;
	@Autowired@Qualifier("cafeKeyService")
	private CafeKeyService cafeKeyService;
	private AtomicInteger counter = new AtomicInteger();
	
	@Override
	public ResultCode<ShopSeller> shopUserLogin(Long shopid,String loginname, String pass) {
		ShopSeller seller = getShopSeller(shopid, loginname);
		if(seller == null){
			return ResultCode.getFailure("用户名或密码错误！");
		}
		if(StringUtils.equalsIgnoreCase(seller.getUserpwd(), pass)){
			return ResultCode.getSuccessReturn(seller);
		}
		return ResultCode.getFailure("用户名或密码错误！");
	}	

	private ShopSeller getShopSeller(Long shopid, String loginname) {
		DetachedCriteria query = DetachedCriteria.forClass(ShopSeller.class);
		query.add(Restrictions.eq("shopid", shopid));
		query.add(Restrictions.eq("loginname", loginname));
		List<ShopSeller> resultList = baseDao.findByCriteria(query);
		return resultList.isEmpty() ? null : resultList.get(0) ;
	}

	@Override
	public ResultCode<OrderContainer> addPosOrder(Long shopid, String citycode, List<PosOrderDetailVo> detailVoList,
			String clientIp) {	
		if(detailVoList == null || detailVoList.isEmpty()){
			return ResultCode.getFailure("购物车为空！");
		}
		List<WheelysOrderDetail> detailList = new ArrayList<WheelysOrderDetail>();
		int totalfee = 0;
		int payfee = 0;
		int quantity = 0;
		for (PosOrderDetailVo detailVo : detailVoList) {
			CafeProduct product = cafeProductService.getProduct(detailVo.getProductid());
			if(product == null)return ResultCode.getFailure("product为空！"+detailVo.getProductid());
			Map<String,String> otherinfoMap = JsonUtils.readJsonToMap(detailVo.getOtherinfo());
			String bean = otherinfoMap.get("bean");
			int price = product.getPrice();
			if(StringUtils.equals(bean, "y")){
				price += 3;
			}
			WheelysOrderDetail detail = new WheelysOrderDetail(product,detailVo.getQuantity(),detailVo.getOtherinfo(),price);
			detailList.add(detail);
			if(detail.getTotalfee() <= 0 || detail.getQuantity() <= 0){
				return ResultCode.getFailure("detail.getTotalfee():"+detail.getTotalfee()+",detail.getQuantity():"+detail.getQuantity());
			}
			totalfee += detail.getTotalfee();
			payfee += detail.getPaidfee();
			quantity += detail.getQuantity();
		}
		CafeShop cafeShop = cafeShopService.getShop(shopid);
		WheelysOrder order = new WheelysOrder(0L, "PAD商户", null, shopid, citycode);
		Timestamp validtime = DateUtil.addMinute(order.getCreatetime(), 60);
		order.setTradeno(getTicketTradeNo());
		order.setOrdertitle(cafeShop.getEsname());
		order.setPayfee(payfee);
		order.setTotalfee(totalfee);
		order.setQuantity(quantity);
		order.setValidtime(validtime);
		//this.orderDiscount(order, detailList);
		CafeKey takekey = cafeKeyService.getCafeKeyByShopid(shopid);
		order.setKeyid(takekey.getId());
		this.baseDao.saveObject(order);
		this.saveOrderDetail(order,detailList);
		OrderContainer container = new OrderContainer();
		container.setOrder(order);
		container.setDetailList(detailList);
		return ResultCode.getSuccessReturn(container);
	}
	
	private String getTicketTradeNo() {
		String s = "P" + DateUtil.format(new Date(), "yyMMddHHmm");
		long num = counter.incrementAndGet()+getIndex()%100000;
		s += StringUtils.leftPad("" + num, 5, '0'); // 订单号
		return s;
	}
	
	private int getIndex(){
		int result = new Random().nextInt(1000*5);
		return result+1; 
	}
	
	private void saveOrderDetail(WheelysOrder order, List<WheelysOrderDetail> detailList){
		for (WheelysOrderDetail detail : detailList) {
			detail.setOrderid(order.getId());
			this.baseDao.saveObject(detail);
		}
	}

	private List<WheelysOrderDetail> getOrderDetail(Long orderid) {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysOrderDetail.class);
		query.add(Restrictions.eq("orderid", orderid));
		List<WheelysOrderDetail> resultList = baseDao.findByCriteria(query);
		return resultList;
	}
	
	@Override
	public ResultCode changeOrderStatus(Long shopid, String status, String tradenolist) {
		status = status.replace("step3", "finish");
		List<WheelysOrder> orderList = getChangeOrderList(shopid, tradenolist);
		for (WheelysOrder order : orderList) {
			if(StringUtils.equals("step1", status) && StringUtils.equals(CafeOrderConstant.STATUS_PAID , order.getStatus())){
				order.setStatus(status);
				order.setUpdatetime(DateUtil.getMillTimestamp());
			}else if(StringUtils.equals("step2", status) && StringUtils.equals("step1", order.getStatus())){
				order.setStatus(status);
				order.setUpdatetime(DateUtil.getMillTimestamp());
			}else if(StringUtils.equals("finish", status) && StringUtils.equals("step2", order.getStatus())){
				order.setStatus(status);
				order.setUpdatetime(DateUtil.getMillTimestamp());
			}
		}
		List<String> list = BeanUtil.getBeanPropertyList(orderList, "tradeno", true);
		this.baseDao.saveObjectList(orderList);
		return ResultCode.getSuccessReturn(list);
	}

	private List<WheelysOrder> getChangeOrderList(Long shopid, String tardenoList) {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysOrder.class);
		if(StringUtils.isNotBlank(tardenoList)){
			String [] tardenoArr = StringUtils.split(tardenoList, ",");
			query.add(Restrictions.in("tradeno", Arrays.asList(tardenoArr)));
		}
		query.add(Restrictions.eq("shopid", shopid));
		query.add(Restrictions.eq("paystatus", CafeOrderConstant.STATUS_PAID));
		query.add(Restrictions.ge("paidtime",DateUtil.getBeginningTimeOfDay(DateUtil.addDay(DateUtil.getMillTimestamp(), -7))));
		query.addOrder(Order.asc("createtime"));
		List<WheelysOrder> resultList = baseDao.findByCriteria(query, 0, 200);
		return resultList;
	}

	@Override
	public ResultCode posOrderPay(Long shopid, String tradeno, String payseqno) {
		WheelysOrder wheelysOrder = getOrderByShopid(tradeno, shopid);
		wheelysOrder.setPaystatus(CafeOrderConstant.STATUS_PAID);
		wheelysOrder.setStatus(CafeOrderConstant.STATUS_PAID);
		wheelysOrder.setGatewayCode(PayConstant.GATEWAYCODE_POS);
		wheelysOrder.setMerchantCode("shopid:"+shopid);
		wheelysOrder.setNetpaid(wheelysOrder.getPayfee());
		wheelysOrder.setPaymethod(PayConstant.PAYMETHOD_POSPAY);
		wheelysOrder.setPayseqno(payseqno);
		wheelysOrder.setPaidtime(DateUtil.getMillTimestamp());
		wheelysOrder.setUpdatetime(wheelysOrder.getPaidtime());
		this.baseDao.saveObject(wheelysOrder);
		return ResultCode.SUCCESS;
	}

	private WheelysOrder getOrderByShopid(String tradeno,Long shopid) {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysOrder.class);
		query.add(Restrictions.eq("tradeno", tradeno));
		query.add(Restrictions.eq("shopid", shopid));
		List<WheelysOrder> resultList = baseDao.findByCriteria(query);
		return resultList.isEmpty() ? null : resultList.get(0);
	}

	@Override
	public ResultCode changeShopStatus(Long shopid, String status) {
		String booking = StringUtils.equals("0", status) ? "close" : "open";
		CafeShop shop = this.baseDao.getObject(CafeShop.class, shopid);
		shop.setBooking(booking);
		this.baseDao.saveObject(shop);
		return ResultCode.SUCCESS;
	}

	@Override
	public List<PosOrderVo> getPosOrderList(Long shopid, String status, Timestamp fromtime) {
		List<WheelysOrder> orderList = getOrderListByShopid(shopid,status,fromtime);
		List<PosOrderVo> orderVoList = new ArrayList<PosOrderVo>();
		for (WheelysOrder order : orderList) {
			PosOrderVo orderVo = new PosOrderVo();
			OpenApiOrderVo vo = new OpenApiOrderVo(order);
			orderVo.setOrder(vo);
			List<WheelysOrderDetail> detailList = getOrderDetail(order.getId());
			List<OpenApiOrderDetailVo> detailVoList = new ArrayList<OpenApiOrderDetailVo>();
			for (WheelysOrderDetail detail : detailList) {
				OpenApiOrderDetailVo detailVo = new OpenApiOrderDetailVo(detail,config.getString("picPath"));
				detailVoList.add(detailVo);
			}
			orderVo.setDetailList(detailVoList);
			if(order.getAddressid() != null){
				MemberAddress memberAddress = this.baseDao.getObject(MemberAddress.class, order.getAddressid());
				if(memberAddress != null){
					OpenApiMemberAddressVo memberAddressVo = new OpenApiMemberAddressVo(memberAddress);
					orderVo.setAddress(memberAddressVo);
				}
			}
			orderVoList.add(orderVo);
		}
		return orderVoList;
	}
	
	private List<WheelysOrder> getOrderListByShopid(Long shopid, String status, Timestamp fromtime) {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysOrder.class);
		query.add(Restrictions.eq("shopid", shopid));
		query.add(Restrictions.eq("paystatus", CafeOrderConstant.STATUS_PAID));
		query.add(Restrictions.ne("status", CafeOrderConstant.STATUS_FINISH));
		if(fromtime != null){
			query.add(Restrictions.gt("paidtime",fromtime));
		}else{
			query.add(Restrictions.ge("paidtime",DateUtil.getBeginningTimeOfDay(DateUtil.addDay(DateUtil.getMillTimestamp(), -1))));
		}
		if(StringUtils.isNotBlank(status)){
			query.add(Restrictions.eq("status", status));
		}
		query.addOrder(Order.asc("paidtime"));
		List<WheelysOrder> resultList = baseDao.findByCriteria(query, 0, 120);
		return resultList;
	}

	@Override
	public List<PosOrderVo> getHistortyOrderList(Long shopid, Timestamp fromtime, Timestamp totime, String tradeno,
			Integer pageno) {
		List<WheelysOrder> orderList = getHistortyWheelysOrderList(shopid,fromtime,totime,tradeno,pageno);
		List<PosOrderVo> orderVoList = new ArrayList<PosOrderVo>();
		for (WheelysOrder order : orderList) {
			PosOrderVo orderVo = new PosOrderVo();
			OpenApiOrderVo vo = new OpenApiOrderVo(order);
			orderVo.setOrder(vo);
			List<WheelysOrderDetail> detailList = getOrderDetail(order.getId());
			List<OpenApiOrderDetailVo> detailVoList = new ArrayList<OpenApiOrderDetailVo>();
			for (WheelysOrderDetail detail : detailList) {
				OpenApiOrderDetailVo detailVo = new OpenApiOrderDetailVo(detail,config.getString("picPath"));
				detailVoList.add(detailVo);
			}
			if(order.getAddressid() != null){
				MemberAddress memberAddress = this.baseDao.getObject(MemberAddress.class, order.getAddressid());
				if(memberAddress != null){
					OpenApiMemberAddressVo memberAddressVo = new OpenApiMemberAddressVo(memberAddress);
					orderVo.setAddress(memberAddressVo);
				}
			}
			orderVo.setDetailList(detailVoList);
			orderVoList.add(orderVo);
		}
		return orderVoList;
	}

	private List<WheelysOrder> getHistortyWheelysOrderList(Long shopid, Timestamp fromtime, Timestamp totime,
			String tradeno,Integer pageno) {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysOrder.class);
		query.add(Restrictions.eq("shopid", shopid));
		List<String> statusList = Arrays.asList(CafeOrderConstant.STATUS_FINISH, CafeOrderConstant.REFUNDSTATUS_PENDING_AUDIT, 
				CafeOrderConstant.REFUNDSTATUS_ALREADY_PASSED,CafeOrderConstant.REFUNDSTATUS_NO_PASSED,
				CafeOrderConstant.REFUNDSTATUS_FINISH,  CafeOrderConstant.REFUNDSTATUS_CANCEL);
		query.add(Restrictions.in("status", statusList));
		query.addOrder(Order.desc("updatetime"));
		if(StringUtils.isNotBlank(tradeno)){
			query.add(Restrictions.like("tradeno", "%"+tradeno));
		}
		if(fromtime != null && totime != null){
			query.add(Restrictions.ge("paidtime",fromtime));
			query.add(Restrictions.le("paidtime",totime));
		}
		List<WheelysOrder> resultList = null;
		if(pageno != null){
			int from = pageno * 20;
			int maxnum = 20;
			resultList = baseDao.findByCriteria(query, from, maxnum);
		}else{
			resultList = baseDao.findByCriteria(query, 0, 120);
		}
		return resultList;
	}
	
}

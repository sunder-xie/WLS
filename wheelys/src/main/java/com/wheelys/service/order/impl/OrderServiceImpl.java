package com.wheelys.service.order.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.beans.support.PropertyComparator;
import org.springframework.stereotype.Service;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.BeanUtil;
import com.wheelys.util.DateUtil;
import com.wheelys.util.JsonUtils;
import com.wheelys.constant.CafeOrderConstant;
import com.wheelys.constant.ElecCardConstant;
import com.wheelys.helper.ElecCardUtil;
import com.wheelys.helper.OrderContainer;
import com.wheelys.model.CafeKey;
import com.wheelys.model.CafeProduct;
import com.wheelys.model.CafeShop;
import com.wheelys.model.CartProduct;
import com.wheelys.model.discount.DiscountActivity;
import com.wheelys.model.order.WheelysOrder;
import com.wheelys.model.order.WheelysOrderDetail;
import com.wheelys.model.pay.ElecCard;
import com.wheelys.model.pay.ElecCardBatch;
import com.wheelys.service.PaymentService;
import com.wheelys.service.cafe.CafeKeyService;
import com.wheelys.service.cafe.CafeProductService;
import com.wheelys.service.cafe.CafeShopService;
import com.wheelys.service.order.DiscountService;
import com.wheelys.service.order.OrderService;
import com.wheelys.service.pay.ElecCardService;
import com.wheelys.web.action.wap.vo.ElecCardVo;
import com.wheelys.web.action.wap.vo.WheelysOrderVo;

@Service("orderService")
public class OrderServiceImpl extends BaseServiceImpl implements OrderService {
	
	@Autowired@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;
	@Autowired@Qualifier("cafeProductService")
	private CafeProductService cafeProductService;
	@Autowired@Qualifier("cafeKeyService")
	private CafeKeyService cafeKeyService;
	@Autowired@Qualifier("discountService")
	private DiscountService discountService;
	@Autowired@Qualifier("elecCardService")
	private ElecCardService elecCardService;
	@Autowired@Qualifier("paymentService")
	private PaymentService paymentService;
	private AtomicInteger counter = new AtomicInteger();

	@Override
	public ResultCode<OrderContainer> addAppOrder(List<CartProduct> cartProductVoList,Long memberid, String mobile,
			String membername, String ukey, Long shopid, Long cardid, String citycode, boolean isSave) {
		ResultCode<OrderContainer> result = this.addOrder(cartProductVoList, memberid, mobile, membername, ukey, shopid, cardid, citycode, isSave);
		if(result.isSuccess()){
			return result;
		}
		return ResultCode.getFailure(result.getMsg());
	}
	
	@Override
	public ResultCode<OrderContainer> addOrder(List<CartProduct> cartProductVoList,Long memberid, String mobile,
			String membername, String ukey, Long shopid, Long cardid, String citycode, boolean isSave) {	
		if(cartProductVoList == null || cartProductVoList.isEmpty()){
			return ResultCode.getFailure("购物车为空！");
		}
		int totalfee = 0;
		int payfee = 0;
		int quantity = 0;
		List<WheelysOrderDetail> detailList = new ArrayList<WheelysOrderDetail>();
		for (CartProduct cartProductVo : cartProductVoList) {
			WheelysOrderDetail detail = new WheelysOrderDetail(cartProductVo.getProduct(),cartProductVo.getQuantity(),cartProductVo.getOtherinfo(),cartProductVo.getPrice());
			detailList.add(detail);
			if(detail.getTotalfee() <= 0 || detail.getQuantity() <= 0){
				return ResultCode.getFailure("detail.getTotalfee():"+detail.getTotalfee()+",detail.getQuantity():"+detail.getQuantity());
			}
			totalfee += detail.getTotalfee();
			payfee += detail.getPaidfee();
			quantity += detail.getQuantity();
		}
		CafeShop cafeShop = cafeShopService.getShop(shopid);
		WheelysOrder order = new WheelysOrder(memberid, membername, ukey, shopid, citycode);
		Timestamp validtime = DateUtil.addMinute(order.getCreatetime(), 60);
		order.setTradeno(getTicketTradeNo());
		order.setOrdertitle(cafeShop.getShopname());
		order.setPayfee(payfee);
		order.setTotalfee(totalfee);
		order.setQuantity(quantity);
		order.setMobile(mobile);
		order.setValidtime(validtime);
		if(cardid == null){
			discountService.setOrderDiscount(order, detailList, cartProductVoList);
		}else{
			this.orderCardDiscount(order, detailList, cardid, true, cartProductVoList);
		}
		OrderContainer container = new OrderContainer();
		if(isSave){
			this.setKey(order);
			this.baseDao.saveObject(order);
			this.saveOrderDetail(order,detailList);
			if(order.getPayfee() == 0){
				ResultCode<WheelysOrder> result = paymentService.payZeroOrder(order);
				if(!result.isSuccess()){
					return ResultCode.getFailure(result.getMsg());
				}
			}
		}else{
			if(order.getSdid() != null && order.getSdid() > 0){
				DiscountActivity discount = discountService.getDiscount(order.getSdid());
				container.setDiscount(discount);
			}else{
				ElecCardVo cardVo = this.useDefaultCard(order, detailList, cartProductVoList);
				container.setCard(cardVo);
			}
		}
		container.setOrder(order);
		Collections.sort(detailList, new PropertyComparator("price", false, false));
		container.setDetailList(detailList);
		return ResultCode.getSuccessReturn(container);
	}
	
	private void orderCardDiscount(WheelysOrder order, List<WheelysOrderDetail> detailList, Long cardid, boolean isLock, List<CartProduct> cartProductVoList) {
		Collections.sort(detailList, new PropertyComparator("price", false, false));
		ElecCard elecCard = elecCardService.getElecCard(order.getMemberid(), cardid, ElecCardConstant.STATUS_SOLD);
		if(elecCard == null) return;
		ElecCardBatch batch = elecCardService.getElecCardBatch(elecCard.getEbatchid(), null);
		if(StringUtils.equals(batch.getCardtype(), ElecCardConstant.CARDTYPE_EXCHANGE)){
			Collections.sort(detailList, new PropertyComparator("price", false, false));
			WheelysOrderDetail detail = null;
			for (WheelysOrderDetail orderDetail : detailList) {
				Integer cardtotalprice = ElecCardUtil.validPrice(order.getShopid(), batch.getValidshopid(), batch.getValidproductid(), cartProductVoList);
				if(ElecCardUtil.validCard(order.getShopid(), batch.getValidshopid(), batch.getValidproductid(), orderDetail.getProductid(), 
				elecCard.getBegintime(), elecCard.getEndtime(),batch.getCardtype(), cardtotalprice, batch.getMinprice())){
					detail = orderDetail;
					break;
				}
			}
			if(detail == null) return;
			detail.setDiscount(detail.getPrice()*100);
			detail.setPaidfee(detail.getTotalfee()-detail.getDiscount());	
			order.setDiscount(detail.getDiscount());
			order.setPayfee(order.getTotalfee()-order.getDiscount());
			order.setCardid(cardid);
			order.setCardpaid(detail.getPrice()*100);
			order.setBatchid(batch.getId());
			if(StringUtils.isNotBlank(batch.getAnnexation())){
				List<Long> goodidList = BeanUtil.getIdList(batch.getAnnexation(), ",");
				for (Long goodis : goodidList) {
					CafeProduct product = cafeProductService.getCacheProduct(goodis);
					WheelysOrderDetail orderDetail = new WheelysOrderDetail(product,1,"{}",product.getPrice());
					orderDetail.setDiscount(product.getPrice());
					orderDetail.setPaidfee(0);
					detailList.add(orderDetail);
				}
			}
		}else if(StringUtils.equals(batch.getCardtype(), ElecCardConstant.CARDTYPE_DEDUCTION)){
			Collections.sort(detailList, new PropertyComparator("price", false, false));
			if(StringUtils.equals(batch.getAmountmark(), ElecCardConstant.MARK_FIXAMOUNT)){
				for (WheelysOrderDetail orderDetail : detailList) {
					Integer cardtotalprice = ElecCardUtil.validPrice(order.getShopid(), batch.getValidshopid(), batch.getValidproductid(), cartProductVoList);
					if(ElecCardUtil.validCard(order.getShopid(), batch.getValidshopid(), batch.getValidproductid(), orderDetail.getProductid(), 
					elecCard.getBegintime(), elecCard.getEndtime(),batch.getCardtype(), cardtotalprice, batch.getMinprice())){
						Integer discountamount = (orderDetail.getPrice() - batch.getAmount())*100;
						if(orderDetail.getPrice() < batch.getAmount()){
							discountamount = 0;
						}
						orderDetail.setDiscount(discountamount);
						orderDetail.setPaidfee(orderDetail.getTotalfee()-orderDetail.getDiscount());	
						order.setDiscount(orderDetail.getDiscount());
						break;
					}
				}
			}else{
				Integer discountamount = batch.getAmount()*100;
				Integer totaldiscount = 0;
				for (int i = 0; i < detailList.size(); i++) {
					WheelysOrderDetail detail = detailList.get(i);
					if(i == detailList.size()-1){
						detail.setDiscount(discountamount-totaldiscount);
					}else{
						double rate = detail.getTotalfee() * 1.0 / order.getTotalfee();
						int damount = (int) (discountamount * rate);
						detail.setDiscount(damount);
						totaldiscount += damount;
					}
					detail.setPaidfee(detail.getTotalfee() - detail.getDiscount());
				}
				order.setDiscount(batch.getAmount()*100);
			}
			order.setPayfee(order.getTotalfee()-order.getDiscount());
			order.setCardid(cardid);
			order.setCardpaid(order.getDiscount());
			order.setBatchid(batch.getId());
		}
		if(isLock){
			elecCardService.lockElecCard(order.getMemberid(), cardid);
		}
	}

	private void setKey(WheelysOrder order){
		try {
			CafeKey cafeKey = cafeKeyService.getCafeKeyByShopid(order.getShopid());
			order.setKeyid(cafeKey.getId());
			order.setTakekey(cafeKey.getName());
		} catch (Exception e) {
			dbLogger.warn("setKey Exception：,memberid:"+order.getMemberid()+",shopid:"+order.getShopid());
		}
	}
	
	private void saveOrderDetail(WheelysOrder order, List<WheelysOrderDetail> detailList){
		String description = "";
		for (WheelysOrderDetail detail : detailList) {
			detail.setOrderid(order.getId());
			this.baseDao.saveObject(detail);
			description += "," + detail.getProductname() + "X" + detail.getQuantity();
		}
		order.setDescription(StringUtils.substring(description, 1));
	}
	
	private String getTicketTradeNo() {
		String s = "A" + DateUtil.format(new Date(), "yyMMddHHmm");
		long num = counter.incrementAndGet()+getIndex()%100000;
		s += StringUtils.leftPad("" + num, 5, '0'); // 订单号
		return s;
	}
	
	private int getIndex(){
		int result = new Random().nextInt(1000*5);
		return result+1; 
	}

	@Override
	public ResultCode<WheelysOrderVo> getWheelysOrderVo(String tradeno, Long memberid) {
		WheelysOrder order = this.getOrder(tradeno, memberid);
		if(order == null){
			return ResultCode.getFailure("订单不存在");
		}
		List<WheelysOrderDetail> detailList = getOrderDetail(order.getId());
		WheelysOrderVo orderVo = new WheelysOrderVo(order);
		orderVo.setDetailList(detailList);
		CafeShop shop = cafeShopService.getShop(order.getShopid());
		orderVo.setShop(shop);
		return ResultCode.getSuccessReturn(orderVo);
	}
	
	private WheelysOrder getOrder(String tradeno,Long memberid) {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysOrder.class);
		query.add(Restrictions.eq("tradeno", tradeno));
		if(memberid != null)
		query.add(Restrictions.eq("memberid", memberid));
		List<WheelysOrder> resultList = baseDao.findByCriteria(query);
		return resultList.isEmpty() ? null : resultList.get(0);
	}
	
	@Override
	public WheelysOrder getOrderByTradeNo(String tradeNo) {
		WheelysOrder wheelysOrder = baseDao.getObjectByUkey(WheelysOrder.class, "tradeno", tradeNo);
		return wheelysOrder;
	}
	
	private List<WheelysOrderDetail> getOrderDetail(Long orderid) {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysOrderDetail.class);
		query.add(Restrictions.eq("orderid", orderid));
		List<WheelysOrderDetail> resultList = baseDao.findByCriteria(query);
		return resultList;
	}

	@Override
	public List<WheelysOrderVo> getOrderList(String status, Long memberid, int pageNo, int maxnum) {
		List<WheelysOrder> resultList = getOrderList(memberid, pageNo, maxnum);
		List<WheelysOrderVo> voList = new ArrayList<WheelysOrderVo>();
		if(resultList.isEmpty()){
			return voList;
		}
		List<Long> idList = BeanUtil.getBeanPropertyList(resultList, "id", true);
		List<WheelysOrderDetail> allDetailList = getOrderDetailByIdList(idList);
		Map map = BeanUtil.groupBeanList(allDetailList, "orderid");
		for (WheelysOrder order : resultList) {
			WheelysOrderVo vo = new WheelysOrderVo(order);
			CafeShop shop = cafeShopService.getShop(order.getShopid());
			vo.setShop(shop);
			List<WheelysOrderDetail> detailList = (List<WheelysOrderDetail>) map.get(order.getId());
			vo.setDetailList(detailList);
			voList.add(vo);
		}
		return voList;
	}

	private List<WheelysOrder> getOrderList(Long memberid, int pageNo, int maxnum) {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysOrder.class);
		query.add(Restrictions.eq("paystatus", CafeOrderConstant.STATUS_PAID));
		query.add(Restrictions.eq("memberid", memberid));
		query.addOrder(Order.desc("createtime"));
		List<WheelysOrder> resultList = baseDao.findByCriteria(query, pageNo * maxnum, maxnum);
		return resultList;
	}
	

	private List<WheelysOrderDetail> getOrderDetailByIdList(List<Long> idList) {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysOrderDetail.class);
		query.add(Restrictions.in("orderid", idList));
		List<WheelysOrderDetail> resultList = baseDao.findByCriteria(query);
		return resultList;
	}

	@Override
	public ResultCode saveOrderCategory(String tradeno, Long memberid, Long addressid,String category, Integer reservedtime) {
		try {
			WheelysOrder wheelysOrder = this.getOrder(tradeno, memberid);
			if(wheelysOrder != null){
				if(addressid != null && StringUtils.equals(category, CafeOrderConstant.CATEGORY_TAKEAWAY)){
					wheelysOrder.setAddressid(addressid);
					wheelysOrder.setCategory(CafeOrderConstant.CATEGORY_TAKEAWAY);
				}else if(StringUtils.equals(category, CafeOrderConstant.CATEGORY_RESERVED)){
					wheelysOrder.setCategory(CafeOrderConstant.CATEGORY_RESERVED);
					int minutes = reservedtime==null ? 0 : reservedtime;
					if(minutes > 0){
						wheelysOrder.setTaketime(DateUtil.addMinute(DateUtil.getMillTimestamp(), minutes));
						Map map = JsonUtils.readJsonToMap(wheelysOrder.getOtherinfo());
						map.put("reservedtime", minutes);
						wheelysOrder.setOtherinfo(JsonUtils.writeMapToJson(map));
					}else{
						wheelysOrder.setCategory(CafeOrderConstant.CATEGORY_TAKE);
					}
				}else if(StringUtils.equals(category, CafeOrderConstant.CATEGORY_TAKE)){
					wheelysOrder.setCategory(CafeOrderConstant.CATEGORY_TAKE);
				}
				this.baseDao.saveObject(wheelysOrder);
			}
		} catch (Exception e) {
			dbLogger.warn("set Category Takeawy Exception, orderid:" + tradeno + ",addressid:" + addressid);
		}
		return ResultCode.SUCCESS;
	}

	@Override
	public ElecCardVo useDefaultCard(WheelysOrder order, List<WheelysOrderDetail> detailList, List<CartProduct> cartProductVoList) {
		if(order.getSdid() != null) return null;
		List<ElecCardVo> cardList = elecCardService.getElecCardVoList(order.getMemberid(),ElecCardConstant.CARDTYPE_EXCHANGE, ElecCardConstant.STATUS_SOLD, 0, 150);
		ElecCardVo elecCardVo = ElecCardUtil.getCardList(cardList, order.getShopid(), cartProductVoList);
		if(elecCardVo == null){
			cardList = elecCardService.getElecCardVoList(order.getMemberid(),ElecCardConstant.CARDTYPE_DEDUCTION, ElecCardConstant.STATUS_SOLD, 0, 150);
			if(cardList.size() > 0){
				elecCardVo = ElecCardUtil.getCardList(cardList, order.getShopid(), cartProductVoList);
			}
		}
		if(elecCardVo != null){
			this.orderCardDiscount(order, detailList, elecCardVo.getId(), false, cartProductVoList);
		}
		return elecCardVo;
	}

	@Override
	public void saveOntherInfo(String tradeno, Long memberid, String otherinfo) {
		WheelysOrder order = this.getOrder(tradeno, memberid);
		order.setOtherinfo(otherinfo);
		this.baseDao.saveObject(order);
	}

	@Override
	public WheelysOrder getLastPaidOrder(Long memberid) {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysOrder.class);
		query.add(Restrictions.eq("paystatus", CafeOrderConstant.STATUS_PAID));
		query.add(Restrictions.eq("memberid", memberid));
		query.addOrder(Order.desc("paidtime"));
		List<WheelysOrder> resultList = baseDao.findByCriteria(query, 0, 1);
		return resultList.isEmpty() ? null : resultList.get(0);
	}

}

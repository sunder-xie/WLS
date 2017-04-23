package com.wheelys.service.order.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.support.concurrent.AtomicCounter;
import com.wheelys.support.redis.GwShardedJedisPool;
import com.wheelys.untrans.CacheService;
import com.wheelys.untrans.impl.AtomicCounter4RedisSharded;
import com.wheelys.util.BeanUtil;
import com.wheelys.util.DateUtil;
import com.wheelys.constant.CafeOrderConstant;
import com.wheelys.constant.DiscountConstant;
import com.wheelys.helper.DiscountUtil;
import com.wheelys.helper.ElecCardUtil;
import com.wheelys.model.CafeProduct;
import com.wheelys.model.CartProduct;
import com.wheelys.model.discount.DiscountActivity;
import com.wheelys.model.discount.MemberJoinDiscount;
import com.wheelys.model.order.WheelysOrder;
import com.wheelys.model.order.WheelysOrderDetail;
import com.wheelys.service.cafe.CafeProductService;
import com.wheelys.service.order.DiscountService;
import com.wheelys.web.action.wap.vo.DiscountActivityVo;

@Service("discountService")
public class DiscountServiceImpl extends BaseServiceImpl implements DiscountService {
	
	@Autowired@Qualifier("shardedJedisPool")
	private GwShardedJedisPool shardedJedisPool;
	@Autowired@Qualifier("cafeProductService")
	private CafeProductService cafeProductService;
	@Autowired@Qualifier("cacheService")
	private CacheService cacheService;

	private Map<Long, AtomicCounter> dacounterMap = new ConcurrentHashMap<>();

	private List<DiscountActivity> getDiscountActivityListByShopid(Long shopid) {
		DetachedCriteria query = DetachedCriteria.forClass(DiscountActivity.class);
		query.add(Restrictions.eq("status", "Y"));
		query.add(Restrictions.gt("endtime", DateUtil.getMillTimestamp()));
		query.add(Restrictions.lt("begintime", DateUtil.getMillTimestamp()));
		query.addOrder(Order.desc("priority"));
		query.addOrder(Order.desc("createtime"));
		List<DiscountActivity> discountList = baseDao.findByCriteria(query);
		return discountList.stream().filter(discount -> BeanUtil.getIdList(discount.getShopids(), ",").contains(shopid)).collect(Collectors.toList());
	}
	
	@Override
	public void setOrderDiscount(WheelysOrder order, List<WheelysOrderDetail> detailList, List<CartProduct> cartProductList) {
		DiscountActivityVo discountActivity = this.getDefaultDiscount(order, detailList, cartProductList);
		if (discountActivity != null) {
			if (StringUtils.equals(DiscountConstant.FIXAMOUNT, discountActivity.getType())) {
				this.fixAmountDiscount(order, detailList, discountActivity);
			} else if (StringUtils.equals(DiscountConstant.ONEBUYONE, discountActivity.getType())) {
				this.onebuyoneDiscount(order, detailList, discountActivity);
			} else if (StringUtils.equals(DiscountConstant.SECONDCUP, discountActivity.getType())) {
				this.secondCupDiscount(order, detailList, discountActivity);
			} else if (StringUtils.equals(DiscountConstant.ZHEKOU, discountActivity.getType())) {
				this.zhekouDiscount(order, detailList, discountActivity);
			} else if (StringUtils.equals(DiscountConstant.FIRSTCUP, discountActivity.getType())) {
				this.firstCupDiscount(order, detailList, discountActivity);
			} else if (StringUtils.equals(DiscountConstant.MANJIAN, discountActivity.getType())) {
				this.manjianDiscount(order, detailList, discountActivity);
			}
			order.setSdid(discountActivity.getId());
			order.setDisreason(discountActivity.getName());
			dbLogger.warn("参加优惠价活动："+discountActivity.getId()+":"+discountActivity.getName()+",memberid:"+order.getMemberid()+",shopid:"+order.getShopid());
		}
	}

	private DiscountActivityVo getDefaultDiscount(WheelysOrder order, List<WheelysOrderDetail> detailList, List<CartProduct> cartProductList){
		List<DiscountActivity> discountList = getDiscountActivityListByShopid(order.getShopid());
		if(discountList == null || discountList.isEmpty())return null;
		WheelysOrder discountOrder = new WheelysOrder();
		BeanUtil.copyProperties(discountOrder, order);
		List<WheelysOrderDetail> discountDetailList = new ArrayList<WheelysOrderDetail>();
		for (WheelysOrderDetail detail : detailList) {
			WheelysOrderDetail discountDetail = new WheelysOrderDetail();
			BeanUtil.copyProperties(discountDetail, detail);
			discountDetailList.add(discountDetail);
		}
		Map<String,List<DiscountActivityVo>> usableMap = new HashMap<String, List<DiscountActivityVo>>();
		for (DiscountActivity discountActivity : discountList) {
			if(!DiscountUtil.validDiscount(discountActivity, cartProductList)){
				continue;
			}
			List<DiscountActivityVo> usableList = usableMap.get(discountActivity.getPriority());
			if(usableList == null) {
				usableList = new ArrayList<>();
				usableMap.put(discountActivity.getPriority(), usableList);
			}
			DiscountActivityVo discountActivityVo = new DiscountActivityVo(discountActivity);
			if(StringUtils.equals(DiscountConstant.FIXAMOUNT, discountActivityVo.getType())){
				this.fixAmountDiscount(discountOrder, discountDetailList, discountActivityVo);
			}else if(StringUtils.equals(DiscountConstant.ONEBUYONE, discountActivityVo.getType())){
				this.onebuyoneDiscount(discountOrder, discountDetailList, discountActivityVo);
			}else if(StringUtils.equals(DiscountConstant.SECONDCUP, discountActivityVo.getType())){
				this.secondCupDiscount(discountOrder, discountDetailList, discountActivityVo);
			}else if(StringUtils.equals(DiscountConstant.ZHEKOU, discountActivityVo.getType())){
				this.zhekouDiscount(discountOrder, discountDetailList, discountActivityVo);
			}else if(StringUtils.equals(DiscountConstant.FIRSTCUP, discountActivityVo.getType())){
				Boolean isJoin = isJoinDiscount(discountOrder.getMemberid(), discountActivityVo.getId());
				//用户是否已经参加过
				if(isJoin) continue;
				this.firstCupDiscount(discountOrder, discountDetailList, discountActivityVo);
			}else if(StringUtils.equals(DiscountConstant.MANJIAN, discountActivityVo.getType())){
				//订单金额和杯数是否符合满减条件
				if(!DiscountUtil.validDiscount(discountActivity, cartProductList)){
					continue;
				}
				this.manjianDiscount(discountOrder, discountDetailList, discountActivityVo);
			}
			discountActivityVo.setSortkey(discountOrder.getDiscount());
			usableList.add(discountActivityVo);
		}
		List<String> priorityList = Arrays.asList("3", "2", "1");
		for (String priority : priorityList) {
			List<DiscountActivityVo> usableList = usableMap.get(priority);
			if(usableList == null || usableList.isEmpty()) continue;
			Collections.sort(usableList, new PropertyComparator("sortkey", false, false));
			for (DiscountActivityVo discountActivityVo : usableList) {
				if(StringUtils.equals(DiscountConstant.FIRSTCUP, discountActivityVo.getType())){
					DiscountActivity discountActivity = baseDao.getObject(DiscountActivity.class, discountActivityVo.getId());
					if(discountActivity.getLimitmaxnum() == null) return discountActivityVo;
					if(discountActivity.getLimitmaxnum() <= discountActivity.getAllowaddnum()) continue;
					String cacheKey = discountOrder.getMemberid() + "usedis1" + discountActivityVo.getId();
					Object obj = cacheService.get(CacheService.REGION_TENMIN, cacheKey);
					if(obj == null){
						//锁名额
						long lockcount = getDiscountCounter(discountActivityVo.getId()).incrementAndGet();
						dbLogger.warn("特价活动计算器 id：" + discountActivityVo.getId() + " memberid：" + discountOrder.getMemberid() + " lockcount：" + lockcount);
						if(discountActivity.getAllowaddnum() + lockcount > discountActivity.getLimitmaxnum()){
							continue;
						}
						//名额绑定用户
						saveMemberJoinDiscount(discountOrder.getMemberid(), discountActivityVo.getId());
						cacheService.set(CacheService.REGION_TENMIN, cacheKey, "1");
					}
					return discountActivityVo;
				}else{
					return discountActivityVo;
				}
			}
		}
		return null;
	}

	private void manjianDiscount(WheelysOrder order, List<WheelysOrderDetail> detailList, DiscountActivityVo discountActivity) {
		if (order.getQuantity().intValue() >= discountActivity.getLimitcup().intValue()
				&& order.getTotalfee().intValue() >= discountActivity.getDiscountamount().intValue()) {
			String validproductid = discountActivity.getValidproductid();
			List<Long> productidList = ElecCardUtil.getProductidList(validproductid);
			int discount = 0;
			List<WheelysOrderDetail> discountOrderDetail = new ArrayList<WheelysOrderDetail>();
			for (WheelysOrderDetail orderDetail : detailList) {
				CafeProduct product = cafeProductService.getCacheProduct(orderDetail.getProductid());
				if (product.getItemid().intValue() == 100) {
					continue;
				}
				if (StringUtils.isNotBlank(validproductid) && !productidList.contains(orderDetail.getProductid())) {
					continue;
				}
				discountOrderDetail.add(orderDetail);
				discount += orderDetail.getPaidfee();
			}
			int discountamount = discountActivity.getDiscountamount() * 100;
			if (discount > discountamount) {
				int totaldiscount = 0;
				for (int i = 0; i < discountOrderDetail.size(); i++) {
					WheelysOrderDetail detail = discountOrderDetail.get(i);
					if (i == discountOrderDetail.size() - 1) {
						detail.setDiscount(discountamount - totaldiscount);
					} else {
						double rate = detail.getTotalfee() * 1.0 / order.getTotalfee();
						int damount = (int) (discountamount * rate);
						detail.setDiscount(damount);
						totaldiscount += damount;
					}
					detail.setPaidfee(detail.getTotalfee() - detail.getDiscount());
				}
				order.setDiscount(discountActivity.getDiscountamount() * 100);
				order.setPayfee(order.getTotalfee() - order.getDiscount());
			}
		}
	}

	private void firstCupDiscount(WheelysOrder order, List<WheelysOrderDetail> detailList, DiscountActivityVo discountActivity) {
		String validproductid = discountActivity.getValidproductid();
		List<Long> productidList = ElecCardUtil.getProductidList(validproductid);
		Collections.sort(detailList, new PropertyComparator("price", false, false));
		WheelysOrderDetail detail = null;
		CafeProduct product = null;
		for (WheelysOrderDetail orderDetail : detailList) {
			product = cafeProductService.getCacheProduct(orderDetail.getProductid());
			if (StringUtils.isNotBlank(validproductid) && !productidList.contains(orderDetail.getProductid())) {
				continue;
			}
			if (product.getItemid().intValue() != 100) {
				detail = orderDetail;
				break;
			}
		}
		if (detail == null || product == null)
			return;
		int discount = product.getPrice() - discountActivity.getFixamount();
		detail.setDiscount(discount * 100);
		detail.setPaidfee(detail.getTotalfee() - detail.getDiscount());
		detail.setDiscountnum(1);
		order.setDiscount(detail.getDiscount());
		order.setDiscountnum(1);
		order.setPayfee(order.getTotalfee() - order.getDiscount());
	}

	private void secondCupDiscount(WheelysOrder order, List<WheelysOrderDetail> detailList, DiscountActivityVo discountActivity) {
		String validproductid = discountActivity.getValidproductid();
		List<Long> productidList = ElecCardUtil.getProductidList(validproductid);
		int discount = 0;
		Collections.sort(detailList, new PropertyComparator("price", false, false));
		Boolean flag = false;
		for (int i = 0; i < detailList.size(); i++) {
			WheelysOrderDetail detail = detailList.get(i);
			if (StringUtils.isNotBlank(validproductid) && !productidList.contains(detail.getProductid())) {
				continue;
			}
			CafeProduct product = cafeProductService.getCacheProduct(detail.getProductid());
			if (product.getItemid().intValue() != 100) {
				int discountnum = detail.getQuantity() / 2;
				if (detail.getQuantity() % 2 == 1) {
					if (flag) {
						discountnum += 1;
					}
					flag = flag ? false : true;
				}
				if (discountnum == 0)
					continue;
				int detaildiscount = detail.getPrice() * discountnum * 100
						- (detail.getPrice() * discountnum * discountActivity.getDiscountrate());
				detail.setDiscount(detaildiscount);
				detail.setDiscountnum(discountnum);
				detail.setPaidfee(detail.getTotalfee() - detail.getDiscount());
			}
			discount += detail.getDiscount();
		}
		order.setDiscount(discount);
		order.setPayfee(order.getTotalfee() - order.getDiscount());
	}

	private void zhekouDiscount(WheelysOrder order, List<WheelysOrderDetail> detailList, DiscountActivityVo discountActivity) {
		String validproductid = discountActivity.getValidproductid();
		List<Long> productidList = ElecCardUtil.getProductidList(validproductid);
		int discount = 0;
		for (WheelysOrderDetail detail : detailList) {
			if (StringUtils.isNotBlank(validproductid) && !productidList.contains(detail.getProductid())) {
				continue;
			}
			CafeProduct product = cafeProductService.getCacheProduct(detail.getProductid());
			if (product.getItemid().intValue() != 100) {
				detail.setDiscount(detail.getTotalfee() - detail.getTotalfee() / 100 * discountActivity.getDiscountrate());
				detail.setPaidfee(detail.getTotalfee() - detail.getDiscount());
			}
			discount += detail.getDiscount();
		}
		order.setDiscount(discount);
		order.setPayfee(order.getTotalfee() - order.getDiscount());
	}

	private void fixAmountDiscount(WheelysOrder order, List<WheelysOrderDetail> detailList, DiscountActivityVo discountActivity) {
		Integer fixamount = discountActivity.getFixamount();
		String validproductid = discountActivity.getValidproductid();
		List<Long> productidList = ElecCardUtil.getProductidList(validproductid);
		Collections.sort(detailList, new PropertyComparator("price", false, false));
		int discount = 0;
		for (WheelysOrderDetail detail : detailList) {
			if (StringUtils.isNotBlank(validproductid) && !productidList.contains(detail.getProductid())) {
				continue;
			}
			CafeProduct product = cafeProductService.getCacheProduct(detail.getProductid());
			if (product.getItemid().intValue() != 100) {
				int detailDiscount = product.getPrice() - fixamount;
				detail.setDiscount(detail.getQuantity() * detailDiscount * 100);
				detail.setPaidfee(detail.getTotalfee() - detail.getDiscount());
			}
			discount += detail.getDiscount();
		}
		order.setDiscount(discount);
		order.setPayfee(order.getTotalfee() - discount);
	}

	private void onebuyoneDiscount(WheelysOrder order, List<WheelysOrderDetail> detailList, DiscountActivityVo discountActivity) {
		String validproductid = discountActivity.getValidproductid();
		List<Long> productidList = ElecCardUtil.getProductidList(validproductid);
		int discountnum = 0;
		int quantity = 0;
		int discount = 0;
		int totalfee = 0;
		for (WheelysOrderDetail detail : detailList) {
			if (StringUtils.isBlank(validproductid) || productidList.contains(detail.getProductid())) {
				CafeProduct product = cafeProductService.getCacheProduct(detail.getProductid());
				if (product.getItemid().intValue() != 100) {
					detail.setDiscountnum(detail.getQuantity());
					detail.setQuantity(detail.getQuantity() * 2);
					detail.setDiscount(detail.getTotalfee());
					detail.setTotalfee(detail.getTotalfee() * 2);
					discountnum += detail.getDiscountnum();
				}
			}
			quantity += detail.getQuantity();
			discount += detail.getDiscount();
			totalfee += detail.getTotalfee();
		}
		order.setDiscountnum(discountnum);
		order.setQuantity(quantity);
		order.setDiscount(discount);
		order.setTotalfee(totalfee);
	}

	private Boolean isJoinDiscount(Long memberid, Long discountid) {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysOrder.class);
		query.add(Restrictions.eq("sdid", discountid));
		query.add(Restrictions.eq("memberid", memberid));
		List paystatusList = Arrays.asList(CafeOrderConstant.STATUS_PAID, CafeOrderConstant.STATUS_PAID_FAILURE, CafeOrderConstant.STATUS_PAID_RETURN);
		query.add(Restrictions.in("paystatus", paystatusList));
		query.add(Restrictions.ge("paidtime", DateUtil.getBeginningTimeOfDay(DateUtil.getMillTimestamp())));
		query.setProjection(Projections.rowCount());
		List<Long> resultList = baseDao.findByCriteria(query);
		return resultList.get(0) > 0;
	}

	@Override
	public void updateDiscount(Long id, String status, String shopids, Timestamp begintime, Timestamp endtime) {
		DiscountActivity discountActivity = this.baseDao.getObject(DiscountActivity.class, id);
		if (StringUtils.isNotBlank(status)) {
			discountActivity.setStatus(status);
		}
		if (StringUtils.isNotBlank(shopids)) {
			discountActivity.setShopids(shopids);
		}
		if (begintime != null) {
			discountActivity.setBegintime(begintime);
		}
		if (endtime != null) {
			discountActivity.setEndtime(endtime);
		}
		this.baseDao.saveObject(discountActivity);
	}

	@Override
	public DiscountActivity getDiscount(Long id) {
		return this.baseDao.getObject(DiscountActivity.class, id);
	}
	
	@Override
	public AtomicCounter getDiscountCounter(Long discountid){
		AtomicCounter counter = dacounterMap.get(discountid);
		if(counter == null){
			counter = new AtomicCounter4RedisSharded("da" + discountid, shardedJedisPool);
			dacounterMap.put(discountid, counter);
		}
		return counter;
	}
	
	private void saveMemberJoinDiscount(Long memberid, Long discountid){
		MemberJoinDiscount joinDiscount = new MemberJoinDiscount(memberid, discountid);
		baseDao.saveObject(joinDiscount);
		
	}
}

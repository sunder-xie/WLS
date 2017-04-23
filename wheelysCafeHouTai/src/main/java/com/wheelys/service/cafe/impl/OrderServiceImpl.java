package com.wheelys.service.cafe.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.BeanUtil;
import com.wheelys.util.DateUtil;
import com.wheelys.constant.OrderConstant;
import com.wheelys.model.CafeShop;
import com.wheelys.model.citymanage.CityManage;
import com.wheelys.model.discount.DiscountActivity;
import com.wheelys.model.pay.WheelysOrder;
import com.wheelys.model.pay.WheelysOrderDetail;
import com.wheelys.service.admin.CityManageService;
import com.wheelys.service.cafe.OrderService;
import com.wheelys.web.action.openapi.vo.WheelysOerderVo;

@Service("orderService")
public class OrderServiceImpl extends BaseServiceImpl implements OrderService {

	@Autowired
	@Qualifier("cityManageService")
	private CityManageService cityManageService;

	@Override
	public List<Long> getOrderedMemberids(Long shopid, Date day) {
		Timestamp beginTimestamp = DateUtil.getBeginTimestamp(day);
		Timestamp endTimestamp = DateUtil.getEndTimestamp(day);
		DetachedCriteria query = DetachedCriteria.forClass(WheelysOrder.class);
		query.setProjection(Projections.property("memberid"));
		query.add(Restrictions.eq("shopid", shopid));
		query.add(Restrictions.eq("paystatus", OrderConstant.PAYSTATUS_PAID));
		query.add(Restrictions.between("paidtime", beginTimestamp, endTimestamp));
		query.addOrder(Order.asc("paidtime"));
		List<Long> memberids = baseDao.findByCriteria(query);
		return memberids;
	}

	@Override
	public WheelysOrder getOrder(String tradeno) {
		return this.baseDao.getObjectByUkey(WheelysOrder.class, "tradeno", tradeno);
	}

	@Override
	public List<WheelysOerderVo> findOrderList(Integer pageNo, int rowsPerPage, String region, String cityCode,
			Long shopId, String status, String tradeno, Date createTimeBegin, Date createTimeEnd, String paymethod,
			String category, String elecCardBatchId) {
		List<WheelysOrder> orderList = getWheelysOrderList(pageNo, rowsPerPage, region, cityCode, shopId, status,
				tradeno, createTimeBegin, createTimeEnd, paymethod, category, elecCardBatchId);
		List<WheelysOerderVo> result = new ArrayList<WheelysOerderVo>();
		if (CollectionUtils.isNotEmpty(orderList)) {
			for (WheelysOrder wheelysOrder : orderList) {
				WheelysOerderVo vo = new WheelysOerderVo();
				BeanUtils.copyProperties(wheelysOrder, vo);
				vo.setTotalfee(wheelysOrder.getTotalfee());
				vo.setDiscount(wheelysOrder.getDiscount());
				vo.setShop(getCafeShop(wheelysOrder));
				vo.setProductName(getProductName(wheelysOrder).toString());
				vo.setActivityType(getDiscountType(wheelysOrder));
				result.add(vo);
			}
		}
		return result;
	}

	@Override
	public int findOrderListCount(String region, String cityCode, Long shopId, String status, String tradeno,
			Date createTimeBegin, Date createTimeEnd, String paymethod, String category, String elecCardBatchId) {
		DetachedCriteria query = getOrderListQuery(region, cityCode, shopId, status, tradeno, createTimeBegin,
				createTimeEnd, paymethod, category, elecCardBatchId);
		if (query == null) {
			return 0;
		}
		query.setProjection(Projections.rowCount());
		List<Long> result = baseDao.findByCriteria(query);
		return result.get(0).intValue();
	}

	private CafeShop getCafeShop(WheelysOrder wheelysOrder) {
		CafeShop shop = baseDao.getObject(CafeShop.class, wheelysOrder.getShopid());
		return shop;
	}

	private String getDiscountType(WheelysOrder wheelysOrder) {
		DiscountActivity discountActivity = baseDao.getObject(DiscountActivity.class, wheelysOrder.getSdid());
		return discountActivity == null ? null : discountActivity.getType();
	}

	private StringBuffer getProductName(WheelysOrder wheelysOrder) {
		StringBuffer productName = new StringBuffer();
		List<WheelysOrderDetail> orderDetailList = baseDao.getObjectListByField(WheelysOrderDetail.class, "orderid",
				wheelysOrder.getId());
		if (CollectionUtils.isNotEmpty(orderDetailList)) {
			for (WheelysOrderDetail wheelysOrderDetail : orderDetailList) {
				productName.append(wheelysOrderDetail.getProductname());
				productName.append("x");
				productName.append(wheelysOrderDetail.getQuantity());
				productName.append("; ");
			}
		}
		return productName;
	}

	private List<WheelysOrder> getWheelysOrderList(Integer pageNo, int rowsPerPage, String region, String cityCode,
			Long shopId, String status, String tradeno, Date createTimeBegin, Date createTimeEnd, String paymethod,
			String category, String elecCardBatchId) {
		DetachedCriteria query = getOrderListQuery(region, cityCode, shopId, status, tradeno, createTimeBegin,
				createTimeEnd, paymethod, category, elecCardBatchId);
		if (query == null) {
			return null;
		}
		int from = pageNo * rowsPerPage;
		return baseDao.findByCriteria(query, from, rowsPerPage);
	}

	private DetachedCriteria getOrderListQuery(String region, String cityCode, Long shopId, String status,
			String tradeno, Date createTimeBegin, Date createTimeEnd, String paymethod, String category,
			String elecCardBatchId) {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysOrder.class);
		query.add(Restrictions.eq("paystatus", "paid"));
		query.add(Restrictions.in("status", OrderConstant.ORDER_STATUS_MAP.keySet()));
		if (StringUtils.isNotBlank(region)) {
			List<CityManage> cityList = cityManageService.findCityManageByRegion(region);
			if (CollectionUtils.isNotEmpty(cityList)) {
				query.add(Restrictions.in("citycode", BeanUtil.getBeanPropertyList(cityList, "citycode", true)));
			} else {
				return null;
			}
		}
		if (StringUtils.isNotBlank(cityCode)) {
			query.add(Restrictions.eq("citycode", cityCode));
		}
		if (shopId != null) {
			query.add(Restrictions.eq("shopid", shopId));
		}
		if (StringUtils.isNotBlank(status)) {
			query.add(Restrictions.eq("status", status));
		}
		if (StringUtils.isNotBlank(tradeno)) {
			query.add(Restrictions.eq("tradeno", tradeno));
		}
		if (createTimeBegin != null) {
			query.add(Restrictions.ge("createtime", DateUtil.getBeginTimestamp(createTimeBegin)));
		}
		if (createTimeEnd != null) {
			query.add(Restrictions.le("createtime", DateUtil.getBeginTimestamp(createTimeEnd)));
		}
		if (StringUtils.isNotBlank(paymethod)) {
			query.add(Restrictions.eq("paymethod", paymethod));
		}
		if (StringUtils.isNotBlank(category)) {
			query.add(Restrictions.eq("category", category));
		}
		if (StringUtils.isNotBlank(elecCardBatchId) && StringUtils.isNumeric(elecCardBatchId)) {
			query.add(Restrictions.eq("batchid", Long.parseLong(elecCardBatchId)));
		}
		return query;
	}

	@Override
	public WheelysOerderVo findOrderByTradeno(String tradeno) {
		List<WheelysOerderVo> list = findOrderList(0, 1, null, null, null, null, tradeno, null, null, null, null, null);
		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

}

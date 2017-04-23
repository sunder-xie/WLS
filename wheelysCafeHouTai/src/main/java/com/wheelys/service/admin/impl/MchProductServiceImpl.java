package com.wheelys.service.admin.impl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.DateUtil;
import com.wheelys.constant.OrderConstant;
import com.wheelys.model.merchant.MchOrder;
import com.wheelys.model.merchant.MchOrderDetail;
import com.wheelys.model.merchant.MchProduct;
import com.wheelys.model.merchant.MchProductItem;
import com.wheelys.service.admin.MchProductService;

@Service("mchProductService")
public class MchProductServiceImpl extends BaseServiceImpl implements MchProductService {

	@Override
	public List<MchProduct> mchProductlist(Long itemid, String name, String status, Long supplierid, Integer pageNo,
			int maxnum) {
		DetachedCriteria query = DetachedCriteria.forClass(MchProduct.class);
		if (supplierid != null) {
			query.add(Restrictions.eq("supplierid", supplierid));
		}
		if (StringUtils.isNotBlank(name)) {
			query.add(Restrictions.like("name", "%" + name + "%"));
		}
		if (itemid != null) {
			query.add(Restrictions.eq("itemid", itemid));
		}
		if (status != null) {
			query.add(Restrictions.eq("status", status));
		}
		query.add(Restrictions.eq("delstatus", "N"));
		query.addOrder(Order.asc("sn"));
		int from = pageNo * maxnum;

		query.addOrder(Order.desc("createtime"));
		List<MchProduct> mchProductList = baseDao.findByCriteria(query, from, maxnum);
		return mchProductList;
	}

	@Override
	public Map<Long, MchProductItem> mchProductItemMap() {
		DetachedCriteria query = DetachedCriteria.forClass(MchProductItem.class);
		List<MchProductItem> mchProductItemList = baseDao.findByCriteria(query);
		Map<Long, MchProductItem> map = new HashMap<Long, MchProductItem>();
		for (MchProductItem item : mchProductItemList) {
			map.put(item.getId(), item);
		}
		return map;
	}

	@Override
	public void addmchproduct(MchProduct mchproduct) {
		this.baseDao.saveObject(mchproduct);

	}

	@Override
	public MchProduct findmchproduct(Long id) {
		MchProduct mchproduct = this.baseDao.getObject(MchProduct.class, id);
		return mchproduct;
	}

	@Override
	public void updateMchproduct(MchProduct upmcproduct) {
		this.baseDao.saveObject(upmcproduct);

	}

	@Override
	public void delMch(Long id, Integer sn, String delstatus) {
		if (sn != null) {
			MchProduct mch = this.baseDao.getObject(MchProduct.class, id);
			mch.setSn(sn);
			this.baseDao.saveObject(mch);
		} else {
			if (StringUtils.isNotBlank(delstatus)) {
				MchProduct mch = this.baseDao.getObject(MchProduct.class, id);
				if (StringUtils.equals(delstatus, "N")) {
					mch.setDelstatus("Y");
					this.baseDao.saveObject(mch);
				} else {
					this.baseDao.saveObject(mch);
				}
			}
		}
	}

	@Override
	public int findOrderCount(String tradeno, String mchname, String username, String status, Long supplierid,
			Timestamp time1, Timestamp time2) {
		DetachedCriteria query = getOrderListQuery(tradeno, mchname, username, status, supplierid, time1, time2);
		query.setProjection(Projections.rowCount());
		List<Long> result = baseDao.findByCriteria(query);
		return result.get(0).intValue();
	}

	private DetachedCriteria getOrderListQuery(String tradeno, String mchname, String username, String status,
			Long supplierid, Timestamp time1, Timestamp time2) {
		DetachedCriteria query = DetachedCriteria.forClass(MchOrder.class);
		query.add(Restrictions.ne("status", OrderConstant.STATUS_NEW));
		if (StringUtils.isNotBlank(tradeno)) {
			query.add(Restrictions.like("tradeno", "%" + tradeno + "%"));
		}
		if (StringUtils.isNotBlank(mchname)) {
			String name = StringUtils.trim(mchname);
			query.add(Restrictions.like("mchname", "%" + name + "%"));
		}
		if (StringUtils.isNotBlank(username)) {
			query.add(Restrictions.like("username", "%" + username + "%"));
		}
		if (StringUtils.isNotBlank(status)&&!StringUtils.equals(status, "all")) {
			query.add(Restrictions.eq("status", status));
		}else{
			query.add(Restrictions.or(Restrictions.eq("paystatus", OrderConstant.PAYSTATUS_PAID), Restrictions.eq("acceptstatus", "Y")));
		}
		if (time1 != null) {
			query.add(Restrictions.ge("createtime", time1));
		}
		if (time2 != null) {
			query.add(Restrictions.le("createtime", time2));
		}
		if (supplierid != null) {
			query.add(Restrictions.eq("supplierid", supplierid));
		}
		return query;
	}
	
	private DetachedCriteria OrderListQuery(String tradeno, String mchname, String username,
			String status, Long supplierid, Timestamp time1, Timestamp time2) {
		DetachedCriteria query = DetachedCriteria.forClass(MchOrder.class);
		query.add(Restrictions.ne("status", OrderConstant.STATUS_NEW));
		if (StringUtils.isNotBlank(tradeno)) {
			query.add(Restrictions.like("tradeno", "%" + tradeno + "%"));
		}

		if (StringUtils.isNotBlank(mchname)) {
			query.add(Restrictions.like("mchname", "%" + mchname + "%"));
		}
		if (StringUtils.isNotBlank(username)) {
			query.add(Restrictions.like("username", "%" + username + "%"));
		}
		if (StringUtils.isNotBlank(status)) {
			if (StringUtils.indexOf(status, ",") > 0) {
				String[] arr = StringUtils.split(status, ",");
				query.add(Restrictions.in("status", Arrays.asList(arr)));
			} else {
				query.add(Restrictions.eq("status", status));
			}
		}
		if (time1 != null) {
			query.add(Restrictions.ge("createtime", time1));
		}
		if (time2 != null) {
			query.add(Restrictions.le("createtime", time2));
		}
		if(supplierid !=null){
			query.add(Restrictions.eq("supplierid", supplierid));
		}
		return query;
	}

	
	
	@Override
	public List<MchOrderDetail> findMchDetail() {
		DetachedCriteria query = DetachedCriteria.forClass(MchOrderDetail.class);
		List<MchOrderDetail> mchDetailList = baseDao.findByCriteria(query);
		return mchDetailList;
	}

	/**
	 * 
	 * @param id
	 * @param status上下架状态
	 */
	@Override
	public void status(Long mchid, String status) {
		MchProduct mchProduct = this.baseDao.getObject(MchProduct.class, mchid);
		if (status != null) {
			if (status.equals("Y")) {
				mchProduct.setStatus("Y");
			}
			if (status.equals("N")) {
				mchProduct.setStatus("N");
			}
			this.baseDao.updateObject(mchProduct);
		}
	}

	@Override
	public int findListCount(Long itemid, String name, Long supplierid) {
		DetachedCriteria query = DetachedCriteria.forClass(MchProduct.class);
		if (itemid != null) {
			query.add(Restrictions.eq("itemid", itemid));
		}
		if (StringUtils.isNotBlank(name)) {
			query.add(Restrictions.like("name", "%" + name + "%"));
		}
		if (supplierid != null) {
			query.add(Restrictions.eq("supplierid", supplierid));
		}
		query.add(Restrictions.eq("delstatus", "N"));
		query.setProjection(Projections.rowCount());
		List<Long> result = baseDao.findByCriteria(query);
		return result.get(0).intValue();
	}

	@Override
	public List<MchOrderDetail> orderList(Long id) {
		DetachedCriteria query = DetachedCriteria.forClass(MchOrderDetail.class);
		query.add(Restrictions.in("orderid", id));
		List<MchOrderDetail> orList = baseDao.findByCriteria(query);
		return orList;
	}

	/**
	 * 判断用户是否存在
	 */
	@Override
	public boolean shopuser(String name, Long supplierid, Long id) {
		DetachedCriteria query = DetachedCriteria.forClass(MchProduct.class);
		query.add(Restrictions.eq("name", name));
		if (id != null) {
			query.add(Restrictions.ne("id", id));
		}
		query.setProjection(Projections.rowCount());
		List<Long> nameList = baseDao.findByCriteria(query);
		if (nameList.get(0) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public MchOrder mchorder(Long id) {
		MchOrder mchorder = this.baseDao.getObject(MchOrder.class, id);
		return mchorder;
	}

	public void updateOrderStatus(Long orderid, String status, String expressInfo) {
		MchOrder mchOrder = this.baseDao.getObject(MchOrder.class, orderid);
		if (StringUtils.equals(OrderConstant.STATUS_ACCEPT, status)) {
			if (StringUtils.equals(OrderConstant.STATUS_NEW_CONFIRM, mchOrder.getStatus())
					|| StringUtils.equals(OrderConstant.PAYSTATUS_PAID, mchOrder.getStatus())) {
				mchOrder.setStatus(status);
				mchOrder.setAcceptstatus("Y");
				mchOrder.setPaystatus(OrderConstant.PAYSTATUS_PAID);
				mchOrder.setPaidtime(DateUtil.getMillTimestamp());
			}
		} else if (StringUtils.equals(OrderConstant.STATUS_SEND, status)) {
			if (StringUtils.equals(OrderConstant.STATUS_ACCEPT, mchOrder.getStatus())) {
				mchOrder.setStatus(status);
				mchOrder.setExpressInfo(expressInfo);
				mchOrder.setSendtime(DateUtil.getMillTimestamp());
			} else if (StringUtils.equals(OrderConstant.STATUS_SEND, mchOrder.getStatus())) {
				mchOrder.setExpressInfo(expressInfo);
			}
		} else if (StringUtils.equals(OrderConstant.STATUS_FINISH, status)) {
			if (StringUtils.equals(OrderConstant.STATUS_SEND, mchOrder.getStatus())) {
				mchOrder.setStatus(status);
			}
		} else if (StringUtils.equals(OrderConstant.STATUS_CANCEL, status)){
				mchOrder.setStatus(status);
		}
		mchOrder.setUpdatetime(DateUtil.getMillTimestamp());
		this.baseDao.updateObject(mchOrder);
	}

	@Override
	public List<MchOrder> findMchorder(String tradeno, String mchname, Long supplierid, String username, String status,
			Timestamp time1, Timestamp time2, Integer pageNo, int maxnum) {
		DetachedCriteria query = getOrderListQuery(tradeno, mchname, username, status, supplierid, time1, time2);
		if (status != null&&!StringUtils.equals(status, "all")) {
			query.add(Restrictions.eq("status", status));
		}
		int from = pageNo * maxnum;
		query.addOrder(Order.desc("id"));
		List<MchOrder> mchOrderList = baseDao.findByCriteria(query, from, maxnum);
		return mchOrderList;
	}

	@Override
	public List<MchOrder> modList(String tradeno, String mchname, Long supplierid, String status, String username,
			Timestamp time1, Timestamp time2, Integer pageNo, int maxnum) {
		DetachedCriteria query = OrderListQuery(tradeno, mchname, username, status, supplierid, time1, time2);
		if (status != null) {
			query.add(Restrictions.eq("status", status));
		}
		int from = pageNo * maxnum;
		query.addOrder(Order.desc("id"));
		List<MchOrder> mchOrderList = baseDao.findByCriteria(query, from, maxnum);
		return mchOrderList;
	}
}

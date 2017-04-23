package com.wheelys.service.admin.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wheelys.Config;
import com.wheelys.api.vo.ResultCode;
import com.wheelys.model.acl.User;
import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.support.ErrorCode;
import com.wheelys.util.DateUtil;
import com.wheelys.util.JsonUtils;
import com.wheelys.constant.PayConstant;
import com.wheelys.constant.WheelysRefundConstant;
import com.wheelys.model.CafeShop;
import com.wheelys.model.CafeShopProfile;
import com.wheelys.model.orderrefund.RefundOrder;
import com.wheelys.model.pay.WheelysOrder;
import com.wheelys.model.pay.WheelysOrderDetail;
import com.wheelys.pay.AliWapPayUtil;
import com.wheelys.pay.WeiXinPayUtil;
import com.wheelys.service.admin.OrderRefundService;
import com.wheelys.web.action.openapi.vo.RefundOrderVo;
import com.wheelys.web.action.openapi.vo.WheelysOerderVo;
import com.wheelys.web.action.openapi.vo.WheelysOrderDetailVo;

@Service("orderRefundService")
public class OrderRefundServiceimpl extends BaseServiceImpl implements OrderRefundService {

	@Autowired
	@Qualifier("config")
	private Config config;

	@Override
	public List<RefundOrder> showRefundList(Integer pageNo, int maxnum) {
		return getRefundOrderList(pageNo, maxnum);
	}

	private List<RefundOrder> getRefundOrderList(Integer pageNo, int maxnum) {
		String hql = " FROM RefundOrder r " + " WHERE 1=1 and r.paystatus='paid' " + " ORDER BY ("
				+ " CASE WHEN r.status='" + WheelysRefundConstant.REFUNDSTATUS_PENDING_AUDIT + "' THEN 1 END"
				+ " ) desc , r.refundcompletetime DESC ,r.createtime DESC";
		int from = pageNo * maxnum;
		return queryByRowsRange(hql, from, maxnum);

	}

	@Override
	public int findListCount() {
		DetachedCriteria query = DetachedCriteria.forClass(RefundOrder.class);
		query.add(Restrictions.eq("paystatus", "paid"));
		query.setProjection(Projections.rowCount());
		List<Long> result = baseDao.findByCriteria(query);
		return result.get(0).intValue();
	}

	private WheelysOrder getWheelysOrder(String tradeno) {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysOrder.class);
		query.add(Restrictions.eq("tradeno", tradeno));
		List<WheelysOrder> mchOrderList = baseDao.findByCriteria(query);
		if (mchOrderList.isEmpty())
			return null;
		WheelysOrder order = mchOrderList.get(0);
		return order;
	}

	private RefundOrder getRefundOrder(String tradeno) {
		DetachedCriteria query = DetachedCriteria.forClass(RefundOrder.class);
		query.add(Restrictions.eq("tradeno", tradeno));
		List<RefundOrder> refundordreList = baseDao.findByCriteria(query);
		if (refundordreList.isEmpty())
			return null;
		RefundOrder order = refundordreList.get(0);
		return order;
	}

	@Override
	public void updateRefundStatus(Long id, String status, Long userid, String nickname, String expressInfo,
			String refundinfo) {
		RefundOrder refund = this.baseDao.getObject(RefundOrder.class, id);
		WheelysOrder order = getWheelysOrder(refund.getTradeno());
		if (StringUtils.equals(WheelysRefundConstant.REFUNDSTATUS_ALREADY_PASSED, status)) {
			if (StringUtils.equals(WheelysRefundConstant.REFUNDSTATUS_PENDING_AUDIT, refund.getStatus())
					|| StringUtils.equals(WheelysRefundConstant.REFUNDSTATUS_NO_PASSED, refund.getStatus())) {
				refund.setStatus(status);
				order.setStatus(status);
				refund.setOperaterid(userid);
				refund.setOperatename(nickname);
				if (StringUtils.isNotBlank(refundinfo)) {
					refund.setRefundinfo(refundinfo);
				}
				refund.setUpdatetime(DateUtil.getMillTimestamp());
				Integer refundamount = order.getNetpaid();
				if (refund.getRefundamount() != null && refund.getRefundamount() > 0
						&& refund.getRefundamount() <= order.getNetpaid()) {
					refundamount = refund.getRefundamount();
				}
				ErrorCode<Map<String, String>> result = null;
				if (StringUtils.equals(order.getPaymethod(), PayConstant.PAYMETHOD_ALIH5)) {// alipay
					result = AliWapPayUtil.refund(order, refundamount, "R" + order.getTradeno());
				} else {
					result = WeiXinPayUtil.refund(order, refundamount, "R" + order.getTradeno());
				}
				String ret = JsonUtils.writeMapToJson(result.getRetval());
				dbLogger.warn("退款成功：" + ret);
				refund.setStatus(WheelysRefundConstant.REFUNDSTATUS_FINISH);
				order.setStatus(WheelysRefundConstant.REFUNDSTATUS_FINISH);
				refund.setOperaterid(userid);
				refund.setOperatename(nickname);
				// 3.8号需求,添加退单完成时间.
				refund.setRefundcompletetime(DateUtil.getMillTimestamp());
			}
		} else if (StringUtils.equals(WheelysRefundConstant.REFUNDSTATUS_FINISH, status)) {
			refund.setStatus(status);
			order.setStatus(status);
			refund.setOperaterid(userid);
			refund.setOperatename(nickname);
		} else if (StringUtils.equals(WheelysRefundConstant.REFUNDSTATUS_NO_PASSED, status)) {
			if (StringUtils.equals(WheelysRefundConstant.REFUNDSTATUS_PENDING_AUDIT, refund.getStatus())) {
				refund.setStatus(status);
				order.setStatus(status);
				refund.setRefundfaildlinfo(expressInfo);
				order.setRefundfaildlinfo(expressInfo);
				refund.setOperaterid(userid);
				refund.setOperatename(nickname);
				if (StringUtils.isNotBlank(refundinfo)) {
					refund.setRefundinfo(refundinfo);
				}
				refund.setUpdatetime(DateUtil.getMillTimestamp());
			}
		} else if (StringUtils.equals(WheelysRefundConstant.REFUNDSTATUS_REFUND_FAILED, status)) {
			refund.setStatus(status);
			order.setStatus(status);
			refund.setOperaterid(userid);
			refund.setOperatename(nickname);
		}
		this.baseDao.updateObject(refund);
		this.baseDao.updateObject(order);
	}

	@Override
	public RefundOrder showRefundinfo(Long id) {
		RefundOrder refund = this.baseDao.getObject(RefundOrder.class, id);
		return refund;
	}

	@Override
	public WheelysOerderVo getOrder(String tradeno) {
		WheelysOrder baseOrder = this.getWheelysOrder(tradeno);
		List<WheelysOrderDetail> detailList = getOrderDetail(baseOrder.getId());
		CafeShop cafeshop = this.baseDao.getObject(CafeShop.class, baseOrder.getShopid());
		CafeShopProfile profile = this.baseDao.getObject(CafeShopProfile.class, baseOrder.getShopid());
		WheelysOerderVo orderVo = new WheelysOerderVo(baseOrder);
		orderVo.setTakekey(baseOrder.getTakekey());
		orderVo.setTradeno(baseOrder.getTradeno());
		orderVo.setPayfee(baseOrder.getPayfee());
		orderVo.setDisreason(baseOrder.getDisreason());
		orderVo.setDiscount(baseOrder.getDiscount());
		orderVo.setCafeShopProfile(profile);
		orderVo.setShop(cafeshop);
		List<WheelysOrderDetailVo> voDetailList = new ArrayList<WheelysOrderDetailVo>();
		for (WheelysOrderDetail detail : detailList) {
			WheelysOrderDetailVo vo = new WheelysOrderDetailVo(detail, config.getString("picPath"));
			voDetailList.add(vo);
		}
		orderVo.setDetailList(voDetailList);
		return orderVo;
	}

	private List<WheelysOrderDetail> getOrderDetail(Long id) {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysOrderDetail.class);
		query.add(Restrictions.eq("orderid", id));
		List<WheelysOrderDetail> resultList = baseDao.findByCriteria(query);
		return resultList;
	}

	@Override
	public List<RefundOrderVo> getOrderList(Long shopid, Timestamp fromtime, Timestamp totime, String tradeno,
			Integer pageno) {
		DetachedCriteria query = DetachedCriteria.forClass(RefundOrder.class);
		query.add(Restrictions.eq("shopid", shopid));
		if (fromtime != null) {
			query.add(Restrictions.ge("createtime", fromtime));
		}
		if (totime != null) {
			query.add(Restrictions.le("createtime", totime));
		}
		if (StringUtils.isNotBlank(tradeno)) {
			query.add(Restrictions.like("tradeno", "%" + tradeno));
		}
		if (pageno == null)
			pageno = 0;
		int from = pageno * 20;
		query.addOrder(Order.desc("createtime"));
		List<RefundOrder> refundList = baseDao.findByCriteria(query, from, 20);
		List<RefundOrderVo> voList = new ArrayList();
		for (RefundOrder order : refundList) {
			WheelysOrder wheelysOrder = getWheelysOrder(order.getTradeno());
			List<WheelysOrderDetail> detailList = getOrderDetail(wheelysOrder.getId());
			List<WheelysOrderDetailVo> detailVoList = new ArrayList<WheelysOrderDetailVo>();
			for (WheelysOrderDetail detail : detailList) {
				WheelysOrderDetailVo vo = new WheelysOrderDetailVo(detail, config.getString("picPath"));
				detailVoList.add(vo);
			}
			RefundOrderVo refundVo = new RefundOrderVo(order, detailList, wheelysOrder.getTakekey(),
					wheelysOrder.getDescription(), wheelysOrder.getDiscount());
			voList.add(refundVo);
		}
		return voList;
	}

	@Override
	public ResultCode<RefundOrder> addRefund(Long userid, Long refunduserid, String refundinfo, String tradeno,
			Integer refundamount) {
		RefundOrder refund = getRefundOrder(tradeno);
		if (refund == null || StringUtils.equals(refund.getStatus(), WheelysRefundConstant.REFUNDSTATUS_NO_PASSED)) {
			WheelysOrder order = getWheelysOrder(tradeno);
			RefundOrder refundOrder = new RefundOrder(tradeno,order,userid,refundinfo,refundamount);
			CafeShop cafeShop = this.baseDao.getObject(CafeShop.class, order.getShopid());
			refundOrder.setShopname(cafeShop.getEsname());
			refundOrder.setShoprole(cafeShop.getContacts());
			refundOrder.setShoptelephone(cafeShop.getShoptelephone());
			this.baseDao.saveObject(refundOrder);
			order.setStatus(refundOrder.getStatus());
			this.baseDao.saveObject(order);
			return ResultCode.getSuccessReturn(refundOrder);
		} else {
			return ResultCode.getFailure("该订单不能提交");
		}
	}

	@Override
	public User user(Long id) {
		User user = this.baseDao.getObject(User.class, id);
		return user;
	}

	@Override
	public List<RefundOrder> showRefundListByTime(Date begin, Date end, Long shopid, Integer pageNo, int maxnum) {
		List<RefundOrder> findRefundLst = this.findRefundList(pageNo, maxnum, shopid, begin, end);
		return findRefundLst;

	}

	private List<RefundOrder> findRefundList(Integer pageNo, int maxnum, Long shopid, Date begin, Date end) {
		String query = this.getHqlQuery(shopid, begin, end);
		int from = pageNo * maxnum;
		return queryByRowsRange(query, from, maxnum);
	}
	private String getHqlQuery(Long shopid, Date begin, Date end) {
		StringBuffer sb = new StringBuffer();
		sb.append(" FROM RefundOrder r " + " WHERE 1=1 and r.paystatus='paid' ");
		if (begin != null || end != null) {
			sb.append(" and r.status='refund_finish' ");
		}
		if (shopid != null) {
			sb.append(" and r.shopid= '"+shopid+"'");
		}
		if (begin != null) {
			sb.append(" and r.refundtime >= '"+begin+"'");
		}
		if (end != null) {
			sb.append(" and r.refundcompletetime <= '"+end+"'");
		}
		sb.append(" ORDER BY ( CASE WHEN r.status='" + WheelysRefundConstant.REFUNDSTATUS_PENDING_AUDIT + "' THEN 1 END )"
				+ " desc , r.refundcompletetime DESC ,r.createtime DESC");
		return sb.toString();
	}
	
	private DetachedCriteria getquery(Long shopid, Date begin, Date end) {
		DetachedCriteria query = DetachedCriteria.forClass(RefundOrder.class);
		query.add(Restrictions.eq("paystatus", "paid"));
		if (begin != null || end != null) {
			query.add(Restrictions.eq("status", WheelysRefundConstant.REFUNDSTATUS_FINISH));
		}
		if (shopid != null) {
			query.add(Restrictions.eq("shopid", shopid));
		}
		if (begin != null) {
			query.add(Restrictions.ge("refundtime", begin));
		}
		if (end != null) {
			query.add(Restrictions.le("refundcompletetime",end));
		}
		return query;
	}

	@Override
	public int findListCount(Date begin, Date end, Long shopid) {
		DetachedCriteria query = this.getquery(shopid, begin, end);
		query.add(Restrictions.eq("paystatus", "paid"));
		query.setProjection(Projections.rowCount());
		List<Long> result = baseDao.findByCriteria(query);
		return result.isEmpty() ? 0 : result.get(0).intValue();
	}

	@Override
	public List<RefundOrder> showRefundListById(Date begin, Date end, Long shopid) {
		DetachedCriteria query = this.getquery(shopid, begin, end);
		query.addOrder(Order.desc("id"));
		List<RefundOrder> list = this.baseDao.findByCriteria(query);
		return list;
	}
}

package com.wheelys.service.admin.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.DateUtil;
import com.wheelys.model.CafeShop;
import com.wheelys.model.order.CardCouponsOrder;
import com.wheelys.model.user.OpenMember;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.admin.ActivityOrdService;
import com.wheelys.service.cafe.CafeShopService;
import com.wheelys.web.action.admin.vo.CardCouponsOrderVo;

@Service("activityOrdService")
public class ActivityOrdServiceImpl extends BaseServiceImpl implements ActivityOrdService {

	@Autowired
	@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;

	private List<CardCouponsOrder> findordList(Integer pageNo, String mobile, int maxnum, Long shopid, Date begin,
			Date end) {
		// 查询活动订单
		DetachedCriteria query = this.getquery(mobile, shopid, begin, end);
		int from = pageNo * maxnum;
		query.addOrder(Order.desc("id"));
		List<CardCouponsOrder> list = this.baseDao.findByCriteria(query, from, maxnum);
		return list;
	}

	public List<CardCouponsOrderVo> findordvoList(Integer pageNo, String mobile, int maxnum, Long shopid, Date begin,
			Date end) {
		List<CardCouponsOrder> findordList = this.findordList(pageNo, mobile, maxnum, shopid, begin, end);
		List<CardCouponsOrderVo> cardvoList = new ArrayList<CardCouponsOrderVo>();
		if (!CollectionUtils.isEmpty(findordList)) {
			for (CardCouponsOrder card : findordList) {
				// 第三方用户昵称, WheelysMember手机号,商铺名称
				CafeShop shop = cafeShopService.getShop(card.getShopid());
				Long memberid = card.getMemberid();
				String nickname = "";
				Long memberid2 = null;
				if (memberid != null) {
					OpenMember openMember = this.findmemberBymemberid(memberid);
					if (openMember != null) {
						nickname = openMember.getNickname();
						memberid2 = openMember.getMemberid();
					}
				}
				String wheelmobile = "";
				if (memberid2 != null) {
					WheelysMember wheelysMember = this.findmobileBynickname(memberid2);
					wheelmobile = wheelysMember.getMobile();
				}
				CardCouponsOrderVo cardvo = new CardCouponsOrderVo(card);
				cardvo.setMobile(wheelmobile);
				cardvo.setNickname(nickname);
				cardvo.setShopname(shop == null ? null : shop.getEsname());
				cardvo.setTradeno(card.getTradeno());
				cardvo.setPaidtime(card.getPaidtime());
				cardvo.setPayfee(card.getPayfee());
				cardvo.setOtherinfo(card.getOtherinfo());
				cardvoList.add(cardvo);
			}
		}
		return cardvoList;
	}

	@Override
	public int findCount(String mobile, Long shopid, Date begin, Date end) {
		// 查询总记录数
		DetachedCriteria query = this.getquery(mobile, shopid, begin, end);
		query.setProjection(Projections.rowCount());
		List<Long> result = this.baseDao.findByCriteria(query);
		int count = result.get(0).intValue();
		return count;
	}

	private DetachedCriteria getquery(String mobile, Long shopid, Date begin, Date end) {
		DetachedCriteria query = DetachedCriteria.forClass(CardCouponsOrder.class);
		query.add(Restrictions.eq("restatus", "N"));
		query.add(Restrictions.eq("paystatus", "paid"));
		query.add(Restrictions.gt("netpaid", 100));
		if (StringUtils.isNotBlank(mobile)) {
			query.add(Restrictions.like("mobile", "%" + mobile + "%"));
		}
		if (shopid != null) {
			query.add(Restrictions.eq("shopid", shopid));
		}
		if (begin != null) {
			query.add(Restrictions.ge("createtime", DateUtil.getBeginTimestamp(begin)));
		}
		if (end != null) {
			query.add(Restrictions.le("createtime", DateUtil.getEndTimestamp(end)));
		}
		return query;
	}

	private OpenMember findmemberBymemberid(Long memberid) {
		DetachedCriteria query = DetachedCriteria.forClass(OpenMember.class);
		query.add(Restrictions.eq("memberid", memberid));
		List<OpenMember> list = this.baseDao.findByCriteria(query);
		if (!CollectionUtils.isEmpty(list)) {
			OpenMember member = list.get(0);
			return member;
		} else {
			return null;
		}

	}

	private WheelysMember findmobileBynickname(Long memberid) {
		WheelysMember wheelysMember = this.baseDao.getObject(WheelysMember.class, memberid);
		return wheelysMember;
	}

}

package com.wheelys.job.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.wheelys.dao.Dao;
import com.wheelys.job.JobService;
import com.wheelys.support.concurrent.AtomicCounter;
import com.wheelys.constant.DiscountConstant;
import com.wheelys.model.discount.DiscountActivity;
import com.wheelys.model.discount.MemberJoinDiscount;
import com.wheelys.service.order.DiscountService;

public class DiscountJobImpl extends JobService {
	
	@Autowired@Qualifier("discountService")
	private DiscountService discountService;
	@Autowired@Qualifier("baseDao")
	private Dao baseDao;

	public void updateDiscountRedisCounter() {
		Timestamp nowtime = Timestamp.valueOf(LocalDateTime.now());
		DetachedCriteria query = DetachedCriteria.forClass(DiscountActivity.class);
		query.add(Restrictions.eq("status", "Y"));
		query.add(Restrictions.eq("type", DiscountConstant.FIRSTCUP));
		query.add(Restrictions.gt("endtime", nowtime));
		query.add(Restrictions.lt("begintime", nowtime));
		query.add(Restrictions.isNotNull("limitmaxnum"));
		query.setProjection(Projections.property("id"));
		List<Long> discountList = baseDao.findByCriteria(query);
		dbLogger.warn("特价活动定时任务执行：" + discountList.size());
		discountList.stream().forEach(id -> {
			DiscountActivity discountActivity = baseDao.getObject(DiscountActivity.class, id);
			if(discountActivity.getLimitmaxnum() > discountActivity.getAllowaddnum()){
				AtomicCounter atomicCounter = discountService.getDiscountCounter(id);
				atomicCounter.set(getMemberJoinDiscount(id));
				dbLogger.warn("特价活动计算器 id：" + discountActivity.getId() + " lockcount：" + atomicCounter.get());
			}
		});
	}
	
	private long getMemberJoinDiscount(Long discountid){
		Timestamp nowtime = Timestamp.valueOf(LocalDateTime.now());
		DetachedCriteria query = DetachedCriteria.forClass(MemberJoinDiscount.class);
		query.add(Restrictions.eq("discountid", discountid));
		query.add(Restrictions.lt("addtime", nowtime));
		query.add(Restrictions.ge("validtime", nowtime));
		query.setProjection(Projections.rowCount());
		List<Integer> result = baseDao.findByCriteria(query);
		long count = 0;
		if(!result.isEmpty()) count = Long.parseLong(result.get(0) + "");
		return count;
	}
}

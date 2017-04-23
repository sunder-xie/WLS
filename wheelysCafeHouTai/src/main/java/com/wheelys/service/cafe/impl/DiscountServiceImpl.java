package com.wheelys.service.cafe.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.BeanUtil;
import com.wheelys.util.DateUtil;
import com.wheelys.util.JsonUtils;
import com.wheelys.constant.DiscountContant;
import com.wheelys.model.CafeShop;
import com.wheelys.model.discount.DiscountActivity;
import com.wheelys.service.cafe.DiscountService;
import com.wheelys.web.action.admin.vo.DiscountActivityVo;

@Service("discountService")
public class DiscountServiceImpl extends BaseServiceImpl implements DiscountService {

	@Override
	public void updateDiscount1(Long id, String status, String shopids, Timestamp begintime, Timestamp endtime) {
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
			discountActivity.setEndtime(DateUtil.addSecond(DateUtil.getLastTimeOfDay(endtime), -10));
		}
		this.baseDao.saveObject(discountActivity);
	}

	@Override
	public void savediscount(DiscountActivity discountActivity) {
		// 保存到数据库中
		this.baseDao.saveObject(discountActivity);
	}

	/**
	 * 获取所有优惠活动列表
	 * 
	 * @param pageNo
	 * @param name
	 * @param activitytype
	 * @param rowsPerPage
	 * @return
	 */
	@Override
	public List<DiscountActivityVo> findAllDiscountList(Integer pageNo, String actname, String activitytype,
			int maxnum) {
		// 查询所有活动列表
		List<DiscountActivity> list = this.getDisCountList(actname, activitytype, pageNo, maxnum);
		List<DiscountActivityVo> voList = new ArrayList();
		List<String> nameList = null;
		for (DiscountActivity d : list) {
			List<Long> shopIdList = BeanUtil.getIdList(d.getShopids(), ",");
			List<CafeShop> cafeShopList = baseDao.getObjectList(CafeShop.class, shopIdList);
			nameList = BeanUtil.getBeanPropertyList(cafeShopList, String.class, "shopname", true);
			String names = StringUtils.join(nameList, ",");
			DiscountActivityVo vo = new DiscountActivityVo(d, names);
			voList.add(vo);
		}
		return voList;
	}

	public List<DiscountActivity> getDisCountList(String actname, String activitytype, Integer pageNo, int maxnum) {
		int from = pageNo * maxnum;
		String hql = " FROM DiscountActivity d" + " WHERE 1=1 ";
		if (StringUtils.isNotBlank(actname)) {
			hql += " AND d.name like ? ";
		}
		if (StringUtils.isNotBlank(activitytype)) {
			hql += " AND d.type=? ";
		}
		hql += " ORDER BY ( CASE WHEN d.status !='" + DiscountContant.STATUSTYPE_4
				+ "' AND current_timestamp()>d.endtime THEN 1 END) asc";
		Object[] params = null;
		if (StringUtils.isNotBlank(actname)) {
			actname = "%" + actname + "%";
			params = new Object[] { actname };
		}
		if (StringUtils.isNotBlank(activitytype)) {
			if (params != null) {
				params = ArrayUtils.add(params, activitytype);
			} else {
				params = new Object[] { activitytype };
			}
		}
		return queryByRowsRange(hql, from, maxnum, params);
	}

	/**
	 * 获取优惠活动的count
	 * 
	 * @param name
	 * @param activitytype
	 * @return
	 */
	@Override
	public int findAllCount(String actname, String activitytype) {
		// 获取活动列表的总个数
		DetachedCriteria query = this.getAllCountQuery(actname, activitytype);
		query.setProjection(Projections.rowCount());
		List<Long> result = this.baseDao.findByCriteria(query);
		int count = result.get(0).intValue();
		return count;
	}

	private DetachedCriteria getAllCountQuery(String actname, String activitytype) {
		DetachedCriteria query = DetachedCriteria.forClass(DiscountActivity.class);
		// 添加过滤条件
		if (StringUtils.isNotBlank(actname)) {
			query.add(Restrictions.like("name", actname));
		}
		if (StringUtils.isNotBlank(activitytype)) {
			query.add(Restrictions.eq("type", activitytype));
		}
		return query;
	}

	private DetachedCriteria getDiscount(String actname) {
		DetachedCriteria query = DetachedCriteria.forClass(DiscountActivity.class);
		if (StringUtils.isNotBlank(actname)) {
			query.add(Restrictions.like("name", "%" + actname + "%"));
		}
		return query;
	}

	@Override
	public DiscountActivity findActiveById(Long id) {
		// 根据id获取具体活动
		DiscountActivity activity = this.baseDao.getObject(DiscountActivity.class, id);
		return activity;
	}

	@Override
	public List<DiscountActivity> showactivityList(String actname, Integer pageNo, int maxnum) {
		// 根据活动id模糊查询
		DetachedCriteria query = this.getDiscount(actname);
		int from = pageNo * maxnum;
		query.addOrder(Order.desc("id"));
		List<DiscountActivity> list = this.baseDao.findByCriteria(query, from, maxnum);
		return list;
	}

	@Override
	public int findListCount(String actname) {
		// 根据条件查询活动数量
		DetachedCriteria query = DetachedCriteria.forClass(DiscountActivity.class);
		if (StringUtils.isNotBlank(actname)) {
			query.add(Restrictions.like("name", "%" + actname + "%"));
		}
		query.setProjection(Projections.rowCount());
		List<Long> result = baseDao.findByCriteria(query);
		return result.get(0).intValue();
	}

	@Override
	public List<Long> itemids(Long id) {
		DiscountActivity dis = this.baseDao.getObject(DiscountActivity.class, id);
		List<Long> idList = BeanUtil.getIdList(dis.getValiditemid(), ",");
		return idList;
	}

	@Override
	public List<Long> productids(Long id) {
		DiscountActivity dis = this.baseDao.getObject(DiscountActivity.class, id);
		List<Long> idList = new ArrayList<>();
		String s = null;
		if (StringUtils.isNotBlank(dis.getValidproductid())) {
			Map<String, String> temp = JsonUtils.readJsonToMap(dis.getValidproductid());
			for (String string : temp.values()) {
				s += "," + string + ",";
			}
		}
		idList = BeanUtil.getIdList(s, ",");
		return idList;
	}
}

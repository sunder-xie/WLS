package com.wheelys.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.BeanUtil;
import com.wheelys.util.DateUtil;
import com.wheelys.model.CafeShop;
import com.wheelys.model.MemberOrderInfo;
import com.wheelys.model.citymanage.CityManage;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.MemberOrderInfoService;
import com.wheelys.service.admin.CityManageService;
import com.wheelys.service.cafe.CafeShopService;

@Service("memberOrderInfoService")
public class MemberOrderInfoServiceImpl extends BaseServiceImpl implements MemberOrderInfoService {
	
	@Autowired
	@Qualifier("cityManageService")
	private CityManageService cityManageService;
	@Autowired
	@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;

	@Override
	public List<MemberOrderInfo> getNewOrderMemberByShopidAndDay(Long shopid, Date day) {
		Timestamp beginTimestamp = DateUtil.getBeginTimestamp(day);
		Timestamp endTimestamp = DateUtil.getEndTimestamp(day);

		DetachedCriteria query = DetachedCriteria.forClass(MemberOrderInfo.class);
		query.add(Restrictions.eq("firstshopid", shopid));
		query.add(Restrictions.between("regstertime", beginTimestamp, endTimestamp));
		query.add(Restrictions.between("firstordertime", beginTimestamp, endTimestamp));
		query.addOrder(Order.asc("firstordertime"));
		List<MemberOrderInfo> infoList = baseDao.findByCriteria(query);
		return infoList;
	}

	@Override
	public List<MemberOrderInfo> getOldOrderMemberByShopidAndDay(Long shopid, Date day) {
		Timestamp beginTimestamp = DateUtil.getBeginTimestamp(day);
		Timestamp endTimestamp = DateUtil.getEndTimestamp(day);

		DetachedCriteria query = DetachedCriteria.forClass(MemberOrderInfo.class);
		query.add(Restrictions.eq("firstshopid", shopid));
		query.add(Restrictions.lt("regstertime", beginTimestamp));
		query.add(Restrictions.between("firstordertime", beginTimestamp, endTimestamp));
		query.addOrder(Order.asc("firstordertime"));
		List<MemberOrderInfo> infoList = baseDao.findByCriteria(query);
		return infoList;
	}

	@Override
	public List<MemberOrderInfo> getMemberInfoByIds(List<Long> memberids) {
		List<MemberOrderInfo> infoList = baseDao.getObjectList(MemberOrderInfo.class, memberids);
		return infoList;
	}

	@Override
	public List<MemberOrderInfo> getMemberLatestOrderInfo(Integer pageNo, int rowPerPage, String region,
			String cityCode, Long shopId, String phone, Date createTimeBegin, Date createTimeEnd) {
		DetachedCriteria query = getMemberOrderInfoQuery(region, cityCode, shopId, phone, createTimeBegin,
				createTimeEnd);
		if (query == null) {
			return null;
		}
		int from = pageNo * rowPerPage;
		return baseDao.findByCriteria(query, from, rowPerPage);
	}

	@Override
	public int getMemberLatestOrderInfoCount(String region, String cityCode, Long shopId, String phone,
			Date createTimeBegin, Date createTimeEnd) {
		DetachedCriteria query = getMemberOrderInfoQuery(region, cityCode, shopId, phone, createTimeBegin,
				createTimeEnd);
		if (query == null) {
			return 0;
		}
		query.setProjection(Projections.rowCount());
		List<Long> count = baseDao.findByCriteria(query);
		return count.get(0).intValue();
	}

	private DetachedCriteria getMemberOrderInfoQuery(String region, String cityCode, Long shopId, String phone,
			Date createTimeBegin, Date createTimeEnd) {
		DetachedCriteria query = DetachedCriteria.forClass(MemberOrderInfo.class);
		List<String> cityCodes = new ArrayList<String>();
		List<Long> shopIds = new ArrayList<>();
		if (StringUtils.isNotBlank(region)) {
			List<CityManage> cityList = cityManageService.findCityManageByRegion(region);
			cityCodes.addAll(BeanUtil.getBeanPropertyList(cityList, "citycode", true));
		}
		if (StringUtils.isNotBlank(cityCode)) {
			cityCodes.add(cityCode);
		}
		if (CollectionUtils.isNotEmpty(cityCodes)) {
			List<CafeShop> shopList = cafeShopService.getCafeShopByCityCods(cityCodes);
			if (CollectionUtils.isNotEmpty(shopList)) {
				shopIds.addAll(BeanUtil.getBeanPropertyList(shopList, "shopid", true));
			} else {
				return null;
			}
		}
		if (shopId != null) {
			shopIds.add(shopId);
		}
		if (CollectionUtils.isNotEmpty(shopIds)) {
			query.add(Restrictions.in("lastshopid", shopIds));
		}
		if (StringUtils.isNotBlank(phone)) {
			List<WheelysMember> members = baseDao.getObjectListByField(WheelysMember.class, "mobile", phone);
			if (CollectionUtils.isNotEmpty(members)) {
				query.add(Restrictions.in("memberid", BeanUtil.getBeanPropertyList(members, "id", true)));
			} else {
				return null;
			}
		}
		if (createTimeBegin != null) {
			query.add(Restrictions.ge("lastordertime", createTimeBegin));
		}
		if (createTimeEnd != null) {
			query.add(Restrictions.le("lastordertime", DateUtil.getEndTimestamp(createTimeEnd)));
		}
		return query;
	}

}

package com.wheelys.service.admin.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.BeanUtil;
import com.wheelys.model.CafeShop;
import com.wheelys.model.citymanage.CityCode;
import com.wheelys.model.citymanage.CityManage;
import com.wheelys.model.company.Company;
import com.wheelys.service.admin.CityManageService;
import com.wheelys.web.action.admin.vo.CityManageVo;

@Service("cityManageService")
public class CityManageServiceImpl extends BaseServiceImpl implements CityManageService {

	@Override
	public void addCity(Long id, String region, String citycode, String cityname) {
		// TODO Auto-generated method stub
		DetachedCriteria query = DetachedCriteria.forClass(CafeShop.class);
		query.add(Restrictions.eq("citycode", citycode));
		query.add(Restrictions.eq("delstatus", "N"));
		List<CafeShop> shopList = baseDao.findByCriteria(query);
		CityManage manage = new CityManage(id, citycode, cityname);
		Set<Long> companyidSet = new TreeSet<>();
		List<String> shopidList = null;
		if (id != null) {
			manage = this.baseDao.getObject(CityManage.class, id);
			manage.setRegion(region);
		} else {
			if (shopList.isEmpty()) {
				manage.setRegion(region);
			} else {
				manage.setRegion(region);
				for (CafeShop shop : shopList) {
					companyidSet.add(shop.getOperatorid());
				}
				String companyids = StringUtils.join(companyidSet, ",");
				manage.setCompanyids(companyids);
				List<Company> companyList = baseDao.getObjectList(Company.class, companyidSet);
				shopidList = BeanUtil.getBeanPropertyList(companyList, String.class, "shopnumber", true);
				String shopids = StringUtils.join(shopidList, ",");
				manage.setShopids(shopids);
			}
		}
		this.baseDao.saveObject(manage);
	}

	@Override
	public void delCity(Long id, Integer sn) {
		CityManage city = this.manage(id);
		if (sn != null) {
			city.setSn(sn);
			this.baseDao.saveObject(city);
			return;
		}
		if (city.getDelstatus().equals("N")) {
			city.setDelstatus("Y");
		}
		this.baseDao.saveObject(city);
	}

	@Override
	public List<CityManageVo> showCityList() {
		// TODO Auto-generated method stub
		List<CityManage> cityList = getCitList();
		List<CityManageVo> voList = new ArrayList<>();
		List<String> nameList = null;
		for (CityManage manage : cityList) {
			if (StringUtils.isNotBlank(manage.getShopids())) {
				List<String> shopIdList = Arrays.asList(manage.getShopids().split(","));
				int idList = shopIdList.size();
				List<Long> companyIdList = BeanUtil.getIdList(manage.getCompanyids(), ",");
				List<Company> companyList = baseDao.getObjectList(Company.class, companyIdList);
				nameList = BeanUtil.getBeanPropertyList(companyList, String.class, "name", true);
				String companynames = StringUtils.join(nameList, ",");
				CityManageVo vo = new CityManageVo(manage, companynames, idList);
				voList.add(vo);
			} else {
				CityManageVo vo = new CityManageVo(manage, null, 0);
				voList.add(vo);
			}
		}
		return voList;
	}

	private List<CityManage> getCitList() {
		DetachedCriteria query = DetachedCriteria.forClass(CityManage.class);
		query.add(Restrictions.eq("delstatus", "N"));
		List<CityManage> cityList = baseDao.findByCriteria(query);
		return cityList;
	}

	@Override
	public CityManage manage(Long id) {
		// TODO Auto-generated method stub
		CityManage city = this.baseDao.getObject(CityManage.class, id);
		return city;
	}

	@Override
	public List<CityCode> codeList(String citycode) {
		DetachedCriteria query = DetachedCriteria.forClass(CityCode.class);
		if (StringUtils.isNotBlank(citycode)) {
			query.add(Restrictions.or(Restrictions.like("citycode", citycode + "%"),
					Restrictions.like("name", citycode + "%")));
		}
		List<CityCode> codeList = baseDao.findByCriteria(query);
		return codeList;
	}

	@Override
	public boolean ifCity(String citycode) {
		DetachedCriteria query = DetachedCriteria.forClass(CityManage.class);
		query.add(Restrictions.eq("delstatus", "N"));
		query.add(Restrictions.eq("citycode", citycode));
		query.setProjection(Projections.rowCount());
		List<Long> result = baseDao.findByCriteria(query);
		return result.get(0) > 0;
	}

	@Override
	public void saveManage(String newcitycode, String oldcitycode, Long shopid, Long companyid) {
		DetachedCriteria query = DetachedCriteria.forClass(CityManage.class);
		query.add(Restrictions.eq("delstatus", "N"));
		query.add(Restrictions.eq("citycode", newcitycode));
		List<CityManage> manageList = baseDao.findByCriteria(query);
		if (StringUtils.equals(newcitycode, oldcitycode)) {
			if (!manageList.isEmpty()) {
				CityManage manage = manageList.get(0);
				List<Long> shopIdList = BeanUtil.getIdList(manage.getShopids(), ",");
				List<Long> companyidsList = BeanUtil.getIdList(manage.getCompanyids(), ",");
				if (!shopIdList.contains(shopid)) {
					shopIdList.add(shopid);
				}
				if (!companyidsList.contains(companyid)) {
					companyidsList.add(companyid);
				}
				String shop = StringUtils.join(shopIdList, ",");
				String company = StringUtils.join(companyidsList, ",");
				manage.setCompanyids(company);
				manage.setShopids(shop);
				this.baseDao.saveObject(manage);
			}
		} else {
			if (!manageList.isEmpty()) {
				DetachedCriteria squery = DetachedCriteria.forClass(CityManage.class);
				squery.add(Restrictions.eq("delstatus", "N"));
				squery.add(Restrictions.eq("citycode", oldcitycode));
				List<CityManage> smanageList = baseDao.findByCriteria(squery);
				if (!smanageList.isEmpty()) {
					CityManage manage = manageList.get(0);
					CityManage smanage = smanageList.get(0);
					List<Long> shopidsList = BeanUtil.getIdList(manage.getShopids(), ",");
					List<Long> companyidsList = BeanUtil.getIdList(manage.getCompanyids(), ",");
					List<Long> oldshopidList = BeanUtil.getIdList(smanage.getShopids(), ",");
					List<Long> oldcompanyidsList = BeanUtil.getIdList(smanage.getCompanyids(), ",");
					if (!shopidsList.contains(shopid)) {
						shopidsList.add(shopid);
					}
					if (oldshopidList.contains(shopid)) {
						oldshopidList.remove(shopid);
					}
					if (!companyidsList.contains(companyid)) {
						companyidsList.add(companyid);
					}
					if (oldcompanyidsList.contains(companyid)) {
						oldcompanyidsList.remove(companyid);
					}
					String shop = StringUtils.join(shopidsList, ",");
					String company = StringUtils.join(companyidsList, ",");
					String oldshop = StringUtils.join(oldshopidList, ",");
					String oldcompany = StringUtils.join(oldcompanyidsList, ",");
					manage.setCompanyids(company);
					manage.setShopids(shop);
					smanage.setShopids(oldshop);
					smanage.setCompanyids(oldcompany);
					this.baseDao.saveObject(manage);
					this.baseDao.saveObject(smanage);
				}
			}
		}
	}

	@Override
	public String shopnameList(Long id) {
		CityManage manage = this.manage(id);
		List<Long> idList = BeanUtil.getIdList(manage.getShopids(), ",");
		List<CafeShop> shopList = baseDao.getObjectList(CafeShop.class, idList);
		List<String> nameList = BeanUtil.getBeanPropertyList(shopList, String.class, "shopname", true);
		String names = StringUtils.join(nameList, ",");
		return names;
	}

	@Override
	public List<CityManage> findCityManageByRegion(String region) {
		DetachedCriteria query = DetachedCriteria.forClass(CityManage.class);
		query.add(Restrictions.eq("region", region));
		return baseDao.findByCriteria(query);
	}

	@Override
	public void changeStatus(Long id, String status) {
		CityManage manage = this.manage(id);
		if (!StringUtils.equals(manage.getStatus(), status)) {
			manage.setStatus(status);
		}
		this.baseDao.saveObject(manage);
	}
}

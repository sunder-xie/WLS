package com.wheelys.service.admin.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.model.citymanage.CityRegion;
import com.wheelys.service.admin.CityRegionService;

@Service("cityRegionService")
public class CityRegionServiceImpl extends BaseServiceImpl implements CityRegionService {

	@Override
	public Map<Long, CityRegion> cityRegionMap() {
		List<CityRegion> cityregionList = getAllCityRegion();
		Map<Long, CityRegion> cityregionMap = new HashMap<Long, CityRegion>();
		for (CityRegion city : cityregionList) {
			cityregionMap.put(city.getId(), city);
		}
		return cityregionMap;
	}

	private List<CityRegion> getAllCityRegion() {
		DetachedCriteria query = DetachedCriteria.forClass(CityRegion.class);
		query.add(Restrictions.eq("delstatus", "N"));
		List<CityRegion> cityregionList = baseDao.findByCriteria(query);
		return cityregionList;
	}

	@Override
	public List<CityRegion> findAllRegion() {
		return getAllCityRegion();
	}

}

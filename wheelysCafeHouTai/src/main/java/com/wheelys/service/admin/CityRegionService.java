package com.wheelys.service.admin;

import java.util.List;
import java.util.Map;

import com.wheelys.model.citymanage.CityRegion;

public interface CityRegionService {

	Map<Long,CityRegion>cityRegionMap();
	
	List<CityRegion> findAllRegion();
}

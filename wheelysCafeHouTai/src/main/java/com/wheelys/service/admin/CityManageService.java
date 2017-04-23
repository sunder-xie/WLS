package com.wheelys.service.admin;

import java.util.List;

import com.wheelys.model.citymanage.CityCode;
import com.wheelys.model.citymanage.CityManage;
import com.wheelys.web.action.admin.vo.CityManageVo;

public interface CityManageService {
	/**
	 * 添加城市
	 * 
	 * @param id
	 * @param region
	 */
	void addCity(Long id, String region, String citycode, String cityname);

	/**
	 * 删除城市
	 * 
	 * @param id
	 */
	void delCity(Long id,Integer sn);

	/**
	 * 查询城市
	 * 
	 * @return
	 */
	List<CityManageVo> showCityList();

	/**
	 * 根据id返回对象
	 * 
	 * @param id
	 * @return
	 */
	CityManage manage(Long id);

	/**
	 * 返回查到的对象
	 * 
	 * @param citycode
	 * @return
	 */
	List<CityCode> codeList(String citycode);

	/**
	 * 查找城市是否存在
	 * 
	 * @param id
	 * @return
	 */
	boolean ifCity(String citycode);

	/**
	 * 通过城市citycode匹配城市
	 * 
	 * @param citycode
	 * @return
	 */
	void saveManage(String newcitycode, String oldcitycode, Long shopid, Long companyid);
	/**
	 * 拿到关联店铺的名字
	 * @param id
	 * @return
	 */
	String shopnameList(Long id);

	List<CityManage> findCityManageByRegion(String region);
	/**
	 * 改变店铺状态
	 * @param id
	 */
	void changeStatus(Long id,String status);
}

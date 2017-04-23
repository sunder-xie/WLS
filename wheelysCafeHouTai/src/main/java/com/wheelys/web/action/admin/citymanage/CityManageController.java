package com.wheelys.web.action.admin.citymanage;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wheelys.util.ValidateUtil;
import com.wheelys.model.citymanage.CityCode;
import com.wheelys.model.citymanage.CityManage;
import com.wheelys.model.citymanage.CityRegion;
import com.wheelys.service.admin.CityManageService;
import com.wheelys.service.admin.CityRegionService;
import com.wheelys.web.action.AnnotationController;
import com.wheelys.web.action.admin.vo.CityManageVo;

@Controller
public class CityManageController extends AnnotationController {

	@Autowired
	@Qualifier("cityManageService")
	private CityManageService cityManageService;
	@Autowired
	@Qualifier("cityRegionService")
	private CityRegionService cityRegionService;

	@RequestMapping("/admin/setup/showcityList.xhtml")
	public String showruleList(ModelMap model) {
		List<CityManageVo> voList = cityManageService.showCityList();
		model.put("voList", voList);
		return "/admin/citymanage/cityManage.vm";
	}

	@RequestMapping("/admin/setup/showAddCity.xhtml")
	public String showaddCity(Long id, ModelMap model) {
		if (id != null) {
			CityManage manage = cityManageService.manage(id);
			model.put("manage", manage);
		}
		Map<Long, CityRegion> regionMap = cityRegionService.cityRegionMap();
		model.put("regionMap", regionMap);
		return "/admin/citymanage/addCity.vm";

	}

	@RequestMapping("/admin/setup/addCity.xhtml")
	public String addCity(Long id, String citycode, String region, String cityinfo, ModelMap model) {
		if (StringUtils.isBlank(citycode) && id == null) {
			return this.showJsonError(model, "城市不能为空");
		}
		if (StringUtils.isBlank(region)) {
			return this.showJsonError(model, "所属大区为空");
		}
		if (StringUtils.isBlank(cityinfo)) {
			return this.showJsonError(model, "请等待城市出现再提交");
		}
		if (StringUtils.equals(cityinfo, "未查询到匹配城市")) {
			return this.showJsonError(model, cityinfo);
		}
		if (!ValidateUtil.isNumber(citycode)) {
			citycode = cityinfo.substring(0, 6);
		}
		CityManage manage = cityManageService.manage(id);
		if (id == null || !StringUtils.equals(citycode, manage.getCitycode())) {
			boolean flag = cityManageService.ifCity(citycode);
			if (flag)
				return this.showJsonError(model, "城市名称已存在");
		}
		String cityname = null;
		if (id == null) {
			String info = cityinfo.substring(7);
			cityname = info.replace(",", "");
		}
		cityManageService.addCity(id, region, citycode, cityname);
		return this.showJsonSuccess(model, null);
	}

	@RequestMapping("/admin/setup/findCity.xhtml")
	@ResponseBody
	public String findCity(String citycode) {
		String result = "";
		if (StringUtils.isBlank(citycode)) {
			result += "城市不能为空";
			return result;
		}
		List<CityCode> showCityList = cityManageService.codeList(citycode);
		if (showCityList.isEmpty()) {
			result += "未查询到匹配城市";
		} else {
			for (CityCode city : showCityList) {
				result += city.getCitycode() + ":" + city.getName() + ",";
			}
		}
		return result;
	}

	@RequestMapping("/admin/setup/delCity.xhtml")
	public String delCity(Long id, Integer sn, ModelMap model) {
		cityManageService.delCity(id, sn);
		return this.showJsonSuccess(model, null);
	}

	@RequestMapping("/admin/setup/showshopname.xhtml")
	public String showshopname(Long id, ModelMap model) {
		String names = cityManageService.shopnameList(id);
		if (StringUtils.isBlank(names)) {
			return this.showJsonSuccess(model, null);
		} else {
			return this.showJsonError(model, names);
		}
	}

	@RequestMapping("/admin/setup/changestatus.xhtml")
	public String changestatus(Long id, String status, ModelMap model) {
		cityManageService.changeStatus(id, status);
		return this.showJsonSuccess(model, status);
	}
}

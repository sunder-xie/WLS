package com.wheelys.web.action.admin.mchrule;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.constant.CityContant;
import com.wheelys.model.merchant.Mchrules;
import com.wheelys.service.admin.MchProductService;
import com.wheelys.service.admin.MchrulesService;
import com.wheelys.util.VmUtils;
import com.wheelys.web.action.AnnotationController;

@Controller
public class MchRuleController extends AnnotationController {
	@Autowired
	@Qualifier("mchrulesService")
	private MchrulesService mchrulesService;
	@Autowired
	@Qualifier("mchProductService")
	private MchProductService mchProductService;
	
	@RequestMapping("/admin/order/showruleList.xhtml")
	public String showruleList(Long supplierid, ModelMap model) {
		if (supplierid == null) {
			supplierid = (long) 1;
		}
		model.put("supplierid", supplierid);
		List<Mchrules> rulesList = mchrulesService.ruleList(supplierid);
		model.put("rulesList", rulesList);
		Map<String, String> itMap = CityContant.CITYMAP;
		model.put("itMap", itMap);
		Mchrules rules  = mchrulesService.findRule(supplierid);
		model.put("rules", rules);
		return "/admin/mchrule/mchRuleList.vm";
	}

	@RequestMapping("/admin/order/showadd.xhtml")
	public String showadd(Long id, Long supplierid, ModelMap model) {
		model.put("supplierid", supplierid);
		Map<String, String> itMap = CityContant.CITYMAP;
		model.put("itMap", itMap);
		Mchrules rules = mchrulesService.rules(id);
		model.put("rules", rules);
		return "/admin/mchrule/addRule.vm";
	}

	@RequestMapping("/admin/order/xiangadd.xhtml")
	public String xiangadd(Long id, Long supplierid, ModelMap model) {
		model.put("supplierid", supplierid);
		Map<String, String> itMap = CityContant.CITYMAP;
		model.put("itMap", itMap);
		Mchrules rules = mchrulesService.rules(id);
		model.put("rules", rules);
		return "/admin/mchrule/xiangRule.vm";
	}

	@RequestMapping("/admin/order/delrule.xhtml")
	public String delrule(Long id, ModelMap model) {
		mchrulesService.delsrules(id);
		return this.showJsonError(model, null);
	}

	@RequestMapping("/admin/order/addrule.xhtml")
	public String addrule(Long id, String citycode, Long supplierid, Float morefreight, Float morecost,
			Float lessfreight, Float lesscost, ModelMap model) {
		if (StringUtils.isBlank(citycode)) {
			return this.showJsonError(model, "城市不能为空");
		}
		if (lessfreight == null && supplierid == 1&&!StringUtils.equals(citycode, CityContant.CITYCODE_SH)) {
			return this.showJsonError(model, "小于200kg时的运费不能为空");
		}
		if (lesscost == null && supplierid == 1 &&!StringUtils.equals(citycode, CityContant.CITYCODE_SH)) {
			return this.showJsonError(model, "小于200kg时的配送费不能为空");
		}
		if (morefreight == null && !StringUtils.equals(citycode, CityContant.CITYCODE_SH)) {
			
			return this.showJsonError(model, "运费不能为空");
		}
		if (morecost == null && !StringUtils.equals(citycode, CityContant.CITYCODE_SH)) {
			return this.showJsonError(model, "配送费不能为空");
		}
		if (lessfreight == null && supplierid == 2) {
			lessfreight = (float) 0;
		}
		if (lesscost == null && supplierid == 2) {
			lesscost = (float) 0;
		}
		if(lessfreight == null && StringUtils.equals(citycode, CityContant.CITYCODE_SH)&&supplierid==1){
			return this.showJsonError(model, "运费不能为空");
		}
		if(lesscost == null && StringUtils.equals(citycode, CityContant.CITYCODE_SH)&&supplierid==1){
			return this.showJsonError(model, "订单额不能为空");
		}
		if (supplierid == 1&&StringUtils.equals(citycode, CityContant.CITYCODE_SH)) {
			morecost = (float) 0;
		}
		if (supplierid == 1&&StringUtils.equals(citycode, CityContant.CITYCODE_SH)) {
			morefreight = (float) 0;
		}
		Mchrules rules = mchrulesService.rules(id);
		if (id == null||!StringUtils.equals(rules.getCitycode(), citycode)) {
			boolean flag = mchrulesService.citycode(citycode, id,supplierid);
			if (flag)
				return this.showJsonError(model, "该城市已有运营规则");
		}
		mchrulesService.add(id, citycode, supplierid, morefreight, morecost, lessfreight, lesscost);
		return this.showJsonSuccess(model, null);
	}

	@RequestMapping("/admin/order/shrule.xhtml")
	public String addrule(Long id, String citycode, Long supplierid, Float lessfreight, Float lesscost,
			ModelMap model) {
		Float morefreight = (float) 0;
		Float morecost = (float) 0;
		if (lesscost == null) {
			return this.showJsonError(model, "订单额为空");
		}
		if (lessfreight == null) {
			return this.showJsonError(model, "运费为空");
		}

		Mchrules rules = mchrulesService.rules(id);
		if (rules != null) {
			double dlessfreight = Double.parseDouble(String.valueOf(lessfreight));
			double dlesscost = Double.parseDouble(String.valueOf(lesscost));
			if (id == null || VmUtils.getDoubleAmount(rules.getLesscost()) == dlesscost && VmUtils.getDoubleAmount(rules.getLessfreight()) == dlessfreight) {
				boolean flag = mchrulesService.citycode(citycode, id, supplierid);
				if (flag)
					return this.showJsonError(model, "该城市已有运营规则");
			}
		}
		mchrulesService.add(id, citycode, supplierid, morefreight, morecost, lessfreight, lesscost);
		return this.showJsonSuccess(model, null);
	}
}

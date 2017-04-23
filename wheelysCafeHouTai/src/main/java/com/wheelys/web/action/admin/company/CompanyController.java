package com.wheelys.web.action.admin.company;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.util.ValidateUtil;
import com.wheelys.web.util.PageUtil;
import com.wheelys.constant.CityContant;
import com.wheelys.model.company.Company;
import com.wheelys.service.admin.CompanyService;
import com.wheelys.util.ValidateChildrenUtils;
import com.wheelys.web.action.AnnotationController;
import com.wheelys.web.action.admin.vo.CompanyVo;

@Controller
public class CompanyController extends AnnotationController {
	@Autowired
	@Qualifier("companyService")
	private CompanyService companyService;

	@RequestMapping("/admin/shop/companyList.xhtml")
	public String showOperatorList(String tradeno, String operatorname, String citycode, Integer pageNo,
			ModelMap model) {
		if (pageNo == null)
			pageNo = 0;
		int rowsPerPage = 10;
		List<CompanyVo> showList = companyService.showList(tradeno, operatorname, citycode, pageNo, rowsPerPage);
		int count = companyService.findCompanyCount(tradeno, operatorname, citycode);
		PageUtil pageUtil = new PageUtil(count, rowsPerPage, pageNo, "admin/shop/companyList.xhtml");
		Map params = new HashMap();
		params.put("tradeno", tradeno);
		params.put("operatorname", operatorname);
		params.put("citycode", citycode);
		pageUtil.initPageInfo(params);
		model.put("pageUtil", pageUtil);
		Map<String, String> itMap = CityContant.CITYMAP;
		model.put("itMap", itMap);
		model.put("showList", showList);
		return "/admin/company/companyManageList.vm";
	}

	@RequestMapping("/admin/shop/showAdd.xhtml")
	public String showAdd(Long operatorid, ModelMap model) {
		Map<String, String> itMap = CityContant.CITYMAP;
		model.put("itMap", itMap);
		Company operator = companyService.findCompany(operatorid);
		model.put("operator", operator);
		return "/admin/company/addcompany.vm";
	}

	@RequestMapping("/admin/shop/delstatus.xhtml")
	public String delstatus(Long operatorid, ModelMap model) {
		companyService.delstatus(operatorid);
		return this.showJsonSuccess(model, null);
	}

	@RequestMapping("/admin/shop/addOperator.xhtml")
	public String addOperator(Long id, String citycode, String name, String contants, String telephone, String password,
			String account, ModelMap model) {
		if (StringUtils.isBlank(citycode)) {
			return this.showJsonError(model, "城市为空！");
		}
		if (StringUtils.isBlank(name)) {
			return this.showJsonError(model, "运营商名字为空！");
		}
		if (StringUtils.isBlank(contants)) {
			return this.showJsonError(model, "联系人为空！");
		}
		if (StringUtils.isBlank(telephone)) {
			return this.showJsonError(model, "手机为空！");
		}
		if (!ValidateUtil.isMobile(telephone)) {
			return this.showJsonError(model, "手机号码不正确！");
		}
		if (StringUtils.isBlank(account)) {
			return this.showJsonError(model, "账号不能为空");
		}
		if (!ValidateChildrenUtils.isEnglistAndNumber(account)) {
			return this.showJsonError(model, "账号有误,只允许英文和数字还有-");
		}
		if (id == null && StringUtils.isNotBlank(password)) {
			if (!ValidateUtil.isPassword(password)) {
				return this.showJsonError(model, "密码格式不正确,只能是字母，数字，英文标点，长度6—14位且不能为空！");
			}
		}
		Company company = companyService.findCompany(id);
		if (id == null || !StringUtils.equals(company.getAccount(), account)) {
			boolean flag = companyService.companyuser(account, id);
			if (flag)
				return this.showJsonError(model, "运营商账号已存在");
		}
		companyService.addCompany(id, citycode, name, contants, telephone, password, account);

		return this.showJsonSuccess(model, null);
	}
}

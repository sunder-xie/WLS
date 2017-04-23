package com.wheelys.web.action.admin.company;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.wheelys.web.util.PageUtil;
import com.wheelys.model.company.CompanyActivity;
import com.wheelys.service.admin.CompanyActityService;
import com.wheelys.web.action.AnnotationController;


@Controller
public class CompanyActityController extends AnnotationController {
	@Autowired
	@Qualifier("operatorActityService")
	private CompanyActityService operatorActityService;

	@RequestMapping("/admin/shop/showOperatorList.xhtml")
	public String showOperatorList(Integer pageNo, ModelMap model) {
		if (pageNo == null)
			pageNo = 0;
		int rowsPerPage = 10;
		int count = operatorActityService.findListCount();
		PageUtil pageUtil = new PageUtil(count, rowsPerPage, pageNo, "admin/shop/showOperatorList.xhtml");
		Map params = new HashMap();
		pageUtil.initPageInfo(params);
		List<CompanyActivity> showList = operatorActityService.showList(pageNo, rowsPerPage);
		model.put("pageUtil", pageUtil);
		model.put("showList", showList);
		return "/admin/company/companyList.vm";
	}

	@RequestMapping("/admin/shop/updateOperator.xhtml")
	public String updateOperator(Long id, String operatorstatus, ModelMap model) {
		operatorActityService.operator(id, operatorstatus);
		return this.showJsonSuccess(model, null);
	}

	@RequestMapping("/admin/shop/operatorDetails.xhtml")
	public String operatorDetails(Long id, ModelMap model) {
		CompanyActivity operatctity = operatorActityService.operator(id, null);
		model.put("operatctity", operatctity);
		return "/admin/company/companyDetail.vm";
	}

	@RequestMapping("/admin/shop/actityDetails.xhtml")
	public String actityDetails() {
		return "/admin/company/companyactityDetail.vm";
	}

	@RequestMapping("/admin/shop/updateactity.xhtml")
	public String updateactity(String actityinfo, ModelMap model) {
		if (StringUtils.isBlank(actityinfo)) {
			return this.showJsonError(model, "活动说明不能为空");
		}
		operatorActityService.actity(actityinfo);
		return this.showJsonSuccess(model, null);
	}
}

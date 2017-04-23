package com.wheelys.web.action.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.wheelys.util.ValidateUtil;
import com.wheelys.model.CafeRecruit;
import com.wheelys.model.Pcemail;
import com.wheelys.service.admin.PcService;
import com.wheelys.util.ValidateChildrenUtils;
import com.wheelys.web.action.AnnotationController;

@Controller
public class PcController extends AnnotationController {

	@Autowired
	@Qualifier("pcService")
	private PcService pcService;

	// PC 首页
	@RequestMapping("/pc/pcIndex.xhtml")
	public String index() {
		return "/pc/zindex.vm";

	}

	// PC 展示报名表
	@RequestMapping("/pc/showSignupForm.xhtml")
	public String showSignupForm() {
		return "/pc/signupForm.vm";

	}

	// PC 报名表
	@RequestMapping("/pc/addSignupForm.xhtml")
	public String addSignupForm(String name, String sex, String idcard, String phone, String wxid, String email,
			String company, String address, String companyphone, String values1, String values2, String values3,
			String values4, String values5, String values6, String values7, String values8, String values9,
			ModelMap model) {
		if (StringUtils.isBlank(name)) {
			return this.showJsonError(model, "姓名不能为空");
		}
		if (StringUtils.isBlank(sex)) {
			return this.showJsonError(model, "性别不能为空");
		}
		if (StringUtils.isBlank(idcard)) {
			return this.showJsonError(model, "身份证不能为空");
		}
		if (StringUtils.isBlank(phone)) {
			return this.showJsonError(model, "电话不能为空");
		}
		if (StringUtils.isBlank(wxid)) {
			return this.showJsonError(model, "微信不能为空");
		}
		if (StringUtils.isBlank(email)) {
			return this.showJsonError(model, "邮箱不能为空");
		}
		if (StringUtils.isBlank(company)) {
			return this.showJsonError(model, "公司名字不能为空");
		}
		if (StringUtils.isBlank(address)) {
			return this.showJsonError(model, "公司地址不能为空");
		}
		if (StringUtils.isBlank(companyphone)) {
			return this.showJsonError(model, "公司电话不能为空");
		}
		if (StringUtils.isBlank(values1)) {
			return this.showJsonError(model, "第一意向不能为空");
		}
		if (StringUtils.isBlank(values4)) {
			return this.showJsonError(model, "关键资源、关键能力、竞争优势框不能为空");
		}
		if (StringUtils.isBlank(values5)) {
			return this.showJsonError(model, "未来发展框不能为空");
		}
		if (StringUtils.isBlank(values6)) {
			return this.showJsonError(model, "困难和障碍框不能为空");
		}
		if (StringUtils.isBlank(values7)) {
			return this.showJsonError(model, "支持和帮助框不能为空");
		}
		if (StringUtils.isBlank(values8)) {
			return this.showJsonError(model, "请完善产业基金信息");
		}
		if (StringUtils.isBlank(values9)) {
			return this.showJsonError(model, "请完善产业基金信息");
		}
		if (!ValidateUtil.isIDCard(idcard)) {
			return this.showJsonError(model, "身份证有误");
		}
		if (!ValidateUtil.isMobile(phone)) {
			return this.showJsonError(model, "手机号码有误");
		}
		if (!ValidateUtil.isEmail(email)) {
			return this.showJsonError(model, "邮箱有误");
		}
		if (!ValidateChildrenUtils.isfixedTelephone(companyphone)) {
			return this.showJsonError(model, "公司电话有误，最少7位最多14位");
		}

		if (ValidateChildrenUtils.isLength(values1) > 2000) {
			return this.showJsonError(model, "第一意向信息超过2000字限制");
		}

		if (!StringUtils.isBlank(values2)) {
			if (ValidateChildrenUtils.isLength(values2) > 2000) {
				return this.showJsonError(model, "第二意向信息超过2000字限制");
			}
		}

		if (!StringUtils.isBlank(values3)) {
			if (ValidateChildrenUtils.isLength(values3) > 2000) {
				return this.showJsonError(model, "第三意向信息超过2000字限制");
			}
		}

		if (ValidateChildrenUtils.isLength(values4) > 2000) {
			return this.showJsonError(model, "关键资源、关键能力、竞争优势信息超过2000字限制");
		}
		if (ValidateChildrenUtils.isLength(values5) > 2000) {
			return this.showJsonError(model, "未来发展信息超过2000字限制");
		}
		if (ValidateChildrenUtils.isLength(values6) > 2000) {
			return this.showJsonError(model, "困难和障碍信息超过2000字限制");
		}
		if (ValidateChildrenUtils.isLength(values7) > 2000) {
			return this.showJsonError(model, "支持和帮助信息超过2000字限制");
		}
		if (ValidateChildrenUtils.isLength(values8) > 2000) {
			return this.showJsonError(model, "第一个产业基金信息超过2000字限制");
		}
		if (ValidateChildrenUtils.isLength(values9) > 2000) {
			return this.showJsonError(model, "第二个产业基金信息超过2000字限制");
		}

		CafeRecruit cafeRecruit = pcService.cafeRedcruit(name, sex, idcard, phone, wxid, email, company, address,
				companyphone, values1, values2, values3, values4, values5, values6, values7, values8, values9);
		return this.showJsonSuccess(model, cafeRecruit);

	}

	// PC 首页邮箱
	@RequestMapping("/pc/addEmail.xhtml")
	public String addEmail(String email, ModelMap model) {
		if (!ValidateUtil.isEmail(email)) {
			return this.showJsonError(model, "邮箱有误");
		}
		Pcemail pcemail = pcService.email(email);
		return this.showJsonSuccess(model, pcemail);
	}

	// PC 智慧范
	@RequestMapping("/pc/wisdom.xhtml")
	public String wisdom() {
		return "/pc/wisdom.vm";
	}

	// PC 科技范
	@RequestMapping("/pc/technology.xhtml")
	public String technology() {
		return "/pc/technology.vm";
	}

	// PC 品质范
	@RequestMapping("/pc/worth.xhtml")
	public String worth() {
		return "/pc/worth.vm";
	}

	// PC 创业团队
	@RequestMapping("/pc/team.xhtml")
	public String team() {
		return "/pc/team.vm";
	}

	// PC 加入我们
	@RequestMapping("/pc/partner.xhtml")
	public String partner() {
		return "/pc/partner.vm";
	}

	// PC 信息页
	@RequestMapping("/pc/info.xhtml")
	public String info() {
		return "/pc/info.vm";
	}

}

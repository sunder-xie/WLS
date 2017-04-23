package com.wheelys.web.action.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.model.acl.User;
import com.wheelys.util.JsonUtils;
import com.wheelys.web.util.PageUtil;
import com.wheelys.constant.BannerConstant;
import com.wheelys.model.banner.WheelysBanner;
import com.wheelys.service.admin.BannerService;
import com.wheelys.util.WebUtils;
import com.wheelys.web.action.AnnotationController;

@Controller
public class BannerController extends AnnotationController {

	@Autowired
	@Qualifier("bannerService")
	private BannerService bannerService;

	@RequestMapping("/admin/shop/shopBannerList.xhtml")
	private String shopBannerList(ModelMap model, Long shopid, Integer pageNo) {
		if (pageNo == null)
			pageNo = 0;
		int rowsPerPage = 10;
		List<WheelysBanner> bannerList = bannerService.getShopBannerList(shopid, pageNo, rowsPerPage);
		model.put("bannerList", bannerList);
		return "/admin/banner/shopBannerList.vm";
	}

	@RequestMapping("/admin/shop/showShopBannerDetail.xhtml")
	public String showShopBannerDetail(Long id, Long shopid, ModelMap model) {
		WheelysBanner banner = bannerService.getBanner(id);
		model.put("banner", banner);
		if (shopid == null) {
			shopid = banner.getShopid();
		}
		model.put("shopid", shopid);
		return "/admin/banner/shopBannerDetail.vm";
	}

	@RequestMapping("/admin/content/bannerList.xhtml")
	private String bannerList(ModelMap model, String type, Integer pageNo) {
		if (pageNo == null)
			pageNo = 0;
		int rowsPerPage = 5;
		List<WheelysBanner> bannerList = bannerService.getBannerList(pageNo, type, rowsPerPage);
		int count = bannerService.findBannerCount(type);
		PageUtil pageUtil = new PageUtil(count, rowsPerPage, pageNo, "admin/content/bannerList.xhtml");
		Map params = new HashMap();
		params.put("type", type);
		pageUtil.initPageInfo(params);
		model.put("pageUtil", pageUtil);
		Map<String, String> itMap = BannerConstant.TYPEMAP;
		model.put("type", type);
		model.put("itMap", itMap);
		model.put("bannerList", bannerList);
		return "/admin/banner/bannerList.vm";
	}

	@RequestMapping("/admin/shop/updateShowstatus.xhtml")
	private String updateShowstatus(Long id, String showstatus, ModelMap model) {
		WheelysBanner banner = bannerService.updateShowstatus(id, null, showstatus);
		if (StringUtils.equals(banner.getType(), BannerConstant.TYPE_SHOP_INDEX)) {
			model.put("shopid", banner.getShopid());
			return "redirect:/admin/shop/shopBannerList.xhtml";
		}
		model.put("type", banner.getType());
		return "redirect:/admin/content/bannerList.xhtml";
	}

	@RequestMapping("/admin/shop/deleteBanner.xhtml")
	private String deleteWechatBanner(Long id, ModelMap model) {
		WheelysBanner banner = bannerService.deleteWechatBanner(id);
		if (StringUtils.equals(banner.getType(), BannerConstant.TYPE_SHOP_INDEX)) {
			model.put("shopid", banner.getShopid());
			return "redirect:/admin/shop/shopBannerList.xhtml";
		}
		model.put("type", banner.getType());
		return "redirect:/admin/content/bannerList.xhtml";
	}

	@RequestMapping("/admin/content/showBannerDetail.xhtml")
	public String showBannerDetail(Long id, ModelMap model) {
		WheelysBanner banner = bannerService.getBanner(id);
		Map<String, String> itMap = BannerConstant.TYPEMAP;
		model.put("itMap", itMap);
		model.put("banner", banner);
		return "/admin/banner/bannerDetail.vm";
	}

	@RequestMapping("/admin/shop/addUpdateBanner.xhtml")
	public String editNews(Long id, String title, HttpServletRequest request, Long shopid, String tourl, String imgurl,
			String type, ModelMap model) {
		Map<String, String> params = WebUtils.getRequestMap(request);
		User user = getLogonMember();
		params.put("username", user.getUsername());
		dbLogger.warn("params:" + JsonUtils.writeMapToJson(params));
		if (StringUtils.isBlank(title)) {
			return this.showJsonError(model, "标题不能为空");
		}
		if (StringUtils.isBlank(type)) {
			return this.showJsonError(model, "分类不能为空");
		}
		if (id == null && StringUtils.isBlank(imgurl)) {
			return this.showJsonError(model, "图片不能为空");
		}
		WheelysBanner banner = bannerService.saveBanner(id, shopid, title, tourl, imgurl, type);
		return this.showJsonSuccess(model, banner);
	}

	/**
	 * 排序
	 * 
	 * @param model
	 * 
	 */
	@RequestMapping("/admin/shop/sortBanner.xhtml")
	public String sortBanner(Long id, Integer sn, ModelMap model) {
		bannerService.updateShowstatus(id, sn, null);
		return this.showJsonSuccess(model, null);
	}
}

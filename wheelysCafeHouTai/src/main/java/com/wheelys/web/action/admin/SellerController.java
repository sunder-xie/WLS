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
import com.wheelys.util.ValidateUtil;
import com.wheelys.web.util.PageUtil;
import com.wheelys.model.CafeShop;
import com.wheelys.model.ShopSeller;
import com.wheelys.service.admin.SellerSerivce;
import com.wheelys.util.ValidateChildrenUtils;
import com.wheelys.util.WebUtils;
import com.wheelys.web.action.AnnotationController;

@Controller
public class SellerController extends AnnotationController {
	@Autowired
	@Qualifier("sellerService")
	private SellerSerivce sellService;

	@RequestMapping("/admin/shop/showSellerList.xhtml")
	public String showSellerList(Long shopid,Integer pageNo, ModelMap model) {
		if (pageNo == null)
			pageNo = 0;
		int rowsPerPage = 10;
		int count =sellService.findShopSellerCount(shopid);
		List<ShopSeller> sellerList = sellService.showSellerList(shopid, pageNo, rowsPerPage);
		CafeShop cafeShop = sellService.cafeShop(shopid);
		PageUtil pageUtil = new PageUtil(count, rowsPerPage, pageNo, "admin/shop/showSellerList.xhtml");
		Map params = new HashMap();
		params.put("shopid", shopid);
		pageUtil.initPageInfo(params);
		model.put("pageUtil", pageUtil);
		model.put("cafeShop", cafeShop);
		model.put("sellerList", sellerList);
		return "/admin/seller/showSellerList.vm";
	}


	/**
	 * 员工修改新增页面展示
	 * 
	 * @return
	 */
	@RequestMapping("/admin/shop/showAddSeller.xhtml")
	public String showAddSeller(Long id,Long shopid,ModelMap model) {
		ShopSeller seller = sellService.seller(id);
		model.put("seller", seller);
		CafeShop cafeShop = sellService.cafeShop(shopid);
		model.put("cafeShop", cafeShop);
		return "/admin/seller/addUpdateSeller.vm";
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @param nickname
	 * @param mobile
	 * @return新增修改
	 */
	@RequestMapping("/admin/shop/addUpdateSeller.xhtml")
	public String addUpdateStaff(Long id, Long shopid, HttpServletRequest request,String username,String loginname,String password, String mobile,String role,
			ModelMap model) {
		Map<String, String> params = WebUtils.getRequestMap(request);
		User user = getLogonMember();
		params.put("username", user.getUsername());
		dbLogger.warn("params:"+JsonUtils.writeMapToJson(params));
		loginname = StringUtils.trim(loginname);
		if (StringUtils.isBlank(loginname)) {
			return this.showJsonError(model, "账号不能为空");
		}
		ShopSeller seller = sellService.seller(id);
		if (id == null || (seller != null && StringUtils.isNotBlank(password))) {
			if (!ValidateUtil.isPassword(password)) {
				return this.showJsonError(model, "密码格式不正确,只能是字母，数字，英文标点，长度6—14位且不能为空！");
			}
		}
		if(StringUtils.isBlank(role)){
			return this.showJsonError(model, "角色不能为空");
		}
		if(!ValidateChildrenUtils.isEnglistAndNumber(loginname)){
			return this.showJsonError(model, "账号有误,只允许英文和数字还有-");
		}
		if (seller == null) {
			seller = new ShopSeller();
		}
		
		if (!StringUtils.equals(seller.getLoginname(), loginname) || id == null) {
			boolean flag = sellService.ShopSeller(shopid,loginname);
		if(flag)
			return this.showJsonError(model, "账号已存在");
		}
		ShopSeller shopSeller = sellService.upAddShopSeller(id,shopid, username, loginname, password, role,mobile);
		return this.showJsonSuccess(model, shopSeller);

	}

	/**
	 * 
	 * @param id
	 * @return删除
	 */
	@RequestMapping("/admin/seller/delSeller.xhtml")
	public String delStaff(Long id, ModelMap model) {
		if (id != null) {
			sellService.delShopSeller(id);
		}
		return this.showJsonSuccess(model, null);
	}


}
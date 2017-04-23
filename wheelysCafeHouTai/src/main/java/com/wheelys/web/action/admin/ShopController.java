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
import com.wheelys.constant.CityContant;
import com.wheelys.model.CafeItem;
import com.wheelys.model.CafeRecruit;
import com.wheelys.model.CafeShop;
import com.wheelys.model.CafeShopProfile;
import com.wheelys.model.ShopExpressAddress;
import com.wheelys.model.ShopSeller;
import com.wheelys.model.banner.WheelysBanner;
import com.wheelys.model.company.Company;
import com.wheelys.service.admin.CityManageService;
import com.wheelys.service.admin.CompanyService;
import com.wheelys.service.admin.ProductService;
import com.wheelys.service.admin.SellerSerivce;
import com.wheelys.service.admin.ShopService;
import com.wheelys.service.admin.UserService;
import com.wheelys.untrans.OSSUploadFileService;
import com.wheelys.util.WebUtils;
import com.wheelys.web.action.AnnotationController;
import com.wheelys.web.action.admin.vo.CafeShopVo;
import com.wheelys.web.action.admin.vo.ProductShopVo;

@Controller
public class ShopController extends AnnotationController {

	@Autowired
	@Qualifier("shopService")
	private ShopService shopService;
	@Autowired
	@Qualifier("ossUploadFileService")
	private OSSUploadFileService ossUploadFileService;
	@Autowired
	@Qualifier("sellerService")
	private SellerSerivce sellService;
	@Autowired
	@Qualifier("productService")
	private ProductService productService;
	@Autowired
	@Qualifier("companyService")
	private CompanyService companyService;
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	@Autowired
	@Qualifier("cityManageService")
	private CityManageService cityManageService;

	/**
	 * 店铺管理列表
	 * 
	 *            店铺状态
	 * @param name
	 *            店铺名称
	 * @param operatorName
	 *            运营商名称
	 * @param cityCode
	 * @param pageNo
	 * @param model
	 * @return
	 */
	@RequestMapping("/admin/shop/store.xhtml")
	public String store(String name, Long operatorid, String citycode, Integer pageNo,
			ModelMap model) {
		if (pageNo == null)
			pageNo = 0;
		int rowsPerPage = 10;
		List<CafeShopVo> shopList = shopService.findlist(name, operatorid, citycode, pageNo, rowsPerPage);
		model.put("shopList", shopList);
		int count = shopService.findListCount(name, operatorid, citycode);
		Map<Long, Company> operatorMap = companyService.mchProductItemMap();
		model.put("operatorMap", operatorMap);
		PageUtil pageUtil = new PageUtil(count, rowsPerPage, pageNo, "admin/shop/store.xhtml");
		Map params = new HashMap();
		params.put("name", name);
		params.put("operatorid", operatorid);
		params.put("citycode", citycode);
		pageUtil.initPageInfo(params);
		model.put("pageUtil", pageUtil);
		Map<String, String> itMap = CityContant.CITYMAP;
		model.put("itMap", itMap);
		User user2 = getLogonMember();
		List<String> wbList = userService.webList(user2.getId());
		model.put("wbList", wbList);
		return "/admin/business/shop/store.vm";
	}

	/**
	 * 修改和新增商家
	 * 
	 */
	@RequestMapping("/admin/shop/updateCafeShop.xhtml")
	public String updateCafeShop(Long shopid, String citycode, String contacts, String booking, String shopname,
			String esname, String shopaddress, String shoptelephone, String shoplon, String shoplat, String time1,
			String time2, String imgurl, Integer shopstatus, Long supplierid, Double shopproportion, Long companyid,
			HttpServletRequest request, ModelMap model) {
		Map<String, String> params = WebUtils.getRequestMap(request);
		User user = getLogonMember();
		params.put("username", user.getUsername());
		dbLogger.warn("params:" + JsonUtils.writeMapToJson(params));

		if (StringUtils.isBlank(citycode)) {
			return this.showJsonError(model, "城市为空！");
		}
		if (supplierid == null) {
			return this.showJsonError(model, "供应商为空！");
		}
		shopname = StringUtils.trim(shopname);
		if (StringUtils.isBlank(shopname)) {
			return this.showJsonError(model, "商家名称为空！");
		}
		if (StringUtils.isBlank(shopaddress)) {
			return this.showJsonError(model, "地址为空！");
		}
		if (StringUtils.isBlank(shoptelephone)) {
			return this.showJsonError(model, "电话号码为空！");
		}
		if (StringUtils.isBlank(shoplon)) {
			return this.showJsonError(model, "经度为空！");
		}
		if (StringUtils.isBlank(shoplat)) {
			return this.showJsonError(model, "纬度为空！");
		}
		if (StringUtils.isBlank(contacts)) {
			return this.showJsonError(model, "联系人为空！");
		}
		if (shopid == null && StringUtils.isBlank(imgurl)) {
			return this.showJsonError(model, "图片不能为空");
		}
		if (shopproportion == null) {
			return this.showJsonError(model, "增长比例不能为空！");
		}
		if (shopproportion < 0 || shopproportion > 100) {
			return this.showJsonError(model, "数字只能大于0小于100！");
		}
		if (companyid == null) {
			return this.showJsonError(model, "运营商不能为空");
		}
		CafeShop upCafeShop = shopService.findshop(shopid);
		if (upCafeShop == null) {
			upCafeShop = new CafeShop(shopname);
		}
		if (!StringUtils.equals(upCafeShop.getShopname(), shopname) || shopid == null) {
			boolean flag = shopService.shopuser(shopname, shopid);
			if (flag)
				return this.showJsonError(model, "商家名称已存在");
		}
		if (!ValidateUtil.isMobile(shoptelephone)) {
			return this.showJsonError(model, "手机号码不正确！");
		}
		if (shopid != null) {
			cityManageService.saveManage(citycode, upCafeShop.getCitycode(), shopid, companyid);
		}
		upCafeShop.setCitycode(citycode);
		upCafeShop.setContacts(contacts);
		upCafeShop.setBooking(booking);
		upCafeShop.setShopstatus(shopstatus);
		upCafeShop.setShopname(shopname);
		upCafeShop.setShopaddress(shopaddress);
		upCafeShop.setShoptelephone(shoptelephone);
		upCafeShop.setShoplon(shoplon);
		upCafeShop.setShoplat(shoplat);
		upCafeShop.setShoptime(time1 + "-" + time2);
		upCafeShop.setShopimg(imgurl);
		upCafeShop.setSupplierid(supplierid);
		upCafeShop.setShopproportion(shopproportion);
		upCafeShop.setEsname(esname);
		if (companyid != upCafeShop.getOperatorid()) {
			companyService.removeCompany(shopid, companyid);
		}

		upCafeShop.setOperatorid(companyid);
		shopService.saveCafeShop(upCafeShop);

		if (shopid == null) {
			String account = StringUtils.leftPad("" + upCafeShop.getShopid(), 5, '0');
			upCafeShop.setShopaccount(account);
			shopService.saveCafeShop(upCafeShop);
			companyService.saveNumber(upCafeShop.getShopid(), companyid);

		}
		if (shopid == null) {
			cityManageService.saveManage(citycode, upCafeShop.getCitycode(), upCafeShop.getShopid(), companyid);
		}
		return this.showJsonSuccess(model, upCafeShop);
	}

	public static void main(String[] args) {
		Integer shopstatus = 1;
		if (StringUtils.equals(shopstatus + "", "1")) {
			System.out.println("open");
		} else if (StringUtils.equals(shopstatus + "", "3")) {
			System.out.println("close");
		}
	}

	/**
	 * 
	 * @param shopid
	 * @param model
	 * @return先修改前进入修改页面并处理时间
	 */
	@RequestMapping("/admin/shop/upCafeShop.xhtml")
	public String upCafeShop(Long shopid, ModelMap model) {
		if (shopid == null) {
			Map<String, String> itMap = CityContant.CITYMAP;
			model.put("itMap", itMap);

		} else {
			CafeShop upCafeShop = shopService.findCafeShop(shopid);
			String str = upCafeShop.getShoptime();
			String[] strs = str.split("[-,]");
			String time1 = strs[0];
			String time2 = strs[1];
			Map<String, String> itMap = CityContant.CITYMAP;
			model.put("itMap", itMap);
			model.put("upCafeShop", upCafeShop);
			model.put("time1", time1);
			model.put("time2", time2);
		}
		Map<Long, Company> operatorMap = companyService.mchProductItemMap();
		model.put("operatorMap", operatorMap);
		return "/admin/business/shop/shopupdate.vm";
	}

	/**
	 * 
	 * @param 删除店铺
	 * @return
	 */
	@RequestMapping("/admin/shop/shopdelete.xhtml")
	public String shopdelete(Long shopid, ModelMap model) {
		shopService.delshop(shopid);
		return this.showJsonSuccess(model, null);
	}

	/**
	 * 判断用户是否存在
	 */
	@RequestMapping("/admin/shop/ifShopname.xhtml")
	public String ifShopname(String shopname, ModelMap model) {
		boolean ckShop = shopService.shopuser(shopname, null);
		if (ckShop == true) {
			return this.showJsonSuccess(model, "success");
		}
		return this.showJsonError(model, "error");
	}

	/**
	 * 显示商家外送信息
	 */
	@RequestMapping("/admin/shop/showExprInfo.xhtml")
	public String showExprInfo(Long shopid, ModelMap model) {
		CafeShopProfile shopProfile = shopService.findShopProfileByShopid(shopid);
		Map<String, String> date = JsonUtils.readJsonToMap(shopProfile.getTimeslot());
		CafeShop cafeShop = shopService.findshop(shopid);
		List<ShopExpressAddress> addressList = shopService.findExpressAddressByShopid(shopid);
		model.put("date", date);
		model.put("cafeShop", cafeShop);
		model.put("shopProfile", shopProfile);
		model.put("addressList", addressList);
		return "/admin/business/shopprofile/shopprofile.vm";
	}

	/**
	 * 保存或修改商家外送信息
	 * 
	 * @param shopid
	 * @param mobile
	 * @param takeawaystatus
	 * @param reservedstatus
	 * @return
	 */
	@RequestMapping("/admin/shop/configExprInfo.xhtml")
	public String configExprInfo(Long shopid, HttpServletRequest request, String mobile, String takeawaystatus,
			String reservedstatus, String time1, String time2, String time3, String time4, String time5, String time6,
			ModelMap model) {
		Map<String, String> params = WebUtils.getRequestMap(request);
		User user = getLogonMember();
		params.put("username", user.getUsername());
		dbLogger.warn("params:" + JsonUtils.writeMapToJson(params));
		Map<String, String> date = new HashMap<String, String>();
		date.put("time1", time1);
		date.put("time2", time2);
		date.put("time3", time3);
		date.put("time4", time4);
		date.put("time5", time5);
		date.put("time6", time6);
		String create = JsonUtils.writeMapToJson(date);
		if (StringUtils.isBlank(mobile)) {
			return this.showJsonError(model, "手机号码为空" + "！");
		}
		if (!ValidateUtil.isMobile(mobile)) {
			return this.showJsonError(model, "手机号码不正确！");
		}
		shopService.saveOrUpdateShopProfile(shopid, mobile, takeawaystatus, reservedstatus, create);
		return this.showJsonSuccess(model, null);
	}

	/**
	 * 删除一条外送地址
	 * 
	 * @param shopid
	 * @param address
	 * @return
	 */
	@RequestMapping("/admin/shop/deleteExprAddr.xhtml")
	public String deleteExprAddr(Long id, ModelMap model) {
		Boolean result = shopService.deleteExprAddrByAddr(id);
		if (result) {
			return this.showJsonSuccess(model, null);
		} else {
			return this.showJsonError(model, null);
		}
	}

	/**
	 * 添加一条外送信息
	 * 
	 * @param shopid
	 * @param address
	 * @return
	 */
	@RequestMapping("/admin/shop/addExprAddr.xhtml")
	public String addExprAddr(Long shopid, HttpServletRequest request, String address, ModelMap model) {
		Map<String, String> params = WebUtils.getRequestMap(request);
		User user = getLogonMember();
		params.put("username", user.getUsername());
		dbLogger.warn("params:" + JsonUtils.writeMapToJson(params));
		Long result = shopService.addExprAddr(shopid, address);
		if (result != null) {
			return this.showJsonSuccess(model, result);
		} else {
			return this.showJsonError(model, null);
		}
	}

	/**
	 * 员工页面展示
	 * 
	 * @param shopid
	 * @param address
	 * @return
	 */
	@RequestMapping("/admin/addEmployee.xhtml")
	public String addEmployee() {
		return "/admin/business/shopprofile/addEmployee.vm";
	}

	/**
	 * 运营商报名管理
	 * 
	 * @param shopid
	 * @param address
	 * @return
	 */
	@RequestMapping("/admin/shop/showSupplier.xhtml")
	public String showSupplier(String name, Integer pageNo, ModelMap model) {
		if (pageNo == null)
			pageNo = 0;
		int rowsPerPage = 10;
		List<CafeRecruit> recruitList = shopService.recruit(name, pageNo, rowsPerPage);
		int count = shopService.findCafeRecruitCount(name);
		PageUtil pageUtil = new PageUtil(count, rowsPerPage, pageNo, "admin/shop/showSupplier.xhtml");
		Map params = new HashMap();
		params.put("name", name);
		pageUtil.initPageInfo(params);
		model.put("pageUtil", pageUtil);
		model.put("recruitList", recruitList);
		return "/admin/business/supplier/showSupplier.vm";
	}

	/**
	 * 供应商详情
	 * 
	 * @param shopid
	 * @param address
	 * @return
	 */
	@RequestMapping("/admin/shop/recruitDetail.xhtml")
	public String detailSupplier(Long id, ModelMap model) {
		CafeRecruit cafeRecruit = shopService.caferecruit(id);
		model.put("cafeRecruit", cafeRecruit);
		return "/admin/business/supplier/deatilSupplier.vm";
	}

	/**
	 * 店铺详情展示
	 * 
	 * @param shopid
	 * @param address
	 * @return
	 */
	@RequestMapping("/admin/shop/showShopDetail.xhtml")
	public String showShopDetail(Long shopid, ModelMap model) {
		CafeShop showShop = shopService.findCafeShop(shopid);
		Map<String, String> itMap = CityContant.CITYMAP;
		List<ShopSeller> sellerList = sellService.showSellerList(shopid, 0, 0);
		List<WheelysBanner> showImgList = shopService.showBannerImgList(shopid);
		Map<CafeItem, List<ProductShopVo>> productMap = shopService.getShopCafeProductMap(shopid);
		CafeShopProfile shopProfile = shopService.findShopProfileByShopid(shopid);
		List<ShopExpressAddress> addressList = shopService.findExpressAddressByShopid(shopid);
		model.put("addressList", addressList);
		model.put("shopProfile", shopProfile);
		model.put("productMap", productMap);
		model.put("showImgList", showImgList);
		model.put("sellerList", sellerList);
		model.put("itMap", itMap);
		model.put("showShop", showShop);
		return "/admin/business/shop/showShopDetails.vm";
	}
	/*
	 * 永久关闭店铺
	 */
	@RequestMapping("/admin/shop/shopclose.xhtml")
	public String shopclose(Long shopid, ModelMap model){
		shopService.shopclose(shopid);
		return this.showJsonSuccess(model, null);
	}
	/*
	 * 查询所有永久关闭店铺
	 */
	@RequestMapping("/admin/shop/showallStopList.xhtml")
	public String showallStopList(String name,String citycode,Long operatorid,Integer pageNo,ModelMap model){
		if (pageNo == null)
			pageNo = 0;
		int rowsPerPage = 10;
		List<CafeShopVo> shopList = shopService.findCloseList(name,citycode,operatorid, pageNo, rowsPerPage);
		model.put("shopList", shopList);
		int count = shopService.findCloseListCount(name, operatorid, citycode);
		Map<Long, Company> operatorMap = companyService.mchProductItemMap();
		model.put("operatorMap", operatorMap);
		PageUtil pageUtil = new PageUtil(count, rowsPerPage, pageNo, "admin/shop/showallStopList.xhtml");
		Map params = new HashMap();
		params.put("name", name);
		params.put("operatorid", operatorid);
		params.put("citycode", citycode);
		pageUtil.initPageInfo(params);
		model.put("pageUtil", pageUtil);
		Map<String, String> itMap = CityContant.CITYMAP;
		model.put("itMap", itMap);
		return "/admin/business/shop/showallStopList.vm";
	}

}





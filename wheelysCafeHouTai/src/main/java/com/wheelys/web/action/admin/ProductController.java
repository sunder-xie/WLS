package com.wheelys.web.action.admin;

import java.sql.Timestamp;
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
import com.wheelys.model.CafeItem;
import com.wheelys.model.CafeProduct;
import com.wheelys.model.CafeShop;
import com.wheelys.model.ProductShop;
import com.wheelys.service.admin.ShopService;
import com.wheelys.service.admin.ProductService;
import com.wheelys.untrans.OSSUploadFileService;
import com.wheelys.util.WebUtils;
import com.wheelys.web.action.AnnotationController;
import com.wheelys.web.action.admin.vo.ProductShopVo;

@Controller
public class ProductController extends AnnotationController {
	@Autowired
	@Qualifier("productService")
	private ProductService productService;
	@Autowired
	@Qualifier("ossUploadFileService")
	private OSSUploadFileService ossUploadFileService;
	@Autowired
	@Qualifier("shopService")
	private ShopService shopService;

	/**
	 * 
	 * @return商品页面展示
	 */
	@RequestMapping("/admin/shop/cafePossess.xhtml")
	public String showCafeProduct(String prodname, String itemid, Integer pageNo, ModelMap model) {
		if (pageNo == null)
			pageNo = 0;
		int rowsPerPage = 10;
		List<CafeProduct> cafeList = productService.showcafeList(prodname, itemid, pageNo, rowsPerPage);
		model.put("cafeList", cafeList);
		int count = productService.findListCount(prodname, itemid);
		PageUtil pageUtil = new PageUtil(count, rowsPerPage, pageNo, "admin/shop/cafePossess.xhtml");
		Map<Long, CafeItem> productMap = productService.getCafeMap();
		model.put("productMap", productMap);
		Map params = new HashMap();
		params.put("prodname", prodname);
		params.put("itemid", itemid);
		pageUtil.initPageInfo(params);
		model.put("pageUtil", pageUtil);
		return "/admin/business/cafe/possess.vm";
	}

	/**
	 * 
	 * @param 根据类别查询商品
	 * @return
	 */
	@RequestMapping("/admin/shop/cafeItem.xhtml")
	public String findCafeItem(Long shopid, ModelMap model) {
		List<CafeItem> cafeItemList = productService.getCafeItemList();
		model.put("cafeItemList", cafeItemList);
		Map<Long, List<CafeProduct>> productMap = productService.getCafeProductMap();
		List<Long> productidList = productService.getProductIdList(shopid);
		CafeShop cafeShop = shopService.findCafeShop(shopid);
		String productidStr = StringUtils.join(productidList, ",");
		model.put("cafeShop", cafeShop);
		model.put("productidStr", productidStr);
		model.put("productidList", productidList);
		model.put("productMap", productMap);
		model.put("shopid", shopid);
		return "/admin/business/cafe/addmerchandise.vm";
	}

	/**
	 * 
	 * @param 添加商品的页面显示
	 * @return
	 */
	@RequestMapping("/admin/shop/showAddProduct.xhtml")
	public String addShowCafeProduct(Long id, ModelMap model) {
		if (id != null) {
			CafeProduct cafeProduct = productService.cafeproduct(id);
			model.put("cafeProduct", cafeProduct);
		}
		Map<Long, CafeItem> productMap = productService.getCafeMap();
		model.put("productMap", productMap);
		return "/admin/business/product/addproduct.vm";
	}

	/**
	 * 
	 * @param 添加和修改商品
	 * @return
	 */
	@RequestMapping("/admin/shop/addProduct.xhtml")
	public String addCafeProduct(Long id, Long itemid, String prodname, String prodenname, Integer prodprice,
			Integer cafehot, Integer cafebean, Integer cafemilk,Integer drink, String imgurl, HttpServletRequest request,
			ModelMap model) {
		Map<String, String> params = WebUtils.getRequestMap(request);
		User user = getLogonMember();
		params.put("username", user.getUsername());
		dbLogger.warn("params:" + JsonUtils.writeMapToJson(params));
		prodname = StringUtils.trim(prodname);
		if (itemid == null) {
			return this.showJsonError(model, "分类不能为空！");
		}
		if (StringUtils.isBlank(prodname)) {
			return this.showJsonError(model, "商品名不能为空！");
		}
		if (StringUtils.isBlank(prodenname)) {
			return this.showJsonError(model, "英文名不能为空！");
		}

		if (prodprice == null) {
			return this.showJsonError(model, "价格不能为空！");
		}
		if (id == null && StringUtils.isBlank(imgurl)) {
			return this.showJsonError(model, "图片不能为空");
		}
		
		CafeProduct cafeProduct = productService.cafeproduct(id);
		if (cafeProduct == null) {
			cafeProduct = new CafeProduct(prodname);
		}

		if (!StringUtils.equals(cafeProduct.getName(), prodname) || id == null) {
			boolean flag = productService.productName(prodname, null, id);
			if (flag)
				return this.showJsonError(model, "商品名称已存在！");
		}
		if (!StringUtils.equals(cafeProduct.getEnname(), prodenname) || id == null) {
			boolean flag = productService.productName(null, prodenname, id);
			if (flag)
				return this.showJsonError(model, "英文名称已存在！");
		}

		Timestamp ts = new Timestamp(System.currentTimeMillis());
		cafeProduct.setItemid(itemid);
		cafeProduct.setName(prodname);
		cafeProduct.setEnname(prodenname);
		cafeProduct.setPrice(prodprice);
		if (StringUtils.isNotBlank(imgurl)) {
			cafeProduct.setImg(imgurl);
		}
		cafeProduct.setCafebean(cafebean);
		cafeProduct.setCafehot(cafehot);
		cafeProduct.setCafemilk(cafemilk);
		cafeProduct.setDisplay(1);
		cafeProduct.setCreatetime(ts);
		cafeProduct.setDrink(drink);
		productService.addProduct(cafeProduct);
		return this.showJsonSuccess(model, cafeProduct);
	}

	/**
	 * 
	 * @param 删除里层中间表
	 * @return
	 */
	@RequestMapping("/admin/shop/upProductShop.xhtml")
	public String delete(Long psid, Long shopid, Long prodid, String delstatus, ModelMap model) {
		productService.upProductShop(psid, shopid, delstatus, prodid);
		return this.showJsonSuccess(model, null);
	}

	/**
	 * 判断商品是否上架
	 * 
	 * @param model
	 * 
	 */
	@RequestMapping("/admin/shop/updateProductProperty.xhtml")
	public String display(Long psid, Integer display, Integer psorder, ModelMap model) {
		productService.updateProcut(psid, psorder, display);
		return this.showJsonSuccess(model, null);
	}

	/**
	 * 店铺商品详细
	 * 
	 */
	@RequestMapping("/admin/shop/details.xhtml")
	public String details(Long psshopid, ModelMap model) {
		Map<CafeItem, List<ProductShopVo>> productMap = productService.getShopCafeProductMap(psshopid);
		model.put("productMap", productMap);
		CafeShop cafeShop = shopService.findCafeShop(psshopid);
		model.put("cafeShop", cafeShop);
		return "/admin/business/shopDetail/details.vm";

	}

	/**
	 * 商品删除
	 * 
	 */
	@RequestMapping("/admin/shop/delshopProduct.xhtml")
	public String delProduct(Long id, Integer display, ModelMap model) {
		CafeProduct cafeProduct = productService.cafeproduct(id);
		if (cafeProduct.getDisplay() == -1) {
			return this.showJsonSuccess(model, null);
		}
		if (display == 1) {
			productService.delProduct(id, display);
		}
		return this.showJsonSuccess(model, null);

	}

	/**
	 * @param 增加商品
	 * @return
	 */
	@RequestMapping("/admin/shop/addProductShop.xhtml")
	public String addProduct(Long shopid, String arr, String productidList, ModelMap model) {
		productService.addProductShop(shopid, arr, productidList);
		return this.showJsonSuccess(model, null);
	}

	/**
	 * @param 查看商品
	 * @return
	 */
	@RequestMapping("/admin/shop/showProductShop.xhtml")
	public String showProduct(Long prodid, ModelMap model) {
		List<ProductShop> pList = productService.product(prodid);
		model.put("pList", pList);
		return "/admin/business/shopDetail/details.vm";
	}

	/**
	 * @param 没下架的店铺
	 * @return
	 */
	@RequestMapping("/admin/shop/showDisplayShop.xhtml")
	public String displayShop(Long prodid, ModelMap model) {
		String name = productService.ca(prodid);
		if (StringUtils.isBlank(name)) {
			return this.showJsonSuccess(model, null);
		} else {
			return this.showJsonError(model, name);
		}
	}
}
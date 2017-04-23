package com.wheelys.web.action.admin;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.model.acl.User;
import com.wheelys.util.JsonUtils;
import com.wheelys.web.util.PageUtil;
import com.wheelys.constant.MchProductConstant;
import com.wheelys.constant.OrderConstant;
import com.wheelys.model.merchant.MchOrder;
import com.wheelys.model.merchant.MchOrderDetail;
import com.wheelys.model.merchant.MchProduct;
import com.wheelys.model.merchant.MchProductItem;
import com.wheelys.service.admin.MchProductService;
import com.wheelys.untrans.OSSUploadFileService;
import com.wheelys.util.WebUtils;
import com.wheelys.web.action.AnnotationController;

@Controller
public class MchController extends AnnotationController {

	@Autowired
	@Qualifier("mchProductService")
	private MchProductService mchProductService;
	@Autowired
	@Qualifier("ossUploadFileService")
	private OSSUploadFileService ossUploadFileService;

	/**
	 * 
	 * 主页
	 */
	@RequestMapping("/admin/order/mchproduct.xhtml")
	public String mchproduct(Long itemid, String name,Long supplierid, Integer pageNo, ModelMap model) {
		if (pageNo == null)
			pageNo = 0;
		int rowsPerPage = 10;
		List<MchProduct> mchProductList = mchProductService.mchProductlist(itemid, name, null,supplierid, pageNo, rowsPerPage);
		model.put("mchProductList",mchProductList);
		int count = mchProductService.findListCount(itemid,name,supplierid);
		PageUtil pageUtil = new PageUtil(count, rowsPerPage, pageNo, "admin/order/mchproduct.xhtml");
		Map params = new HashMap();
		params.put("itemid", itemid);
		params.put("name", name);
		params.put("supplierid", supplierid);
		pageUtil.initPageInfo(params);
		model.put("pageUtil", pageUtil);
		Map<Long, MchProductItem> itemMap = mchProductService.mchProductItemMap();
		model.put("itemMap", itemMap);
		Map<Long,String>comMap = MchProductConstant.TYPEMAP;
		model.put("comMap", comMap);
		model.put("supplierid", supplierid);
		return "/admin/mchpoduct/mchproduct.vm";
	}

	@RequestMapping("/admin/order/mch/findupdate.xhtml")
	public String update(Long id,Long supplierid, ModelMap model) {
		if (id != null) {
			MchProduct idMchproduct = mchProductService.findmchproduct(id);
			model.put("idMchproduct", idMchproduct);
		}
		Map<Long, MchProductItem> itemMap = mchProductService.mchProductItemMap();
		model.put("itemMap", itemMap);
		model.put("supplierid",supplierid);
		return "/admin/mchpoduct/updatemch.vm";
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param itemid
	 * @param weight
	 * @param unit
	 * @param description
	 * @param price
	 * @param model
	 * @return修改
	 */
	@RequestMapping("/admin/order/mch/updatch.xhtml")
	public String idfindmch(Long id, String mchname,Long supplierid, Long itemid, Integer weight, String unit, String description,
			Float price, String imgurl,Integer sn,HttpServletRequest request, ModelMap model) {
		Map<String, String> params = WebUtils.getRequestMap(request);
		User user = getLogonMember();
		params.put("username", user.getUsername());
		dbLogger.warn("params:"+JsonUtils.writeMapToJson(params));
		mchname = StringUtils.trim(mchname);
		if (StringUtils.isBlank(mchname)) {
			return this.showJsonError(model, "商品名不能为空！");
		}
		if (itemid == null) {
			return this.showJsonError(model, "分类不能为空");
		}
		if (StringUtils.isBlank(unit)) {
			return this.showJsonError(model, "一级单位不能为空！");
		}
		if (weight == null) {
			return this.showJsonError(model, "重量不能为空");
		}
		if (price == null) {
			return this.showJsonError(model, "价格不能为空");
		}
		if (sn == null) {
			return this.showJsonError(model, "序号不能为空");
		}
		if (id == null && StringUtils.isBlank(imgurl)) {
			return this.showJsonError(model, "图片不能为空");
		}

		MchProduct idMchproduct = mchProductService.findmchproduct(id);
		if (idMchproduct == null) {
			idMchproduct = new MchProduct(mchname);
		}
		if (!StringUtils.equals(idMchproduct.getName(), mchname) || id == null) {
			boolean flag = mchProductService.shopuser(mchname,supplierid, id);
			if (flag)
				return this.showJsonError(model, "商品名称已存在");
		}
		idMchproduct.setStatus("Y");
		idMchproduct.setItemid(itemid);
		idMchproduct.setWeight(weight);
		idMchproduct.setUnit(unit);
		idMchproduct.setDescription(description);
		idMchproduct.setPrice((int) (price * 100));
		idMchproduct.setName(mchname);
		idMchproduct.setSn(sn);
		if(supplierid!=null){
			idMchproduct.setSupplierid(supplierid);
		}
		
		if (StringUtils.isNotBlank(imgurl)) {
			idMchproduct.setImgurl(imgurl);
		}
		mchProductService.updateMchproduct(idMchproduct);
		return this.showJsonSuccess(model, idMchproduct);
	}

	/*
	 * 删除和排序
	 */
	@RequestMapping("/admin/order/mch/delmch.xhtml")
	public String delmch(Long id,Integer sn,String delstatus, ModelMap model) {
		mchProductService.delMch(id,sn,delstatus);
		return this.showJsonSuccess(model, null);
	}

	/**
	 * 
	 * 
	 * @param model订单表展示
	 * @return
	 */

	@RequestMapping("/admin/order/mchorder.xhtml")
	public String mchorder(String tradeno, String mchname,Long supplierid, String username, String status, Integer pageNo,
			Timestamp time1, Timestamp time2, ModelMap model) {
		if (pageNo == null)
			pageNo = 0;
		int rowsPerPage = 10;
		StringUtils.trim(mchname);
		List<MchOrder> mchOrderList = mchProductService.modList(tradeno, mchname, supplierid, status, username, time1, time2, pageNo, rowsPerPage);
		int count = mchProductService.findOrderCount(tradeno, mchname, username, status,supplierid, time1, time2);
		Map<Long,String>comMap = MchProductConstant.TYPEMAP;
		PageUtil pageUtil = new PageUtil(count, rowsPerPage, pageNo, "admin/order/mchorder.xhtml");
		Map params = new HashMap();
		params.put("status", status);
		params.put("tradeno", tradeno);
		params.put("mchname", mchname);
		params.put("username", username);
		params.put("time1", time1);
		params.put("time2", time2);
		params.put("supplierid", supplierid);
		pageUtil.initPageInfo(params);
		model.put("pageUtil", pageUtil);
		model.put("mchOrderList", mchOrderList);
		model.put("comMap", comMap);
		return "/admin/mchOrder/mchOrder.vm";

	}

	/**
	 * 
	 * @return订单详情
	 */

	@RequestMapping("/admin/order/mchorderdetails.xhtml")
	public String mchorderdetails(Long id, ModelMap model) {
		List<MchOrderDetail> mchDetailList = mchProductService.orderList(id);
		MchOrder mchorder = mchProductService.mchorder(id);
		model.put("mchDetailList", mchDetailList);
		model.put("mchorder", mchorder);
		return "/admin/mchOrder/mchOrderDetails.vm";
	}

	/**
	 * 
	 * @param response
	 * @return下载订单详情
	 */

	@RequestMapping("/admin/order/orderdetailsExcel.xhtml")
	public String mchorderdetails(Long id, ModelMap model, HttpServletResponse response) {
		List<MchOrderDetail> mchDetailList = mchProductService.orderList(id);
		MchOrder mchorder = mchProductService.mchorder(id);
		model.put("mchDetailList", mchDetailList);
		model.put("mchorder", mchorder);
		this.download("xls", response);
		return "/admin/mchOrder/mchOrderDetailsExcel.vm";
	}

	/**
	 * 判断商品是否上架
	 * 
	 * @param model
	 * 
	 */
	@RequestMapping("/admin/order/shop/status.xhtml")
	public String display(Long mchid, String status, ModelMap model) {
		mchProductService.status(mchid, status);
		return this.showJsonSuccess(model, null);
	}

	/**
	 * 
	 * @param shopname
	 * @param model
	 * @return判断商品是否存在
	 */
	@RequestMapping("/admin/order/mch/ifmchname.xhtml")
	public String ifmchname(String mchname, Long id,Long supplierid,ModelMap model) {
		boolean ckShop = mchProductService.shopuser(mchname,supplierid, id);
		if (ckShop == true) {
			return this.showJsonSuccess(model, "success");
		}
		return this.showJsonError(model, "error");
	}

	/**
	 * 
	 * @param orderid
	 * @param status
	 * @param expressInfo
	 * @param model
	 * @return判断状态
	 */
	@RequestMapping("/admin/order/status.xhtml")
	public String iforder(Long orderid, String status,HttpServletRequest request,String expressInfo, ModelMap model){
		Map<String, String> params = WebUtils.getRequestMap(request);
		User user = getLogonMember();
		params.put("username", user.getUsername());
		dbLogger.warn("params:"+JsonUtils.writeMapToJson(params));
		if (StringUtils.equals(OrderConstant.STATUS_FINISH, status)
				|| StringUtils.equals(OrderConstant.STATUS_ACCEPT, status)
				|| StringUtils.equals(OrderConstant.STATUS_SEND, status)
				|| StringUtils.equals(OrderConstant.STATUS_CANCEL, status)
				|| StringUtils.equals(OrderConstant.PAYSTATUS_PAID, status)
				) {
			mchProductService.updateOrderStatus(orderid, status, expressInfo);
		}
		return this.showJsonSuccess(model, null);
	}

	/**
	 * 
	 * @param orderid
	 * @param expressInfo
	 * @return报表
	 */
	@RequestMapping("/admin/report/reportOrder.xhtml")
	public String findReport(String tradeno, String mchname, String username, Integer pageNo, Timestamp time1,
			String status, Timestamp time2, ModelMap model) {
		if (pageNo == null)
			pageNo = 0;
		int rowsPerPage = 500;
		List<MchOrder> mchOrderList = mchProductService.findMchorder(tradeno, mchname,null,username,status,time1, time2, pageNo,
				rowsPerPage);
		int count = mchProductService.findOrderCount(tradeno, mchname,username, status,null, time1, time2);
		PageUtil pageUtil = new PageUtil(count, rowsPerPage, pageNo, "/admin/mch/ifreport.xhtml");
		Map params = new HashMap();
		params.put("status", status);
		pageUtil.initPageInfo(params);
		model.put("pageUtil", pageUtil);
		model.put("orderReportList", mchOrderList);
		return "/admin/mchReport/mchReport.vm";
	}
	
	/**
	 * 
	 * @return订单详情
	 */

	@RequestMapping("/admin/order/mchReportDetails.xhtml")
	public String mchReportDetails(Long id, ModelMap model) {
		List<MchOrderDetail> mchDetailList = mchProductService.orderList(id);
		MchOrder mchorder = mchProductService.mchorder(id);
		model.put("mchDetailList", mchDetailList);
		model.put("mchorder", mchorder);
		return "/admin/mchReport/mchReportDetails.vm";
	}
	
}

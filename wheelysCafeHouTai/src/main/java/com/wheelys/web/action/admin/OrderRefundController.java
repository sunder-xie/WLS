package com.wheelys.web.action.admin;

import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.wheelys.model.acl.User;
import com.wheelys.util.DateUtil;
import com.wheelys.util.JsonUtils;
import com.wheelys.web.util.PageUtil;
import com.wheelys.constant.WheelysRefundConstant;
import com.wheelys.model.CafeShop;
import com.wheelys.model.orderrefund.RefundOrder;
import com.wheelys.service.admin.OrderRefundService;
import com.wheelys.service.cafe.CafeShopService;
import com.wheelys.util.ValidateChildrenUtils;
import com.wheelys.util.WebUtils;
import com.wheelys.web.action.AnnotationController;
import com.wheelys.web.action.openapi.vo.WheelysOerderVo;

@Controller
public class OrderRefundController extends AnnotationController {

	@Autowired
	@Qualifier("orderRefundService")
	private OrderRefundService orderRefundService;
	@Autowired
	@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;

	@RequestMapping("/admin/orderrefund/showRefundListByTime.xhtml")
	public String showRefundListByTime(Long shopid, Date begin, Date end,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo, ModelMap model) {
		if (begin != null)
			begin = DateUtil.getBeginTimestamp(begin);
		if (end != null  )
			end = DateUtil.getBeginTimestamp(end);
		int rowsPerPage = 10;
		List<RefundOrder> refundList = orderRefundService.showRefundListByTime(begin, end, shopid, pageNo, rowsPerPage);
		List<CafeShop> shopList = cafeShopService.getOpenShopList();
		Integer totalPrice = this.getTotalPrice(orderRefundService.showRefundListById(begin, end, shopid));
		model.put("totalPrice", totalPrice / 100.0);	
		model.put("shopList", shopList);
		User user = getLogonMember();
		int count = orderRefundService.findListCount(begin, end, shopid);
		PageUtil pageUtil = new PageUtil(count, rowsPerPage, pageNo, "admin/orderrefund/showRefundListByTime.xhtml");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("shopid", shopid);
		if (begin != null)
			params.put("begin", begin);
		if (end != null)
			params.put("end", end);
		pageUtil.initPageInfo(params);
		model.put("user", user);
		model.put("pageUtil", pageUtil);
		model.put("refundList", refundList);
		return "/admin/orderrefund/refundDetails.vm";
	}

	@RequestMapping("/admin/orderrefund/showRefundList.xhtml")
	public String showRefundList(ModelMap model, Integer pageNo) {
		if (pageNo == null)
			pageNo = 0;
		int rowsPerPage = 10;
		List<RefundOrder> refundList = orderRefundService.showRefundList(pageNo, rowsPerPage);
		List<CafeShop> shopList = cafeShopService.getOpenShopList();
		model.put("shopList", shopList);
		Integer totalPrice = this.getTotalPrice(orderRefundService.showRefundListById(null, null, null));
		model.put("totalPrice", totalPrice / 100.0);
		model.put("shopList", shopList);
		User user = getLogonMember();
		int count = orderRefundService.findListCount();
		PageUtil pageUtil = new PageUtil(count, rowsPerPage, pageNo, "admin/orderrefund/showRefundList.xhtml");
		Map<String, Object> params = new HashMap<String, Object>();
		pageUtil.initPageInfo(params);
		model.put("user", user);
		model.put("pageUtil", pageUtil);
		model.put("refundList", refundList);
		return "/admin/orderrefund/refundDetails.vm";
	}

	@RequestMapping("/admin/orderrefund/showRefundinfo.xhtml")
	public String showRefundinfo(ModelMap model, Long id) {
		RefundOrder refund = orderRefundService.showRefundinfo(id);
		model.put("refund", refund);
		return "/admin/orderrefund/refundfaildinfo.vm";
	}

	@RequestMapping("/admin/orderrefund/showinfo.xhtml")
	public String showinfo(ModelMap model, Long id) {
		RefundOrder refund = orderRefundService.showRefundinfo(id);
		model.put("refund", refund);
		return "/admin/orderrefund/refundinfo.vm";
	}

	@RequestMapping("/admin/orderrefund/showOrderDetail.xhtml")
	public String showOrderDetail(ModelMap model, String tradeno) {
		WheelysOerderVo base = orderRefundService.getOrder(tradeno);
		model.put("base", base);
		return "/admin/orderrefund/refundorderdetail.vm";
	}

	/**
	 * 
	 * @param orderid
	 * @param status
	 * @param expressInfo
	 * @param model
	 * @return判断状态
	 */
	@RequestMapping("/admin/orderrefund/refundStatus.xhtml")
	public String refundStatus(Long id, String showstatus, String refundinfo, String expressInfo,
			HttpServletRequest request, ModelMap model) {
		RefundOrder refund = orderRefundService.showRefundinfo(id);
		Map<String, String> params = WebUtils.getRequestMap(request);
		User user = getLogonMember();
		params.put("username", user.getUsername());
		params.put("showstatus", showstatus);
		params.put("orderid", refund.getTradeno());
		dbLogger.warn("params:" + JsonUtils.writeMapToJson(params));
		if (StringUtils.equals(WheelysRefundConstant.REFUNDSTATUS_ALREADY_PASSED, showstatus)
				|| StringUtils.equals(WheelysRefundConstant.REFUNDSTATUS_NO_PASSED, showstatus)
				|| StringUtils.equals(WheelysRefundConstant.REFUNDSTATUS_CANCEL, showstatus)
				|| StringUtils.equals(WheelysRefundConstant.REFUNDSTATUS_FINISH, showstatus)
				|| StringUtils.equals(WheelysRefundConstant.REFUNDSTATUS_REFUND_FAILED, showstatus)
				|| StringUtils.equals(WheelysRefundConstant.REFUNDSTATUS_PENDING_AUDIT, showstatus)) {
			{
				if (ValidateChildrenUtils.isLength(expressInfo) > 2000) {
					return this.showJsonError(model, "输入框超过2000字限制");
				} else {
					orderRefundService.updateRefundStatus(id, showstatus, user.getId(), user.getNickname(), expressInfo,
							refundinfo);
				}
			}
		}
		return this.showJsonSuccess(model, null);
	}

	/**
	 * 获取总计退款
	 * 
	 * @return
	 */
	private Integer getTotalPrice(List<RefundOrder> refundOrderList) {
		if (refundOrderList == null || refundOrderList.size() <= 0)
			refundOrderList = new ArrayList<RefundOrder>();
		Integer totalPrice = 0;
		for (RefundOrder refundOrder : refundOrderList) {
			if (StringUtils.equals(WheelysRefundConstant.REFUNDSTATUS_FINISH, refundOrder.getStatus())) {
				totalPrice += refundOrder.getRefundamount();
			}
		}
		return totalPrice;
	}
}
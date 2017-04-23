package com.wheelys.web.action.admin.member;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.model.acl.User;
import com.wheelys.web.util.PageUtil;
import com.wheelys.constant.CityContant;
import com.wheelys.model.CafeShop;
import com.wheelys.model.citymanage.CityRegion;
import com.wheelys.service.MemberOrderInfoService;
import com.wheelys.service.MemberService;
import com.wheelys.service.admin.CityRegionService;
import com.wheelys.service.admin.UserService;
import com.wheelys.service.cafe.CafeShopService;
import com.wheelys.web.action.AnnotationController;
import com.wheelys.web.action.admin.vo.WheelysMemberVo;

@Controller
public class MemberController extends AnnotationController {

	@Autowired
	@Qualifier("memberService")
	private MemberService memberService;
	@Autowired
	@Qualifier("cityRegionService")
	private CityRegionService cityRegionService;
	@Autowired
	@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	@Autowired
	@Qualifier("memberOrderInfoService")
	private MemberOrderInfoService memberOrderInfoService;

	@RequestMapping("/admin/member/getAllMember.xhtml")
	public String getAllMember(ModelMap model, Integer pageNo, String region, String cityCode, Long shopId,
			String phone, Date createTimeBegin, Date createTimeEnd) {
		if (pageNo == null) {
			pageNo = 0;
		}
		int rowPerPage = 10;
		List<WheelysMemberVo> memberList = memberService.findMemberWithLatestOrder(pageNo, rowPerPage, region, cityCode,
				shopId, phone, createTimeBegin, createTimeEnd);
		model.put("memberList", memberList);
		int count = memberOrderInfoService.getMemberLatestOrderInfoCount(region, cityCode, shopId, phone,
				createTimeBegin, createTimeEnd);
		PageUtil pageUtil = new PageUtil(count, rowPerPage, pageNo, "admin/member/getAllMember.xhtml");
		// 判断是否有查询订单的权限
		User user = getLogonMember();
		String haveAuth = "false";
		List<String> webList = userService.webList(user.getId());
		if (webList.contains("allOrder")) {
			haveAuth = "true";
		}
		model.put("haveAuth", haveAuth);
		model.put("cityMap", CityContant.CITYMAP);
		List<CityRegion> regionList = cityRegionService.findAllRegion();
		model.put("regionList", regionList);
		List<CafeShop> shopList = cafeShopService.getOpenShopList();
		model.put("shopList", shopList);
		Map<String, Object> params = new HashMap<>();
		params.put("region", region);
		params.put("cityCode", cityCode);
		params.put("region", region);
		params.put("shopId", shopId);
		params.put("phone", phone);
		params.put("createTimeBegin", createTimeBegin);
		params.put("createTimeEnd", createTimeEnd);
		pageUtil.initPageInfo(params);
		model.put("pageUtil", pageUtil);
		return "/admin/member/allMemberDetails.vm";
	}
}

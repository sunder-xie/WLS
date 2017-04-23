package com.wheelys.web.action.admin.notice;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.util.DateUtil;
import com.wheelys.web.util.PageUtil;
import com.wheelys.model.CafeShop;
import com.wheelys.model.notice.NoticeManage;
import com.wheelys.service.admin.NoticeService;
import com.wheelys.service.admin.ShopService;
import com.wheelys.web.action.AnnotationController;
import com.wheelys.web.action.admin.vo.NoticeVo;

@Controller
public class NoticeController extends AnnotationController {

	@Autowired
	@Qualifier("noticeService")
	private NoticeService noticeService;
	@Autowired
	@Qualifier("shopService")
	private ShopService shopService;

	@RequestMapping("/admin/content/shownotice.xhtml")
	public String shownoticeList(Integer pageNo, ModelMap model) {
		if (pageNo == null)
			pageNo = 0;
		int rowsPerPage = 10;
		List<NoticeVo> showList = noticeService.showList(pageNo, rowsPerPage);
		int count = noticeService.findCount();
		PageUtil pageUtil = new PageUtil(count, rowsPerPage, pageNo, "admin/content/shownotice.xhtml");
		Timestamp nowtime = DateUtil.getMillTimestamp();
		Map params = new HashMap();
		pageUtil.initPageInfo(params);
		model.put("pageUtil", pageUtil);
		model.put("showList", showList);
		model.put("nowtime", nowtime);
		return "/admin/notice/noticeList.vm";
	}

	@RequestMapping("/admin/content/showaddnotice.xhtml")
	public String showaddnotice(Long id, ModelMap model) {
		NoticeManage notice = noticeService.notice(id);
		List<CafeShop> shopList = shopService.cafeShopList();
		if (id != null) {
			List<String> idList = noticeService.shoids(id);
			model.put("idList", idList);
		}
		model.put("shopList", shopList);
		model.put("notice", notice);
		return "/admin/notice/addnotice.vm";
	}

	@RequestMapping("/admin/content/addnotice.xhtml")
	public String addnotice(Long id, String noticename, String content, String validshopid, Timestamp begintime,
			Timestamp endtime, ModelMap model) {
		if (StringUtils.isBlank(noticename)) {
			return this.showJsonError(model, "通知名字不能为空");
		}
		if (StringUtils.isBlank(content)) {
			return this.showJsonError(model, "通知内容不能为空");
		}
		if (begintime == null) {
			return this.showJsonError(model, "开始时间不能为空");
		}
		if (endtime == null) {
			return this.showJsonError(model, "结束时间不能为空");
		}
		if (StringUtils.isBlank(validshopid)) {
			return this.showJsonError(model, "勾选的店铺不能为空");
		}
		noticeService.addNotice(id, noticename, content, validshopid, begintime, endtime);
		return this.showJsonSuccess(model, null);
	}

	@RequestMapping("/admin/content/delnotice.xhtml")
	public String delnotice(Long id, ModelMap model) {
		noticeService.delStatus(id);
		return this.showJsonSuccess(model, null);
	}

	@RequestMapping("/admin/test/test1.xhtml")
	public String test(Long shopid, ModelMap model) {
		noticeService.voList(shopid);
		return this.showJsonSuccess(model, null);
	}
}

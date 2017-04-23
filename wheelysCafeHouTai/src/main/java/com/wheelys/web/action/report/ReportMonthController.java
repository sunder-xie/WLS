package com.wheelys.web.action.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.util.DateUtil;
import com.wheelys.constant.CityContant;
import com.wheelys.constant.PayConstant;
import com.wheelys.model.CafeShop;
import com.wheelys.model.report.ReportOrderDatePaymethod;
import com.wheelys.service.cafe.CafeShopService;
import com.wheelys.util.VmUtils;
import com.wheelys.service.report.SellResultService;
import com.wheelys.web.action.AnnotationController;
import com.wheelys.web.action.report.vo.ReportBackSellProductVo;
import com.wheelys.web.action.report.vo.ReportOrderPayMethodVo;
import com.wheelys.web.action.report.vo.summaryVo.ReportSummaryActive;
import com.wheelys.web.action.report.vo.summaryVo.ReportSummaryYun;
import com.wheelys.web.action.report.vo.summaryVo.ReportSummaryZi;

@Controller
public class ReportMonthController extends AnnotationController {

	@Autowired
	@Qualifier("sellResultService")
	private SellResultService sellResultService;
	@Autowired
	@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;

	/*
	 * 按日期统计销售
	 */
	@RequestMapping("/admin/report/reportPayMethodList.xhtml")
	public String reportPayMethodList(ModelMap model, Long shopid, String cityname, Date begin, Date end) {
		List<CafeShop> shopList = cafeShopService.getOpenShopList();
		model.put("shopList", shopList);
		model.put("citymap", CityContant.CITYMAP);
		if (begin == null) {
			begin = new Date();
		}
		if (end == null) {
			end = new Date();
		}
		List<ReportOrderDatePaymethod> payMethodList = sellResultService.findPayMethodList(shopid, cityname, begin,
				end);
		Map<Long, Map<String, ReportOrderPayMethodVo>> payMethodMap = new LinkedHashMap<Long, Map<String, ReportOrderPayMethodVo>>();
		for (ReportOrderDatePaymethod paymethod : payMethodList) {
			Map<String, ReportOrderPayMethodVo> voMap = payMethodMap.get(paymethod.getShopid());
			if (voMap == null) {
				voMap = new HashMap<String, ReportOrderPayMethodVo>();
				payMethodMap.put(paymethod.getShopid(), voMap);
			}
			ReportOrderPayMethodVo payMethodVo = voMap
					.get(paymethod.getShopid() + "_" + DateUtil.formatDate(paymethod.getDate()));
			if (payMethodVo == null) {
				payMethodVo = new ReportOrderPayMethodVo(paymethod);
				voMap.put(paymethod.getShopid() + "_" + DateUtil.formatDate(paymethod.getDate()), payMethodVo);
			}
			if (StringUtils.isNotBlank(paymethod.getSellmethod())) {
				payMethodVo.setSellmethod(paymethod.getSellmethod());
			}
			if (StringUtils.equals(paymethod.getPaymethod(), PayConstant.PAYMETHOD_WEIXINH5)) {
				payMethodVo.setWnetpaid(paymethod.getNetpaid());
				payMethodVo.setWquantity(paymethod.getQuantity());
			} else if (StringUtils.equals(paymethod.getPaymethod(), PayConstant.PAYMETHOD_ALIH5)) {
				payMethodVo.setZnetpaid(paymethod.getNetpaid());
				payMethodVo.setZquantity(paymethod.getQuantity());
			}
			if (PayConstant.ALLPAYMETHOD.contains(paymethod.getPaymethod())) {
				payMethodVo.setTotalquantity(payMethodVo.getTotalquantity() + paymethod.getQuantity());
				payMethodVo.setTotalfee(payMethodVo.getTotalfee() + paymethod.getTotalfee());
				payMethodVo.setAvprice(VmUtils.getAmount(payMethodVo.getTotalfee() / payMethodVo.getTotalquantity()));
				payMethodVo.setTotalprice(payMethodVo.getTotalprice() + paymethod.getNetpaid());
			}
		}
		Map<CafeShop, List<ReportOrderPayMethodVo>> payMethodVoMap = new LinkedHashMap<CafeShop, List<ReportOrderPayMethodVo>>();
		for (Long key : payMethodMap.keySet()) {
			List<ReportOrderPayMethodVo> voList = new ArrayList<ReportOrderPayMethodVo>(payMethodMap.get(key).values());
			Collections.sort(voList, new PropertyComparator("date", false, true));
			payMethodVoMap.put(cafeShopService.getCacheShop(key), voList);
		}
		model.put("payMethodVoMap", payMethodVoMap);
		return "/admin/report/reportPayMethodList.vm";
	}
	/*
	 * 导出reportPayMethodList表 excel
	 */

	/*
	 * 按店面统计各店销售业绩
	 */
	@RequestMapping("/admin/report/reportByShopSellList.xhtml")
	public String reportOrderList(ModelMap model, Long shopid, String cityname, Date begin, Date end) {
		List<CafeShop> shopList = cafeShopService.getOpenShopList();
		model.put("shopList", shopList);
		model.put("citymap", CityContant.CITYMAP);
		if (begin == null) {
			begin = new Date();
		}
		if (end == null) {
			end = new Date();
		}
		List<ReportBackSellProductVo> sellBackList = sellResultService.getBackSellList(shopid, cityname, begin, end);
		Map<String, ReportBackSellProductVo> shopDateMap = new LinkedHashMap<String, ReportBackSellProductVo>();
		List<CafeShop> cafeShopList = new ArrayList<CafeShop>();
		Map<String, Integer> totalMap = new HashMap<String, Integer>();
		for (ReportBackSellProductVo Vo : sellBackList) {
			CafeShop cafeShop = cafeShopService.getCacheShop(Vo.getShopid());
			if (cafeShop != null) {
				if (!cafeShopList.contains(cafeShop)) {
					cafeShopList.add(cafeShop);
				}
			}
			String key = Vo.getShopid() + "_" + Vo.getDate();
			shopDateMap.put(key, Vo);
			Integer subtotalfee = totalMap.get(Vo.getDate() + "_subtotalfee");
			Integer subquantity = totalMap.get(Vo.getDate() + "_subquantity");
			if (subtotalfee == null)
				subtotalfee = 0;
			if (subquantity == null)
				subquantity = 0;
			subtotalfee += Vo.getSubtotalfee();
			subquantity += Vo.getSubquantity();
			totalMap.put(Vo.getDate() + "_subtotalfee", subtotalfee);
			totalMap.put(Vo.getDate() + "_subquantity", subquantity);
		}
		model.put("totalMap", totalMap);
		model.put("cafeShopList", cafeShopList);
		model.put("shopDateMap", shopDateMap);
		List<String> dateList = new ArrayList<String>();
		int diff = DateUtil.getDiffDay(begin, end);
		for (int i = 0; i <= diff; i++) {
			dateList.add(DateUtil.formatDate(DateUtil.addDay(begin, i)));
		}
		model.put("dateList", dateList);
		return "/admin/report/reportByShopSellList.vm";
	}

	/*
	 * summary表数据聚合
	 */
	@RequestMapping("/admin/report/reportByShopSummary.xhtml")
	public String reportByShopSummary(ModelMap model, Date begin, Date end) {
		if (begin == null) {
			begin = new Date() ;
		}
		if (end == null) {
			end = new Date();
		}
		ReportSummaryYun yunVo = sellResultService.findByYunShopSummary(begin, end);
		ReportSummaryActive activeVo = sellResultService.findActiveSummary(begin, end);
		ReportSummaryZi ziVo = sellResultService.findByZiSummary(begin, end);
		Integer totalshopsize = 0;// 三种不同店面有几种开业计算wheelys平台平均值
		List<Integer> list = new ArrayList<>();
		if (activeVo.getShopcount() > 0) {
			list.add(activeVo.getShopcount());
		}
		if (ziVo.getShopcount() > 0) {
			list.add(ziVo.getShopcount());
		}
		if (yunVo.getShopcount() > 0) {
			list.add(yunVo.getShopcount());
		}
		if (yunVo.getCloseshopcount()>0) {
			list.add(yunVo.getCloseshopcount());
		}
		totalshopsize = list.size();
		model.put("totalshopsize", totalshopsize);
		model.put("yunVo", yunVo);
		model.put("ziVo", ziVo);
		model.put("activeVo", activeVo);
		return "/admin/report/reportByShopSummary.vm";
	}

	/*
	 * summary表excel格式
	 */
	@RequestMapping("/admin/report/reportByShopSummaryExcel.xhtml")
	public String summaryExcel(HttpServletResponse response,Double ratio1,Double ratio2,Double ratio3,
			  				   ModelMap model,Date begin, Date end){
		if (begin == null) {
			begin = new Date() ;
		}
		if (end == null) {
			end = new Date();
		}
		ReportSummaryYun yunVo = sellResultService.findByYunShopSummary(begin, end);
		ReportSummaryActive activeVo = sellResultService.findActiveSummary(begin, end);
		ReportSummaryZi ziVo = sellResultService.findByZiSummary(begin, end);
		Integer totalshopsize = 0;// 三种不同店面有几种开业计算wheelys平台平均值
		List<Integer> list = new ArrayList<>();
		if (activeVo.getShopcount() > 0) {
			list.add(activeVo.getShopcount());
		}
		if (ziVo.getShopcount() > 0) {
			list.add(ziVo.getShopcount());
		}
		if (yunVo.getShopcount() > 0) {
			list.add(yunVo.getShopcount());
		}
		if (yunVo.getCloseshopcount()>0) {
			list.add(yunVo.getCloseshopcount());
		}
		totalshopsize = list.size();
		model.put("totalshopsize", totalshopsize);
		model.put("yunVo", yunVo);
		model.put("ziVo", ziVo);
		model.put("activeVo", activeVo);
		//计算wheelys平台根据比例推算
		if (ratio1==null) {
			ratio1=0d;
		}
		if (ratio2==null) {
			ratio2=0d;
		}
		if (ratio3==null) {
			ratio3=0d;
		}
		model.put("ratio1", ratio1);
		model.put("ratio2", ratio2);
		model.put("ratio3", ratio3);
		int tcprice=(int) (yunVo.getSubnetpaid()*ratio1*0.01);
		int	totalnetpaid=yunVo.getSubnetpaid()+yunVo.getSubclosenetpaid()+ziVo.getSubnetpaid()+
						 activeVo.getSubnetpaid();
		
		int bomcbprice=(int) (totalnetpaid*ratio3*0.01);
		int gysfdprice=(int) (bomcbprice*ratio2*0.01);
		int subprice=tcprice+gysfdprice;
		model.put("tcprice", tcprice);
		model.put("bomcbprice", bomcbprice);
		model.put("gysfdprice", gysfdprice);
		model.put("subprice", subprice);
		this.download("xls", response);
		return "/admin/report/exportExcel/reportByShopSummaryExcel.vm";
	}
	
	/*
	 * 按日期统计销售导出
	 */
	@RequestMapping("/admin/report/reportPayMethodListExcel.xhtml")
	public String reportPayMethodListExcel(ModelMap model, Long shopid, String cityname, Date begin, Date end,
			HttpServletResponse response) {
		List<CafeShop> shopList = cafeShopService.getOpenShopList();
		model.put("shopList", shopList);
		model.put("citymap", CityContant.CITYMAP);
		if (begin == null) {
			begin = new Date();
		}
		if (end == null) {
			end = new Date();
		}
		List<ReportOrderDatePaymethod> payMethodList = sellResultService.findPayMethodList(shopid, cityname, begin,
				end);
		Map<Long, Map<String, ReportOrderPayMethodVo>> payMethodMap = new LinkedHashMap<Long, Map<String, ReportOrderPayMethodVo>>();
		for (ReportOrderDatePaymethod paymethod : payMethodList) {
			Map<String, ReportOrderPayMethodVo> voMap = payMethodMap.get(paymethod.getShopid());
			if (voMap == null) {
				voMap = new HashMap<String, ReportOrderPayMethodVo>();
				payMethodMap.put(paymethod.getShopid(), voMap);
			}
			ReportOrderPayMethodVo payMethodVo = voMap
					.get(paymethod.getShopid() + "_" + DateUtil.formatDate(paymethod.getDate()));
			if (payMethodVo == null) {
				payMethodVo = new ReportOrderPayMethodVo(paymethod);
				voMap.put(paymethod.getShopid() + "_" + DateUtil.formatDate(paymethod.getDate()), payMethodVo);
			}
			if (StringUtils.isNotBlank(paymethod.getSellmethod())) {
				payMethodVo.setSellmethod(paymethod.getSellmethod());
			}
			if (StringUtils.equals(paymethod.getPaymethod(), PayConstant.PAYMETHOD_WEIXINH5)) {
				payMethodVo.setWnetpaid(paymethod.getNetpaid());
				payMethodVo.setWquantity(paymethod.getQuantity());
			} else if (StringUtils.equals(paymethod.getPaymethod(), PayConstant.PAYMETHOD_ALIH5)) {
				payMethodVo.setZnetpaid(paymethod.getNetpaid());
				payMethodVo.setZquantity(paymethod.getQuantity());
			}
			if (PayConstant.ALLPAYMETHOD.contains(paymethod.getPaymethod())) {
				payMethodVo.setTotalquantity(payMethodVo.getTotalquantity() + paymethod.getQuantity());
				payMethodVo.setTotalfee(payMethodVo.getTotalfee() + paymethod.getTotalfee());
				payMethodVo.setAvprice(VmUtils.getAmount(payMethodVo.getTotalfee() / payMethodVo.getTotalquantity()));
				payMethodVo.setTotalprice(payMethodVo.getTotalprice() + paymethod.getNetpaid());
			}
		}
		Map<CafeShop, List<ReportOrderPayMethodVo>> payMethodVoMap = new LinkedHashMap<CafeShop, List<ReportOrderPayMethodVo>>();
		for (Long key : payMethodMap.keySet()) {
			List<ReportOrderPayMethodVo> voList = new ArrayList<ReportOrderPayMethodVo>(payMethodMap.get(key).values());
			Collections.sort(voList, new PropertyComparator("date", false, true));
			payMethodVoMap.put(cafeShopService.getCacheShop(key), voList);
		}
		model.put("payMethodVoMap", payMethodVoMap);
		this.download("xls", response);
		return "/admin/report/exportExcel/reportPayMethodListExcel.vm";
	}

	/*
	 * 按店面统计各店销售业绩导出
	 */
	@RequestMapping("/admin/report/reportByShopSellListExcel.xhtml")
	public String reportOrderListExcel(ModelMap model, Long shopid, String cityname, Date begin, Date end,
			HttpServletResponse response) {
		List<CafeShop> shopList = cafeShopService.getOpenShopList();
		model.put("shopList", shopList);
		model.put("citymap", CityContant.CITYMAP);
		if (begin == null) {
			begin = new Date();
		}
		if (end == null) {
			end = new Date();
		}
		List<ReportBackSellProductVo> sellBackList = sellResultService.getBackSellList(shopid, cityname, begin, end);
		Map<String, ReportBackSellProductVo> shopDateMap = new LinkedHashMap<String, ReportBackSellProductVo>();
		List<CafeShop> cafeShopList = new ArrayList<CafeShop>();
		Map<String, Integer> totalMap = new HashMap<String, Integer>();
		for (ReportBackSellProductVo Vo : sellBackList) {
			CafeShop cafeShop = cafeShopService.getCacheShop(Vo.getShopid());
			if (cafeShop != null) {
				if (!cafeShopList.contains(cafeShop)) {
					cafeShopList.add(cafeShop);
				}
			}
			String key = Vo.getShopid() + "_" + Vo.getDate();
			shopDateMap.put(key, Vo);
			Integer subtotalfee = totalMap.get(Vo.getDate() + "_subtotalfee");
			Integer subquantity = totalMap.get(Vo.getDate() + "_subquantity");
			if (subtotalfee == null)
				subtotalfee = 0;
			if (subquantity == null)
				subquantity = 0;
			subtotalfee += Vo.getSubtotalfee();
			subquantity += Vo.getSubquantity();
			totalMap.put(Vo.getDate() + "_subtotalfee", subtotalfee);
			totalMap.put(Vo.getDate() + "_subquantity", subquantity);
		}
		model.put("totalMap", totalMap);
		model.put("cafeShopList", cafeShopList);
		model.put("shopDateMap", shopDateMap);
		List<String> dateList = new ArrayList<String>();
		int diff = DateUtil.getDiffDay(begin, end);
		for (int i = 0; i <= diff; i++) {
			dateList.add(DateUtil.formatDate(DateUtil.addDay(begin, i)));
		}
		model.put("dateList", dateList);
		this.download("xls", response);
		return "/admin/report/exportExcel/reportByShopSellListExcel.vm";
	}

}

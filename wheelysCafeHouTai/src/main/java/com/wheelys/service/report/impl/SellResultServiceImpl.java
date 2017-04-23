package com.wheelys.service.report.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.BeanUtil;
import com.wheelys.util.DateUtil;
import com.wheelys.constant.CityContant;
import com.wheelys.model.CafeShop;
import com.wheelys.model.company.Company;
import com.wheelys.model.report.ReportOrderDatePaymethod;
import com.wheelys.service.cafe.CafeShopService;
import com.wheelys.service.report.SellResultService;
import com.wheelys.util.VmUtils;
import com.wheelys.web.action.report.vo.ReportBackSellProductVo;
import com.wheelys.web.action.report.vo.summaryVo.ReportSummaryActive;
import com.wheelys.web.action.report.vo.summaryVo.ReportSummaryYun;
import com.wheelys.web.action.report.vo.summaryVo.ReportSummaryZi;

@Service("sellResultService")
public class SellResultServiceImpl extends BaseServiceImpl implements SellResultService {
	@Autowired
	@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;

	@Override
	public List<ReportOrderDatePaymethod> findPayMethodList(Long shopid, String cityname, Date begin, Date end) {
		// 获取支付方式列表
		DetachedCriteria query = DetachedCriteria.forClass(ReportOrderDatePaymethod.class);
		query.addOrder(Order.asc("date"));
		if (VmUtils.isNotEmpObj(shopid)) {
			query.add(Restrictions.eq("shopid", shopid));
		}
		if (StringUtils.isNotBlank(cityname)) {
			query.add(Restrictions.eq("citycode", cityname));
		}
		if (begin != null && end != null) {
			Date time1 = DateUtil.getBeginningTimeOfDay(begin);
			Date time2 = DateUtil.getLastTimeOfDay(end);
			query.add(Restrictions.ge("date", time1));
			query.add(Restrictions.le("date", time2));
		}
		List<ReportOrderDatePaymethod> ReportPaymethodList = this.baseDao.findByCriteria(query, 0, 20000);
		return ReportPaymethodList;
	}

	@Override
	public List<ReportBackSellProductVo> getBackSellList(Long shopid, String cityname, Date date1, Date date2) {
		// 获取售出列表
		String ordertitle = "";// 店铺id
		String begin = "";// 开始时间sql
		String end = "";// 结束时间sql
		String citynamecondition = "";// 所选城市
		List<String> time = getTime(date1, date2);
		if (shopid != null) {
			ordertitle = " AND shopid=" + shopid + " ";
		}
		if (date1 != null) {
			begin = " AND  date >= '" + time.get(0) + "'";
			end = " AND  date <= '" + time.get(1) + "'";
		}
		if (StringUtils.isNotBlank(cityname) && CityContant.ALLCITY.contains(cityname)) {
			citynamecondition = " AND citycode=? ";
		}
		String sql = "SELECT shopid,shopname,sum(netpaid),sum(quantity),date,sellmethod " 
				+ "FROM ReportOrderDatePaymethod "
				+ "WHERE  1=1 " + begin + end + ordertitle + citynamecondition
				+ " GROUP BY shopid,date ORDER BY date ASC";
		List list = null;
		if(StringUtils.isNotBlank(cityname) && CityContant.ALLCITY.contains(cityname)){
			list = this.baseDao.findByHql(sql,cityname);
		}else{
			list = this.baseDao.findByHql(sql);
		}
		List<ReportBackSellProductVo> voList = new ArrayList<ReportBackSellProductVo>();
		for (int i = 0; i < list.size(); i++) {
			ReportBackSellProductVo vo = new ReportBackSellProductVo();
			Object[] arr = (Object[]) list.get(i);
			vo.setShopid(getLnum(arr[0]));
			vo.setShopname(arr[1].toString());
			vo.setSubtotalfee(getNum(arr[2]));
			vo.setSubquantity(getNum(arr[3]));
			vo.setDate(arr[4].toString());
			String method="";
			if (arr[5]!=null) {
				method=arr[5].toString();
			}
			if (StringUtils.isNotBlank(method)) {
				vo.setSellmethod(method);
			}else{
				vo.setSellmethod("");
			}
			voList.add(vo);
		}
		return voList;
	}

	private List<String> getTime(Date time, Date time2) {
		Date beginningTimeOfDay = DateUtil.getBeginningTimeOfDay(time);// 一天的开始的时间
		Date lastTimeOfDay = DateUtil.getLastTimeOfDay(time2);// 一天的结束的时间
		List<String> list = new ArrayList<>();
		String formatTimestamp = DateUtil.formatTimestamp(beginningTimeOfDay);// 转化为yy-mm-dd
		String formatTimestamp2 = DateUtil.formatTimestamp(lastTimeOfDay);
		list.add(formatTimestamp);
		list.add(formatTimestamp2);
		return list;
	}

	private Integer getNum(Object num) {
		if (num == null)
			return 0;
		return Integer.parseInt(num.toString());
	}

	private Long getLnum(Object num) {
		if (num == null)
			return 0L;
		return Long.parseLong(num.toString());
	}
	//直营店id
	private List<Long> companyidList = Arrays.asList(35L);
	//直营活动店id
	private List<Long> activeidList = Arrays.asList(36L);
	
	private List<Long> findshopidList(){
		DetachedCriteria query = DetachedCriteria.forClass(Company.class);
		List<Long> listNull=new ArrayList<>();
		if (!companyidList.isEmpty()) {
			query.add(Restrictions.in("id", companyidList));
			query.setProjection(Projections.property("shopnumber"));
			List<String> companyList = this.baseDao.findByCriteria(query);
			String idString = StringUtils.join(companyList, ",");
			List<Long> shopidList = BeanUtil.getIdList(idString, ",");
			return shopidList;
		}else{
			return listNull;
		}
	}
	private List<Long> findactiveidList(){
		DetachedCriteria query = DetachedCriteria.forClass(Company.class);
		List<Long> listNull=new ArrayList<>();
		if (activeidList.size()>0) {
			query.add(Restrictions.in("id", activeidList));
			query.setProjection(Projections.property("shopnumber"));
			List<String> companyList = this.baseDao.findByCriteria(query);
			String idString = StringUtils.join(companyList, ",");
			List<Long> shopidList = BeanUtil.getIdList(idString, ",");
			return shopidList;
			
		}else{
			return listNull;
		}
	}
	
	//将date转为Timestamp类型
	private Timestamp getDate(Date date){
		String formatDate = DateUtil.formatDate(date);
		Timestamp timestamp = DateUtil.parseTimestamp(formatDate,"yyyy-MM-dd");
		return timestamp;
	}
	@Override
	public ReportSummaryYun findByYunShopSummary(Date time1, Date time2) {
		Timestamp begin = getDate(time1);
		Timestamp end = getDate(time2);
		// 获取自营店数据
		List<Long> shopidList = this.findshopidList();
		// 获取运营店数据
		List<Long> activeidList1 = this.findactiveidList();
		if (activeidList1.size()>0) {
			for (Long activeid : activeidList1) {
				shopidList.add(activeid);
			}
		}
		List list=new ArrayList<>();
		if (!shopidList.isEmpty()) {
			String sql="SELECT shopid,count(distinct date),sum(netpaid),sum(quantity) FROM ReportOrderDatePaymethod "+
					"where shopid in (SELECT shopid FROM ReportOrderDatePaymethod where date=? "
					+ "and shopid NOT IN ("+StringUtils.join(shopidList,",")+") " +
					"GROUP BY shopid) AND date>=? AND date<=? GROUP BY shopid ORDER BY shopid ASC"+")";
			list= this.baseDao.findByHql(sql,end,begin,end);
		}else{
			String sql2="SELECT shopid,count(distinct date),sum(netpaid),sum(quantity) FROM ReportOrderDatePaymethod "+
					"where shopid in(SELECT shopid FROM ReportOrderDatePaymethod where date=? "+
					"GROUP BY shopid) AND date>=? AND date<=? GROUP BY shopid ORDER BY shopid ASC";
			list= this.baseDao.findByHql(sql2,end,begin,end);
		}
		List<Long> list2=new ArrayList<>();
		//查询所有所选时间段运营商id
		String sql2="SELECT shopid FROM ReportOrderDatePaymethod "+
				"where date>=? AND date<=? GROUP BY shopid";
		list2=this.baseDao.findByHql(sql2,begin,end);
		//查询所选时间段所有未停业店id
		String sql3="SELECT shopid FROM ReportOrderDatePaymethod where date=? GROUP BY shopid";
		List<Long> list3=this.baseDao.findByHql(sql3,end);
		//所有停业店铺id
		List<Long> list4=new ArrayList<>();
		List<CafeShop> cafeShops=new ArrayList<CafeShop>();
		for (Long cafeShop : list2) {
			if (!list3.contains(cafeShop)) {
				list4.add(cafeShop);
			}
		}
		if (!list4.isEmpty()) {
			for (Long shopid : list4) {
				cafeShops.add(cafeShopService.getCacheShop(shopid));
			}
		}
		ReportSummaryYun yun=new ReportSummaryYun(new Date());
		//查询所有停业店铺总金额,总杯数,日均销售杯数
		List closeList=new ArrayList<>();
		if (!list4.isEmpty()) {
			String sql4="SELECT shopid,count(distinct date),sum(netpaid),sum(quantity) FROM ReportOrderDatePaymethod "+
					"where shopid IN ("+StringUtils.join(list4,",")+") " +
					"AND date>=? AND date<=? GROUP BY shopid ORDER BY shopid ASC"+")";
			closeList= this.baseDao.findByHql(sql4,begin,end);
		}
		if (!closeList.isEmpty()) {
			Integer num=0;
			Integer num2=0;
			Integer num3=0;
			int dayquantity=0;
			for (int i = 0; i < closeList.size(); i++) {
				Object[] arr = (Object[]) closeList.get(i);
				num = getNum(arr[1]);//天数
				num2+= getNum(arr[2]);//一个店铺当月销售额总计
				num3+= getNum(arr[3]);//一个店铺当月杯数总计
				dayquantity += getNum(arr[3])/num;//每家店当月日均销售杯数
			}
			yun.setSubclosenetpaid(num2);
			yun.setSubclosequantity(num3);
			yun.setAvclosequantity(dayquantity/closeList.size());
			yun.setCloseshopcount(closeList.size());
		}
		
		yun.setCafeShops(cafeShops);
		if (list.size()>0){
			Integer num=0;
			Integer num2=0;
			Integer num3=0;
			int dayquantity=0;
			int daysenetpaid=0;
			for (int i = 0; i < list.size(); i++) {
				 Object[] arr = (Object[]) list.get(i);
				 num = getNum(arr[1]);//天数
				 dayquantity += getNum(arr[3])/num;//每家店当月日均销售杯数
				 num2+= getNum(arr[2]);//一个店铺当月销售额总计
				 num3+= getNum(arr[3]);//一个店铺当月杯数总计
				 daysenetpaid += getNum(arr[2])/num;//每家店日均收入
			}
			yun.setDayshopnetpaid(daysenetpaid/list.size());
			yun.setShopcount(list.size());
			yun.setSubnetpaid(num2);
			yun.setSubquantity(num3);
			yun.setAvshopprice(num2/list.size());
			yun.setAvquantity(dayquantity/list.size());
			yun.setAvnetpaid(num2/num3);
			return yun;
		}else{
			return yun;
		}
		
	}
	@Override
	public ReportSummaryZi findByZiSummary(Date time1, Date time2) {
		Timestamp begin = getDate(time1);
		Timestamp end = getDate(time2);
		// 获取自营店数据
		List<Long> shopidList = this.findshopidList();
		int diffDay = DateUtil.getDiffDay(begin, end)+1;
		List list=new ArrayList<>();
		if (!shopidList.isEmpty()) {
			//每月最后一天没有停业,就不算停业
		/*	String sql1="SELECT shopid,netpaid,quantity FROM ReportOrderDatePaymethod "+
					"where shopid in(SELECT shopid FROM ReportOrderDatePaymethod where date=?"+
					"GROUP BY shopid) AND date>=? AND date<=? GROUP BY date,shopid ORDER BY shopid ASC";*/
			//查询满足条件自营店id
			String sql2="SELECT shopid,count(distinct date),sum(netpaid),sum(quantity) FROM ReportOrderDatePaymethod "+
					"where shopid in (SELECT shopid FROM ReportOrderDatePaymethod where date=? "
					+ "and shopid IN ("+StringUtils.join(shopidList,",")+") " +
					"GROUP BY shopid) AND date>=? AND date<=? GROUP BY shopid ORDER BY shopid ASC"+")";
			list= this.baseDao.findByHql(sql2,end,begin,end);
		}
		ReportSummaryZi zi=new ReportSummaryZi(new Date());
		zi.setDiffDay(diffDay);
		if (list.size()>0) {
		Integer num=0;
		Integer num2=0;
		Integer num3=0;
		int dayquantity=0;
		int daynetpaid=0;
		for (int i = 0; i < list.size(); i++) {
			Object[] arr = (Object[]) list.get(i);
			 num = getNum(arr[1]);//天数
			 dayquantity += getNum(arr[3])/num;//每家店当月日均销售杯数
			 daynetpaid +=getNum(arr[2])/num;//每家店日均销售额 
			 num2+= getNum(arr[2]);//一个店铺当月销售额总计
			 num3+= getNum(arr[3]);//一个店铺当月杯数总计
		}
		zi.setShopcount(list.size());
		zi.setSubnetpaid(num2);
		zi.setSubquantity(num3);
		zi.setAvnetpaid(num2/num3);
		zi.setAvquantity(dayquantity/list.size());
		zi.setDayshopnetpaid(daynetpaid/list.size());
		return zi;
		}else {
			return zi;
		}
	}

	@Override
	public ReportSummaryActive findActiveSummary(Date time1, Date time2) {
		Timestamp begin = getDate(time1);
		Timestamp end = getDate(time2);
		List<Long> activeidList3 = this.findactiveidList();
		// 获取自营活动店统计数据
		List list=new ArrayList<>();
		if (activeidList.size()>0) {
			String sql="SELECT shopid,count(distinct date),sum(netpaid),sum(quantity) FROM ReportOrderDatePaymethod "+
					"where shopid in (SELECT shopid FROM ReportOrderDatePaymethod where date=? "
					+ "and shopid IN ("+StringUtils.join(activeidList3,",")+") " +
					"GROUP BY shopid) AND date>=? AND date<=? GROUP BY shopid ORDER BY shopid ASC"+")";
			list= this.baseDao.findByHql(sql,end,begin,end);
		}
		ReportSummaryActive active=new ReportSummaryActive(new Date());
		if (!list.isEmpty()) {
			Integer num=0;
			Integer num2=0;
			Integer num3=0;
			int dayquantity=0;
			int avprice=0;
			for (int j = 0; j < list.size(); j++) {
				Object[] arr = (Object[]) list.get(j);
				num = getNum(arr[1]);//天数
				dayquantity += getNum(arr[3])/num;//每家店当月日均销售杯数
				num2+= getNum(arr[2]);//一个店铺当月销售额总计
				num3+= getNum(arr[3]);//一个店铺当月杯数总计
				avprice+= getNum(arr[2])/getNum(arr[3]);//每杯均价
			}
			active.setShopcount(list.size());
			active.setSubnetpaid(num2);
			active.setSubquantity(num3);
			active.setAvquantity(dayquantity/list.size());
			active.setAvnetpaid(avprice/list.size());
			return active;
		}else{
			return active;
		}
	}

}




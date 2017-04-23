package com.wheelys.service.report.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.BeanUtil;
import com.wheelys.util.DateUtil;
import com.wheelys.constant.CafeOrderConstant;
import com.wheelys.constant.OrderConstant;
import com.wheelys.constant.PayConstant;
import com.wheelys.model.CafeShop;
import com.wheelys.model.pay.WheelysOrder;
import com.wheelys.model.pay.WheelysOrderDetail;
import com.wheelys.model.report.ReportOrderDatePaymethod;
import com.wheelys.model.report.ReportOrderDateProduct;
import com.wheelys.model.report.ReportOrderMemberShopDate;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.cafe.CafeShopService;
import com.wheelys.service.report.OrderReportService;
import com.wheelys.util.VmUtils;
import com.wheelys.web.action.report.vo.ReportOrderDatePaymethodVo;
import com.wheelys.web.action.report.vo.ReportOrderDateProductVo;

@Service("orderReportService")
public class OrderReportServiceImpl extends BaseServiceImpl implements OrderReportService {
	
	@Autowired
	@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;

	@Override
	public List<ReportOrderDatePaymethod> saveReportByDatePaymenthod(Date date) {
		List<String> time = getTime(date, date);
		String sql = "SELECT shopid,paymethod,ordertitle,sum(totalfee),count(*),sum(quantity),sum(netpaid),"
				+ " sum(discount) FROM WheelysOrder WHERE paystatus= '" + CafeOrderConstant.STATUS_PAID
				+ "' AND paidtime >= '" + time.get(0) + "' " + " AND paidtime <= '" + time.get(1)
				+ "'  GROUP BY shopid, paymethod  ";
		List list = baseDao.findByHql(sql);
		List<ReportOrderDatePaymethod> orderList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			ReportOrderDatePaymethod report = new ReportOrderDatePaymethod();
			Object[] arr = (Object[]) list.get(i);
			report.setShopid(getLnum(arr[0])); // 店铺id
			CafeShop cafeShop = this.baseDao.getObject(CafeShop.class, report.getShopid());
			report.setPaymethod(arr[1].toString());// 支付方式
			report.setShopname(cafeShop.getEsname());// 商铺名字
			report.setDate(date); // 时间
			String ukey = DateUtil.formatDate(report.getDate()) + "_" + report.getShopid() + "_"
					+ report.getPaymethod();// 时间_shopid_paymethod
			ReportOrderDatePaymethod reportOrderDatePaymethod = baseDao.getObjectByUkey(ReportOrderDatePaymethod.class,
					"ukey", ukey);
			if (reportOrderDatePaymethod != null) {
				report = reportOrderDatePaymethod;
			}
			CafeShop shop = cafeShopService.getShop(report.getShopid());
			report.setTotalfee(getNum(arr[3]));
			report.setOrdercount(getNum(arr[4]));
			report.setQuantity(getNum(arr[5]));
			report.setNetpaid(getNum(arr[6])); // 实际杯数
			report.setDiscount(getNum(arr[7])); // 实际金额（分）
			try {
				int paybrokerage = (int) (report.getNetpaid() * 0.006D);
				int comdeduction = (int) (report.getNetpaid() * (shop.getShopproportion() / 100.0));// 公司扣点
				int settlementamount = (report.getNetpaid() - (paybrokerage + comdeduction));
				report.setPaybrokerage(paybrokerage);// 支付平台手续费
				report.setCompanybrokerage(comdeduction);// 公司扣点
				report.setCompanyrate(shop.getShopproportion());// 公司扣点比率
				report.setSettlementamount(settlementamount);
			} catch (Exception e) {
				dbLogger.warn("saveReportByDatePaymenthod shop.getShopid()：" + shop.getShopid());
			}
			report.setUkey(ukey); // 唯一标示
			report.setCitycode(shop.getCitycode());
			orderList.add(report); // 存入集合
			baseDao.saveObject(report);// 将对象存入数据库
		}
		return orderList;
	}

	/**
	 * 获取店铺当天销售情况
	 */
	@Override
	public ReportOrderDatePaymethod getTodayShopReport(Long shopid, Date date) {
		List<String> time = getTime(date, date);
		String sql = "SELECT shopid,sum(quantity),sum(netpaid), count(*),sum(totalfee) "
				+ " FROM WheelysOrder WHERE shopid = " + shopid + " and paystatus= '" + CafeOrderConstant.STATUS_PAID
				+ "' AND paidtime >= '" + time.get(0) + "' " + " AND paidtime <= '" + time.get(1)
				+ "'  GROUP BY shopid ";
		List list = baseDao.findByHql(sql);
		ReportOrderDatePaymethod orderWheelys = new ReportOrderDatePaymethod();
		orderWheelys.setQuantity(0); // 杯数
		orderWheelys.setNetpaid(0); // 金额
		orderWheelys.setOrdercount(0);// 多少单
		if (list.isEmpty())
			return orderWheelys;
		Object[] arr = (Object[]) list.get(0);
		orderWheelys.setShopid(getLnum(arr[0])); // 店铺id
		orderWheelys.setQuantity(getNum(arr[1])); // 杯数
		orderWheelys.setNetpaid(getNum(arr[2])); // 金额
		orderWheelys.setOrdercount(getNum(arr[3]));// 多少单
		return orderWheelys;
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

	/*
	 * 
	 * @一共多少单
	 */
	public Long findAllNum() {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysOrder.class); // 根据字段的区间，查找对象的集合
		query.add(Restrictions.eq("paystatus", CafeOrderConstant.STATUS_PAID));// 等于这个值
		query.setProjection(Projections.rowCount());
		List<Long> result = baseDao.findByCriteria(query);
		return result.get(0);
	}

	/*
	 * 
	 * @当日多少单销售额
	 */
	public void findByToDay(Map page) {
		List<ReportOrderDatePaymethod> list = saveReportByDatePaymenthod(new Date());// 调用查询添加的方法输入今天的时间返回今天的所有数据的集合
		Integer money = 0;// 销售额
		Integer num = 0;// 多少单
		for (ReportOrderDatePaymethod orderwheelys : list) {
			money += orderwheelys.getNetpaid();// 获取销售额相加
			num += orderwheelys.getOrdercount();// 获取多少单相加
		}
		page.put("dayorder", num);// 多少单
		page.put("daysales", money);// 销售额
	}

	/*
	 * 查询会员人数
	 * 
	 */
	public Long findUserCount() {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysMember.class); // 根据字段的区间，查找对象的集合
		query.setProjection(Projections.rowCount());
		List<Long> result = baseDao.findByCriteria(query);
		return result.get(0);
	}

	/*
	 * 页面展示的全部信息
	 * 
	 */

	@Override
	public Map toDayReport() {
		Map page = new HashMap();// 页面展示的信息需要的对象
		findByToDay(page);// 当日多少单，多少销售额
		page.put("user", findUserCount());// 会员人数
		page.put("allsales", findAllNum());// 总销订单
		List<ReportOrderDatePaymethod> reportList = getTJ();
		page.put("reportshop", reportList);// 商家统计包括今日的所有商家
		List<CafeShop> openShopList = cafeShopService.getOpenShopList("open");
		List<Long> idList = BeanUtil.getBeanPropertyList(reportList, "shopid", true);
		for (CafeShop cafeShop : openShopList) {
			if(idList.contains(cafeShop.getShopid())){
				continue;
			}
			ReportOrderDatePaymethod reportOrderDatePaymethod = new ReportOrderDatePaymethod(cafeShop);
			reportList.add(reportOrderDatePaymethod);
		}
		return page;
	}

	// 当日商家销售统计
	private List<ReportOrderDatePaymethod> getTJ() {
		Date date = new Date();
		Date beginningTimeOfDay = DateUtil.getBeginningTimeOfDay(date);// 一天的开始的时间
		Date lastTimeOfDay = DateUtil.getLastTimeOfDay(date);// 一天的结束的时间
		String sql = "SELECT shopid,sum(quantity),sum(netpaid),count(*),sum(discount),sum(totalfee),ordertitle FROM WheelysOrder WHERE paystatus= '"
				+ CafeOrderConstant.STATUS_PAID + "' AND  createtime >= '"
				+ DateUtil.formatTimestamp(beginningTimeOfDay) + "' and" + " createtime <= '"
				+ DateUtil.formatTimestamp(lastTimeOfDay) + "' GROUP BY shopid";
		List list = baseDao.findByHql(sql);
		List<ReportOrderDatePaymethod> reportList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			ReportOrderDatePaymethod reportOrderDatePaymethod = new ReportOrderDatePaymethod();
			Object[] arr = (Object[]) list.get(i);
			reportOrderDatePaymethod.setShopid(getLnum(arr[0])); // 店铺id
			CafeShop cafeShop = this.baseDao.getObject(CafeShop.class, reportOrderDatePaymethod.getShopid());
			reportOrderDatePaymethod.setShopname(cafeShop.getEsname());// 店铺名字
			reportOrderDatePaymethod.setQuantity(getNum(arr[1]));// 当日杯数
			reportOrderDatePaymethod.setNetpaid(getNum(arr[2]));// 当日销售额
			reportOrderDatePaymethod.setOrdercount(getNum(arr[3]));// 多少单
			reportOrderDatePaymethod.setDiscount(getNum(arr[4]));// 优惠额
			reportOrderDatePaymethod.setTotalfee(getNum(arr[5]));// 原价
			reportList.add(reportOrderDatePaymethod);
		}
		Collections.sort(reportList, new PropertyComparator("netpaid", false, false));
		return reportList;
	}

	/*
	 * 根据商铺id和时间查询商铺这段时间每日的销售信息
	 * 
	 */
	@Override
	public Map<String, ReportOrderDateProductVo> toDayReportProduct(Long shopid, Date date) {
		String ordertitle = "";
		String begin = "";// 开始时间sql
		String end = "";// 结束时间sql
		List<String> time = getTime(date, date);
		if (shopid != null) {
			ordertitle = " AND b.shopid=" + shopid + " ";
		}
		if (date != null) {
			begin = " AND b.paidtime >= '" + time.get(0) + "'";
			end = " AND b.paidtime <= '" + time.get(1) + "'";
		}

		String sql = "SELECT a.productid ,a.productname ,a.price ,sum(a.quantity) ,sum(a.totalfee) ,sum(a.paidfee),b.ordertitle,sum(a.discount) ,sum(a.discountnum),b.shopid "
				+ "FROM WheelysOrderDetail a  JOIN  WheelysOrder b ON  a.orderid=b.id " + "WHERE b.paystatus= '"
				+ CafeOrderConstant.STATUS_PAID + "'" + begin + end + ordertitle
				+ " GROUP BY b.shopid ,a.productid ORDER BY b.shopid ASC, sum(a.quantity) DESC";
		List list = baseDao.findByHql(sql);
		Map<String, ReportOrderDateProductVo> voMap = new HashMap<String, ReportOrderDateProductVo>();
		for (int i = 0; i < list.size(); i++) {
			Object[] arr = (Object[]) list.get(i);
			ReportOrderDateProduct reportOrderDateProduct = new ReportOrderDateProduct();
			reportOrderDateProduct.setShopid(getLnum(arr[9]));
			reportOrderDateProduct.setProductid(getLnum(arr[0]));// 商品id
			reportOrderDateProduct.setDate(date);// 时间
			reportOrderDateProduct.setUkey(DateUtil.formatDate(date) + "_" + getLnum(arr[9]) + "_" + getLnum(arr[0]));
			ReportOrderDateProduct rodp = baseDao.getObjectByUkey(ReportOrderDateProduct.class, "ukey",
					reportOrderDateProduct.getUkey());
			if (rodp != null) {
				reportOrderDateProduct = rodp;
			}
			CafeShop cafeShop = this.baseDao.getObject(CafeShop.class, reportOrderDateProduct.getShopid());
			reportOrderDateProduct.setShopname(cafeShop.getEsname());// 商铺名字
			reportOrderDateProduct.setProductname(arr[1].toString());// 商品名字
			reportOrderDateProduct.setPrice(getNum(arr[2]));// 单价
			reportOrderDateProduct.setQuantity(getNum(arr[3]));// 总数
			reportOrderDateProduct.setTotalfee(getNum(arr[4]));// 总价
			reportOrderDateProduct.setPaidfee(getNum(arr[5]));// 实付
			reportOrderDateProduct.setDiscount(getNum(arr[7]));// 优惠金额
			reportOrderDateProduct.setDiscountnum(getNum(arr[8]));// 优惠杯数
			reportOrderDateProduct.setBomcost(i);
			this.baseDao.saveObject(reportOrderDateProduct);
			ReportOrderDateProductVo vo = new ReportOrderDateProductVo(reportOrderDateProduct);
			String key = "productid:" + getLnum(arr[0]);
			if (voMap.get(key) != null) {
				vo = voMap.get(key);
			}
			vo.setQuantity(vo.getQuantity() + reportOrderDateProduct.getQuantity());
			vo.setTotalfee(vo.getTotalfee() + reportOrderDateProduct.getTotalfee());
			vo.setDiscount(vo.getDiscount() + reportOrderDateProduct.getDiscount());
			vo.setPaidfee(vo.getPaidfee() + reportOrderDateProduct.getPaidfee());
			vo.setDiscountnum(vo.getDiscountnum() + reportOrderDateProduct.getDiscountnum());
			voMap.put(key, vo);
		}
		return voMap;
	}

	/*
	 * 根据订单id查询所有的订单详细
	 * 
	 */
	@Override
	public List<WheelysOrderDetail> findAllByOderid(Long orderid) {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysOrderDetail.class); // 根据字段的区间，查找对象的集合
		query.add(Restrictions.eq("orderid", orderid));// 等于这个值
		return baseDao.findByCriteria(query);
	}

	/*
	 * 根据商铺id和时间还有付款方式查询订单列表
	 * 
	 */
	@Override
	public List<WheelysOrder> savefindByfindBy(Long shopid, Date fromdate, Date todate, String paymethod) {
		Timestamp fromtime = DateUtil.getBeginTimestamp(fromdate);// 一天的开始的时间
		Timestamp endtime = DateUtil.getEndTimestamp(todate);// 一天的结束的时间
		DetachedCriteria query = DetachedCriteria.forClass(WheelysOrder.class); // 根据字段的区间，查找对象的集合
		query.add(Restrictions.eq("paystatus", CafeOrderConstant.STATUS_PAID));
		if (shopid != null) {
			query.add(Restrictions.eq("shopid", shopid));// 等于这个值
		}
		query.add(Restrictions.ge("paidtime", fromtime));// 大于等于这个值
		query.add(Restrictions.le("paidtime", endtime)); // 小于等于这个值
		if (StringUtils.isNotBlank(paymethod)) {
			query.add(Restrictions.eq("paymethod", paymethod));// 等于这个值
		}
		return baseDao.findByCriteria(query);
	}

	/*
	 * 将时间转化为一天 的开始时间可一天的结束时间
	 */
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

	@Override
	public List<ReportOrderDatePaymethodVo> findReport(Long shopid, Date time1, Date time2, String mothed) {
		List<String> time = getTime(time1, time2);
		String shopname = "";
		String begin = "";
		String end = "";
		String paythod = "";
		String meth = "";
		String meth1 = "";
		int a = 1;
		if (shopid != null) {
			shopname = " AND shopid=" + shopid + "";
		}
		if (time1 != null) {
			begin = " AND date>='" + time.get(0) + "'";
		}
		if (time2 != null) {
			end = " AND date<='" + time.get(1) + "'";
		}
		if (StringUtils.isNotBlank(mothed) && PayConstant.ALLPAYMETHOD.contains(mothed)) {
			paythod = " AND paymethod='" + mothed + "'";
			meth = ",paymethod";
			meth1 = meth;
			if (mothed.equals("显示所有支付方法")) {
				meth1 = "";
				paythod = "";
			}
			a = 0;
		}
		String sql = "SELECT shopid,shopname,date" + meth
				+ ",SUM(totalfee),SUM(discount),SUM(netpaid),SUM(quantity),SUM(ordercount) FROM ReportOrderDatePaymethod WHERE 1=1 "
				+ " " + shopname + begin + end + paythod + " GROUP BY shopid,shopname,date" + meth1
				+ " ORDER BY shopid ASC,date ASC";
		List list = baseDao.findByHql(sql);
		List<ReportOrderDatePaymethodVo> reportList = new ArrayList<ReportOrderDatePaymethodVo>();
		for (int i = 0; i < list.size(); i++) {
			Object[] arr = (Object[]) list.get(i);
			ReportOrderDatePaymethodVo reportOrderDatePaymethod = new ReportOrderDatePaymethodVo(arr[2].toString());
			reportOrderDatePaymethod.setShopid(getLnum(arr[0]));// 商铺id
			CafeShop ca = baseDao.getObject(CafeShop.class, reportOrderDatePaymethod.getShopid());
			if(ca == null) continue;
			reportOrderDatePaymethod.setShopname(ca.getEsname());// 商铺名字
			reportOrderDatePaymethod.setPaymethod(arr[3].toString());
			reportOrderDatePaymethod.setTotalfee(getNum(arr[4 - a]));// 原价
			reportOrderDatePaymethod.setDiscount(getNum(arr[5 - a]));// 优惠金额
			reportOrderDatePaymethod.setNetpaid(getNum(arr[6 - a]));// 实付金额
			reportOrderDatePaymethod.setQuantity(getNum(arr[7 - a]));// 杯数
			reportOrderDatePaymethod.setOrdercount(getNum(arr[8 - a]));// 单数
			try {
				int paybrokerage = (int) (reportOrderDatePaymethod.getNetpaid() * 0.006D);
				int comdeduction = (int) (reportOrderDatePaymethod.getNetpaid() * (ca.getShopproportion() / 100.0));// 公司扣点
				int settlementamount = (reportOrderDatePaymethod.getNetpaid() - (paybrokerage + comdeduction));
				reportOrderDatePaymethod.setPaybrokerage(paybrokerage);// 支付平台手续费
				reportOrderDatePaymethod.setCompanybrokerage(comdeduction);// 公司扣点
				reportOrderDatePaymethod.setCompanyrate(ca.getShopproportion());// 公司扣点比率
				reportOrderDatePaymethod.setSettlementamount(settlementamount);
			} catch (Exception e) {
				dbLogger.warn("ca.getShopproportion()：" + ca);
			}
			reportList.add(reportOrderDatePaymethod);
		}
		return reportList;
	}

	@Override
	public List<ReportOrderDateProductVo> getReportProductList(Long shopid, Date date1, Date date2,
			String productName) {
		String ordertitle = "";
		String begin = "";// 开始时间sql
		String end = "";// 结束时间sql
		String name = "";
		List<Object> params = new ArrayList<>();
		if (shopid != null) {
			ordertitle = " AND shopid=? ";
			params.add(shopid);
		}
		if (date1 != null) {
			begin = " AND  date >= ? ";
			end = " AND  date <= ? ";
			params.add(DateUtil.getBeginTimestamp(date1));
			params.add(DateUtil.getEndTimestamp(date2));
		}
		if (StringUtils.isNotBlank(productName)) {
			name = " AND productname = ? ";
			params.add(productName);
		}
		String sql = "SELECT productid,productname, date, sum(quantity), sum(paidfee),shopname,sum(totalfee),sum(discount)  "
				+ "FROM ReportOrderDateProduct " + "WHERE  1=1 " + ordertitle + begin + end + name
				+ " GROUP BY productid ,date,shopid ORDER BY date ASC, sum(quantity) DESC";
		String datestr1 = DateUtil.formatDate(date1);// 转化为yy-mm-dd
		String datestr2 = DateUtil.formatDate(date2);
		if (!StringUtils.equals(datestr1, datestr2)) {
			sql = "SELECT productid,productname, date, sum(quantity), sum(paidfee),shopname,sum(totalfee),sum(discount)  "
					+ "FROM ReportOrderDateProduct " + "WHERE  1=1 " + ordertitle + begin + end + name
					+ " GROUP BY productid ,shopid ORDER BY sum(quantity) DESC";
		}
		List list = null;
		if (params.size() > 0) {
			list = baseDao.findByHql(sql, params.toArray());
		} else {
			list = baseDao.findByHql(sql);
		}
		List<ReportOrderDateProductVo> voList = new ArrayList<ReportOrderDateProductVo>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				ReportOrderDateProductVo vo = new ReportOrderDateProductVo();
				Object[] arr = (Object[]) list.get(i);
				vo.setProductname(arr[1].toString());
				if (!StringUtils.equals(datestr1, datestr2)) {
					vo.setDate(datestr1 + " " + datestr2);
				} else {
					vo.setDate(datestr1);
				}
				vo.setQuantity(getNum(arr[3]));
				vo.setPaidfee(getNum(arr[4]));
				vo.setShopname(arr[5].toString());
				vo.setTotalfee(getNum(arr[6]));
				vo.setDiscount(getNum(arr[7]));
				voList.add(vo);
			}
		}
		return voList;
	}

	@Override
	public void insertDailyShopReport(String beginDateStr, String endDateStr) {
		Date beginDate = null;
		Date endDate = null;
		if (VmUtils.isBlank(beginDateStr)) {
			beginDateStr = DateUtil.getCurDateStr();
		}
		beginDate = DateUtil.parseDate(beginDateStr);

		if (VmUtils.isBlank(endDateStr)) {
			endDateStr = DateUtil.getCurDateStr();
		}
		endDate = DateUtil.parseDate(endDateStr);

		Date currentDate = DateUtil.getCurDate();
		while (beginDate.before(currentDate) && beginDate.before(endDate)) {
			generateData(beginDate);
			beginDate = DateUtil.addDay(beginDate, 1);
		}
	}

	@Override
	public void generateData(Date date) {
		Timestamp beginTimestamp = DateUtil.getBeginTimestamp(date);
		Timestamp endTimestamp = DateUtil.getEndTimestamp(date);

		String hql1 = "select shopid, count(tradeno), count(distinct memberid ), sum(netpaid) from WheelysOrder where paidtime >= ? and paidtime <= ? and paystatus = ? group by shopid order by paidtime asc";
		String hql2 = "select firstshopid, count(memberid) from MemberOrderInfo where firstordertime >= ? and firstordertime <= ? and regstertime >= ? and regstertime <= ? group by firstshopid";
		String hql3 = "select firstshopid, count(memberid) from MemberOrderInfo where firstordertime >= ? and firstordertime <= ? and regstertime < ? group by firstshopid";

		List orderList = baseDao.findByHql(hql1, beginTimestamp, endTimestamp, OrderConstant.PAYSTATUS_PAID);
		List infoList1 = baseDao.findByHql(hql2, beginTimestamp, endTimestamp, beginTimestamp, endTimestamp);
		List infoList2 = baseDao.findByHql(hql3, beginTimestamp, endTimestamp, beginTimestamp);

		Map<Long, ReportOrderMemberShopDate> map = new HashMap<>();

		if (!VmUtils.isEmptyList(orderList)) {
			for (int i = 0; i < orderList.size(); i++) {
				Object[] orderArr = (Object[]) orderList.get(i);
				Long shopid = getNum(orderArr[0]).longValue();
				CafeShop shop = baseDao.getObject(CafeShop.class, shopid);
				String dailyKey = shopid.toString() + "_" + DateUtil.formatDate(date);
				ReportOrderMemberShopDate report = baseDao.getObjectByUkey(ReportOrderMemberShopDate.class, "dailykey",
						dailyKey);
				if (VmUtils.isEmpObj(report)) {
					report = new ReportOrderMemberShopDate(shopid, shop.getEsname(), date);
				}
				report.setShopid(shopid);
				report.setShopname(shop.getEsname());
				report.setDay(date);
				report.setDailykey(shopid.toString() + "_" + DateUtil.formatDate(date));
				report.setSumdailyordernum(getNum(orderArr[1]));
				report.setSumdailyorderusernum(getNum(orderArr[2]));
				report.setSumdailyorderpaidfee(getNum(orderArr[3]));
				map.put(shopid, report);
			}

			if (!VmUtils.isEmptyList(infoList1)) {
				for (int i = 0; i < infoList1.size(); i++) {
					Object[] infoArr = (Object[]) infoList1.get(i);
					Long shopid = getNum(infoArr[0]).longValue();
					ReportOrderMemberShopDate report = map.get(shopid);
					if (VmUtils.isNotEmpObj(report)) {
						report.setNewregisteredmemberorder(getNum(infoArr[1]));
					}
				}
			}
			if (!VmUtils.isEmptyList(infoList2)) {
				for (int i = 0; i < infoList2.size(); i++) {
					Object[] infoArr = (Object[]) infoList2.get(i);
					Long shopid = getNum(infoArr[0]).longValue();
					ReportOrderMemberShopDate report = map.get(shopid);
					if (VmUtils.isNotEmpObj(report)) {
						report.setFirstordernum(getNum(infoArr[1]));
						report.setFirstorderusernum(getNum(infoArr[1]));
					}
				}
			}
			baseDao.saveObjectList(map.values());
		}
	}

	@Override
	public List<ReportOrderDateProductVo> getReportProductListByDate(Long shopid, Date date1, Date date2) {
		String ordertitle = "";
		String begin = "";// 开始时间sql
		String end = "";// 结束时间sql
		List<String> time = getTime(date1, date2);
		if (shopid != null) {
			ordertitle = " AND shopid=" + shopid + " ";
		}
		if (date1 != null) {
			begin = " AND  date >= '" + time.get(0) + "'";
			end = " AND  date <= '" + time.get(1) + "'";
		}
		String sql = "SELECT productid,productname, date, sum(quantity), sum(paidfee),shopname,sum(totalfee),sum(discount)  "
				+ "FROM ReportOrderDateProduct " + "WHERE  1=1 " + begin + end + ordertitle
				+ " GROUP BY productid ,date,shopid ORDER BY date ASC, sum(quantity) DESC";
		List list = baseDao.findByHql(sql);
		List<ReportOrderDateProductVo> voList = new ArrayList<ReportOrderDateProductVo>();
		for (int i = 0; i < list.size(); i++) {
			ReportOrderDateProductVo vo = new ReportOrderDateProductVo();
			Object[] arr = (Object[]) list.get(i);
			vo.setProductid(getLnum(arr[0]));
			vo.setProductname(arr[1].toString());
			vo.setDate(arr[2].toString());
			vo.setQuantity(getNum(arr[3]));
			vo.setPaidfee(getNum(arr[4]));
			vo.setShopname(arr[5].toString());
			vo.setTotalfee(getNum(arr[6]));
			vo.setDiscount(getNum(arr[7]));
			voList.add(vo);
		}
		return voList;
	}

	@Override
	public void generamethodteData(Date date) {
		// 查询一天的各店铺的销售方式
		Timestamp beginTimestamp = DateUtil.getBeginTimestamp(date);
		Timestamp endTimestamp = DateUtil.getEndTimestamp(date);
		String sql = "SELECT shopid,disreason FROM WheelysOrder WHERE "
				+ "paidtime>=? AND paidtime<=? group by shopid,disreason";
		List list = this.baseDao.findByHql(sql, beginTimestamp, endTimestamp);
		Map<Long, String> methodMap = new HashMap<Long, String>();

		for (int i = 0; i < list.size(); i++) {
			Object[] arr = (Object[]) list.get(i);
			Long shopid = 0l;
			String method = "";
			if (arr != null && arr.length > 0) {
				shopid = getLnum(arr[0]);
				if (arr[1] != null) {
					method = arr[1].toString();
				} else {
					method = "原价";
				}
			}
			if (methodMap.containsKey(shopid)) {
				List<String> enList = new ArrayList<>();
				String entry = methodMap.get(shopid);
				enList.add(entry);
				enList.add(method);
				String join = StringUtils.join(enList, ",");
				methodMap.put(shopid, join);
			} else {
				methodMap.put(shopid, method);
			}
		}
		DetachedCriteria query = DetachedCriteria.forClass(ReportOrderDatePaymethod.class);
		query.add(Restrictions.ge("date", beginTimestamp));
		query.add(Restrictions.le("date", endTimestamp));
		List<ReportOrderDatePaymethod> ReportOrderDatePaymethodList = this.baseDao.findByCriteria(query);
		for (ReportOrderDatePaymethod Paymethod : ReportOrderDatePaymethodList) {
			Paymethod.setSellmethod(methodMap.get(Paymethod.getShopid()));
		}
		this.baseDao.updateObjectList(ReportOrderDatePaymethodList);
	}
}

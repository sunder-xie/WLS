package com.wheelys.service.report.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.DateUtil;
import com.wheelys.model.report.ReportOrderMemberShopDate;
import com.wheelys.service.report.ShopReportService;
import com.wheelys.util.VmUtils;
import com.wheelys.web.action.report.vo.ReportOrderMemberShopDateVo;

@Service("shopReportService")
public class ShopReportServiceImpl extends BaseServiceImpl implements ShopReportService {

	@Override
	public List<ReportOrderMemberShopDate> getLatestReport() {
		Date curDate = DateUtil.getCurDate();
		DetachedCriteria query = DetachedCriteria.forClass(ReportOrderMemberShopDate.class);
		query.add(Restrictions.eq("day", curDate));
		query.addOrder(Order.asc("day"));
		List<ReportOrderMemberShopDate> reportList = baseDao.findByCriteria(query);
		return reportList;
	}

	@Override
	public List<ReportOrderMemberShopDateVo> getReportList(Long shopid, Date begin, Date end) {
		DetachedCriteria query = DetachedCriteria.forClass(ReportOrderMemberShopDate.class);
		query.addOrder(Order.asc("day"));
		if(VmUtils.isNotEmpObj(shopid)){
			query.add(Restrictions.eq("shopid", shopid));
		}
		if(VmUtils.isEmpObj(begin)){
			begin = DateUtil.addDay(DateUtil.getCurDate(), -1);
		}
		if(VmUtils.isEmpObj(end)){
			end = DateUtil.getCurDate();
		}
		query.add(Restrictions.ge("day", begin));
		query.add(Restrictions.le("day", end));
		List<ReportOrderMemberShopDate> reportList = baseDao.findByCriteria(query);
		List<ReportOrderMemberShopDateVo> reportVoList = new ArrayList<ReportOrderMemberShopDateVo>();
		Map<Long, ReportOrderMemberShopDateVo> reportMap = new HashMap<Long, ReportOrderMemberShopDateVo>();
		for (ReportOrderMemberShopDate report : reportList) {
			if(shopid != null){
				ReportOrderMemberShopDateVo reportVo = new ReportOrderMemberShopDateVo(report);
				reportVoList.add(reportVo);
			}else{
				ReportOrderMemberShopDateVo reportVo = reportMap.get(report.getShopid());
				if(reportVo == null){
					reportVo = new ReportOrderMemberShopDateVo(report);
					reportVo.setDay(DateUtil.formatDate(begin) + "  " + DateUtil.formatDate(end));
					reportVoList.add(reportVo);
					reportMap.put(report.getShopid(), reportVo);
				}else{
					reportVo.setNewregisteredmemberorder(reportVo.getNewregisteredmemberorder()+report.getNewregisteredmemberorder());
					reportVo.setFirstordernum(reportVo.getFirstordernum()+report.getFirstordernum());
					reportVo.setFirstorderusernum(reportVo.getFirstorderusernum()+report.getFirstorderusernum());
					reportVo.setRepurchasenum(reportVo.getRepurchasenum()+report.getRepurchasenum());
					reportVo.setSumdailyordernum(reportVo.getSumdailyordernum()+report.getSumdailyordernum());
					reportVo.setSumdailyorderpaidfee(reportVo.getSumdailyorderpaidfee()+report.getSumdailyorderpaidfee());
					reportVo.setSumdailyorderusernum(reportVo.getSumdailyorderusernum()+report.getSumdailyorderusernum());
				}
			}
		}
		return reportVoList;
	}

}

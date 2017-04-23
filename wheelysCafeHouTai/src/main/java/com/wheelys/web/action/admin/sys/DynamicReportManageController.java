package com.wheelys.web.action.admin.sys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

import com.wheelys.dao.Dao;
import com.wheelys.model.acl.User;
import com.wheelys.support.ErrorCode;
import com.wheelys.util.BeanUtil;
import com.wheelys.util.BindUtils;
import com.wheelys.util.ValidateUtil;
import com.wheelys.model.dynamicreport.DynamicReport;
import com.wheelys.service.sys.DynamicReportHelper;
import com.wheelys.service.sys.DynamicReportService;
import com.wheelys.web.action.AnnotationController;

@Controller
public class DynamicReportManageController extends AnnotationController{
	@Autowired@Qualifier("dynamicReportService")
	private DynamicReportService dynamicReportService;
	@Autowired@Qualifier("baseDao")
	private Dao baseDao;
	
	@RequestMapping("/admin/sysmgr/report/dynamicReportList.xhtml")
	public String reportList(ModelMap model){
		List<DynamicReport> reportList = dynamicReportService.getDynReportList();
		Map<String, DynamicReport> reportMap = BeanUtil.groupBeanList(reportList, "category");
		model.put("reportMap", reportMap);
		return "admin/dynamicreport/dynamicReportList.vm";
	}
	@RequestMapping("/admin/sysmgr/report/modifyReport.xhtml")
	public String modifyReport(Long rid, ModelMap model){
		DynamicReport report = null;
		if(rid!=null){
			report = baseDao.getObject(DynamicReport.class, rid);
			model.put("report", report);
		}
		return "admin/dynamicreport/modifyDynamicReport.vm";
	}
	@RequestMapping("/admin/sysmgr/report/saveReport.xhtml")
	public String saveReport(Long rid, HttpServletRequest request, ModelMap model){
		DynamicReport report = null;
		if(rid!=null){
			report = baseDao.getObject(DynamicReport.class, rid);
		}else{
			report = new DynamicReport();
		}
		BindUtils.bindData(report, request.getParameterMap());
		User user = getLogonMember();
		if(!StringUtils.startsWithIgnoreCase(report.getQrysql(), "select")){
			return showJsonError(model, "must start with select");
		}
		ErrorCode code = dynamicReportService.saveReport(report, user);
		if(code.isSuccess()) return "redirect:/admin/sysmgr/report/dynamicReportList.xhtml";
		return showJsonError(model, code.getMsg());
	}
	@RequestMapping("/admin/sysmgr/report/getQry.xhtml")
	public String getQry(Long rid, ModelMap model){
		DynamicReport report = baseDao.getObject(DynamicReport.class, rid);
		User user = getLogonMember();
		DynamicReportHelper helper = new DynamicReportHelper(report);
		dynamicReportService.checkRights(report, user);
		model.put("helper", helper);
		model.put("report", report);
		return "admin/dynamicreport/qryForm.vm";
	}
	@RequestMapping("/admin/sysmgr/report/executeReport.xhtml")
	public String executeReport(Long rid, String auth, HttpServletRequest request, HttpServletResponse response, String download, ModelMap model){
		final DynamicReport report = baseDao.getObject(DynamicReport.class, rid);
		User user = getLogonMember();
		DynamicReportHelper helper = new DynamicReportHelper(report, request);
		dynamicReportService.checkRights(report, user);
		model.put("helper", helper);
		model.put("report", report);
		List<Map<String, Object>> dataList = dynamicReportService.getReportDataList(report, 0, helper.getParameterList(), user);
		boolean needAuth = StringUtils.containsIgnoreCase(report.getQrysql(), "mobile");
		if(needAuth && StringUtils.isNotBlank(auth)){
			//String t = StringUtil.md5(report.getQrysql());
			//
		}
		List<String> fieldList = new ArrayList<>();
		if(!dataList.isEmpty()){
			fieldList.addAll(dataList.get(0).keySet());
		}
		Collections.sort(fieldList, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				int idx1 = StringUtils.indexOfIgnoreCase(report.getQrysql(), o1);
				int idx2 = StringUtils.indexOfIgnoreCase(report.getQrysql(), o2);
				return idx1 - idx2;
			}
		});
		model.put("fieldList", fieldList);
		for(Map<String, Object> data: dataList){//过滤手机号
			for(String key: fieldList){
				Object v = data.get(key);
				if(v!=null && v instanceof String){
					String m = StringUtils.trim((String) v);
					if(m.length()==11 && ValidateUtil.isMobile(m)){
						
					}
				}
			}
		}
		model.put("dataList", dataList);
		if(StringUtils.isNotBlank(download)){
			download(download, response);
			return "admin/dynamicreport/reportResultTxt.vm";	
		}else{
			return "admin/dynamicreport/reportResult.vm";	
		}
		
	}
}

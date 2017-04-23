package com.wheelys.service.sys;

import java.util.List;
import java.util.Map;

import com.wheelys.model.acl.User;
import com.wheelys.support.ErrorCode;
import com.wheelys.model.dynamicreport.DynamicReport;

public interface DynamicReportService {
	/**
	 * 获取所有动态报表
	 * @return
	 */
	List<DynamicReport> getDynReportList();

	ErrorCode saveReport(DynamicReport report, User user);
	/**
	 * 检查报表的招行权限
	 * @param report
	 * @param user
	 */
	void checkRights(DynamicReport report, User user);

	List<Map<String, Object>> getReportDataList(DynamicReport report, int from, List params, User user);
	
}

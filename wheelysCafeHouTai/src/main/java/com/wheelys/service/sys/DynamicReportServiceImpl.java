package com.wheelys.service.sys;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.wheelys.dao.Dao;
import com.wheelys.model.acl.User;
import com.wheelys.support.ErrorCode;
import com.wheelys.untrans.monitor.MonitorService;
import com.wheelys.untrans.monitor.SysLogType;
import com.wheelys.model.dynamicreport.DynamicReport;
@Service("dynamicReportService")
public class DynamicReportServiceImpl implements DynamicReportService {
	@Autowired@Qualifier("baseDao")
	private Dao baseDao;
	@Autowired@Qualifier("monitorService")
	private MonitorService monitorService;
	@Autowired@Qualifier("hibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	private List<Map<String, Object>> queryMapBySQL(final String sql, final int from,
			final int maxnum,final  Object... params) {
		List result = (List) hibernateTemplate.execute(new HibernateCallback(){
			public List doInHibernate(Session session) {
				Query query = session.createSQLQuery(sql);
				query.setFirstResult(from).setMaxResults(maxnum).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if (params != null) {
					int i = 0;
					for (int length = params.length; i < length; i++)
						query.setParameter(i, params[i]);
				}
				return query.list();
			}
		});
		return result;
	}

	@Override
	public List<DynamicReport> getDynReportList() {
		return baseDao.getObjectList(DynamicReport.class, "id", false, 0, 5000);
	}

	@Override
	public ErrorCode saveReport(DynamicReport report, User user) {
		// TODO: 检查报表中的敏感信息，如密码等
		baseDao.saveObject(report);
		return ErrorCode.SUCCESS;
	}

	@Override
	public void checkRights(DynamicReport report, User user) {
		// TODO: 限制权限？？
	}

	@Override
	public List<Map<String, Object>> getReportDataList(DynamicReport report, int from, List params, User user) {
		// TODO: 检查报表中的敏感信息，如密码等
		String sql = report.getQrysql();
		List<Map<String, Object>> result = queryMapBySQL(sql, from, report.getMaxnum(), params.toArray());
		
		Map<String, String> entry = new LinkedHashMap<>();
		entry.put("userid", "" + user.getId());
		entry.put("tag", "execQuery");
		entry.put("sql", sql);
		entry.put("params", StringUtils.join(params, ","));
		monitorService.addSysLog(SysLogType.userOp, entry);

		return result;
	}

}

package com.wheelys.service.admin.impl;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Service;
import com.wheelys.api.vo.ResultCode;
import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.model.CafeShop;
import com.wheelys.model.company.Actity;
import com.wheelys.model.company.Company;
import com.wheelys.model.company.CompanyActivity;
import com.wheelys.service.admin.CompanyActityService;

@Service("operatorActityService")
public class CompanyActityServiceImpl extends BaseServiceImpl implements CompanyActityService {

	@Override
	public List<CompanyActivity> showList(Integer pageNo, Integer rowsPerPage) {
		DetachedCriteria query = DetachedCriteria.forClass(CompanyActivity.class);
		int from = pageNo * rowsPerPage;
		query.addOrder(Order.desc("id"));
		List<CompanyActivity> operatorActityList = baseDao.findByCriteria(query, from, rowsPerPage);

		return operatorActityList;
	}

	@Override
	public CompanyActivity operator(Long id, String operatorstatus) {
		// TODO Auto-generated method stub
		CompanyActivity operator = this.baseDao.getObject(CompanyActivity.class, id);
		if (StringUtils.isNotBlank(operatorstatus)) {
			operator.setCompanystatus(operatorstatus);
		}
		this.baseDao.saveObject(operator);
		return operator;
	}

	@Override
	public void actity(String actity) {
		Actity actityadd = new Actity(actity);
		this.baseDao.saveObject(actityadd);
	}

	@Override
	public ResultCode<CompanyActivity> addoperator(Long shoid, String operatorinfo) {
		if (StringUtils.isNotBlank(operatorinfo)) {
			CompanyActivity actity = new CompanyActivity(shoid, operatorinfo);
			CafeShop cafeShop = this.baseDao.getObject(CafeShop.class, shoid);
			actity.setCompanyid(cafeShop.getOperatorid());
			Company com = this.baseDao.getObject(Company.class, cafeShop.getOperatorid());
			actity.setCompanyname(com.getName());
			actity.setShopname(cafeShop.getShopname());
			actity.setTelephone(cafeShop.getShoptelephone());
			actity.setContants(cafeShop.getContacts());
			this.baseDao.saveObject(actity);
			return ResultCode.getSuccessReturn(null);
		} else {
			return ResultCode.getFailure("请填写活动内容");
		}
	}

	@Override
	public int findListCount() {
		// TODO Auto-generated method stub
		DetachedCriteria query = DetachedCriteria.forClass(CompanyActivity.class);
		query.setProjection(Projections.rowCount());
		List<Long> result = baseDao.findByCriteria(query);
		return result.get(0).intValue();
	}
}

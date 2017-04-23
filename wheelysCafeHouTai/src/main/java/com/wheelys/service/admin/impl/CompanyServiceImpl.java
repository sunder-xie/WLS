package com.wheelys.service.admin.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.BeanUtil;
import com.wheelys.util.StringUtil;
import com.wheelys.model.CafeShop;
import com.wheelys.model.company.Company;
import com.wheelys.service.admin.CompanyService;
import com.wheelys.web.action.admin.vo.CompanyVo;

@Service("companyService")
public class CompanyServiceImpl extends BaseServiceImpl implements CompanyService {

	@Override
	public void addCompany(Long id, String citycode, String name, String contants,
			String telephone, String password,String account) {
		Company company = this.baseDao.getObject(Company.class, id);
		if (company == null) {
			company = new Company(name, contants, telephone, citycode, account);
		}
		if (StringUtils.isNotBlank(password)) {
			password = StringUtil.md5(password);
			company.setPassword(password);
		}
		if(id!=null){
			company.setCitycode(citycode);
			company.setContants(contants);
			company.setName(name);
			company.setTelephone(telephone);
			company.setPassword(password);
			company.setAccount(account);
		}
		this.baseDao.saveObject(company);
	}

	@Override
	public List<CompanyVo> showList(String tradeno, String companyname, String citycode, Integer pageNo, int maxnum) {
		// TODO Auto-generated method stub
		DetachedCriteria query = DetachedCriteria.forClass(Company.class);
		if (StringUtils.isNotBlank(tradeno)) {
			query.add(Restrictions.eq("companyid", tradeno));
		}
		String name = StringUtils.trim(companyname);
		if (StringUtils.isNotBlank(companyname)) {
			query.add(Restrictions.like("name", "%" + name + "%"));
		}
		if (StringUtils.isNotBlank(citycode)) {
			query.add(Restrictions.like("citycode", "%" + citycode + "%"));
		}
		query.add(Restrictions.eq("delstatus", "N"));
		int from = pageNo * maxnum;
		List<Company> showList = baseDao.findByCriteria(query, from, maxnum);
		List<CompanyVo> voList = new ArrayList<>();
		for (Company o : showList) {
			int c = this.findCount(o.getId());
			CompanyVo v = new CompanyVo(o, c);
			voList.add(v);
		}
		return voList;
	}

	@Override
	public int findCompanyCount(String tradeno, String companyname, String citycode) {
		DetachedCriteria query = DetachedCriteria.forClass(Company.class);
		query.add(Restrictions.eq("delstatus", "N"));
		query.setProjection(Projections.rowCount());
		if (StringUtils.isNotBlank(tradeno)) {
			query.add(Restrictions.eq("id", tradeno));
		}
		String name = StringUtils.trim(companyname);
		if (StringUtils.isNotBlank(companyname)) {
			query.add(Restrictions.like("name", "%" + name + "%"));
		}
		if (StringUtils.isNotBlank(citycode)) {
			query.add(Restrictions.like("citycode", "%" + citycode + "%"));
		}
		List<Long> result = baseDao.findByCriteria(query);
		return result.get(0).intValue();
	}

	@Override
	public Company findCompany(Long companyid) {
		Company company = this.baseDao.getObject(Company.class, companyid);
		return company;
	}

	@Override
	public void delstatus(Long companyid) {
		Company company = this.baseDao.getObject(Company.class, companyid);
		if (StringUtils.equals(company.getDelstatus(), "N")) {
			company.setDelstatus("Y");
		}
		this.baseDao.saveObject(company);
	}

	@Override
	public Map<Long, Company> mchProductItemMap() {
		DetachedCriteria query = DetachedCriteria.forClass(Company.class);
		query.add(Restrictions.eq("delstatus", "N"));
		List<Company> companyList = baseDao.findByCriteria(query);
		Map<Long, Company> companyMap = new HashMap<Long, Company>();
		for (Company company : companyList) {
			companyMap.put(company.getId(), company);
		}
		return companyMap;
	}

	@Override
	public void removeCompany(Long shopid, Long companyid) {
		if (shopid != null) {
			CafeShop cafeShop = this.baseDao.getObject(CafeShop.class, shopid);
			String operid = String.valueOf(shopid);
			if (cafeShop.getOperatorid() != companyid) {
				Company company = this.baseDao.getObject(Company.class, cafeShop.getOperatorid());
				if (company == null) {
					saveNumber(shopid, companyid);
				} else {
					List<String> rList = Arrays.asList(company.getShopnumber().split(","));
					List<String> list = new ArrayList<>(rList);
					if (rList.contains(operid)) {
						list.remove(operid);
					}
					String name = StringUtils.join(list, ",");
					company.setShopnumber(name);
					this.baseDao.saveObject(company);
					this.saveNumber(shopid, companyid);
				}
			}
		}
	}

	@Override
	public void saveNumber(Long shopid, Long companyid){
		if (companyid != null) {
			CafeShop cafeShop = this.baseDao.getObject(CafeShop.class, shopid);
			Company company = this.baseDao.getObject(Company.class, companyid);
			if (company == null) {
				company = this.baseDao.getObject(Company.class, cafeShop.getOperatorid());
			}
			String operid = String.valueOf(cafeShop.getShopid());
			if (StringUtils.isNotBlank(company.getShopnumber())) {
				List<String> rList = Arrays.asList(company.getShopnumber().split(","));
				List<String> list = new ArrayList<>(rList);
				if (!rList.contains(operid)) {
					list.add(operid);
				}
				String name = StringUtils.join(list, ",");
				company.setShopnumber(name);
			} else {
				company.setShopnumber(operid);
			}
			this.baseDao.saveObject(company);
		}
	}

	@Override
	public int findCount(Long id) {
		DetachedCriteria query = DetachedCriteria.forClass(Company.class);
		query.add(Restrictions.eq("delstatus", "N"));
		query.add(Restrictions.eq("id", id));
		List<String> li = new ArrayList<>();
		List<Company> opList = baseDao.findByCriteria(query);
		for (Company o : opList) {
			li.add(o.getShopnumber());
		}
		String name = StringUtils.join(li, ",");
		List<Long> pList = BeanUtil.getIdList(name, ",");
		return pList.size();
	}

	@Override
	public boolean companyuser(String account, Long id) {
		DetachedCriteria query = DetachedCriteria.forClass(Company.class);
		query.add(Restrictions.eq("account", account));
		if (id != null) {
			query.add(Restrictions.ne("id", id));
		}
		query.setProjection(Projections.rowCount());
		List<Long> companyList = baseDao.findByCriteria(query);
		return companyList.get(0) > 0;
	}
}

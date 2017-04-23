package com.wheelys.service.admin.impl;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.wheelys.model.acl.User;
import com.wheelys.model.acl.WebModule;
import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.StringUtil;
import com.wheelys.service.admin.CommonService;

@Service("commonService")
public class CommonServiceImpl extends BaseServiceImpl implements CommonService {

	@Override
	public List<WebModule> webList(Long userid) {
		DetachedCriteria query = DetachedCriteria.forClass(WebModule.class);
		User user = this.baseDao.getObject(User.class, userid);
		List<String> rList = Arrays.asList(user.getRolenames().split(","));
		query.add(Restrictions.in("rolenames", rList));
		query.add(Restrictions.eq("display", "Y"));
		query.add(Restrictions.eq("target", "wmainFrame"));
		query.addOrder(Order.asc("matchorder"));
		List<WebModule> moduleList = baseDao.findByCriteria(query);
		return moduleList;
	}
	public static void main(String[] args) {
		System.out.println(StringUtil.md5("testStringUtil.md5"));
	}

	@Override
	public List<WebModule> showWebList(String menucode,Long userid) {
		DetachedCriteria query = DetachedCriteria.forClass(WebModule.class);
		User user = this.baseDao.getObject(User.class, userid);
		List<String> rList = Arrays.asList(user.getRolenames().split(","));
		query.add(Restrictions.in("rolenames", rList));
		query.add(Restrictions.like("menucode", menucode + "%"));
		query.add(Restrictions.eq("display", "Y"));
		query.add(Restrictions.eq("target", "_blank"));
		query.addOrder(Order.asc("matchorder"));
		List<WebModule> showTwowebModuleList = baseDao.findByCriteria(query);
		return showTwowebModuleList;
	}

	@Override
	public Map<WebModule, List<WebModule>> showWebModuleMap(Long userid) {
		Map<WebModule, List<WebModule>> webModuleMap = new LinkedHashMap<WebModule, List<WebModule>>();
		List<WebModule> webModuleList = webList(userid);
		for (WebModule webModule : webModuleList) {
			List<WebModule> showTwoList = showWebList(webModule.getMenucode(),userid);
			webModuleMap.put(webModule, showTwoList);
		}
		return webModuleMap;
	}


}

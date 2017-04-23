package com.wheelys.service.admin.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.wheelys.model.acl.User;
import com.wheelys.model.acl.WebModule;
import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.BeanUtil;
import com.wheelys.service.admin.UserService;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl implements UserService {

	@Override
	public void addUser(User user) {
		this.baseDao.saveObject(user);
	}

	@Override
	public List<User> showStaffList(Integer pageNo, int maxnum) {
		DetachedCriteria query = DetachedCriteria.forClass(User.class);
		query.addOrder(Order.desc("id"));
		query.add(Restrictions.eq("accountEnabled", "Y"));
		int from = pageNo * maxnum;
		List<User> showStaffList = baseDao.findByCriteria(query, from, maxnum);
		return showStaffList;
	}

	@Override
	public int findStaffMangeCount() {
		DetachedCriteria query = DetachedCriteria.forClass(User.class);
		query.add(Restrictions.eq("accountEnabled", "Y"));
		query.setProjection(Projections.rowCount());
		List<Long> result = baseDao.findByCriteria(query);
		return result.get(0).intValue();

	}

	@Override
	public User user(Long id) {
		User user = this.baseDao.getObject(User.class, id);
		return user;
	}

	@Override
	public boolean username(String username, Long userid, String oldPassword) {
		DetachedCriteria query = DetachedCriteria.forClass(User.class);
		if (StringUtils.isNotBlank(username)) {
			query.add(Restrictions.eq("username", username));
		}
		if (StringUtils.isNotBlank(oldPassword)) {
			query.add(Restrictions.eq("password", oldPassword));
		}
		if (userid != null) {
			query.add(Restrictions.ne("id", userid));
		}
		query.setProjection(Projections.rowCount());
		List<Long> nameList = baseDao.findByCriteria(query);
		if (nameList.get(0) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public void delstate(User user) {
		this.baseDao.removeObject(user);
	}

	@Override
	public List<WebModule> webMdouleList() {
		DetachedCriteria query = DetachedCriteria.forClass(WebModule.class);
		query.add(Restrictions.eq("display", "Y"));
		query.add(Restrictions.eq("target", "wmainFrame"));
		query.addOrder(Order.asc("matchorder"));
		List<WebModule> webModuleList = baseDao.findByCriteria(query);
		return webModuleList;

	}

	@Override
	public List<WebModule> showList(String menucode) {
		DetachedCriteria query = DetachedCriteria.forClass(WebModule.class);
		query.add(Restrictions.like("menucode", menucode + "%"));
		query.add(Restrictions.eq("display", "Y"));
		query.add(Restrictions.eq("target", "_blank"));
		query.addOrder(Order.asc("matchorder"));
		List<WebModule> showTwowebModuleList = baseDao.findByCriteria(query);
		return showTwowebModuleList;
	}

	@Override
	public Map<WebModule, List<WebModule>> getWebModuleMap() {
		Map<WebModule, List<WebModule>> webModuleMap = new LinkedHashMap<WebModule, List<WebModule>>();
		List<WebModule> webModuleList = this.webMdouleList();
		for (WebModule webModule : webModuleList) {
			List<WebModule> showTwoList = showList(webModule.getMenucode());
			webModuleMap.put(webModule, showTwoList);
		}
		return webModuleMap;
	}

	@Override
	public List<String> webList(Long userid) {
		DetachedCriteria query = DetachedCriteria.forClass(WebModule.class);
		List<String> s = new ArrayList<String>();
		User user = this.baseDao.getObject(User.class, userid);
		List<String> rList = Arrays.asList(user.getRolenames().split(","));
		query.add(Restrictions.in("rolenames", rList));
		query.add(Restrictions.eq("display", "Y"));
		query.addOrder(Order.asc("matchorder"));
		List<WebModule> webList = baseDao.findByCriteria(query);
		for (WebModule web : webList) {
			s.add(web.getRolenames());
		}
		return s;
	}

	/*
	 * @Override public void UpdateWebModule(Long id, Long userid,String
	 * webidList,String arr, String status) {
	 * 
	 * }
	 * 
	 * }
	 */

	@Override
	public void UpdateWebModule(Long id, String arr, Long userid, String status) {
		User user = this.baseDao.getObject(User.class, userid);
		List<Long> idList = BeanUtil.getIdList(arr, ",");
		Set<String> mencodeSet = new TreeSet<>();
		for (Long d : idList) {
			WebModule webModule = this.baseDao.getObject(WebModule.class, d);
			mencodeSet.add(webModule.getRolenames());
			String oneModule = StringUtils.substring(webModule.getMenucode(), 0, 2);
			DetachedCriteria squery = DetachedCriteria.forClass(WebModule.class);
			squery.add(Restrictions.eq("target", "wmainFrame"));
			squery.add(Restrictions.eq("menucode", oneModule));
			List<WebModule> nameList = baseDao.findByCriteria(squery);
			for (WebModule name : nameList) {
				mencodeSet.add(name.getRolenames());
			}
		}
		List<String> rList = Arrays.asList(user.getRolenames().split(","));
		List<String> list = new CopyOnWriteArrayList<>(rList);
		for (String s : list) {
			if (!mencodeSet.contains(s) && !StringUtils.equals(s, "admin")) {
				list.remove(s);
			}
		}
		String name = StringUtils.join(list, ",");
		user.setRolenames(name);
		this.baseDao.saveObject(user);

		for (String add : mencodeSet) {
			if (!list.contains(add)) {
				list.add(add);
			}
		}
		String newname = StringUtils.join(list, ",");
		user.setRolenames(newname);
		this.baseDao.saveObject(user);
	}

	@Override
	public List<String> urlList() {
		DetachedCriteria query = DetachedCriteria.forClass(WebModule.class);
		query.add(Restrictions.or(Restrictions.eq("target", "wmainFrame"), Restrictions.eq("target", "_blank")));
		query.add(Restrictions.eq("display", "Y"));
		List<WebModule> webList = baseDao.findByCriteria(query);
		List<String> webUrlList = new ArrayList<String>();
		for (WebModule web : webList) {
			webUrlList.add(web.getModuleurl());
		}
		return webUrlList;
	}

	@Override
	public List<String> userUrl(Long userid) {
		User user = user(userid);
		List<String>rolenamesList = Arrays.asList(user.getRolenames().split(","));
		DetachedCriteria query = DetachedCriteria.forClass(WebModule.class);
		query.add(Restrictions.eq("target", "_blank"));
		query.add(Restrictions.eq("display", "Y"));
		List<WebModule> webList = baseDao.findByCriteria(query);
		List<String>urlList = new ArrayList<>();
		for(WebModule module : webList){
			for(String rolenames :rolenamesList){
				if(StringUtils.equals(module.getRolenames(), rolenames)){
					urlList.add(module.getModuleurl());
				}
			}
		}
		return urlList;
	}

	@Override
	public List<String> oneUrl(Long userid) {
		User user = user(userid);
		List<String>rolenamesList = Arrays.asList(user.getRolenames().split(","));
		DetachedCriteria query = DetachedCriteria.forClass(WebModule.class);
		query.add(Restrictions.eq("target", "wmainFrame"));
		query.add(Restrictions.eq("display", "Y"));
		List<WebModule> webList = baseDao.findByCriteria(query);
		List<String>urlList = new ArrayList<>();
		for(WebModule module : webList){
			for(String rolenames :rolenamesList){
				if(StringUtils.equals(module.getRolenames(), rolenames)){
					urlList.add(module.getModuleurl());
				}
			}
		}
		return urlList;
	}
}

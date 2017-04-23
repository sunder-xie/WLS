package com.wheelys.web.action.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.model.acl.User;
import com.wheelys.model.acl.WebModule;
import com.wheelys.util.JsonUtils;
import com.wheelys.util.StringUtil;
import com.wheelys.util.ValidateUtil;
import com.wheelys.web.util.PageUtil;
import com.wheelys.service.admin.UserService;
import com.wheelys.util.WebUtils;
import com.wheelys.web.action.AnnotationController;

@Controller
public class UserController extends AnnotationController {
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@RequestMapping("/admin/staff/showUserList.xhtml")
	public String showStaffList(Integer pageNo, ModelMap model) {
		if (pageNo == null)
			pageNo = 0;
		int rowsPerPage = 10;
		List<User> userList = userService.showStaffList(pageNo, rowsPerPage);
		int count = userService.findStaffMangeCount();
		PageUtil pageUtil = new PageUtil(count, rowsPerPage, pageNo, "admin/staff/showUserList.xhtml");
		Map params = new HashMap();
		pageUtil.initPageInfo(params);
		model.put("pageUtil", pageUtil);
		model.put("userList", userList);
		return "/admin/user/showStaffList.vm";
	}

	/**
	 * 员工修改新增页面展示
	 * 
	 * @return
	 */
	@RequestMapping("/admin/staff/showAddStaff.xhtml")
	public String showAddStaff(Long id, ModelMap model) {
		User user = userService.user(id);
		model.put("user", user);
		return "/admin/user/addUpdateStaff.vm";
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @param nickname
	 * @param mobile
	 * @return新增修改
	 */
	@RequestMapping("/admin/staff/addUpdateStaff.xhtml")
	public String addUpdateStaff(Long id, HttpServletRequest request, String username, String nickname, String password,
			String mobile, ModelMap model) {
		Map<String, String> params = WebUtils.getRequestMap(request);
		User user2 = getLogonMember();
		params.put("username", user2.getUsername());
		dbLogger.warn("params:" + JsonUtils.writeMapToJson(params));
		if (StringUtils.isBlank(username)) {
			return this.showJsonError(model, "用户名不能为空");
		}
		if (StringUtils.isBlank(nickname)) {
			return this.showJsonError(model, "姓名不能为空");
		}
		User user = userService.user(id);
		if (id == null || (user != null && StringUtils.isNotBlank(password))) {
			if (!ValidateUtil.isPassword(password)) {
				return this.showJsonError(model, "密码格式不正确,只能是字母，数字，英文标点，长度6—14位且不能为空！");
			}
		}
		if (StringUtils.isBlank(mobile)) {
			return this.showJsonError(model, "电话不能为空");
		}
		if (!ValidateUtil.isMobile(mobile)) {
			return this.showJsonError(model, "手机号码不正确！");
		}
		if (user == null) {
			user = new User();
		}
		username = StringUtils.trim(username);
		if (!StringUtils.equals(user.getUsername(), username) || id == null) {
			boolean flag = userService.username(username, id, null);
			if (flag)
				return this.showJsonError(model, "用户名已存在");
		}
		if (id != null && StringUtils.isBlank(password)) {
			user.setPassword(user.getPassword());
		} else {
			password = StringUtil.md5(password);
			user.setPassword(password);
		}
		if (id == null) {
			user.setRolenames("admin");
		}
		user.setUsername(username);
		user.setNickname(nickname);
		user.setMobile(mobile);
		user.setAccountEnabled("Y");
		userService.addUser(user);
		return this.showJsonSuccess(model, user);

	}

	/**
	 * 
	 * @param id
	 * @return删除
	 */
	@RequestMapping("/admin/staff/delStaff.xhtml")
	public String delStaff(Long id, ModelMap model) {
		if (id != null) {
			User user = userService.user(id);
			userService.delstate(user);
		}
		return this.showJsonSuccess(model, null);
	}

	/**
	 * 密码修改展示
	 * 
	 * @param shopid
	 * @param address
	 * @return
	 */
	@RequestMapping("/admin/staff/showPassword.xhtml")
	public String showPassword(ModelMap model) {
		User user = getLogonMember();
		model.put("user", user);
		return "/admin/updatepassword/updatePassword.vm";
	}

	/**
	 * 
	 * @param id
	 * @param password1
	 * @param password2
	 * @param password3
	 * @param model
	 * @return密码修改
	 */

	@RequestMapping("/admin/staff/updatePassword.xhtml")
	public String updatePassword(Long id, String oldPassword, HttpServletRequest request, String newPassword,
			String confirmPassword, ModelMap model) {
		Map<String, String> params = WebUtils.getRequestMap(request);
		User user2 = getLogonMember();
		params.put("username", user2.getUsername());
		dbLogger.warn("params:" + JsonUtils.writeMapToJson(params));
		User user = userService.user(id);
		if (!StringUtils.equals(newPassword, confirmPassword)) {
			return this.showJsonError(model, "两次密码输入不一致");
		}
		if (StringUtils.isBlank(oldPassword)) {
			return this.showJsonError(model, "旧密码不能为空");
		}
		if (id != null && StringUtils.isNotBlank(newPassword)) {
			if (!ValidateUtil.isPassword(newPassword)) {
				return this.showJsonError(model, "密码格式不正确,只能是字母，数字，英文标点，长度6—14位且不能为空！");
			}
		}
		String old = StringUtil.md5(oldPassword);
		if (!StringUtils.equals(user.getPassword(), old)) {
			boolean flag = userService.username(null, id, old);
			if (!flag) {
				return this.showJsonError(model, "与旧密码不匹配");
			}
		}
		String password = StringUtil.md5(newPassword);
		user.setPassword(password);
		userService.addUser(user);
		return this.showJsonSuccess(model, user);
	}

	/**
	 * 
	 * @param model菜单显示
	 * @return
	 */
	@RequestMapping("/admin/staff/showWebModule.xhtml")
	public String showWebModule(Long userid, ModelMap model) {
		List<String> webidList = userService.webList(userid);
		model.put("webidList", webidList);
		String webidStr = StringUtils.join(webidList, ",");
		Map<WebModule, List<WebModule>> webModuleMap = userService.getWebModuleMap();
		model.put("webModuleMap", webModuleMap);
		User user = userService.user(userid);
		model.put("webidStr", webidStr);
		model.put("user", user);
		List<WebModule> list = userService.webMdouleList();
		model.put("list", list);
		return "/admin/user/showWebModuleList.vm";
	}

	/**
	 * 
	 * @param修改权限
	 * @return
	 */
	@RequestMapping("/admin/staff/upshowWebModule.xhtml")
	public String delete(Long id, Long userid, String arr, String status, ModelMap model) {
		userService.UpdateWebModule(id, arr, userid, status);
		return this.showJsonSuccess(model, null);
	}
	
}
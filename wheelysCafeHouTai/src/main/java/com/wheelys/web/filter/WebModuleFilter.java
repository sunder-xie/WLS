package com.wheelys.web.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.cons.ApiConstant;
import com.wheelys.model.acl.User;
import com.wheelys.service.WheelysLoginService;
import com.wheelys.service.admin.UserService;
import com.wheelys.util.WebLogger;
import com.wheelys.util.WebUtils;
import com.wheelys.util.WheelysLogger;
import com.wheelys.web.support.token.MemberEncodeAuthenticationToken;
import com.wheelys.web.util.LoginUtils;

/**
 * 权限过滤器
 * 
 * @author dell
 *
 */
public class WebModuleFilter extends OncePerRequestFilter {
	private WheelysLogger dbLogger = WebLogger.getLogger(getClass());
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	@Autowired
	@Qualifier("wheelysLoginService")
	private WheelysLoginService wheelysLoginService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException, NullPointerException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			return;
		}
		User user = new User();
		try {
			if (auth.isAuthenticated() && auth instanceof MemberEncodeAuthenticationToken) {// 登录
				user = ((MemberEncodeAuthenticationToken) auth).getMember();
			}
			if (user == null) {
				String ip = WebUtils.getRemoteIp(request);
				String sessid = LoginUtils.getSessid(request);
				ResultCode<MemberEncodeAuthenticationToken> result = wheelysLoginService
						.getLogonMemberByMemberEncodeAndIp(sessid, ip);
				if (result.isSuccess()) {
					user = result.getRetval().getMember();
				}
			}
		} catch (Exception e) {
			dbLogger.warn("exception", e);
		}

		if (StringUtils.isBlank(request.getRequestURI())) {
			ApiFilterHelper.writeErrorResponse(response, ApiConstant.CODE_PARAM_ERROR, "参数有误");
			return;
		} else {
			if (user != null) {
				List<String> allList = userService.urlList();
				List<String> twoUrlList = userService.userUrl(user.getId());
				List<String> oneUrlList = userService.oneUrl(user.getId());
				if (allList.contains(request.getRequestURI().replace("/houtai/", ""))
						|| StringUtils.equals("/houtai/admin/common/menu.xhtml", request.getRequestURI())
						|| StringUtils.equals("/houtai/admin/index.xhtml", request.getRequestURI())) {

					if (StringUtils.equals("/houtai/admin/common/menu.xhtml", request.getRequestURI())
							|| StringUtils.equals("/houtai/admin/index.xhtml", request.getRequestURI())) {
						filterChain.doFilter(request, response);
						return;
					}
					if (twoUrlList.contains(request.getRequestURI().replace("/houtai/", ""))) {
						filterChain.doFilter(request, response);
						return;
					} else {
						request.getRequestDispatcher("/404.vm").forward(request, response);
						return;
					}

				} else if (!allList.contains(request.getRequestURI().replace("/houtai/", ""))) {
					String[] arrurl = request.getRequestURI().split("/");
					String stringurl = arrurl[2] + "/" + arrurl[3];
					for (String url : allList) {
						if (url.startsWith(stringurl)) {
							for (String userurl : oneUrlList) {
								if (userurl.contains(stringurl)) {
									filterChain.doFilter(request, response);
									return;
								}
							}
						}
					}
				}
			} else {
				request.getRequestDispatcher("/404.vm").forward(request, response);
				return;
			}
			request.getRequestDispatcher("/404.vm").forward(request, response);
			return;
		}
	}

	@Override
	public void initFilterBean() throws ServletException {
	}

	@Override
	public void destroy() {
	}

}

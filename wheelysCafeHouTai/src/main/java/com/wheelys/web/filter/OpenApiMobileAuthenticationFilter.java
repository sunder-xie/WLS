package com.wheelys.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.wheelys.util.WheelysLogger;
import com.wheelys.util.WebLogger;

/**
 * API2.0身份过滤器
 * 
 * @author taiqichao
 * 
 */
public class OpenApiMobileAuthenticationFilter extends OncePerRequestFilter  {
	private final transient WheelysLogger dbLogger = WebLogger.getLogger(getClass());
	
	private final static List<String> IGNORE_MEMBER_URILIST = new ArrayList<>();
	static {
		IGNORE_MEMBER_URILIST.add("/openapi/mobile/cinema/query.xhtml");
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try{
			filterChain.doFilter(request, response);
		}catch(Exception e) {
			dbLogger.error(e, 15);
		}
	}
	@Override
	public void initFilterBean() throws ServletException {
	}
	
	@Override
	public void destroy() {
	}
	
}

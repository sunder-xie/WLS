package com.wheelys.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

public class InnerApiAuthFilter extends OncePerRequestFilter {
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		//综上条件校验通过
		try{
			//执行下面方法链
			filterChain.doFilter(request, response);
		}finally{
			//记录成功日志
		}
	}

	@Override
	protected void initFilterBean() throws ServletException {
		
	}
}

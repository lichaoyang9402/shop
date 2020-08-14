package com.log.filter;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
/**   
 * 版权: 版权归    2019 Oxygen Forward 所有
 * @作者: lichaoyang
 * @日期: 2019年7月9日 下午3:59:12  
 * @类描述: 
 *
 */

//@Component
public class MyFilter implements  Filter{
	
	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("初始化1111.。。");
		Filter.super.init(filterConfig);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
	
		String parameter = request.getParameter("name")+"";
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		HttpServletRequest req = (HttpServletRequest)request;
		
		HttpServletResponse res = (HttpServletResponse)response;
		
		
		req.setCharacterEncoding("UTF-8");
		
		res.setContentType("text/html;charset=UTF-8");
		
		
		
		String requestURI = req.getRequestURI();
		
		System.out.println(requestURI);
		
		System.out.println(request.getServerName()+":"+request.getServerPort());
		
		System.out.println(parameter);
		
		
		int remotePort = req.getRemotePort();
		
		
		System.out.println(remotePort);
		
		chain.doFilter(request, response);	
		
		
		
		
	}

	@Override
	public void destroy() {
		Filter.super.destroy();
		
		System.out.println("销毁。。。。");
	}
	
	
}

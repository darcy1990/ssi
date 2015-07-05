package com.winit.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class FilterDemo implements Filter {
	
	private Logger logger = Logger.getLogger(Filter.class);
	
	private String encode = null;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		if (encode != null && !encode.equals("")) {
			req.setCharacterEncoding(encode);
			res.setCharacterEncoding(encode);
		}
		else {
			req.setCharacterEncoding("UTF-8");
			res.setCharacterEncoding("UTF-8");
		}
		logger.info("Filter: " + encode);
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.encode = config.getInitParameter("encode");
	}

}

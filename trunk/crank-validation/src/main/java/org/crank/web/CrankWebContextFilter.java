package org.crank.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CrankWebContextFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		new CrankWebContext(request.getParameterMap(), new RequestMap((HttpServletRequest)request), 
				new SessionMap(((HttpServletRequest)request).getSession()), 
				new CookieMap(((HttpServletRequest) request).getCookies(),(HttpServletResponse)response));
		chain.doFilter(request, response);
		CrankWebContext.clearCrankWebContext();

	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

}

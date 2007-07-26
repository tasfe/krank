package org.crank.web;

import javax.servlet.ServletContext;

public class ServletContextUtils {
	private static ThreadLocal<ServletContext> threadLocal = new ThreadLocal<ServletContext>();
	
	public static void setServletContext(final ServletContext aServletContext) {
		threadLocal.set(aServletContext);
	}
	
	public static ServletContext context() {
		return threadLocal.get();
	}

}

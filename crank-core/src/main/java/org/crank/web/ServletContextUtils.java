package org.crank.web;

import javax.servlet.ServletContext;

/** Stores the ServletContext (application scope) so that is available to anyone on the request thread. 
 * 
 */
public class ServletContextUtils {
    /** Holds the ServletContext in the current thread. */ 
	private static ThreadLocal<ServletContext> threadLocal = new ThreadLocal<ServletContext>();
	
	/** Set the ServletContext in the thread. 
	 * @param aServletContext ServletContext
	 */
	public static void setServletContext(final ServletContext aServletContext) {
		threadLocal.set(aServletContext);
	}
	
	/** Get the Servlet context from the thread.
	 *  @return  ServletContext associated with the thread.
	 */
	public static ServletContext context() {
		return threadLocal.get();
	}

}

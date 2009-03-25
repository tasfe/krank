package org.crank.web;

import javax.servlet.http.HttpServletRequest;
/**
 * Associates the request with the current thread so that it can be easily accessed.
 * @author Rick Hightower, Chris Mathias
 *
 */
public class HttpRequestUtils {
    
	/** holds the request object. */
    private static ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<HttpServletRequest>();
    
    /** Set the request object in the current thread. 
     * @param aHttpRequest the request object
     * */
    public static void setHttpRequest(final HttpServletRequest aHttpRequest) {
        threadLocal.set(aHttpRequest);
    }
    
    /**
     * 
     * @return the request object associated with the current thread.
     */
    public static HttpServletRequest request() {
        return threadLocal.get();
    }

}

package org.crank.web;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestUtils {
    
    private static ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<HttpServletRequest>();
    
    public static void setHttpRequest(final HttpServletRequest aHttpRequest) {
        threadLocal.set(aHttpRequest);
    }
    
    public static HttpServletRequest request() {
        return threadLocal.get();
    }

}

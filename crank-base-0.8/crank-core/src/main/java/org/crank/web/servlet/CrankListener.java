package org.crank.web.servlet;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import org.crank.web.HttpRequestUtils;
import org.crank.web.ServletContextUtils;



public class CrankListener implements ServletRequestListener {

	public void requestDestroyed(ServletRequestEvent sre) {
		ServletContextUtils.setServletContext(null);
        HttpRequestUtils.setHttpRequest( null );
	}

	public void requestInitialized(ServletRequestEvent sre) {
		ServletContextUtils.setServletContext(sre.getServletContext());
        HttpRequestUtils.setHttpRequest( (HttpServletRequest) sre.getServletRequest() );
	}

}

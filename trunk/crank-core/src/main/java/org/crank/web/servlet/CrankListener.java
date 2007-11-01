package org.crank.web.servlet;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import org.crank.web.HttpRequestUtils;
import org.crank.web.ServletContextUtils;


/**
 * This puts the request object and the Servlet context object into TLVs so that they can be easily accessed.
 * @author Rick Hightower
 * @see org.crank.web.HttpRequestUtils
 * @see  org.crank.web.ServletContextUtils
 */
public class CrankListener implements ServletRequestListener {

	/**
	 * Null out the TLVs (Thread Local Variables).
	 * @param sre event
	 */
	public void requestDestroyed(ServletRequestEvent sre) {
		ServletContextUtils.setServletContext(null);
        HttpRequestUtils.setHttpRequest( null );
	}

	/**
	 * Populate TLVs (Thread Local Variables).
	 * @param sre event
	 */
	public void requestInitialized(ServletRequestEvent sre) {
		ServletContextUtils.setServletContext(sre.getServletContext());
        HttpRequestUtils.setHttpRequest( (HttpServletRequest) sre.getServletRequest() );
	}

}

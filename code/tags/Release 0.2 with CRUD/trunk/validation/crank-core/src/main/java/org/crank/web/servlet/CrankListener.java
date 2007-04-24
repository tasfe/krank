package org.crank.web.servlet;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import org.crank.web.ServletContextUtils;



public class CrankListener implements ServletRequestListener {

	public void requestDestroyed(ServletRequestEvent sre) {
		ServletContextUtils.setServletContext(null);
	}

	public void requestInitialized(ServletRequestEvent sre) {
		ServletContextUtils.setServletContext(sre.getServletContext());
	}

}

package org.crank.web.jsf.support;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.crank.annotations.design.DependsOnJSF;
import org.crank.annotations.design.DependsOnSpring;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.jsf.FacesContextUtils;

@DependsOnJSF @DependsOnSpring
public class JSFSpringApplicationContextFinder {
	
	private static ApplicationContext applicationContext;

	public static ApplicationContext getApplicationContext(ServletContext sc) {
		
		if (applicationContext==null) {
			return WebApplicationContextUtils.getWebApplicationContext(sc);
		} else {
			return applicationContext;
		}
	}

	public static ApplicationContext getApplicationContext() {
		ApplicationContext returnAppContext;
		if (applicationContext==null) {
			returnAppContext = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
		} else {
			returnAppContext = applicationContext;
		}
		assert returnAppContext!=null;
		return returnAppContext;
	}

	public static void setApplicationContext(ApplicationContext aapplicationContext) {
		applicationContext = aapplicationContext;
	}

}

package org.crank.web.jsf.support;

import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

import org.crank.annotations.design.DependsOnJSF;
import org.crank.annotations.design.DependsOnSpring;
import org.crank.core.ResourceBundleLocator;
import org.springframework.context.support.MessageSourceResourceBundle;

@DependsOnJSF @DependsOnSpring
public class SpringMessageSourceAsResourceBundleLocator implements ResourceBundleLocator{

	public ResourceBundle getBundle() {
    	return new MessageSourceResourceBundle(JSFSpringApplicationContextFinder.getApplicationContext(), 
    			FacesContext.getCurrentInstance().getViewRoot().getLocale());
	}

}

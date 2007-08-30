package org.crank.web.jsf.support;

import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

import org.crank.annotations.design.DependsOnJSF;
import org.crank.core.ResourceBundleLocator;

@DependsOnJSF
public class JSFResourceBundleLocator implements ResourceBundleLocator {

	public ResourceBundle getBundle() {
		String messageBundle = FacesContext.getCurrentInstance().getApplication().getMessageBundle();
	    ResourceBundle bundle = null;
	    bundle = ResourceBundle.getBundle(messageBundle);
	    assert bundle!=null : "Bundle should not be null";
	    return bundle;
	}

}

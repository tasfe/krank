package org.crank.web.spring.support;
import java.util.ResourceBundle;


import org.crank.core.ResourceBundleLocator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceResourceBundle;

public class SpringResourceBundleLocator implements ResourceBundleLocator, ApplicationContextAware {

	private ApplicationContext applicationContext;
	
	public ResourceBundle getBundle() {
		
    	return new MessageSourceResourceBundle(applicationContext, 
    			LocaleContextHolder.getLocale());
	}

	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException {
		this.applicationContext = aApplicationContext;
		
	}

}

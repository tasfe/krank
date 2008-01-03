package org.crank.web.jsf.support;


import org.crank.core.ResourceBundleLocator;
import org.crank.message.MessageUtils;

public class JSFMessageUtils {
	private static ResourceBundleLocator resourceBundleLocator = new JSFResourceBundleLocator(); 
	
	public static String createLabel(String fieldName) {
		return MessageUtils.getLabel(fieldName, resourceBundleLocator.getBundle());
	}
	
	public static String createLabelNoPlural(String fieldName) {
		return MessageUtils.createLabelNoPlural(fieldName, resourceBundleLocator.getBundle());
	}
	
	public static String createLabelWithNameSpace(String namespace, String fieldName) {
		return MessageUtils.createLabelWithNameSpace(namespace, fieldName, resourceBundleLocator.getBundle());
	}

	public static void setResourceBundleLocator(
			ResourceBundleLocator resourceBundleLocator) {
		JSFMessageUtils.resourceBundleLocator = resourceBundleLocator;
	}
	
}

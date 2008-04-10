package org.crank.web.jsf.support;

import org.crank.core.ResourceBundleLocator;
import org.crank.crud.controller.CrudUtils;
import org.crank.message.MessageUtils;

public class JSFMessageUtils {
	private static ResourceBundleLocator resourceBundleLocator = new JSFResourceBundleLocator();

	public static String createLabel(String fieldName) {
		return MessageUtils.getLabel(fieldName, resourceBundleLocator
				.getBundle());
	}

	public static String createLabelNoPlural(String fieldName) {
		return MessageUtils.createLabelNoPlural(fieldName,
				resourceBundleLocator.getBundle());
	}

	public static String createLabelWithNameSpace(String namespace,
			String fieldName) {
		return MessageUtils.createLabelWithNameSpace(namespace, fieldName,
				resourceBundleLocator.getBundle());
	}

	public static String createToolTipWithNameSpace(String namespace,
			String fieldName, Class<?> clazz) {
		if (CrudUtils.isNameSpaceToolTip(clazz, fieldName)) {
			return MessageUtils.createToolTipWithNameSpace(namespace,
					fieldName, resourceBundleLocator.getBundle(),
					MessageUtils.TOOL_TIP);
		}
		return null;
	}

	public static String createLabelToolTipWithNameSpace(String namespace,
			String fieldName, Class<?> clazz) {
		if (CrudUtils.isNameSpaceToolTip(clazz, fieldName)) {
			return MessageUtils.createToolTipWithNameSpace(namespace,
					fieldName, resourceBundleLocator.getBundle(),
					MessageUtils.LABEL_TOOL_TIP);
		}
		return null;
	}

	public static void setResourceBundleLocator(
			ResourceBundleLocator resourceBundleLocator) {
		JSFMessageUtils.resourceBundleLocator = resourceBundleLocator;
	}

}

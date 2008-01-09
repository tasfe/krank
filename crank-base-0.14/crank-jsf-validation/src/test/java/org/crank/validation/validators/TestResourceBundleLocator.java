package org.crank.validation.validators;

import java.util.ResourceBundle;


import org.crank.core.ResourceBundleLocator;
import org.crank.web.jsf.test.TestConstants;

public class TestResourceBundleLocator implements ResourceBundleLocator {

	public ResourceBundle getBundle() {
		return ResourceBundle.getBundle(TestConstants.TEST_BUNDLE);	
	}

}

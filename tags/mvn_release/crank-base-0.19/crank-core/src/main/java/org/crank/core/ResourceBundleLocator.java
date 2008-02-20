package org.crank.core;

import java.util.ResourceBundle;

import org.crank.annotations.design.ExtentionPoint;

@ExtentionPoint
public interface ResourceBundleLocator {
	public ResourceBundle getBundle();
}

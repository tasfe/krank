package org.crank.core;

import java.io.InputStream;
import java.util.Properties;

public class CrankConstants {
	
	private static Properties props = new Properties();
	
	static {
		try {
			InputStream resourceAsStream = CrankConstants.class.getResourceAsStream("crank.properties");
			if (resourceAsStream!=null) {
				props.load(resourceAsStream);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static final String FRAMEWORK_PREFIX = props.getProperty("FRAMEWORK_PREFIX", "crank");
	public static final String FRAMEWORK_DELIM = props.getProperty("FRAMEWORK_DELIM", "/");
	public static final String OBJECT_REGISTRY = props.getProperty("OBJECT_REGISTRY", 
			"org.crank.core.spring.support.SpringApplicationContextObjectRegistry");
    public static final String LOG = props.getProperty("LOG", 
    "org.crank.core.Log");
	

}

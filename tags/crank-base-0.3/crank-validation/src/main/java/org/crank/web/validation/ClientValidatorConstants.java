package org.crank.web.validation;

import java.io.InputStream;
import java.util.Properties;


/**
 * This defines constants that are unlikely to change.
 * 
 * However, you can change them if you really need to.
 * 
 * If you really need to change these, just create a
 * crank-web.properties file and put it on your classpath.
 * 
 * The name of the property is the same name as the constant.
 * 
 * @author Rick Hightower
 *
 */
public class ClientValidatorConstants {
	
	private static Properties props = new Properties();
	
	static {
		try {
			InputStream resourceAsStream = ClientValidatorConstants.
                  class.getResourceAsStream("crank-web.properties");
			if (resourceAsStream!=null) {
				props.load(resourceAsStream);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
    /** Specifies the validator resource id. */
	public static final String VALIDATOR_RESOURCE_VIEW_ID = props.getProperty("VALIDATOR_RESOURCE_VIEW_ID", "CRANK_validatorResource");
	/** Specifies that the resource will be using an extention called .faces. */
    public static final String EXTENTION = props.getProperty("EXTENTION", ".faces");
	

}

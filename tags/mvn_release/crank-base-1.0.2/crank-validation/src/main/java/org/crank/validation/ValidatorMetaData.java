/**
 * 
 */
package org.crank.validation;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores the validator name 
 * and a list of name value pairs for the validator. */
public class ValidatorMetaData {
	private String name = null;
	
	private Map<String, Object> properties = new HashMap<String, Object>();
	
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	public Map<String, Object> getProperties() {
		return properties;
	}
}
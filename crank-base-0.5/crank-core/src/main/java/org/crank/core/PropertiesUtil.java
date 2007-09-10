package org.crank.core;

import java.util.Map;

public interface PropertiesUtil {
	public void copyProperties(Object object, Map<String, Object> properties);
	public Map<String, Object> getObjectPropertiesAsMap(Object object);
    public Object getPropertyValue(String propertyName, Object object);
}

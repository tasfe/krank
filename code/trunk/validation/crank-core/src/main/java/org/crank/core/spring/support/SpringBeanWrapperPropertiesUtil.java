package org.crank.core.spring.support;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.crank.core.PropertiesUtil;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class SpringBeanWrapperPropertiesUtil implements PropertiesUtil {

	public void copyProperties(Object object, Map<String, Object> properties) {
		BeanWrapper wrapper = new BeanWrapperImpl(object);
		Set<Entry<String, Object>> props = properties.entrySet();
		for (Entry<String, Object> entry : props) {
				if (wrapper.isWritableProperty(entry.getKey())) {
					wrapper.setPropertyValue(entry.getKey(), entry.getValue());
				}
		}
	}

}

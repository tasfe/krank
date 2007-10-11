package org.crank.core.spring.support;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.crank.core.PropertiesUtil;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class SpringBeanWrapperPropertiesUtil implements PropertiesUtil,
		Serializable {
	private static final long serialVersionUID = 1L;

	public void copyProperties(Object object, Map<String, Object> properties) {
		BeanWrapper wrapper = new BeanWrapperImpl(object);
		Set<Entry<String, Object>> props = properties.entrySet();
		for (Entry<String, Object> entry : props) {
			if (wrapper.isWritableProperty(entry.getKey())) {
				wrapper.setPropertyValue(entry.getKey(), entry.getValue());
			}
		}
	}

	public Map<String, Object> getObjectPropertiesAsMap(Object object) {
		BeanWrapper wrapper = new BeanWrapperImpl(object);
		Map<String, Object> properties = new HashMap<String, Object>();
		PropertyDescriptor[] propertyDescriptors = wrapper
				.getPropertyDescriptors();
		for (PropertyDescriptor pd : propertyDescriptors) {
			String name = pd.getName();
			properties.put(name, wrapper.getPropertyValue(name));
		}
		return properties;
	}

	public Object getPropertyValue(String propertyName, Object object) {
		BeanWrapper wrapper = new BeanWrapperImpl(object);
		return wrapper.getPropertyValue(propertyName);
	}

}

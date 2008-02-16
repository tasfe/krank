package org.crank.core;

import java.util.Collection;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class CollectionUtils {

	public static boolean valueExists(String propertyName, Object value,
			Collection<?> collection) {
		
		for (Object o : collection) {
			BeanWrapper bw = new BeanWrapperImpl(o);
			Object propertyValue = bw.getPropertyValue(propertyName);
			if (propertyValue.equals(value)) {
				return true;
			}
		}
		return false;
	}

}

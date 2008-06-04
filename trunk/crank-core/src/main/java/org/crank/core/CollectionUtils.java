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

    public static int countTrue(String propertyName, Collection<?> collection) {

        int count=0;
        for (Object o : collection) {
            BeanWrapper bw = new BeanWrapperImpl(o);
            Boolean propertyValue = (Boolean)bw.getPropertyValue(propertyName);
            if (Boolean.TRUE.equals(propertyValue)) {
                count++;
            }
        }
        return count;
    }

    public static int countFalse(String propertyName, Collection<?> collection) {

        int count=0;
        for (Object o : collection) {
            BeanWrapper bw = new BeanWrapperImpl(o);
            Boolean propertyValue = (Boolean)bw.getPropertyValue(propertyName);
            if (Boolean.FALSE.equals(propertyValue)) {
                count++;
            }
        }
        return count;
    }

}

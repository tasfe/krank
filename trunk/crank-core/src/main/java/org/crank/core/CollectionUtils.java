package org.crank.core;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

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

    public static int countValue(String propertyName, Object value,
            Collection<?> collection) {
		int count=0;
        for (Object o : collection) {
            BeanWrapper bw = new BeanWrapperImpl(o);
            Object propertyValue = bw.getPropertyValue(propertyName);
            if (propertyValue.equals(value)) {
                count++;
            }
        }
        return count;
    }

    public static Collection<?> filterByValue(String propertyName, Object value,
            Collection<?> collection) {
        List<Object> list = new ArrayList<Object>(collection.size());
        for (Object o : collection) {
            BeanWrapper bw = new BeanWrapperImpl(o);
            Object propertyValue = bw.getPropertyValue(propertyName);
            if (propertyValue.equals(value)) {
                list.add(o);
            }
        }
        return list;
    }

    public static Collection<?> filterByNotValue(String propertyName, Object value,
            Collection<?> collection) {
        List<Object> list = new ArrayList<Object>(collection.size());
        for (Object o : collection) {
            BeanWrapper bw = new BeanWrapperImpl(o);
            Object propertyValue = bw.getPropertyValue(propertyName);
            if (!propertyValue.equals(value)) {
                list.add(o);
            }
        }
        return list;
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

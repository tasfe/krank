package org.crank.crud.jsf.support;

import java.io.Serializable;
import org.springframework.beans.BeanWrapperImpl;

public abstract class CrudUtils {
    public static String getIdStr(Object aEntity, String idPropertyName) {
        Serializable id = getId(aEntity, idPropertyName);
        if (id != null) {
            return id.toString();
        }
        return null;
    }

    public static Serializable getId(Object aEntity, String idPropertyName) {
        return (Serializable) new BeanWrapperImpl(aEntity).getPropertyValue(idPropertyName);
    }

    public static Serializable getIdObject(String idValue, Class<?> classTypeOfId) {
        if (classTypeOfId == Long.class) {
            return new Long(idValue);
        }

        if (classTypeOfId == Integer.class) {
            return new Integer(idValue);
        }

        if (classTypeOfId == String.class) {
            return idValue;
        }

        throw new IllegalStateException("Currently only supports Long, Integer and String id types. ");
    }
    
    public static Long getNumRowsToDisplay(Long count, Integer maxRows) {
    	if (count > maxRows) {
    		return maxRows.longValue();
    	}
    	return count;
    }
    
    public static String formatAsUrl( String value ) {
    	StringBuilder result = new StringBuilder();
    	
    	if (value != null) {
    		if (!value.toLowerCase().startsWith("http")) {
    			result.append("http://");
    		}
    		result.append(value);
    	}

    	return result.toString();
    }
    
}

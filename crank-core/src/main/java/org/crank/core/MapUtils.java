package org.crank.core;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.crank.core.spring.support.SpringBeanWrapperPropertiesUtil;

public class MapUtils {
    private static PropertiesUtil propertiesUtil = new SpringBeanWrapperPropertiesUtil();
    
    @SuppressWarnings("unchecked")
    public static Map convertListToMap(String propertyName, Collection list) {
        Map map = new LinkedHashMap(list.size());
        for (Object object : list) {
            map.put( propertiesUtil.getPropertyValue( propertyName, object ), object );
        }
        return map;
    }
    @SuppressWarnings("unchecked")
    public static Map convertArrayToMap(String propertyName, Object [] array) {
        Map map = new LinkedHashMap(array.length);
        for (Object object : array) {
            map.put( propertiesUtil.getPropertyValue( propertyName, object ), object );
        }
        return map;
    }

}

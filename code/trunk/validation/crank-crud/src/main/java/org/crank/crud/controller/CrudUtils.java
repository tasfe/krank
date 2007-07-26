package org.crank.crud.controller;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.crank.core.AnnotationData;
import org.crank.core.AnnotationUtils;
import org.crank.core.MapUtils;


public class CrudUtils {
    private static Set<String> allowedPackages = new HashSet<String>();
    static {
        allowedPackages.add( "javax.persistence" );
    }
    
    public static boolean isManyToOne(Class clazz, String propertyName) {
        List<AnnotationData> annotationDataForProperty = AnnotationUtils.getAnnotationDataForProperty( clazz, propertyName, false, allowedPackages );
        if (annotationDataForProperty.size()==0) {
            annotationDataForProperty = AnnotationUtils.getAnnotationDataForField( clazz, propertyName, allowedPackages );
        }
        Map map = MapUtils.convertListToMap( "name", annotationDataForProperty);
        return map.get( "manyToOne" ) != null; 
    }
    
    @SuppressWarnings("unchecked")
    public static String getPropertyEntityName(Class clazz, String propertyName) {
        Map<String, PropertyDescriptor> map = null;
        
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo( clazz );
            map = MapUtils.convertArrayToMap("name", beanInfo.getPropertyDescriptors());
        } catch (IntrospectionException e) {
            throw new RuntimeException (e);
        }
        
        PropertyDescriptor descriptor = map.get(propertyName);
        return descriptor.getPropertyType().getSimpleName();
    }
}

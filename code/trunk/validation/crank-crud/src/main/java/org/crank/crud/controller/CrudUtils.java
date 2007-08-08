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
    
    public static boolean isRequired(Class clazz, String propertyName) {
        
        PropertyDescriptor descriptor = getPropertyDescriptor( clazz, propertyName);
        if (descriptor.getPropertyType().isPrimitive()) {
            return true;
        }
        
        Map map = getAnnotationDataAsMap( clazz, propertyName );
        
        boolean found = map.get( "required" ) != null;
        /* If you found an annotation called required, return true. */
        if (found) {
            return true;
        } else {
            /*Otherwise check to see if a column annotation data can be found. */
            found = map.get( "column" ) != null;
            if (found) {
                /* If the column annotation data was found, see if the nullable flag was set. */
                AnnotationData ad = (AnnotationData) map.get( "column" );
                Object object = ad.getValues().get("nullable");
                /* If the nullable flag was set, return its value. */
                if (object != null) {
                    Boolean bool = (Boolean) object;
                    return !bool.booleanValue();
                } else {
                    /* Otherwise, if the nullable value was not set, then return false. */
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    public static boolean isLargeText(Class clazz, String propertyName) {
                
        Map map = getAnnotationDataAsMap( clazz, propertyName );
        
        boolean found = map.get( "column" ) != null;
        /* If you found an annotation called required, return true. */
        if (found) {
                /* If the column annotation data was found, see if the length flag was set. */
                AnnotationData ad = (AnnotationData) map.get( "column" );
                Object object = ad.getValues().get("length");
                /* If the nullable flag was set, return its value. */
                if (object != null) {
                    Integer length = (Integer) object;
                    return length.intValue() > 80;
                } else {
                    /* Otherwise, if the nullable value was not set, then return false. */
                    return false;
                }
        }
        return false;
        
    }

    public static int textSize(Class clazz, String propertyName) {
        
        Map map = getAnnotationDataAsMap( clazz, propertyName );
        
        boolean found = map.get( "column" ) != null;
        /* If you found an annotation called required, return true. */
        if (found) {
                /* If the column annotation data was found, see if the length flag was set. */
                AnnotationData ad = (AnnotationData) map.get( "column" );
                Object object = ad.getValues().get("length");
                /* If the nullable flag was set, return its value. */
                if (object != null) {
                    Integer length = (Integer) object;
                    return length.intValue();
                } else {
                    /* Otherwise, if the nullable value was not set, then return false. */
                    return 0;
                }
        }
        return 0;
        
    }

    private static Map getAnnotationDataAsMap( Class clazz, String propertyName ) {
        List<AnnotationData> annotationDataForProperty = AnnotationUtils.getAnnotationDataForProperty( clazz, propertyName, false, allowedPackages );
        if (annotationDataForProperty.size()==0) {
            annotationDataForProperty = AnnotationUtils.getAnnotationDataForField( clazz, propertyName, allowedPackages );
        }
        Map map = MapUtils.convertListToMap( "name", annotationDataForProperty);
        return map;
    }

    public static boolean isManyToOne(Class clazz, String propertyName) {
        Map map = getAnnotationDataAsMap( clazz, propertyName );
        return map.get( "manyToOne" ) != null; 
    }
    
    @SuppressWarnings("unchecked")
    public static String getPropertyEntityName(Class clazz, String propertyName) {
        PropertyDescriptor descriptor = getPropertyDescriptor( clazz, propertyName);

        AnnotationData data = (AnnotationData)MapUtils.convertListToMap( "name",
                AnnotationUtils.getAnnotationDataForClass( descriptor.getPropertyType(), allowedPackages )).get( "entity" );
        if (data != null) {
            String entityName = (String) data.getValues().get( "name");
            if (entityName != null && entityName.trim().length() > 0){
                return (String) data.getValues().get( "name");
            }
        }
        return descriptor.getPropertyType().getSimpleName();
    }

    public static boolean isEntity (Class clazz) {
        AnnotationData data = (AnnotationData)MapUtils.convertListToMap( "name",
                AnnotationUtils.getAnnotationDataForClass( clazz, allowedPackages )).get( "entity" );
        return data != null;
    }

    public static String getClassEntityName(Class clazz) {
        AnnotationData data = (AnnotationData)MapUtils.convertListToMap( "name",
                AnnotationUtils.getAnnotationDataForClass( clazz, allowedPackages )).get( "entity" );
        if (data != null) {
            String entityName = (String) data.getValues().get( "name");
            if (entityName != null && entityName.trim().length() > 0){
                return (String) data.getValues().get( "name");
            }
        }
        return clazz.getSimpleName();
    }

    @SuppressWarnings("unchecked")
    private static PropertyDescriptor getPropertyDescriptor( Class clazz, String propertyName) {
        Map<String, PropertyDescriptor> map = null;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo( clazz );
            map = MapUtils.convertArrayToMap("name", beanInfo.getPropertyDescriptors());
        } catch (IntrospectionException e) {
            throw new RuntimeException (e);
        }
        
        PropertyDescriptor descriptor = map.get(propertyName);
        return descriptor;
    }
    
    public static String getObjectId(DetailController detailController, Object row) {
        return detailController.getObjectId( row );
    }
    
}

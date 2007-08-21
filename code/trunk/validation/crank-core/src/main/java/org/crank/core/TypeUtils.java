package org.crank.core;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;


public class TypeUtils {

    public static boolean isDate( Class<?> type, String propertyName ) {
        PropertyDescriptor pd = getPropertyDescriptor( type, propertyName );
        Class<?> propertyType = pd.getPropertyType();
        if (Date.class.isAssignableFrom( propertyType )) {
            return true;
        }
        return false;
    }

    public static boolean isText( Class<?> type, String propertyName ) {
        PropertyDescriptor pd = getPropertyDescriptor( type, propertyName );
        Class<?> propertyType = pd.getPropertyType();

        if (propertyType == String.class || propertyType == Integer.class || propertyType == BigDecimal.class
                || propertyType == BigInteger.class || propertyType == Character.class || propertyType == Long.class
                || propertyType == Short.class || propertyType == Byte.class || propertyType == Float.class
                || propertyType == Double.class
                || ( propertyType.isPrimitive() && !propertyType.getName().equals( "boolean" ) && !propertyType.getName().equals( "enum" ) )) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEnum( final Class<?> type, final String propertyName ) {
        PropertyDescriptor pd = getPropertyDescriptor( type, propertyName );
        Class<?> propertyType = pd.getPropertyType();
        if (propertyType == Enum.class) {
            return true;
        } else if (propertyType.isEnum()) {
            return true;
        }
        return false;
    }

    public static boolean isBoolean( final Class<?> type, final String propertyName ) {
        PropertyDescriptor pd = getPropertyDescriptor( type, propertyName );
        Class<?> propertyType = pd.getPropertyType();
        if (propertyType == Boolean.class) {
            return true;
        } else if (propertyType.isPrimitive() && "boolean".equals( propertyType.getName() )) {
            return true;
        }
        return false;
    }

    public static PropertyDescriptor getPropertyDescriptor( final Class<?> type, final String propertyName ) {
        if (!propertyName.contains( "." )) {
            return doGetPropertyDescriptor( type, propertyName );
        } else {
            String [] propertyNames = propertyName.split( "[.]" );
            Class<?> clazz = type;
            PropertyDescriptor propertyDescriptor=null;
            for (String pName : propertyNames) {
                propertyDescriptor = doGetPropertyDescriptor( clazz, pName );
                if (propertyDescriptor == null) {
                    return null;
                }
                clazz = propertyDescriptor.getPropertyType();
            }
            return propertyDescriptor;
         }
    }

    private static PropertyDescriptor doGetPropertyDescriptor( final Class<?> type, final String propertyName ) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo( type );
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : propertyDescriptors) {
                if (pd.getName().equals( propertyName )) {
                    return pd;
                }
            }
            return null;
   
        } catch (Exception ex) {
            throw new RuntimeException( "Unable to get property " + propertyName + " for class " + type,
                    ex );
        }
    }

}

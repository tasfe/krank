package org.crank.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class TypeUtils {

    @SuppressWarnings("unchecked")
    public static boolean isDate( Class type, String propertyName ) {
        PropertyDescriptor pd = getPropertyDescriptor( type, propertyName );
        Class propertyType = pd.getPropertyType();
        if (propertyType.isAssignableFrom( Date.class )) {
            return true;
        }
        return false;
    }

    public static boolean isText( Class type, String propertyName ) {
        PropertyDescriptor pd = getPropertyDescriptor( type, propertyName );
        Class propertyType = pd.getPropertyType();

        if (propertyType == String.class || propertyType == Integer.class || propertyType == BigDecimal.class
                || propertyType == BigInteger.class || propertyType == Character.class || propertyType == Long.class
                || propertyType == Short.class || propertyType == Byte.class || propertyType == Float.class
                || propertyType == Double.class
                || ( propertyType.isPrimitive() && !propertyType.getName().equals( "boolean" ) )) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isBoolean( final Class type, final String propertyName ) {
        PropertyDescriptor pd = getPropertyDescriptor( type, propertyName );
        Class propertyType = pd.getPropertyType();
        if (propertyType == Boolean.class) {
            return true;
        } else if (propertyType.isPrimitive() && "boolean".equals( propertyType.getName() )) {
            return true;
        }
        return false;
    }

    private static PropertyDescriptor getPropertyDescriptor( final Class type, final String propertyName ) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo( type );
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : propertyDescriptors) {
                if (pd.getName().equals( propertyName )) {
                    return pd;
                }
            }
            return null;

        } catch (IntrospectionException ex) {
            throw new RuntimeException( ex );
        }
    }

}

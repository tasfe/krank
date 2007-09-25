package org.crank.core;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
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
    
    public static boolean isUrl( String value ) {

        if (value != null) {
        	if  ( 
        			(value.toLowerCase().indexOf(".com") > -1) ||
        			(value.toLowerCase().indexOf(".net") > -1)
        		) {
        		return true;
        	}
            return false;
        } else {
            return false;
        }
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
    	if (type==null || propertyName==null) {
    		throw new CrankException("Class type and propertyName must not be null, type=%s, propertyName=%s", type, propertyName );
    	}
        PropertyDescriptor pd = getPropertyDescriptor( type, propertyName );
        if (pd==null) {
        	throw new CrankException("The Property was not found!, type=%s, propertyName=%s", type, propertyName );
        }
        Class<?> propertyType = pd.getPropertyType();
        if (propertyType == Boolean.class) {
            return true;
        } else if (propertyType.isPrimitive() && "boolean".equals( propertyType.getName() )) {
            return true;
        }
        return false;
    }

    public static PropertyDescriptor getPropertyDescriptor( final Class<?> type, final String propertyName ) {
    	if (type==null || propertyName==null) {
    		throw new CrankException("Class type and propertyName must not be null, type=%s, propertyName=%s", type, propertyName );
    	}

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

    public static Field getField( final Class<?> type, final String fieldName ) {
        if (!fieldName.contains( "." )) {
            return doFindFieldInHeirarchy( type, fieldName );
        } else {
            String [] fieldNames = fieldName.split( "[.]" );
            Class<?> clazz = type;
            Field  field=null;
            for (String fName : fieldNames) {
                field = doFindFieldInHeirarchy( clazz, fName );
                if (field == null) {
                    return null;
                }
                clazz = field.getType();
            }
            return field;
         }
    }

    private static Field doFindFieldInHeirarchy(Class<?> clazz, String propertyName) {
        Field field = doGetField(clazz, propertyName);
        
        Class<?> sclazz = clazz.getSuperclass();
        if (field == null) {
	        while (true) {
	        	if (sclazz!=null) {
	        		field = doGetField(sclazz, propertyName);
	        		sclazz = sclazz.getSuperclass();
	        	}
	        	if (field!=null) {
	        		break;
	        	}
	        	if (sclazz==null) {
	        		break;
	        	}
	        }
        }
        return field;
    }

    private static Field doGetField(Class<?> clazz, String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField( fieldName );
        } catch (SecurityException se) {
            field = null;
        } catch (NoSuchFieldException nsfe) {
            field = null;
        }
        if (field == null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                if (f.getName().equals( fieldName )) {
                    field = f;
                }
            }
        }        
        if (field != null) {
            field.setAccessible( true );
        }
        return field;
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

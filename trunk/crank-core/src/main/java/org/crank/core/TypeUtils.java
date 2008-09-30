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
    	if (type==null || propertyName==null) {
    		throw new CrankException("Class type and propertyName must not be null, type=%s, propertyName=%s", type, propertyName );
    	}
        PropertyDescriptor pd = getPropertyDescriptor( type, propertyName );
        if (pd==null) {
        	return false;
        }
        Class<?> propertyType = pd.getPropertyType();
        if (Date.class.isAssignableFrom( propertyType )) {
            return true;
        }
        return false;
    }
    
    public static boolean isUrl( Object value ) {
    	boolean rv = false;
        if (value != null && value instanceof String) {
    		String lcValue = ((String)value).toLowerCase();
        	if  (
                    (lcValue.indexOf(".com") > 0)  ||
                    (lcValue.indexOf(".org") > 0)  ||
                    (lcValue.indexOf(".edu") > 0)  ||
                    (lcValue.indexOf(".biz") > 0)  ||
                    (lcValue.indexOf(".info") > 0) ||
                    (lcValue.indexOf(".mobi") > 0) ||
                    (lcValue.indexOf(".us") > 0)   ||
                    (lcValue.indexOf(".ca") > 0)   ||
                    (lcValue.indexOf(".net") > 0)
                ) {
        		rv = true;            	
        	}
        }
        return rv;
    }

    public static boolean isInCollection( Object value, Object collection ) {
    	boolean rv = false;
        if ((value != null) && (!"".equals(value)) && (collection != null) && (!"".equals(collection))) {
        	if ((value instanceof String) && (collection instanceof String)) {
        		String tmp = "," + collection + ',';
        		rv =  tmp.contains("," + value + ",");
            }
        }        
        return rv;
    }

    public static boolean isText( Class<?> type, String propertyName ) {
       	if (type==null || propertyName==null) {
    		throw new CrankException("Class type and propertyName must not be null, type=%s, propertyName=%s", type, propertyName );
    	}
        PropertyDescriptor pd = getPropertyDescriptor( type, propertyName );
        if (pd==null) {
        	return true;
        }
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
       	if (type==null || propertyName==null) {
    		throw new CrankException("Class type and propertyName must not be null, type=%s, propertyName=%s", type, propertyName );
    	}
        PropertyDescriptor pd = getPropertyDescriptor( type, propertyName );
        if (pd==null) {
        	//throw new CrankException("The Property was not found!, type=%s, propertyName=%s", type, propertyName );
        	return false;
        }
        
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
        	//throw new CrankException("The Property was not found!, type=%s, propertyName=%s", type, propertyName );
        	return false;
        }
        Class<?> propertyType = pd.getPropertyType();
        if (propertyType == Boolean.class) {
            return true;
        } else if (propertyType.isPrimitive() && "boolean".equals( propertyType.getName() )) {
            return true;
        }
        return false;
    }

    public static boolean isFloat( final Class<?> type, final String propertyName ) {
    	if (type==null || propertyName==null) {
    		throw new CrankException("Class type and propertyName must not be null, type=%s, propertyName=%s", type, propertyName );
    	}
        PropertyDescriptor pd = getPropertyDescriptor( type, propertyName );
        if (pd==null) {
        	//throw new CrankException("The Property was not found!, type=%s, propertyName=%s", type, propertyName );
        	return false;
        }
        Class<?> propertyType = pd.getPropertyType();
        if (propertyType == Float.class) {
            return true;
        } else if (propertyType.isPrimitive() && "float".equals( propertyType.getName() )) {
            return true;
        }
        return false;
    }

    public static boolean isInteger( final Class<?> type, final String propertyName ) {
    	if (type==null || propertyName==null) {
    		throw new CrankException("Class type and propertyName must not be null, type=%s, propertyName=%s", type, propertyName );
    	}
        PropertyDescriptor pd = getPropertyDescriptor( type, propertyName );
        if (pd==null) {
        	//throw new CrankException("The Property was not found!, type=%s, propertyName=%s", type, propertyName );
        	return false;
        }
        Class<?> propertyType = pd.getPropertyType();
        if (propertyType == Integer.class) {
            return true;
        } else if (propertyType.isPrimitive() && "int".equals( propertyType.getName() )) {
            return true;
        }
        return false;
    }

    public static boolean isShort( final Class<?> type, final String propertyName ) {
    	if (type==null || propertyName==null) {
    		throw new CrankException("Class type and propertyName must not be null, type=%s, propertyName=%s", type, propertyName );
    	}
        PropertyDescriptor pd = getPropertyDescriptor( type, propertyName );
        if (pd==null) {
        	//throw new CrankException("The Property was not found!, type=%s, propertyName=%s", type, propertyName );
        	return false;
        }
        Class<?> propertyType = pd.getPropertyType();
        if (propertyType == Short.class) {
            return true;
        } else if (propertyType.isPrimitive() && "short".equals( propertyType.getName() )) {
            return true;
        }
        return false;
    }

    public static boolean isLong( final Class<?> type, final String propertyName ) {
    	if (type==null || propertyName==null) {
    		throw new CrankException("Class type and propertyName must not be null, type=%s, propertyName=%s", type, propertyName );
    	}
        PropertyDescriptor pd = getPropertyDescriptor( type, propertyName );
        if (pd==null) {
        	//throw new CrankException("The Property was not found!, type=%s, propertyName=%s", type, propertyName );
        	return false;
        }
        Class<?> propertyType = pd.getPropertyType();
        if (propertyType == Long.class) {
            return true;
        } else if (propertyType.isPrimitive() && "long".equals( propertyType.getName() )) {
            return true;
        }
        return false;
    }

    public static boolean isString( final Class<?> type, final String propertyName ) {
    	if (type==null || propertyName==null) {
    		throw new CrankException("Class type and propertyName must not be null, type=%s, propertyName=%s", type, propertyName );
    	}
        PropertyDescriptor pd = getPropertyDescriptor( type, propertyName );
        if (pd==null) {
        	//throw new CrankException("The Property was not found!, type=%s, propertyName=%s", type, propertyName );
        	return false;
        }
        Class<?> propertyType = pd.getPropertyType();
        if (propertyType == String.class) {
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

    public static Class<?> getPropertyType( final Class<?> type, final String propertyName ) {
        try {
            return getPropertyDescriptor(type, propertyName).getPropertyType();
        } catch (Exception ex) {
            throw new CrankException(ex, "Unable to retrieve property descriptor for %s of class %s", propertyName, type.getName());
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
            Class<?> superclass = type.getSuperclass();
            if (superclass!=null) {
            	return doGetPropertyDescriptor(superclass, propertyName);
            }
            return null;
   
        } catch (Exception ex) {
            throw new RuntimeException( "Unable to get property " + propertyName + " for class " + type,
                    ex );
        }
    }

}

package org.crank.crud.controller;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.lang.annotation.Annotation;


import org.crank.core.AnnotationData;
import org.crank.core.AnnotationUtils;
import org.crank.core.CrankException;
import org.crank.core.MapUtils;
import org.crank.core.TypeUtils;
import org.crank.crud.model.PersistedFile;

import javax.persistence.Entity;


public class CrudUtils {
    private static Set<String> allowedPackages = new HashSet<String>();
    static {
        allowedPackages.add( "javax.persistence" );
        allowedPackages.add( "org.crank.crud.annotations" );
        allowedPackages.add( "org.crank.jsf.annotations" );
        allowedPackages.add( "org.crank.annotations.validation" );
    }

    public static long sysTime() {
        return System.currentTimeMillis();
    }
    public static boolean isFilterPropertyText(boolean autoCreatePrependParentAlias, Class<?> parentType, String property) {
        if (parentType == null) {
            return true;
        }
        if (autoCreatePrependParentAlias) {
    		return TypeUtils.isText(parentType, property);
    	} else {
        	String propMinusLeadingName = property.substring(property.indexOf('.')+1, property.length());
    		return TypeUtils.isText(parentType, propMinusLeadingName);
    	}
    }    
    public static boolean isFilterPropertyDate(boolean autoCreatePrependParentAlias, Class<?> parentType, String property) {
        if (parentType == null) {
            return false;
        }
    	if (autoCreatePrependParentAlias) {
    		return TypeUtils.isDate(parentType, property);
    	} else {
        	String propMinusLeadingName = property.substring(property.indexOf('.')+1, property.length());
    		return TypeUtils.isDate(parentType, propMinusLeadingName);
    	}
    }    
    public static boolean isFilterPropertyBoolean(boolean autoCreatePrependParentAlias, Class<?> parentType, String property) {
        if (parentType == null) {
            return false;
        }
    	if (autoCreatePrependParentAlias) {
    		return TypeUtils.isBoolean(parentType, property);
    	} else {
        	String propMinusLeadingName = property.substring(property.indexOf('.')+1, property.length());
    		return TypeUtils.isBoolean(parentType, propMinusLeadingName);
    	}
    }
    public static boolean isFilterPropertyEnum(boolean autoCreatePrependParentAlias, Class<?> parentType, String property) {
        if (parentType == null) {
            return false;
        }
    	if (autoCreatePrependParentAlias) {
    		return TypeUtils.isEnum(parentType, property);
    	} else {
        	String propMinusLeadingName = property.substring(property.indexOf('.')+1, property.length());
    		return TypeUtils.isEnum(parentType, propMinusLeadingName);
    	}
    }
    
    public static boolean isRequired(Class<?> clazz, String propertyName) {
        if (clazz == null) {
            return false;
        }  	
    	if (clazz == null || propertyName == null) {
    		throw new CrankException("CrankUtils.isRequired: Null arguments are not allowed " +
    				" clazz = %s, propertyName = %s ", clazz, propertyName);
    	}
        try {
	        PropertyDescriptor descriptor = getPropertyDescriptor( clazz, propertyName);
	        if (descriptor==null) {
	        	throw new CrankException("CrankUtils.isRequired: Unable to find property descriptor");
	        }
	        if (descriptor.getPropertyType().isPrimitive()) {
	            return true;
	        }
	        
	        Map<String, AnnotationData> map = getAnnotationDataAsMap( clazz, propertyName );


            boolean found = map.get( "required" ) != null;

            /* If you found an annotation called required, return true. */
	        if (found) {
	            return true;
	        } else {
	            /*Otherwise check to see if a column annotation data can be found. */

	        	return (isRequiredColumnNullable(map, "column") || isRequiredColumnNullable(map, "joinColumn") 
	        			|| isRequiredColumnOptional(map, "manyToOne"));
	        }
        } catch (Exception ex) {
    		throw new CrankException(ex, "CrankUtils.isRequired: Problem %s" +
    				" clazz = %s, propertyName = %s ", ex.getMessage(), clazz, propertyName);
        	
        }
    }
    
    public static String getToolTip(Class<?> clazz, String propertyName) {
    	if (clazz == null || propertyName == null) {
    		throw new CrankException("CrankUtils.getToolTip: Null arguments are not allowed " +
    				" clazz = %s, propertyName = %s ", clazz, propertyName);
    	}
        try {
	        PropertyDescriptor descriptor = 
	        	getPropertyDescriptor( clazz, propertyName);
	        if (descriptor==null) {
	        	throw new CrankException("CrankUtils.getToolTip: Unable to find property descriptor");
	        }
	        
	        Map<String, AnnotationData> map = getAnnotationDataAsMap( clazz, propertyName );
	        if (map.containsKey( "toolTip" ) ) {
	            return (String) ((AnnotationData) map.get( "toolTip" )).getValues().get("value");
	        }
	        return null;
        } catch (Exception ex) {
    		throw new CrankException(ex, "CrankUtils.getToolTip: Problem %s" +
    				" clazz = %s, propertyName = %s ", ex.getMessage(), clazz, propertyName);
        	
        }
    }
    
    public static boolean isNameSpaceToolTip(Class<?> clazz, String propertyName) {
    	if (clazz == null || propertyName == null) {
    		throw new CrankException("CrankUtils.isNameSpaceToolTip: Null arguments are not allowed " +
    				" clazz = %s, propertyName = %s ", clazz, propertyName);
    	}
        try {
	        PropertyDescriptor descriptor = 
	        	getPropertyDescriptor( clazz, propertyName);
	        if (descriptor==null) {
	        	throw new CrankException("CrankUtils.isNameSpaceToolTip: Unable to find property descriptor");
	        }
	        
	        Map<String, AnnotationData> map = getAnnotationDataAsMap( clazz, propertyName );
	        if (map.containsKey( "toolTipFromNameSpace" ) ) {
	            return true;
	        }
	        return false;
        } catch (Exception ex) {
    		throw new CrankException(ex, "CrankUtils.getToolTip: Problem %s" +
    				" clazz = %s, propertyName = %s ", ex.getMessage(), clazz, propertyName);
        	
        }
    }
    
    public static String getLabelToolTip(Class<?> clazz, String propertyName) {
    	if (clazz == null || propertyName == null) {
    		throw new CrankException("CrankUtils.getLabelToolTip: Null arguments are not allowed " +
    				" clazz = %s, propertyName = %s ", clazz, propertyName);
    	}
        try {
	        PropertyDescriptor descriptor = 
	        	getPropertyDescriptor( clazz, propertyName);
	        if (descriptor==null) {
	        	throw new CrankException("CrankUtils.getLabelToolTip: Unable to find property descriptor");
	        }
	        
	        Map<String, AnnotationData> map = getAnnotationDataAsMap( clazz, propertyName );
	        if (map.containsKey( "toolTip" ) ) {
	            return (String) ((AnnotationData) map.get( "toolTip" )).getValues().get("labelValue");
	        }
	        return null;
        } catch (Exception ex) {
    		throw new CrankException(ex, "CrankUtils.getToolTip: Problem %s" +
    				" clazz = %s, propertyName = %s ", ex.getMessage(), clazz, propertyName);
        	
        }
    }
    
    private static boolean isRequiredColumnNullable(Map<String, AnnotationData> map, String columnType) {
    	boolean result = false;
    	
        boolean found = map.get( columnType ) != null;

        if (found) {
                /* If the column annotation data was found, see if the length flag was set. */
                AnnotationData ad =  map.get( columnType );
                Object object = ad.getValues().get("nullable");
                /* If the nullable flag was set, return its value. */
                if (object != null) {
                    Boolean bool = (Boolean) object;
                    return !bool.booleanValue();
                } else {
                    /* Otherwise, if the nullable value was not set, then return false. */
                    return false;
                }
        }
        
        return result;
    }
    
    private static boolean isRequiredColumnOptional(Map<String, AnnotationData> map, String columnType) {
    	boolean result = false;
    	
        boolean found = map.get( columnType ) != null;

        if (found) {
                AnnotationData ad = map.get( columnType );
                Object object = ad.getValues().get("optional");
                /* If the optional flag was set, return its value. */
                if (object != null) {
                    Boolean bool = (Boolean) object;
                    return !bool.booleanValue();
                } else {
                    /* Otherwise, if the nullable value was not set, then return false. */
                    return false;
                }
        }
        
        return result;
    }
    

    public static boolean isLargeText(Class<?> clazz, String propertyName) {
    	
        try {
            if (!(getPropertyDescriptor(clazz, propertyName).getPropertyType() == String.class)) {
                return false;
            }
            Map<String, AnnotationData> map = getAnnotationDataAsMap( clazz, propertyName );

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
        } catch (Exception ex) {
            throw new CrankException(ex, "Unable to get property %s from class %s", propertyName, clazz);
        }
        
    }
    
    public static int textSize(Class<?> clazz, String propertyName) {
        return columnSize( clazz, propertyName );
    }

    public static boolean isFile(Class<?> clazz, String propertyName) {
        if (PersistedFile.class.isAssignableFrom( getPropertyDescriptor( clazz, propertyName ).getPropertyType() )) {
            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
	public static int columnSize(Class<?> clazz, String propertyName) {

        boolean found = false;
        List<AnnotationData> list = AnnotationUtils.getAnnotationDataForClass(clazz, allowedPackages);
        Map<String, AnnotationData> map = MapUtils.convertListToMap("name", list);
        if (map.get("attributeOverrides") != null) {
            Object[] values = (Object[]) map.get("attributeOverrides").getValues().get("value");

            List<AnnotationData> extractValidationAnnotationData = AnnotationUtils.extractValidationAnnotationData((Annotation[]) values, allowedPackages);

            for (AnnotationData ad : extractValidationAnnotationData) {
                if (propertyName.equals(ad.getValues().get("name"))) {
                    if (ad.getValues().get("column") != null) {
                        ad = new AnnotationData((Annotation) ad.getValues().get("column"), allowedPackages);
                        Object object = ad.getValues().get("length");
                        if (object != null) {
                            found = true;
                            Integer length = (Integer) object;
                            return length.intValue();
                        }
                    }
                }
            }
        }

        map = getAnnotationDataAsMap( clazz, propertyName );
        
        found = map.get( "column" ) != null;
        /* If you found an annotation called required, return true. */
        if (found) {
                /*
				 * If the column annotation data was found, see if the length
				 * flag was set.
				 */
                AnnotationData ad = (AnnotationData) map.get( "column" );
                Object object = ad.getValues().get("length");
                /* If the nullable flag was set, return its value. */
                if (object != null) {
                    Integer length = (Integer) object;
                    return length.intValue();
                }
        }
        return 0;
        
    }

    @SuppressWarnings("unchecked")
	private static Map<String, AnnotationData> getAnnotationDataAsMap( Class<?> clazz, String propertyName ) {
        Collection<AnnotationData> annotationDataForProperty = AnnotationUtils.getAnnotationDataForFieldAndProperty( clazz, propertyName, allowedPackages );
        Map<String, AnnotationData> map = MapUtils.convertListToMap( "name", annotationDataForProperty);
        return map;
    }

    public static boolean hasParentProperty(String propertyName) {
         return propertyName.contains(".");
    }

    public static String getParentProperty(String propertyName) {
        int index = propertyName.lastIndexOf(".");
        if (index != -1) {
            return propertyName.substring(0, index);
        } else {
            return propertyName;
        }
    }

    public static boolean isManyToOne(Class<?> clazz, String propertyName) {
        Map<String, AnnotationData> map = getAnnotationDataAsMap(clazz, propertyName);
        return map.get("manyToOne") != null;
    }

    public static boolean isParentManyToOne(Class<?> clazz, String propertyName) {
        return isManyToOne(clazz, getParentProperty(propertyName));
    }

    public static boolean isManyToOneOptional(Class<?> clazz, String propertyName) {
        Map<String, AnnotationData> map = getAnnotationDataAsMap( clazz, propertyName );
        AnnotationData data = map.get( "manyToOne" );
        if (data != null) {
        	Boolean optional = (Boolean) data.getValues().get("optional");
        	if (optional == null) {
        		return false;
        	} else {
        		return optional;
        	}
        }
        return false;
    }

    public static boolean isOneToOne(Class<?> clazz, String propertyName) {
        Map<String, AnnotationData> map = getAnnotationDataAsMap( clazz, propertyName );
        return map.get( "oneToOne" ) != null; 
    }
    
    public static boolean isEnumerated(Class<?> clazz, String propertyName) {
        Map<String, AnnotationData> map = getAnnotationDataAsMap( clazz, propertyName );
        return map.get( "enumerated" ) != null; 
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


    public static String getEntityName(Class<?> aType) {
		Entity entity = (Entity) aType.getAnnotation(Entity.class);
		if (entity == null) {
			return aType.getSimpleName();
		}
		String entityName = entity.name();

		if (entityName == null) {
			return aType.getSimpleName();
		} else if (!(entityName.length() > 0)) {
			return aType.getSimpleName();
		} else {
			return entityName;
		}

	}

    public static boolean isEntity (Class<?> clazz) {
        AnnotationData data = (AnnotationData)MapUtils.convertListToMap( "name",
                AnnotationUtils.getAnnotationDataForClass( clazz, allowedPackages )).get( "entity" );
        return data != null;
    }

    public static boolean isEmbeddable (Class<?> clazz) {
        AnnotationData data = (AnnotationData)MapUtils.convertListToMap( "name",
                AnnotationUtils.getAnnotationDataForClass( clazz, allowedPackages )).get( "embeddable" );
        return data != null;
    }

    public static String getClassEntityName(Class<?> clazz) {
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
        return TypeUtils.getPropertyDescriptor( clazz, propertyName );
    }
    
    @SuppressWarnings("unchecked")
	public static String getObjectId(DetailController detailController, Object row) {
    	
    	if (row instanceof Row) {
    		row = ((Row)row).getObject();
    	}
    		
    	if (row == null) {
    		return "-1";
    	} else {
    		return detailController.getObjectId( row );
    	}
    }
    

	public static String getEnumLabel(final String enumConstant,
			final ResourceBundle bundle) {

		String label;

		/** Look for enumConstant, e.g., ACTIVE_ACCOUNT == Active Account. */
		try {
			label = bundle.getString(enumConstant);
		} catch (MissingResourceException mre) {
			label = generateEnumLabelValue(enumConstant);
		}

		return label;
	}

	public static String generateEnumLabelValue(final String enumConstant) {
		StringBuffer buffer = new StringBuffer(enumConstant.length() * 2);
		char[] chars = enumConstant.toCharArray();
        boolean capNextChar = false;

		for (int index = 0; index < chars.length; index++) {
			char cchar = chars[index];

            if (cchar == '_') {
                buffer.append(' ');
                capNextChar = true;
                continue;
            }
            
            if (capNextChar) {
                capNextChar = false;
                cchar = Character.toUpperCase(cchar);
				buffer.append(cchar);
				continue;
            }

			if (index == 0) {
				cchar = Character.toUpperCase(cchar);
				buffer.append(cchar);
				continue;
			}

			cchar = Character.toLowerCase(cchar);
			buffer.append(cchar);
		}

		return buffer.toString();
	}

}

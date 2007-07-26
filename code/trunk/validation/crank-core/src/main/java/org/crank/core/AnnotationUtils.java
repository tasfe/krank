package org.crank.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.crank.annotations.design.NeedsRefactoring;

public class AnnotationUtils {

    public static List<AnnotationData> getAnnotationDataForProperty(Class clazz, String propertyName, boolean useReadMethod, Set<String> allowedPackages) {
        return extractValidationAnnotationData(extractAllAnnotationsForProperty(clazz, propertyName, useReadMethod), allowedPackages);
    }
    
    public static List<AnnotationData> getAnnotationDataForField(Class clazz, String propertyName, Set<String> allowedPackages) {
        return extractValidationAnnotationData(findFieldAnnotations( clazz, propertyName ), allowedPackages);
    }
    
    
    /**
     * Create an annotation data list.
     * 
     * @param annotations list of annotations.
     * @return
     */
    private static List<AnnotationData> extractValidationAnnotationData(
            Annotation[] annotations, Set<String> allowedPackages) {
        List<AnnotationData> annotationsList = new ArrayList<AnnotationData>();
        for (Annotation annotation : annotations) {
            AnnotationData annotationData = new AnnotationData(annotation, allowedPackages);
            if (annotationData.isAllowed()) {
                annotationsList.add(annotationData);
            }
        }
        return annotationsList;
    }

    /**
     * Extract all annotations for a given property.
     * Searches current class and if none found searches
     * super class for annotations. We do this because the class
     * could be proxied with AOP.
     * 
     * @param clazz Class containing the property.
     * @param propertyName The name of the property.
     * @return
     */
    @NeedsRefactoring("There has to be a better way to do this. Read comments " +
            "about potential bug.")
    private static Annotation[] extractAllAnnotationsForProperty(Class clazz, String propertyName, boolean useRead) {
        try {

            Annotation[] annotations = findPropertyAnnotations(clazz, propertyName, useRead);

            /* In the land of dynamic proxied AOP classes, 
             * this class could be a proxy. This seems like a bug 
             * waiting to happen. So far it has worked... */
            if (annotations.length == 0) {
                annotations = findPropertyAnnotations(clazz.getSuperclass(), propertyName, useRead);
            }
            return annotations;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }
    
    /**
     * Find annotations given a particular property name and clazz. This figures
     * out the writeMethod for the property, and uses the write method 
     * to look up the annotations.
     * @param clazz The class that holds the property.
     * @param propertyName The name of the property.
     * @return
     * @throws IntrospectionException
     */
    private static Annotation[] findPropertyAnnotations(Class clazz, String propertyName, boolean useRead)
            throws IntrospectionException {
                
        /* Grab the bean info that has the write method info. */
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        
        /* Get the writeMethod and read its annotations. */
        Method method = findMethodForProperty(propertyName, beanInfo, useRead);
        if (method!=null) {
            Annotation[] annotations = method.getAnnotations();
            return annotations;
        } else {
            return new Annotation[]{};
        }
    }
    
    private static Annotation[] findFieldAnnotations(Class clazz, String propertyName) {
        Field field = findFieldForProperty(clazz, propertyName);
        Annotation[] annotations = field.getAnnotations();
        return annotations;
    }

    private static Field findFieldForProperty(Class clazz, String propertyName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField( propertyName );
        } catch (SecurityException se) {
            field = null;
        } catch (NoSuchFieldException nsfe) {
            field = null;
        }
        if (field == null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                if (f.getName().equals( propertyName )) {
                    field = f;
                }
            }
        }
        
        if (field != null) {
            field.setAccessible( true );
        }
        return field;
    }

    /**
     * Finds a write method for a given property.
     * @param propertyName
     * @param beanInfo
     * @return
     */
    private static Method findMethodForProperty(String propertyName, BeanInfo beanInfo, boolean read) {
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            if (propertyName.equals(descriptor.getName())) {
                if (read) {
                    return descriptor.getReadMethod();                    
                } else {
                    return descriptor.getWriteMethod();
                }
            }
        }
        return null;
    }
    
    
}

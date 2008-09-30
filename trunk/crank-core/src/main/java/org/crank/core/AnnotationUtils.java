package org.crank.core;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import org.crank.annotations.design.NeedsRefactoring;

public class AnnotationUtils {

    public static List<AnnotationData> getAnnotationDataForProperty(Class<?> clazz, String propertyName, boolean useReadMethod, Set<String> allowedPackages) {
        return extractValidationAnnotationData(extractAllAnnotationsForProperty(clazz, propertyName, useReadMethod), allowedPackages);
    }
    
    public static List<AnnotationData> getAnnotationDataForField(Class<?> clazz, String propertyName, Set<String> allowedPackages) {
        return extractValidationAnnotationData(findFieldAnnotations( clazz, propertyName ), allowedPackages);
    }
    
    public static List<AnnotationData> getAnnotationDataForClass(Class<?> clazz, Set<String> allowedPackages) {
        return extractValidationAnnotationData(findClassAnnotations( clazz ), allowedPackages);
    }
    
    private static Annotation[] findClassAnnotations( Class<?> clazz ) {
        return clazz.getAnnotations();
    }

    public static Collection<AnnotationData> getAnnotationDataForFieldAndProperty(Class<?> clazz, String propertyName, Set<String> allowedPackages) {
        /* Extract the AnnotationData from the Java annotations. */
        List<AnnotationData> propertyAnnotationDataList =
            AnnotationUtils.getAnnotationDataForProperty( clazz, propertyName, false, allowedPackages);

        /* Read the field annotations.  */
        List<AnnotationData> fieldAnnotationDataList =
            AnnotationUtils.getAnnotationDataForField( clazz, propertyName, allowedPackages );

        /* Combine the annotations from field and properties. Field validations take precedence over property validations. */
        Map<String, AnnotationData> map = new HashMap<String, AnnotationData>(propertyAnnotationDataList.size() + fieldAnnotationDataList.size());

        /* Add the property annotations to the map. */
        for(AnnotationData annotationData : propertyAnnotationDataList) {
              map.put(annotationData.getName(), annotationData);
        }

        /* Add the field annotations to the map allowing them to overide the property annotations. */
        for(AnnotationData annotationData : fieldAnnotationDataList) {
              map.put(annotationData.getName(), annotationData);
        }
        return map.values();
    }


    /**
     * Create an annotation data list.
     * 
     * @param annotations list of annotations.
     * @return
     */
    public static List<AnnotationData> extractValidationAnnotationData(
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
    private static Annotation[] extractAllAnnotationsForProperty(Class<?> clazz, String propertyName, boolean useRead) {
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
            throw new CrankException(ex, "Unable to extract annotations for property %s of class %s. useRead = %s", propertyName, clazz, useRead);
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
    private static Annotation[] findPropertyAnnotations(Class<?> clazz, String propertyName, boolean useRead)
            throws IntrospectionException {
        
        PropertyDescriptor propertyDescriptor = TypeUtils.getPropertyDescriptor(clazz, propertyName);
        if (propertyDescriptor == null) {
            return new Annotation[]{};    
        }
        Method accessMethod = null;
        
        if (useRead) {
            accessMethod = propertyDescriptor.getReadMethod();                    
        } else {
            accessMethod = propertyDescriptor.getWriteMethod();
        }
        
        if (accessMethod!=null) {
            Annotation[] annotations = accessMethod.getAnnotations();
            return annotations;
        } else {
            return new Annotation[]{};
        }
    }
    
    private static Annotation[] findFieldAnnotations(Class<?> clazz, String propertyName) {
        Field field = TypeUtils.getField(clazz, propertyName);        
        if (field==null) {
            return new Annotation[] {};
        }
        Annotation[] annotations = field.getAnnotations();
        return annotations;
    }

    
    
}

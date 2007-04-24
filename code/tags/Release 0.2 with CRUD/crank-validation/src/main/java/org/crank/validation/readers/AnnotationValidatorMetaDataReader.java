package org.crank.validation.readers;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.crank.annotations.design.AllowsConfigurationInjection;
import org.crank.annotations.design.Implements;
import org.crank.annotations.design.NeedsRefactoring;
import org.crank.validation.ValidatorMetaData;
import org.crank.validation.ValidatorMetaDataReader;

/**
 * 
 * <p>
 * <b>AnnotationValidatorMetaDataReader</b> reads validation meta-data from
 * annotations.
 * </p>
 * 
 * <p>
 * This class reads a annotation as follows: You pass in the base package of the
 * annotatoins it defaults to "org.crank.annotations.validation". It then takes
 * the <code>name</code> of the <code>ValidatorMetaData</code> and
 * captilalizes the first letter. Thus if you pass the package
 * "com.mycompany.annotations", and
 * <code>ValidatorMetaData.name = "required"</code>, then it will look for an
 * annotation called com.mycompany.annotations.Required. The idea behind this is
 * that you can use annotation without polluting your model classes with Crank
 * annotations.
 * </p>
 * 
 * <p>
 * The parent class that owns the annotation should have annotation as follows:
 * 
 * <pre>
 *   @Required @Length (min=10, max=100)
 *   public String getFirstName(){...
 *   
 *   @Required @Range (min=10, max=100) 
 *   public void setAge() {...
 * </pre>
 * 
 * The <b>firstName</b> corresponds to a property of the Foo class. The
 * <b>firstName</b> is associated with the validation rules <b>required</b>
 * and <b>length</b>. The <b>length</b> validation rule states the minimum and
 * maximum allowed number of characters with the <b>min</b> and <b>max</b>
 * parameters.
 * </p>
 * 
 * <p>
 * Two different frameworks read this meta-data (curently). Our validation
 * framework, which is mostly geared towards server-side validation and our
 * client-side JavaScript framework, which is geared towards producing
 * client-side JavaScript.
 * </p>
 * @author Rick Hightower
 * 
 */
public class AnnotationValidatorMetaDataReader implements ValidatorMetaDataReader {

    /** Holds a cache of meta-data to reduce parsing with regex and to avoid
     * reflection. 
     * Since this could get hit by multiple threads. I made it threadsafe.
     * */
    private static Map<String, List<ValidatorMetaData>> metaDataCache = 
        Collections.synchronizedMap(new HashMap<String, List<ValidatorMetaData>>());

    /** Holds a list of pacakges that contain annotations that we will process.
     * If the annotation package is not in this list, it will not be processed.
     */
    private Set<String> validationAnnotationPackages = new HashSet<String>();
    {
        /* By default, we only process our own annotions. */
        validationAnnotationPackages.add("org.crank.annotations.validation");
    }

    /**
     * Read the meta-data from annotations. This copies the meta-data
     * from the annotations into a POJO. It first checks the meta-data cache,
     * if the meta data is not found in the cache it then reads it from the 
     * class.
     * 
     * @param clazz The class that contains the annotations.
     * @param propertyName The name of the property that we are reading 
     * the annotation meta-data from.
     */
    @Implements(interfaceClass=ValidatorMetaDataReader.class)
    public List<ValidatorMetaData> readMetaData(Class clazz, String propertyName) {

        /* Generate a key to the cache based on the classname and the propertyName. */
        String propertyKey = clazz.getName() + "." + propertyName;

        /* Look up the validation meta data in the cache. */
        List<ValidatorMetaData> validatorMetaDataList = metaDataCache.get(propertyKey);

        /* If the meta-data was not found, then generate it. */
        if (validatorMetaDataList == null) { // if not found
            /* Read the annotations from the class based on the property name. */
            Annotation[] annotations = extractAllAnnotationsForProperty(clazz,
                    propertyName);
            /* Extract the AnnotationData from the Java annotations. */ 
            List<AnnotationData> annotationDataList = 
                extractValidationAnnotationData(annotations);

            /* Extract the POJO based meta-data from the annotations. */
            validatorMetaDataList = 
                extractMetaDataFromAnnotations(annotationDataList);

            /* Put it in the cache to avoid the processing in the future.
             * Design notes: The processing does a lot of reflection, there
             * is no need to do this each time.
             */
            metaDataCache.put(propertyKey, validatorMetaDataList);
        }
        return validatorMetaDataList;

    }

    /** This is a helper class that helps us extract annotation data
     * from the Annotations. 
     * @author Rick Hightower
     *
     */
    @NeedsRefactoring("Break this out into a seperate class and put it in " +
            "a utitlity package.")
    class AnnotationData {
        /** The actual Java annotation. */
        private Annotation annotation;

        /** The name of the classname of the annotation. */
        private String annotationClassName;

        /** The simple name of the annotation. */
        private String annotationSimpleName;

        /** The package of the annotation. */
        private String annotationPackageName;
        
        

        AnnotationData(Annotation annotation) {

            this.annotationSimpleName = annotation.annotationType().getSimpleName();
            this.annotationClassName = annotation.annotationType().getName();
            this.annotationPackageName = annotationClassName.substring(0, annotationClassName.length()
                    - annotationSimpleName.length() - 1);
            this.annotation = annotation;
        }

        /** Determines if this is an annotation we care about. 
         * Checks to see if the package name is in the set.
         * */
        @NeedsRefactoring("This would have to be changed if we move " +
                "this class to a seperate class in another package.")
        boolean isValidationAnnotation() {
            return validationAnnotationPackages.contains(annotationPackageName);
        }

        /**
         * Get the name of the annotation by lowercasing the first letter
         * of the simple name, e.g., short name Required becomes required.
         * @return
         */
        @NeedsRefactoring("Since this gets called a lot we should initialize the" +
                " name in the constructor.")
        String getName() {

            return annotationSimpleName.substring(0, 1).toLowerCase() 
            + annotationSimpleName.substring(1);
        }

        /**
         * Get the values from the annotation.
         * We use reflection to turn the annotation into a simple HashMap
         * of values. 
         * @return
         */
        @NeedsRefactoring("It seems like this would get called a lot," +
                "perhaps we should cache this as well. Maybe only initialize it " +
                "in the constructor as well. " + 
                "There has got to be a better way to extract values using " +
                "reflection and annotations")
        Map<String, Object> getValues() {
            /* Holds the value map. */
            Map<String, Object> values = new HashMap<String, Object>();
            /* Get the declared methods from the actual annotation. */
            Method[] methods = annotation.annotationType().getDeclaredMethods();
            
            final Object [] noargs = (Object[]) null;
            
            /* Iterate through declared methods and extract values
             * by invoking decalared methods if they are no arg methods.
             */
            for (Method method : methods) {
                /* If it is a no arg method assume it is an annoation value. */
                if (method.getParameterTypes().length == 0) {
                    try {
                        /* Get the value. */
                        Object value = method.invoke(annotation, noargs);
                        values.put(method.getName(), value);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            return values;

        }

    }

    /**
     * Create an annotation data list.
     * 
     * @param annotations list of annotations.
     * @return
     */
    private List<AnnotationData> extractValidationAnnotationData(
            Annotation[] annotations) {
        List<AnnotationData> annotationsList = new ArrayList<AnnotationData>();
        for (Annotation annotation : annotations) {
            AnnotationData annotationData = new AnnotationData(annotation);
            if (annotationData.isValidationAnnotation()) {
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
    private Annotation[] extractAllAnnotationsForProperty(Class clazz, String propertyName) {
        try {

            Annotation[] annotations = findAnnotations(clazz, propertyName);

            /* In the land of dynamic proxied AOP classes, 
             * this class could be a proxy. This seems like a bug 
             * waiting to happen. So far it has worked... */
            if (annotations.length == 0) {
                annotations = findAnnotations(clazz.getSuperclass(), propertyName);
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
    private Annotation[] findAnnotations(Class clazz, String propertyName)
            throws IntrospectionException {
        
//        Exception ex = new Exception();
//        ex.fillInStackTrace();
//        ex.printStackTrace();
//        System.ou.println("####### FIND ANNOTATION CLASS: " + clazz.getName());
//        System.ou.println("####### FIND ANNOTATION CLASS: #" + propertyName + "#");
        
        /* Grab the bean info that has the write method info. */
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        
        /* Get the writeMethod and read its annotations. */
        Method writeMethod = findWriteMethodForProperty(propertyName, beanInfo);
        if (writeMethod!=null) {
        Annotation[] annotations = writeMethod.getAnnotations();
            return annotations;
        } else {
            return new Annotation[]{};
        }
    }

    /**
     * Finds a write method for a given property.
     * @param propertyName
     * @param beanInfo
     * @return
     */
    private Method findWriteMethodForProperty(String propertyName, BeanInfo beanInfo) {
        Method writeMethod = null;
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            if (propertyName.equals(descriptor.getName())) {
                writeMethod = descriptor.getWriteMethod();
                break;
            }
        }
//        assert writeMethod != null : "Found write method for " + propertyName;
        return writeMethod;
    }

    /**
     * Extract meta-data from the annotationData we collected thus far.
     * @param annotations The annotationData (preprocessed annotations).
     * @return
     */
    private List<ValidatorMetaData> extractMetaDataFromAnnotations(
            List<AnnotationData> annotations) {
        List<ValidatorMetaData> list = new ArrayList<ValidatorMetaData>();

        for (AnnotationData annotationData : annotations) {
            ValidatorMetaData validatorMetaData = convertAnnotationDataToValidatorMetaData(annotationData);
            list.add(validatorMetaData);
        }

        return list;
    }

    /**
     * Converts an AnnotationData into a ValidatorMetaData POJO.
     * @param annotationData
     * @return
     */
    @NeedsRefactoring("This method shows we are calling annotationData.getValues a lot. " +
            "Therefore, we must cache the results of getValues as the annoationData is static " +
            "per property per class. ")
    private ValidatorMetaData convertAnnotationDataToValidatorMetaData(
            AnnotationData annotationData) {
        
        ValidatorMetaData metaData = new ValidatorMetaData();
        metaData.setName(annotationData.getName());

        /* INNEFFECIENT... FIX THIS... see @NeedRefactoring at 
         * getValues and above.*/
        metaData.setProperties(annotationData.getValues());

        return metaData;
    }

    /** We allow a set of validation annotation packages to be configured. */
    @AllowsConfigurationInjection
    public void setValidationAnnotationPackages(Set<String> validationAnnotationPackages) {
        this.validationAnnotationPackages = validationAnnotationPackages;
    }

}

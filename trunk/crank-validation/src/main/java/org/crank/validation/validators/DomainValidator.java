package org.crank.validation.validators;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.crank.core.AnnotationData;
import org.crank.core.AnnotationUtils;
import org.crank.core.MapUtils;
import org.crank.validation.ValidationContext;
import org.crank.validation.ValidatorMessage;
import org.crank.validation.ValidatorMessageHolder;
import org.crank.validation.validators.AbstractValidator;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * This validator is designed for domain-driven validation
 * of a group of child objects from a parent.  For instance, a parent
 * with a OneToMany relationship with a child may need to validate
 * the child against the other children associated with the parent.
 * In this scenario, the validation framework will invoke this 
 * validator when the @DomainValidation annotation is used on a field
 * in the child object by invoking the parent object validation
 * method identified by the "method" attribute.  The parent object
 * is identified by the "parentProperty" attribute.  If the parent
 * attribute is null or missing, then the validation method
 * is presumed to exist in the child.  
 * 
 * The validation method will be passed the child object and the 
 * field value as method.invoke(child, fieldValue)
 * 
 * @author Paul Tabor
 *
 */
public class DomainValidator extends AbstractValidator {

	private Object rootObject;
	
	public void setRootObject(Object rootObject) {
		this.rootObject = rootObject;
	}
	
	private ValidationContext validationContext;
	
    private static Set<String> allowedPackages = new HashSet<String>();
    static {
        allowedPackages.add( "org.crank.annotations.validation" );
    }	
	
    public DomainValidator() {
    	validationContext = ValidationContext.getCurrentInstance();
    }
    
    public DomainValidator(ValidationContext validationContext) {
    	this.validationContext = validationContext;
    }
    
	public ValidatorMessageHolder validate(Object fieldValue, String fieldLabel) {
		// So, we already know that this field has been decorated with @DomainValidation annotation.  
		// That's why we're here. We need to read the validation attributes to find the appropriate  
		// template method (parent or child) to invoke for validation.

		ValidatorMessage message = new ValidatorMessage();
		this.noMessages = true;
		boolean error = false;
		Object child;
		
		// The first "parent" is actually the child decorated with the validation annotation
		if (rootObject == null) {
			// Grab it from the validation context if it hasn't been provided
	        child = ValidationContext.getCurrentInstance().getParentObject();
		} else {
	        child = rootObject;
		}
        
        List<AnnotationData> annotationDataForProperty = AnnotationUtils.getAnnotationDataForProperty( child.getClass(), fieldLabel, false, allowedPackages );
        if (annotationDataForProperty.size()==0) {
            annotationDataForProperty = AnnotationUtils.getAnnotationDataForField( child.getClass(), fieldLabel, allowedPackages );
        }
        
        Map map = MapUtils.convertListToMap( "name", annotationDataForProperty);
        
        boolean found = map.get( "domainValidation" ) != null;
        boolean sameLevel = false;

        if (found) {
            AnnotationData ad = (AnnotationData) map.get( "domainValidation" );
            Object parentReference = ad.getValues().get("parentProperty");
            Object validator = null;

            if ((parentReference != null) && (!"".equals(parentReference))) {
                // If the parentProperty is specified, then find the parent class
            	
                BeanWrapper wrapper = new BeanWrapperImpl(child);
                validator = wrapper.getPropertyValue( (String)parentReference );

            } else {
            	// Otherwise, the validation method is assumed to be in the child
            	validator = child;
            	sameLevel = true;
            }
            
            // Make sure the method exists
        	Method m = null;

        	if (validator != null) {
            	try {
            		
            		String methodName = (String)ad.getValues().get("method");

            		if (sameLevel) {
                		Class[] parameters=new Class[1];
                		BeanWrapper wrapper = new BeanWrapperImpl(child);
                		parameters[0]=wrapper.getPropertyType( fieldLabel );
                		m = validator.getClass().getDeclaredMethod(methodName, parameters);
            		} else {
                		Class[] parameters=new Class[2];
                		parameters[0]=child.getClass();
                		BeanWrapper wrapper = new BeanWrapperImpl(child);
                		parameters[1]=wrapper.getPropertyType( fieldLabel );
                		m = validator.getClass().getDeclaredMethod(methodName, parameters);
            		}

            	} catch (NoSuchMethodException nsme) {
            		message.setDetail(nsme.getMessage());
            		message.setSummary(nsme.getMessage());
            		error = true;
            		nsme.printStackTrace();
            	} catch (Exception e) {
            		message.setDetail(e.getMessage());
            		message.setSummary(e.getMessage());
            		error = true;
            		e.printStackTrace();
            	}            	
            }

        	// Invoke the validation method and watch for any exceptions
        	try {
        		if (sameLevel) {
            		m.invoke(validator, new Object[]{fieldValue});
        		} else {
            		m.invoke(validator, new Object[]{child,fieldValue});
        		}
        	} catch (IllegalAccessException iae) {
        		message.setDetail(iae.getMessage());
        		message.setSummary(iae.getMessage());
        		error = true;
        		iae.printStackTrace();
        	} catch (InvocationTargetException ite) {
        		message.setDetail(ite.getMessage());
        		message.setSummary(ite.getMessage());
        		error = true;
        		ite.printStackTrace();
        	} catch (Exception e) {
        		message.setDetail(e.getMessage());
        		message.setSummary(e.getMessage());
        		error = true;
        		e.printStackTrace();
        	}            	
        }

        // If there were any errors, populate the validation message
        if (error) {
        	populateMessage(message, fieldLabel);
        }

        return message;
	}
	
    /**
     * Clean up the Validation context.
     * If you love something set it free. If it does not come back to you,
     * hunt it down and really destroy it. Clean up the Thread local variable.
     */
//    protected void cleanup() {
//        ValidationContext context = (ValidationContext) 
//            ValidationContext.getCurrentInstance();
//        context.free();
//    }

    /**
     * Register a new ValidationContext and initialize it.
     * 
     * @param facesContext
     * @param inputComponent
     * @param validatorData
     */
//    private void registerValidationContext(FacesContext facesContext, UIInput inputComponent, ValidatorData validatorData) {
//        JSFValidationContext context = new JSFValidationContext(inputComponent);
//        context.setParentObject(validatorData.lookupParentObject(facesContext));
//        context.setParams(inputComponent.getAttributes());
//        context.register(context);
//    }	

}

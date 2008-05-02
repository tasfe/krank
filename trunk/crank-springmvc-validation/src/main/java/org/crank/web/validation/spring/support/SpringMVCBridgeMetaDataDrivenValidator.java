package org.crank.web.validation.spring.support;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.crank.core.CrankConstants;
import org.crank.core.CrankContext;
import org.crank.core.ObjectRegistry;
import org.crank.core.PropertiesUtil;
import org.crank.core.spring.support.SpringBeanWrapperPropertiesUtil;
import org.crank.validation.FieldValidator;
import org.crank.validation.ValidatorMessage;
import org.crank.validation.ValidatorMessageHolder;
import org.crank.validation.ValidatorMessages;
import org.crank.validation.ValidatorMetaData;
import org.crank.validation.ValidatorMetaDataReader;
import org.crank.validation.readers.AnnotationValidatorMetaDataReader;
import org.crank.validation.validators.CompositeValidator;
import org.crank.web.CrankWebContext;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class SpringMVCBridgeMetaDataDrivenValidator implements Validator {
	private ValidatorMetaDataReader validatorMetaDataReader = new AnnotationValidatorMetaDataReader();
	private PropertiesUtil validatorPropertiesUtil = new SpringBeanWrapperPropertiesUtil();
	
	public boolean supports(final Class clazz) {
		return true;
	}

	public void setValidatorPropertiesUtil(PropertiesUtil validatorPropertiesUtil) {
		this.validatorPropertiesUtil = validatorPropertiesUtil;
	}

	public void validate(final Object object, final Errors errors) {
		SpringValidatorContext.create();
		validateObject(object, errors);
		SpringValidatorContext.destroy();
		
	}

	private void validateObject(final Object object, final Errors errors) {

        List <MessageHolder> validationMessages = doValidateObject(object);
        for (MessageHolder message  : validationMessages) {
             extractMessages(message.propertyPath, errors, message.holder);
        }

	}

    @SuppressWarnings("unchecked")
	private void extractMessages(final String property, final Errors errors, ValidatorMessageHolder holder) {
		ValidatorMessages messages = (ValidatorMessages) holder;
		for (ValidatorMessage message : messages){
			errors.rejectValue(property, null, null, message.getDetail());
		}
	}


    class MessageHolder {
        private String propertyPath;
        private ValidatorMessageHolder holder;

        MessageHolder(String propertyPath, ValidatorMessageHolder holder) {
            this.propertyPath = propertyPath;
            this.holder = holder;
        }
    }

    private List <MessageHolder> doValidateObject(final Object object) {
        return doValidateObject(object, null);
    }
    private List <MessageHolder> doValidateObject(final Object object, List <MessageHolder> validationMessages) {
		List<PropertyDescriptor> fieldsToValidate = getFieldsToValidate(object);
		Map<String, Object> objectPropertiesAsMap = validatorPropertiesUtil.getObjectPropertiesAsMap(object);
        if (validationMessages==null) {
            validationMessages = new ArrayList<MessageHolder>();
        }
        
        for (PropertyDescriptor field : fieldsToValidate){

            /* Keep track of the field name and parentObject so the field validators can access them. */
            SpringValidatorContext.get().pushProperty(field.getName());
            SpringValidatorContext.get().setParentObject(object);
			if (shouldFieldBeValidated()) {
				Object propertyObject = objectPropertiesAsMap.get(field.getName());
				validateProperty(object, propertyObject, field.getName(), validationMessages);
				if (propertyObject!=null) {
					doValidateObject(propertyObject, validationMessages);
				}
			}
			SpringValidatorContext.get().pop();
		}

        return validationMessages;

    }

    @SuppressWarnings("unchecked")
	private boolean shouldFieldBeValidated() {
		CrankWebContext crankWebContext = CrankWebContext.getInstance();
        Set paramSet = crankWebContext.getRequestParameters().keySet();
        String bindingPath = SpringValidatorContext.getBindingPath();
		return paramSet.contains(bindingPath) || shouldNestedFieldBeValidated(bindingPath, paramSet); 
	}

	private boolean shouldNestedFieldBeValidated(String bindingPath, Set<String> paramSet) {
		
		//bp department 			param department.address.line1
		//bp adress		 			param department.address.line1
		//bp firstName		 		param department.address.line1

		for (String param : paramSet) {
			if (param.startsWith(bindingPath)) {
				return true;
			}
		}
		return false;
	}

    private void validateProperty(final Object object, final Object objectProperty, final String property,
            List <MessageHolder> vMessageHolders) {

        List<ValidatorMetaData> metaDataList = readMetaData(object.getClass(),
                property);
        CompositeValidator cv = createValidator(metaDataList);
        ValidatorMessageHolder holder = cv.validate(objectProperty, property);
        vMessageHolders.add(new MessageHolder(SpringValidatorContext.getBindingPath(), holder));
    }
    


	private List<PropertyDescriptor> getFieldsToValidate(Object object) {
		/** TODO read validate field list from request param. 
		 * TODO create a request context simliar to JSF facesContext to easily
		 * get params.
		 */
		List<PropertyDescriptor> properties;
		//IF PARAM NOT FOUND... GET ALL PROPERTIES 
		BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(object.getClass());
		} catch (IntrospectionException e) {
			
			throw new RuntimeException(e);
		}
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		properties = new ArrayList<PropertyDescriptor>(propertyDescriptors.length);
		for (PropertyDescriptor pd : propertyDescriptors) {
			if (!pd.getName().equals("class")) {
				properties.add(pd);
			}
		}
		return properties;
	}

	protected List<ValidatorMetaData> readMetaData(Class clazz,
			String propertyName) {
		return validatorMetaDataReader.readMetaData(clazz,
				propertyName);
	}
	
	/**
	 * Create the validator by looking it up in the ObjectRegistry and then
	 * populating it with values from the meta-data list.
	 * 
	 * @param validationMetaDataList
	 *            Holds metadataInformation about validation.
     * @return composite validator with all of the validators for this property present.
	 */
	protected CompositeValidator createValidator(
			List<ValidatorMetaData> validationMetaDataList) {

		/*
		 * A field (property) can be associated with many validators so we use a
		 * CompositeValidator to hold all of the validators associated with this
		 * validator.
		 */
		CompositeValidator compositeValidator = new CompositeValidator(); // hold
																			// all
																			// of
																			// the
																			// validators
																			// associated
																			// with
																			// the
																			// field.

		/*
		 * Lookup the list of validators for the current field and initialize
		 * them with validation meta-data properties.
		 */
		List<FieldValidator> validatorsList = 
			lookupTheListOfValidatorsAndInitializeThemWithMetaDataProperties(validationMetaDataList);

		compositeValidator.setValidatorList(validatorsList);

		return compositeValidator;
	}
	

	/**
	 * Lookup the list of validators for the current field and initialize them
	 * with validation meta-data properties.
     * @param validationMetaDataList list of validation meta-data
     * @return list of field validators.
	 */
	private List<FieldValidator> 
	   lookupTheListOfValidatorsAndInitializeThemWithMetaDataProperties(
			   List<ValidatorMetaData> validationMetaDataList) {
		
		List<FieldValidator> validatorsList = new ArrayList<FieldValidator>();

		/*
		 * Look up the crank validators and then apply the properties from the
		 * validationMetaData to them.
		 */
		for (ValidatorMetaData validationMetaData : validationMetaDataList) {
			/* Look up the FieldValidator. */
			FieldValidator validator = lookupValidatorInRegistry(
					validationMetaData
					.getName());
			/*
			 * Apply the properties from the validationMetaData to the
			 * validator.
			 */
			applyValidationMetaDataPropertiesToValidator(validationMetaData,
					validator);
			validatorsList.add(validator);
		}
		return validatorsList;
	}
	
	/**
	 * This method looks up the validator in the registry.
	 * 
	 * 
	 * @param validationMetaDataName
	 *            The name of the validator that we are looking up.
	 * 
	 * @return field validator
	 */
	private FieldValidator lookupValidatorInRegistry(
			String validationMetaDataName) {
		ObjectRegistry applicationContext = CrankContext.getObjectRegistry();

		return (FieldValidator) applicationContext
				.getObject(CrankConstants.FRAMEWORK_PREFIX
						+ CrankConstants.FRAMEWORK_DELIM + "validator"
						+ CrankConstants.FRAMEWORK_DELIM
						+ validationMetaDataName, FieldValidator.class);
	}

	/**
	 * This method applies the properties from the validationMetaData to the
	 * validator uses Spring's BeanWrapperImpl.
	 * 
	 * @param metaData validation meta data
     * @param validator field validator
	 * 
	 */
	private void applyValidationMetaDataPropertiesToValidator(
			ValidatorMetaData metaData, FieldValidator validator) {
		Map<String, Object> properties = metaData.getProperties();
        ifPropertyBlankRemove(properties, "detailMessage");
        ifPropertyBlankRemove(properties, "summaryMessage");
        //TODO left off here.
		this.validatorPropertiesUtil.copyProperties(validator,
				properties);
	}

    /** Removes a property if it is null or an empty string. 
     *  This allows the property to have a null or emtpy string in the
     *  meta-data but we don't copy it to the validator if the property
     *  is not set.
     * @param properties  properties
     * @param property    property
     * */
	private void ifPropertyBlankRemove(Map<String, Object> properties, String property) {
	    Object object = properties.get(property);
        if (object == null) {
            properties.remove(property);
        } else if (object instanceof String) {
            String string = (String) object;
            if ("".equals(string.trim())){
                properties.remove(property);    
            }
        }
    }

	public void setValidatorMetaDataReader(
			ValidatorMetaDataReader validatorMetaDataReader) {
		this.validatorMetaDataReader = validatorMetaDataReader;
	}

}


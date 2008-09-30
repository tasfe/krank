package org.crank.validation;

import org.crank.validation.readers.AnnotationValidatorMetaDataReader;
import org.crank.validation.validators.CompositeValidator;
import org.crank.core.PropertiesUtil;
import org.crank.core.ObjectRegistry;
import org.crank.core.CrankContext;
import org.crank.core.CrankConstants;
import org.crank.core.spring.support.SpringBeanWrapperPropertiesUtil;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.beans.PropertyDescriptor;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.IntrospectionException;

/**
 * Created by IntelliJ IDEA.
 * User: richardhightower
 * Date: May 2, 2008
 * Time: 1:59:50 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class RecursiveDescentPropertyValidator {
    protected ValidatorMetaDataReader validatorMetaDataReader = new AnnotationValidatorMetaDataReader();
    protected PropertiesUtil validatorPropertiesUtil = new SpringBeanWrapperPropertiesUtil();
 
    protected class MessageHolder {
        public String propertyPath;
        public ValidatorMessageHolder holder;
        MessageHolder(String propertyPath, ValidatorMessageHolder holder) {
            this.propertyPath = propertyPath;
            this.holder = holder;
        }
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

    protected List<ValidatorMetaData> readMetaData(Class<?> clazz,
            String propertyName) {
        return validatorMetaDataReader.readMetaData(clazz,
                propertyName);
    }

    private void validateProperty(final Object object, final Object objectProperty, final String property,
            List <MessageHolder> vMessageHolders) {

        List<ValidatorMetaData> metaDataList = readMetaData(object.getClass(),
                property);
        CompositeValidator cv = createValidator(metaDataList);
        ValidatorMessageHolder holder = cv.validate(objectProperty, property);
        vMessageHolders.add(new MessageHolder(ValidationContext.getBindingPath(), holder));
    }

    protected abstract boolean shouldFieldBeValidated() ;

    
    public void setValidatorPropertiesUtil(PropertiesUtil validatorPropertiesUtil) {
		this.validatorPropertiesUtil = validatorPropertiesUtil;
	}

    public List<MessageHolder> validateObject(final Object object) {
        ValidationContext.create();
        List <MessageHolder> list =  validateObject(object, null);
        ValidationContext.destroy();
        return list;
    }
    public List <MessageHolder> validateObject(final Object object, List <MessageHolder> validationMessages) {
		List<PropertyDescriptor> fieldsToValidate = getFieldsToValidate(object);
		Map<String, Object> objectPropertiesAsMap = validatorPropertiesUtil.getObjectPropertiesAsMap(object);
        if (validationMessages==null) {
            validationMessages = new ArrayList<MessageHolder>();
        }

        for (PropertyDescriptor field : fieldsToValidate){

            /* Keep track of the field name and parentObject so the field validators can access them. */
            ValidationContext.get().pushProperty(field.getName());
            ValidationContext.get().setParentObject(object);
			if (shouldFieldBeValidated()) {
				Object propertyObject = objectPropertiesAsMap.get(field.getName());
				validateProperty(object, propertyObject, field.getName(), validationMessages);
				if (propertyObject!=null) {
					validateObject(propertyObject, validationMessages);
				}
			}
			ValidationContext.get().pop();
		}

        return validationMessages;

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

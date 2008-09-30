package org.crank.web.validation.jsf.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import org.crank.annotations.design.DependsOnJSF;
import org.crank.annotations.design.ExpectsInjection;
import org.crank.core.CrankConstants;
import org.crank.core.CrankContext;
import org.crank.core.ObjectRegistry;
import org.crank.core.PropertiesUtil;
import org.crank.validation.FieldValidator;
import org.crank.validation.ValidationContext;
import org.crank.validation.ValidatorMetaData;
import org.crank.validation.ValidatorMetaDataReader;
import org.crank.validation.validators.CompositeValidator;

/**
 * 
 * <p>
 * This class allows us to perform JSF validations based on meta-data stored in
 * our classes.
 * </p>
 * 
 * <p>
 * This class acts as a bridge from the Crank validator world to the JSF world.
 * </p>
 * 
 * <p>
 * This allows us to easily associated validators with model properties'
 * meta-data.
 * 
 * We then delegate validation to the crank validation system.
 * </p>
 * 
 * @author Rick Hightower
 */
@DependsOnJSF
public class JSFBridgeMetaDataDrivenValidator extends
		AbstractJSFBridgeValidator {
	private ValidatorMetaDataReader jsfBridgeValidatorMetaDataReader = null;
    private Class<?> formClass;
    private String fieldName;
    private Object parentObject;

	private PropertiesUtil jsfBridgeValidatorPropertiesUtil = null;

    @Override
	/**
	 * Find the crank validator.
	 * 
	 * 
	 */
	@DependsOnJSF
	protected FieldValidator findValidatorAndFieldName(
			FacesContext facesContext, UIComponent component,
			String[] fieldNameHolder) {

		/*
		 * Get the value expression associated with the component that was
		 * passed.
		 */
		UIInput inputComponent = (UIInput) component;
		ValueExpression valueExpression = inputComponent
				.getValueExpression("value");
		String expressionString = valueExpression.getExpressionString();
        /*
         * Extract the parent object expression and the property name (field)
         * that we are validating.
         */
//		System.out.println("expressionString = " + expressionString);
//		System.out.println("formClass = " + formClass.getName());
//		System.out.println("fieldName = " + fieldName);
//		System.out.println("parentObject = " + (parentObject==null?"null":parentObject.getClass().getName()));
        ValidatorData validatorData = new ValidatorData(expressionString, facesContext, formClass, fieldName, parentObject);

        registerValidationContext(facesContext, inputComponent, validatorData);
		fieldNameHolder[0] = validatorData.getPropertyNameOfTheField();

		/* Read the metaDataList for this property of this parent object. */
		List<ValidatorMetaData> metaDataList = readMetaData(validatorData
				.getParentClassOfTheField(), validatorData.getPropertyNameOfTheField());
		CompositeValidator cv = createValidator(metaDataList);
		return cv;
	}
    
    /**
     * Clean up the Validation context.
     * If you love something set it free. If it does not come back to you,
     * hunt it down and really destroy it. Clean up the Thread local variable.
     */
    protected void cleanup() {
        JSFValidationContext context = (JSFValidationContext) 
            ValidationContext.getCurrentInstance();
        context.free();
    }

    /**
     * Register a new ValidationContext and initialize it.
     * 
     * @param facesContext
     * @param inputComponent
     * @param validatorData
     */
    private void registerValidationContext(FacesContext facesContext, UIInput inputComponent, ValidatorData validatorData) {
        JSFValidationContext context = new JSFValidationContext(inputComponent);
        context.setParentObject(validatorData.lookupParentObject(facesContext));
        context.setParams(inputComponent.getAttributes());
        context.register(context);
    }

	/**
	 * Create the validator by looking it up in the ObjectRegistry and then
	 * populating it with values from the meta-data list.
	 * 
	 * @param validationMetaDataList
	 *            Holds metadataInformation about validation.
	 * @return
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

		compositeValidator.setValidatorList( validatorsList);

		return compositeValidator;
	}

	/**
	 * Lookup the list of validators for the current field and initialize them
	 * with validation meta-data properties.
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

	protected List<ValidatorMetaData> readMetaData(Class<?> clazz,
			String propertyName) {
		initIfNeeded();
		return jsfBridgeValidatorMetaDataReader.readMetaData(clazz,
				propertyName);
	}

	/** For dependency injection of the ValidatorMetaDataReader. */
	@ExpectsInjection
	public void setJsfBridgeValidatorMetaDataReader(
			ValidatorMetaDataReader jsfBridgeValidatorMetaDataReader) {
		this.jsfBridgeValidatorMetaDataReader = 
			jsfBridgeValidatorMetaDataReader;
	}

	/**
	 * This method looks up the validator in the registry.
	 * 
	 * 
	 * @param validationMetaDataName
	 *            The name of the validator that we are looking up.
	 * 
	 * @return
	 */
	private FieldValidator lookupValidatorInRegistry(
			String validationMetaDataName) {
		ObjectRegistry applicationContext = CrankContext.getObjectRegistry();

		FieldValidator validator = (FieldValidator) applicationContext
				.getObject(CrankConstants.FRAMEWORK_PREFIX
						+ CrankConstants.FRAMEWORK_DELIM + "validator"
						+ CrankConstants.FRAMEWORK_DELIM
						+ validationMetaDataName, FieldValidator.class);
		return validator;
	}

	/**
	 * Look up dependencies in object registry.
	 * 
	 */
	private void initIfNeeded() {
		if (jsfBridgeValidatorMetaDataReader == null
				|| jsfBridgeValidatorPropertiesUtil == null) {
			CrankContext.getObjectRegistry().resolveCollaborators(this);
			assert jsfBridgeValidatorMetaDataReader != null : "The " +
					"jsfBridgeValidatorMetaDataReader was found in " +
					"the Spring application context file";
		}
	}

	/**
	 * This method applies the properties from the validationMetaData to the
	 * validator uses Spring's BeanWrapperImpl.
	 * 
	 * 
	 * @param validationMetaDataName
	 *            The name of the validator that we are looking up.
	 * 
	 * @return
	 */
	private void applyValidationMetaDataPropertiesToValidator(
			ValidatorMetaData metaData, FieldValidator validator) {
		initIfNeeded();
		Map<String, Object> properties = metaData.getProperties();
        ifPropertyBlankRemove(properties, "detailMessage");
        ifPropertyBlankRemove(properties, "summaryMessage");
		this.jsfBridgeValidatorPropertiesUtil.copyProperties(validator,
				properties);
	}

    /** Removes a property if it is null or an empty string. 
     *  This allows the property to have a null or emtpy string in the
     *  meta-data but we don't copy it to the validator if the property
     *  is not set.
     *  @author Rick Hightower 
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

    public void setJsfBridgeValidatorPropertiesUtil(
			PropertiesUtil jsfBridgeValidatorPropertiesUtil) {
		this.jsfBridgeValidatorPropertiesUtil = 
			jsfBridgeValidatorPropertiesUtil;
	}

    public Class<?> getFormClass() {
        return formClass;
    }

    public void setFormClass( Class<?> formClass ) {
        this.formClass = formClass;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName( String fieldName ) {
        this.fieldName = fieldName;
    }

	public Object getParentObject() {
		return parentObject;
	}

	public void setParentObject(Object parentObject) {
		this.parentObject = parentObject;
	}

}

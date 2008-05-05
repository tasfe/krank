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
import org.crank.validation.*;
import org.crank.validation.readers.AnnotationValidatorMetaDataReader;
import org.crank.validation.validators.CompositeValidator;
import org.crank.web.CrankWebContext;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class SpringMVCBridgeMetaDataDrivenValidator extends RecursiveDescentPropertyValidator implements Validator {
	
	public boolean supports(final Class clazz) {
		return true;
	}


	public void validate(final Object object, final Errors errors) {
		validateObject(object, errors);
	}

	private void validateObject(final Object object, final Errors errors) {
        List <MessageHolder> validationMessages = validateObject(object);
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





    @SuppressWarnings("unchecked")
	protected boolean shouldFieldBeValidated() {
		CrankWebContext crankWebContext = CrankWebContext.getInstance();
        Set paramSet = crankWebContext.getRequestParameters().keySet();
        String bindingPath = ValidationContext.getBindingPath();
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
}


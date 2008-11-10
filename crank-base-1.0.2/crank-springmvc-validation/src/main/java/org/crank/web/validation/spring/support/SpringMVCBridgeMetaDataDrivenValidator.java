package org.crank.web.validation.spring.support;

import java.util.List;
import java.util.Set;

import org.crank.validation.*;
import org.crank.web.CrankWebContext;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class SpringMVCBridgeMetaDataDrivenValidator extends RecursiveDescentPropertyValidator implements Validator {
	
	@SuppressWarnings("unchecked")
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


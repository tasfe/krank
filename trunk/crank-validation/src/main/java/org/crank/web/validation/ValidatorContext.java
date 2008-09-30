package org.crank.web.validation;

import java.util.Collections;
import java.util.Map;

/** Holds fieldName being validated and validation rule meta-data. */
@SuppressWarnings("unchecked")
public class ValidatorContext {
	
	private Map<String, Object> validationRuleMetaData = 
        Collections.EMPTY_MAP;
    
	private String fieldName = " NOT SET! ";
    
	protected Map<String, Object> getValidationRuleMetaData() {
		return validationRuleMetaData;
	}
	protected void setValidationRuleMetaData(Map<String, Object> arguments) {
		this.validationRuleMetaData = arguments;
	}
	protected String getFieldName() {
		return fieldName;
	}
	protected void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

}

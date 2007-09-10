package org.crank.validation.validators;

import org.crank.annotations.design.Implements;
import org.crank.validation.FieldValidator;
import org.crank.validation.ValidatorMessage;
import org.crank.validation.ValidatorMessageHolder;


/** 
 * LongRangeValidator works with all integer ranges. 
 * 
 */
public class LengthValidator extends AbstractValidator {

	/** Perform the actual validation. 
     * @param fieldValue the value to validate
     * @param the logical name of the value used for generating error messages
     * 
     */
    @Implements (interfaceClass=FieldValidator.class)
	public ValidatorMessageHolder validate(Object fieldValue, String fieldLabel) {
		ValidatorMessage validatorMessage = new ValidatorMessage();
        if (fieldValue == null) {
        	return validatorMessage;
        }
        
        String value = fieldValue.toString();
        
        if (!(value.length() >= min && value.length() <= max)) {
            populateMessage(validatorMessage, fieldLabel, min, max);
        }

		
		return validatorMessage;
		
	}

	/** The min value. */
	private Long min = 0L;
	/** The max value. */
    private Long max = Long.MAX_VALUE;
    public void setMax( Long max ) {
        this.max = max;
    }
    public void setMin( Long min ) {
        this.min = min;
    }

}

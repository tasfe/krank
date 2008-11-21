package org.crank.validation.validators;

import org.crank.annotations.design.AllowsConfigurationInjection;
import org.crank.annotations.design.Implements;
import org.crank.message.MessageSpecification;
import org.crank.validation.FieldValidator;
import org.crank.validation.ValidatorMessage;
import org.crank.validation.ValidatorMessageHolder;


/** 
 * LongRangeValidator works with all integer ranges. 
 * 
 */
public class LongRangeValidator extends AbstractRangeValidator {

	/** Perform the actual validation. 
     * @param fieldValue the value to validate
     * @param the logical name of the value used for generating error messages
     * 
     */
    @SuppressWarnings("unchecked")
	@Implements (interfaceClass=FieldValidator.class)
	public ValidatorMessageHolder validate(Object fieldValue, String fieldLabel) {
		ValidatorMessage validatorMessage = new ValidatorMessage();
        if (fieldValue == null) {
        	return validatorMessage;
        }

		dynamicallyInitIfNeeded(fieldValue);
		
		if (!super.isValueGreaterThanMin((Comparable)fieldValue)){
			    populateMessage(underMin, validatorMessage, fieldLabel, min);
		} else if (!super.isValueLessThanMax((Comparable)fieldValue)) {
				populateMessage(overMax, validatorMessage, fieldLabel, max);
		}
		
		return validatorMessage;
		
	}

	/** The min value. */
	private Long min;
	/** The max value. */
    private Long max;
    /* What type are we working with: Integer, Long, etc. */
    private Class<?> type;
    /* The underMin message used if the value is under the min. */
    private MessageSpecification underMin;
    /* The overMax message used if the value is over the max. */
    private MessageSpecification overMax;
    
    /* Initialize this instance. */
    public void init() {
    	/* If the underMin message was not injected, create a default. */
    	if (underMin==null) {
    		underMin = new MessageSpecification();
    		underMin.setDetailMessage("{validator.range.underMin.detail}");
    		underMin.setSummaryMessage("{validator.range.underMin.summary}");
    		underMin.setResourceBundleLocator(this.getResourceBundleLocator());
    	}
    	/* If the overMax message was not injected, create a default. */
    	if (overMax==null) {
    		overMax = new MessageSpecification();
    		overMax.setDetailMessage("{validator.range.overMax.detail}");
    		overMax.setSummaryMessage("{validator.range.overMax.summary");
    		overMax.setResourceBundleLocator(this.getResourceBundleLocator());
    	}
    	/* If the type was not injected, stop initialization. */
    	if (type==null) {
    		return;
    	}
    	/* Initialize based on type for all Integer value
    	 * so that LongRangeValidator can be used
    	 * for int, short, byte, and long. */
		if (! isInitialized()) {
			if (type.equals(Integer.class)) {
				init(new Integer(min.intValue()), new Integer(max.intValue()));
			} else if (type.equals(Byte.class)) {
				init(new Byte(min.byteValue()), new Byte(max.byteValue()));
			} else if (type.equals(Short.class)) {
				init(new Short(min.byteValue()), new Short(max.byteValue()));
			} else {
				init(min,max);
			}
		}
    }


	/**
	 * If the type was not initialized, we can still figure it out at runtime.
	 * @param value
	 */
	private void dynamicallyInitIfNeeded(Object value) {
		/* Check to see if this class was already initialized,
		 * if not, initialize it based on the type of the value.
		 */
		if (! isInitialized()) {
			if (value instanceof Integer) {
				init(new Integer(min.intValue()), new Integer(max.intValue()));
			} else if (value instanceof Byte) {
				init(new Byte(min.byteValue()), new Byte(max.byteValue()));
			} else if (value instanceof Short) {
				init(new Short(min.shortValue()), new Short(max.shortValue()));
			} else {
				init(min,max);
			}
		}
	}


	@AllowsConfigurationInjection
	public void setMax(Long max) {
		this.max = max;
	}


	@AllowsConfigurationInjection
	public void setMin(Long min) {
		this.min = min;
	}

	@AllowsConfigurationInjection
	public void setType(Class<?> type) {
		this.type = type;
	}

	@AllowsConfigurationInjection
	protected void setOverMax(MessageSpecification overMax) {
		this.overMax = overMax;
	}
	
	@AllowsConfigurationInjection
	protected void setUnderMin(MessageSpecification underMin) {
		this.underMin = underMin;
	}

}

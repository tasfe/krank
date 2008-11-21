package org.crank.validation;


import java.io.Serializable;



/**
 * <p>
 * <small>
 * Validator interface for crank validation.
 * This would get used by commons and our other validation mechanism.
 * </small>
 * </p>
 * @author Rick Hightower
 */
public interface FieldValidator extends Serializable {
    /**
     * Validates a single field.
     * @param object object to validate
     * @return A messages whose hasError is set to true if there was an error.
     */
    ValidatorMessageHolder validate(Object fieldValue, String fieldLabel);
}

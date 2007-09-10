package org.crank.validation.validators;


import org.crank.validation.ValidatorMessage;
import org.crank.validation.ValidatorMessageHolder;


/**
 *
 * <p>
 * <small>
 * Required validator.
 * </small>
 * </p>
 * @author Rick Hightower
 */
public class RequiredValidator extends AbstractValidator {
    
    public void init () {
        this.setDetailMessage( "{validator.required.detail}" );
        this.setSummaryMessage( "{validator.required.summary}" );
    }

    public ValidatorMessageHolder validate(Object object, String fieldLabel) {
        ValidatorMessage message = new ValidatorMessage();

        if (object instanceof String) {
            String string = (String) object;
            boolean valid =  string != null && !string.trim().equals("");
            if (!valid) {
                populateMessage(message, fieldLabel);
            }

        } else {
            if (object == null) {
                populateMessage(message, fieldLabel);
            }
        }

        return message;
    }


}

package org.crank.validation.validators;


import java.util.ArrayList;
import java.util.List;


import org.crank.validation.FieldValidator;
import org.crank.validation.ValidatorMessage;
import org.crank.validation.ValidatorMessageHolder;
import org.crank.validation.ValidatorMessages;


/**
 *
 * <p>
 * Combines a bunch of validators into one.
 * </p>
 * @author Rick Hightower
 */
public class CompositeValidator implements FieldValidator {
    private List<FieldValidator> list = new ArrayList<FieldValidator>();
    private List <String> detailArgs;
    private List <String> summaryArgs;


    public void setList(List<FieldValidator> list) {
        this.list = list;
    }

    public ValidatorMessageHolder validate(Object object, String fieldLabel) {
        ValidatorMessages messages = new ValidatorMessages();
        for (FieldValidator validator : list) {
            putArgs(validator);
            ValidatorMessage message = (ValidatorMessage) validator.validate(object, fieldLabel);
            if (message.hasError()) {
                messages.add(message);
            }
        }

        return messages;
    }

    private void putArgs(FieldValidator validator) {
        if (validator instanceof AbstractValidator) {
            AbstractValidator aValidator = (AbstractValidator) validator;
            aValidator.setDetailArgs(this.detailArgs);
            aValidator.setSummaryArgs(this.summaryArgs);
        }
    }


    public void setDetailArgs(List<String> detailArgKeys) {
        this.detailArgs = detailArgKeys;
    }


    public void setSummaryArgs(List<String> summaryArgKeys) {
        this.summaryArgs = summaryArgKeys;
    }


}

package org.crank.validation.validators;





import org.crank.core.NameAware;
import org.crank.message.MessageSpecification;
import org.crank.validation.FieldValidator;
import org.crank.validation.ValidatorMessage;


/**
 *
 * <p>
 * <small>
 * Base class for some validators.
 * </small>
 * </p>
 * @author Rick Hightower
 */
public abstract class AbstractValidator extends MessageSpecification implements NameAware, FieldValidator {

    
    public boolean noMessages = false;
    
    public boolean isNoMessages() {
        return noMessages;
    }

    public void setNoMessages(boolean noMessages) {
        this.noMessages = noMessages;
    }

    protected void populateMessage(ValidatorMessage message, String fieldLabel, Object... args) {
    	populateMessage(null, message, fieldLabel, args);
    }

    protected void populateMessage(MessageSpecification ms, ValidatorMessage message, String fieldLabel, Object... args) {
        if (ms==null) {
        	ms = this;
        }
        
        ms.setCurrentSubject(fieldLabel);
        if (!noMessages) {
            message.setSummary(ms.createSummaryMessage(args));
            message.setDetail(ms.createDetailMessage(args));
        }
        ms.setCurrentSubject(null);
        message.setHasError(true);
        
    }
    

}

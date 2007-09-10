package org.crank.web.validation.jsf.support;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.crank.annotations.design.DependsOnJSF;
import org.crank.annotations.design.Implements;
import org.crank.validation.FieldValidator;
import org.crank.validation.ValidatorMessage;
import org.crank.validation.ValidatorMessageHolder;
import org.crank.validation.ValidatorMessages;

/** Base class Spring aware, JSF bridge to our validation system. 
 * This uses the template design pattern and expects subclasses 
 * to extend and implement the <code>findValidator</code> method.
 * @author Rick Hightower
 * */
@DependsOnJSF
public abstract class AbstractJSFBridgeValidator implements Validator, Serializable {

	/**
	 * Validate the field value.
	 * @param facesContext FacesContext
	 * @param component component
	 * @param fieldValueToValidate The object to validate
	 * 
	 */
	@Implements (interfaceClass=Validator.class)
	public void validate(FacesContext facesContext, UIComponent component,
			Object fieldValueToValidate) {
		
		String[] fieldNameHolder = new String[] { "" };
		
		/* Find the validator. How the validator is found
		 * varies amongst subclasses.
		 */
		FieldValidator validator = findValidatorAndFieldName(facesContext,
				component, fieldNameHolder);

		/* Validate the field. */
		ValidatorMessageHolder message = validator.validate(
				fieldValueToValidate, fieldNameHolder[0]);

		convertMessageToFacesMessages(message, facesContext, component);
        
        cleanup();
	}

	protected abstract void cleanup() ;

    /**
	 * This converts a ValidatorMessageHolder to a FacesMessage or 
	 * FacesMessages and adds them to the facesContext. 
	 * @param imessage
	 */
	private void convertMessageToFacesMessages(ValidatorMessageHolder imessage,
			FacesContext facesContext, UIComponent component) {
		if (imessage instanceof ValidatorMessage) {
			ValidatorMessage message = (ValidatorMessage) imessage;
			if (message.hasError()) {
				throw new ValidatorException(
						convertMessageToFacesMessage(message));
			}
		} else if (imessage instanceof ValidatorMessages) {
			ValidatorMessages messages = (ValidatorMessages) imessage;
			for (ValidatorMessage message : messages) {
				if (message.hasError()) {
					facesContext.addMessage(
							component.getClientId(facesContext),
							convertMessageToFacesMessage(message));
					UIInput uiComponent = (UIInput) component;
					uiComponent.setValid(false);
				}
			}
		}
	}

	/**
	 * Converts a Crank validation message into a FacesValidation message.
	 * @param message
	 * @return
	 */
	private FacesMessage convertMessageToFacesMessage(ValidatorMessage message) {
		FacesMessage facesMessage = new FacesMessage();
		facesMessage.setDetail(message.getDetail());
		facesMessage.setSummary(message.getSummary());
		facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
		return facesMessage;
	}

	/**
	 * Must be overriden by subclass.
	 * @param facesContext
	 * @param component
	 * @return
	 */
	protected abstract FieldValidator findValidatorAndFieldName(
			FacesContext facesContext, UIComponent component,
			String[] fieldNameHolder);
}
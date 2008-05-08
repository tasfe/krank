package org.crank.crud.jsf.support;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.crank.message.MessageManagerUtils;
import org.crank.message.SimpleMessageManager;

public class JsfMessageActionListener extends MessageProcessor implements
		ActionListener {

	private ActionListener defaultActionListener;
	
	public JsfMessageActionListener (ActionListener defaultActionListener) {
		this.defaultActionListener = defaultActionListener;
	}
	
	public void processAction(ActionEvent event)
			throws AbortProcessingException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		try {
			MessageManagerUtils.setCurrentInstance(new SimpleMessageManager());
			defaultActionListener.processAction(event);
			
        } catch (FacesException ex) {
            Throwable rootCause = ex;
            while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
                rootCause = rootCause.getCause();
            }
            
            String message = rootCause.getMessage();
            if (message != null) {
                facesContext.addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
            }
            /* Populate stack trace for display. */
            extractErrorMessage(facesContext, ex);
            logError(ex);           
		} catch (Exception ex) {
			/* If there was an exception, stay on the current view and add
			 * an error message.
			 */
			/* Reviewed with Paul Tabor. Paul asked for localization of message. 
			 * see ex.getLocalizedMessage() also see how Validation framework localizes messages.
			 * 
			 */
			facesContext.addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
			
			/* Populate stack trace for display. */
			extractErrorMessage(facesContext, ex);
			logError(ex);			
		}
        generateFacesMessages(facesContext);

	}

}

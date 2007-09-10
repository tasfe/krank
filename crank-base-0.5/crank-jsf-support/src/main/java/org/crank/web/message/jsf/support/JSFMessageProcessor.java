package org.crank.web.message.jsf.support;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.crank.annotations.design.DependsOnJSF;
import org.crank.message.MessageProcessor;
import org.crank.metadata.ErrorHandlerData;
import org.crank.metadata.Severity;

@DependsOnJSF
public class JSFMessageProcessor implements MessageProcessor{

	/** 
	 * Handle the exception and covert it into a FacesMessage. 
	 * @param exception The exception we are handling
	 * @param handler The informaiton on how to handle this exception
	 * 
	 * */
	public String handleException(Exception exception, ErrorHandlerData handler) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		
		/* Populate Severity. */
		if (handler.getSeverity()==Severity.ERROR) {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
		} else if (handler.getSeverity()==Severity.FATAL) {
			message.setSeverity(FacesMessage.SEVERITY_FATAL);
		} else if (handler.getSeverity()==Severity.INFO) {
			message.setSeverity(FacesMessage.SEVERITY_INFO);
		} else if (handler.getSeverity()==Severity.WARN) {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
		} 
				
		/* Populate Error messages. */
		if (handler.isUseExceptionForDetail()) {
			message.setDetail(exception.getLocalizedMessage());
			message.setSummary(handler.getMessageSummary());
		} else if (handler.isUseMessageBundleForMessage()){
			throw new UnsupportedOperationException("We don't support I18N yet"); 
		} else if (handler.isUseMessageBundleForArgs()) {
			throw new UnsupportedOperationException("We don't support I18N yet");
		}
		 else {
			message.setDetail(handler.getMessageDetail());
			message.setSummary(handler.getMessageSummary());
		}
		
		/* Add message to FacesContext. */
		if (handler.getId().equals("")) {
			facesContext.addMessage(null, message);
		} else {
			facesContext.addMessage(handler.getId(), message);
		}
		
		/* Return the outcome. */
		return handler.getOutcome();
	}

}

package org.crank.crud.jsf.support;


import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.crank.message.MessageManagerUtils;
import org.crank.message.SimpleMessageManager;

/** 
 * Turns exceptions into JSF Messages. 
 * @author Rick Hightower
 * Used to add error handling to non-JSF based classes.
 *
 */
public class JsfMessageInterceptor extends MessageProcessor implements MethodInterceptor {

	/**
	 * Wrap method calls with error handling.
	 */
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		try {
			MessageManagerUtils.setCurrentInstance(new SimpleMessageManager());
			Object returnObject = methodInvocation.proceed();
			
			generateFacesMessages(facesContext);
			
			return returnObject;
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
			
			return null;
		}
	}

}

package org.crank.crud.jsf.support;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.crank.message.MessageManager;
import org.crank.message.MessageManagerUtils;
import org.crank.message.SimpleMessageManager;

/** 
 * Turns exceptions into JSF Messages. 
 * @author Rick Hightower
 * Used to add error handling to non-JSF based classes.
 *
 */
public class JsfMessageInterceptor implements MethodInterceptor {

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

	private void logError(Exception ex) {
		System.err.println(ex.getMessage());
		ex.printStackTrace(System.err);
	}

	private void extractErrorMessage(FacesContext facesContext, Exception ex) {
		StringWriter writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		pw.println("An error occurred ");
		pw.println(ex.getMessage());
		ex.printStackTrace(pw);
		facesContext.getExternalContext().getRequestMap().put("crankErrorException", writer.toString());
	}

	private void generateFacesMessages(FacesContext facesContext) {
		MessageManager mm = MessageManagerUtils.getCurrentInstance();
		
		addMessages(facesContext, FacesMessage.SEVERITY_INFO, mm.getStatusMessages());
		addMessages(facesContext, FacesMessage.SEVERITY_WARN, mm.getWarningMessages());
		addMessages(facesContext, FacesMessage.SEVERITY_FATAL, mm.getFatalMessages());
		addMessages(facesContext, FacesMessage.SEVERITY_ERROR, mm.getErrorMessages());
	}

	private void addMessages(FacesContext facesContext, Severity severity, List<String> messages) {
		for (String message : messages) {
			facesContext.addMessage(null, 
					new FacesMessage(severity, message, null));				
		}
	}

}

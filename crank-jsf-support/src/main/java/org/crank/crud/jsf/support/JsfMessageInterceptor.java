package org.crank.crud.jsf.support;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.crank.message.MessageManagerUtils;
import org.crank.message.SimpleMessageManager;

/** 
 * Turns exceptions into JSF Messages. 
 * @author Rick Hightower
 * Used to add error handling to non-JSF base classes.
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
			
			List<String> messages = MessageManagerUtils.getCurrentInstance().getMessages();
			
			/* Add messages for display. */
			for (String message : messages) {
				facesContext.addMessage(null, 
						new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));				
			}
			
			return returnObject;
		} catch (Exception ex) {
			/* If there was an exception, stay on the current view and add
			 * an error message.
			 */
			facesContext.addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
			
			/* Populate stack trace for display. */
			StringWriter writer = new StringWriter();
			PrintWriter pw = new PrintWriter(writer);
			pw.println("An error occurred ");
			pw.println(ex.getMessage());
			ex.printStackTrace(pw);
			facesContext.getExternalContext().getRequestMap().put("crankErrorException", writer.toString());
			System.err.println(ex.getMessage());
			ex.printStackTrace(System.err);
			
			return null;
		}
	}

}

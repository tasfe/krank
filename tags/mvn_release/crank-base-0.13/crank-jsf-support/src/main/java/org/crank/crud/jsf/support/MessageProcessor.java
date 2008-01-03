package org.crank.crud.jsf.support;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import org.crank.message.MessageManager;
import org.crank.message.MessageManagerUtils;

public class MessageProcessor {

	public MessageProcessor() {
		super();
	}

	protected void logError(Exception ex) {
		System.err.println(ex.getMessage());
		ex.printStackTrace(System.err);
	}

	protected void extractErrorMessage(FacesContext facesContext, Exception ex) {
		StringWriter writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		pw.println("An error occurred ");
		pw.println(ex.getMessage());
		ex.printStackTrace(pw);
		facesContext.getExternalContext().getRequestMap().put("crankErrorException", writer.toString());
	}

	protected void generateFacesMessages(FacesContext facesContext) {
		MessageManager mm = MessageManagerUtils.getCurrentInstance();
		
		addMessages(facesContext, FacesMessage.SEVERITY_INFO, mm.getStatusMessages());
		addMessages(facesContext, FacesMessage.SEVERITY_WARN, mm.getWarningMessages());
		addMessages(facesContext, FacesMessage.SEVERITY_FATAL, mm.getFatalMessages());
		addMessages(facesContext, FacesMessage.SEVERITY_ERROR, mm.getErrorMessages());
	}

	protected void addMessages(FacesContext facesContext, Severity severity, List<String> messages) {
		for (String message : messages) {
			facesContext.addMessage(null, 
					new FacesMessage(severity, message, null));				
		}
	}

}
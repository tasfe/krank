package org.crank.message;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class SimpleMessageManager implements MessageManager {

	private List<String> statusMessages = new ArrayList<String>();
	private List<String> errorMessages = new ArrayList<String>();
	private List<String> fatalMessages = new ArrayList<String>();
	private List<String> warningMessages = new ArrayList<String>();
	
	private String formatString (String message, Object[] args) {
		return (new Formatter()).format(message, args).toString();		
	}
	
	public void addStatusMessage(String message, Object... args) {
		statusMessages.add(formatString(message,args));
	}

	public void addErrorMessage(String message, Object... args) {
		this.errorMessages.add(formatString(message,args));
	}

	public void addFatalMessage(String message, Object... args) {
		this.fatalMessages.add(formatString(message,args));
		
	}

	public void addWarningMessage(String message, Object... args) {
		this.warningMessages.add(formatString(message,args));
	}

	public List<String> getErrorMessages() {
		return this.errorMessages;
	}

	public List<String> getFatalMessages() {
		return this.fatalMessages;
	}

	public List<String> getStatusMessages() {
		return statusMessages;
	}

	public List<String> getWarningMessages() {
		return this.warningMessages;
	}

}

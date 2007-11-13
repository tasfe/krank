package org.crank.message;

import java.util.ArrayList;
import java.util.List;

public class SimpleMessageManager implements MessageManager {

	private List<String> statusMessages = new ArrayList<String>();
	private List<String> errorMessages = new ArrayList<String>();
	private List<String> fatalMessages = new ArrayList<String>();
	private List<String> warningMessages = new ArrayList<String>();
	
	public void addStatusMessage(String message) {
		statusMessages.add(message);
	}


	public void addErrorMessage(String message) {
		this.errorMessages.add(message);
	}

	public void addFatalMessage(String message) {
		this.fatalMessages.add(message);
		
	}

	public void addWarningMessage(String message) {
		this.warningMessages.add(message);
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

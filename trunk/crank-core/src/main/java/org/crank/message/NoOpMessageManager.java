package org.crank.message;

import java.util.Collections;
import java.util.List;

public class NoOpMessageManager implements MessageManager {

	public void addStatusMessage(String message) {
	}
	
	public void addErrorMessage(String message) {
	}

	public void addFatalMessage(String message) {
	}

	public void addWarningMessage(String message) {
	}

	@SuppressWarnings("unchecked")
	public List<String> getStatusMessages() {
		return Collections.EMPTY_LIST;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getErrorMessages() {
		return Collections.EMPTY_LIST;
	}

	@SuppressWarnings("unchecked")
	public List<String> getFatalMessages() {
		return Collections.EMPTY_LIST;
	}

	@SuppressWarnings("unchecked")
	public List<String> getWarningMessages() {
		return Collections.EMPTY_LIST;
	}

}

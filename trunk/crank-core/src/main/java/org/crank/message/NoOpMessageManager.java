package org.crank.message;

import java.util.Collections;
import java.util.List;

public class NoOpMessageManager implements MessageManager {

	public void addStatusMessage(String message, Object... args) {
	}
	
	public void addErrorMessage(String message, Object... args) {
	}

	public void addFatalMessage(String message, Object... args) {
	}

	public void addWarningMessage(String message, Object... args) {
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

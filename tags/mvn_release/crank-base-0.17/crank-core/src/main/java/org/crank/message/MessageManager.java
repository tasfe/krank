package org.crank.message;

import java.util.List;

public interface MessageManager {
	
	public void addStatusMessage(String message, Object... args);
	public void addErrorMessage(String message, Object... args);
	public void addFatalMessage(String message, Object... args);
	public void addWarningMessage(String message, Object... args);
	
	public List<String> getStatusMessages();
	public List<String> getErrorMessages();
	public List<String> getFatalMessages();
	public List<String> getWarningMessages();
}

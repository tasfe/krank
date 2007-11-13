package org.crank.message;

import java.util.List;

public interface MessageManager {
	
	public void addStatusMessage(String message);
	public void addErrorMessage(String message);
	public void addFatalMessage(String message);
	public void addWarningMessage(String message);
	
	public List<String> getStatusMessages();
	public List<String> getErrorMessages();
	public List<String> getFatalMessages();
	public List<String> getWarningMessages();
}

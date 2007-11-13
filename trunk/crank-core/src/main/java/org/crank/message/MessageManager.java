package org.crank.message;

import java.util.List;

public interface MessageManager {
	
	public void addStatusMessage(String message);

	public List<String> getMessages();
}

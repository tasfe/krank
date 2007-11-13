package org.crank.message;

import java.util.ArrayList;
import java.util.List;

public class SimpleMessageManager implements MessageManager {

	private List<String> messages = new ArrayList<String>();
	
	public void addStatusMessage(String message) {
		messages.add(message);
	}

	public List<String> getMessages() {
		return messages;
	}

}

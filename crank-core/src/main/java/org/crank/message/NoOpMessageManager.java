package org.crank.message;

import java.util.Collections;
import java.util.List;

public class NoOpMessageManager implements MessageManager {

	public void addStatusMessage(String message) {
	}

	@SuppressWarnings("unchecked")
	public List<String> getMessages() {
		return Collections.EMPTY_LIST;
	}
	

}

package org.crank.message;

import org.crank.annotations.design.ExtentionPoint;
import org.crank.metadata.ErrorHandlerData;

@ExtentionPoint
public interface MessageProcessor {
	
	public String handleException(Exception exception, ErrorHandlerData handler);

}

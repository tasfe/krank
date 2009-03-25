package org.crank.metadata;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**Holds the ErrorHandler information for a method.
 * Note we cache the information collected from the annotations for speed and ease
 * of processing.
 *
 */
@SuppressWarnings("unchecked")
public class ErrorHanlderMethodInfo implements Serializable{
	
	
	public ErrorHanlderMethodInfo () {
		exceptions = new HashSet<Class>(10);
		errorHandlerMap = new HashMap<Class, ErrorHandlerData>(10);
	}

	public static ErrorHanlderMethodInfo getInstance(List<ErrorHandlerData> errorHandlerList) {
		ErrorHanlderMethodInfo errorHanlderMethodInfo = new ErrorHanlderMethodInfo();
		
		/* Populate the Set and map. And find the default handler if any. */
		for (ErrorHandlerData errorHandlerData : errorHandlerList){
				if (errorHandlerData.isDefaultHandler()) {
					errorHanlderMethodInfo.setDefaultErrorHandler(errorHandlerData);
					errorHanlderMethodInfo.setDefaultHandlerPresent(true);				
				} else {
					errorHanlderMethodInfo.getExceptions().add(errorHandlerData.getExceptionClass());
					errorHanlderMethodInfo.getErrorHandlerMap().put(errorHandlerData.getExceptionClass(), errorHandlerData);
				}
		}
		return errorHanlderMethodInfo;

	}
	
	/** All of the exceptions that are handled. */
	private Set<Class> exceptions; 
	/** The method associated with this ErrorHanlderMethodInfo. */
	private Method method; 
	/**	Map of Exeption Classes to ErrorHandlers. */
	private Map <Class, ErrorHandlerData> errorHandlerMap;
	/**	Do we have a default handler? */
	private boolean defaultHandlerPresent;
	
	/** The default ErrorHandler if we have one. */
	private ErrorHandlerData defaultErrorHandler;
	
	public ErrorHandlerData getDefaultErrorHandler() {
		return defaultErrorHandler;
	}
	public void setDefaultErrorHandler(ErrorHandlerData defaultErrorHandler) {
		this.defaultErrorHandler = defaultErrorHandler;
	}
	public boolean isDefaultHandlerPresent() {
		return defaultHandlerPresent;
	}
	public void setDefaultHandlerPresent(boolean defaultHandlerPresent) {
		this.defaultHandlerPresent = defaultHandlerPresent;
	}
	public Map<Class, ErrorHandlerData> getErrorHandlerMap() {
		return errorHandlerMap;
	}
	public void setErrorHandlerMap(Map<Class, ErrorHandlerData> errorHandlerMap) {
		this.errorHandlerMap = errorHandlerMap;
	}
	public Set<Class> getExceptions() {
		return exceptions;
	}
	public void setExceptions(Set<Class> exceptions) {
		this.exceptions = exceptions;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	

}

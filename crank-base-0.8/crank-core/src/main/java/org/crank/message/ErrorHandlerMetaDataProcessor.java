package org.crank.message;

import java.lang.reflect.Method;

import org.crank.annotations.design.ExtentionPoint;
import org.crank.metadata.ErrorHanlderMethodInfo;

/** Looks up and processes ErrorHandlerData which can be in 
 * Spring configuration files, annotations or other places we have
 * not even imagined.
 */
@ExtentionPoint
public interface ErrorHandlerMetaDataProcessor {

	/**
	 * Check to see if the ErrorHandlerMethodInfo is in the errorHandlerInfoMap.
	 * If not, build it, put it in the map and then return it.
	 * 
	 * @param targetMethod The method we are building ErrorInfo for.
	 * @return ErrorHanlderMethodInfo All of the stuff we need to handle the exception effectively.
	 */
	public abstract ErrorHanlderMethodInfo findOrBuildErrorHandlerMethodInfo(
			Method targetMethod);

}
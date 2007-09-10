package org.crank.message;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.crank.message.support.spring.ErrorHandlerUtils;
import org.crank.metadata.ErrorHandlerData;
import org.crank.metadata.ErrorHanlderMethodInfo;

/**
 * Processes the annotations and turns them into error messages.
 * 
 * @author Rick Hightower
 *
 */
public class ErrorHandlerAnnotationProcessor implements ErrorHandlerMetaDataProcessor {

	/** Holds a map of ErrorHandlerInfo objects; using the Method as the key. 
	 * Note we cache the information collected from the annotations for speed and ease
	 * of processing.
	 * */
	protected static Map<Method, ErrorHanlderMethodInfo> errorHandlerInfoMap = new HashMap<Method, ErrorHanlderMethodInfo>();
	
	


	/* (non-Javadoc)
	 * @see org.crank.message.ErrorHandlerMetaDataProcessor#findOrBuildErrorHandlerMethodInfo(java.lang.reflect.Method)
	 */
	public ErrorHanlderMethodInfo findOrBuildErrorHandlerMethodInfo(Method targetMethod) {
		/* Get the ErrorHanlderMethodInfo from the map. */
		ErrorHanlderMethodInfo errorHanlderMethodInfo = errorHandlerInfoMap.get(targetMethod);

		/* If we did not find it in the map, then we have to build it from the annotations. */
		if (errorHanlderMethodInfo == null) {
			List<ErrorHandlerData> errorHandlerList = buildErrorHandlerDataListFromAnnotations(targetMethod);
			errorHanlderMethodInfo = buildErrorHanlderMethodInfoFromList(errorHandlerList);
			errorHanlderMethodInfo.setMethod(targetMethod);
			errorHandlerInfoMap.put(targetMethod, errorHanlderMethodInfo);
		}
		return errorHanlderMethodInfo;
	}

	/**
	 * Build ErrorHandler list from Method annotations. I used AnnotationUtils, which I learned
	 * about by attending Juergen Hoeller Spring Experience talk in Florida December 2006.
	 * @param targetMethod The method of interest. The one that has the annotations.
	 * @return list of ErrorHanlders.
	 */
	protected List<ErrorHandlerData> buildErrorHandlerDataListFromAnnotations(Method targetMethod) {
		return ErrorHandlerUtils.buildErrorHandlerDataListFromAnnotations(targetMethod);
	}

	/**
	 * Build ErrorHanlderMethodInfo from list of ErrorHanlders.
	 * @param errorHandlerList list of ErrorHanlders
	 * @throws AssertionError If miscongured, let developer know.
	 * @return Error Hanlder Method Info
	 */
	protected ErrorHanlderMethodInfo buildErrorHanlderMethodInfoFromList(List<ErrorHandlerData> errorHandlerList) {
		return ErrorHanlderMethodInfo.getInstance(errorHandlerList);
	}


}

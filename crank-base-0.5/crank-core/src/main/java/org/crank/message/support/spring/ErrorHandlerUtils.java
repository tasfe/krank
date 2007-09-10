package org.crank.message.support.spring;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.crank.annotations.ErrorHandler;
import org.crank.annotations.ErrorHandlers;
import org.crank.annotations.design.DependsOnSpring;
import org.crank.metadata.ErrorHandlerData;
import org.springframework.core.annotation.AnnotationUtils;

@DependsOnSpring
public class ErrorHandlerUtils {
	
	/**
	 * Build ErrorHandler list from Method annotations. I used AnnotationUtils, which I learned
	 * about by attending Juergen Hoeller Spring Experience talk in Florida December 2006.
	 * @param targetMethod The method of interest. The one that has the annotations.
	 * @return list of ErrorHanlders.
	 */
	@SuppressWarnings("unchecked")
    public static List<ErrorHandlerData> buildErrorHandlerDataListFromAnnotations(Method targetMethod) {

		/* Holds a list of errorHandlers */
		List<ErrorHandlerData> errorHandlerList = new ArrayList<ErrorHandlerData>();

		/* Find the class level annotations. */
		Class targetClass = targetMethod.getDeclaringClass();
		ErrorHandlers errorHandlers = (ErrorHandlers) targetClass.getAnnotation(ErrorHandlers.class);
		addToList(errorHandlers, errorHandlerList);

		/* See if there is a single error handler present at the class level if so add it to the list. */
		ErrorHandler errorHandler = (ErrorHandler) targetClass.getAnnotation(ErrorHandler.class);
		addToList(errorHandlerList, errorHandler);
		
		/* Find the annotation. */
		errorHandlers = AnnotationUtils.findAnnotation(targetMethod, ErrorHandlers.class);
		addToList(errorHandlers, errorHandlerList);
		
		/* See if there is a single error handler present if so add it to the list. */
		errorHandler = AnnotationUtils.findAnnotation(targetMethod, ErrorHandler.class);
		addToList(errorHandlerList, errorHandler);
		
		return errorHandlerList;
	}

	private static void addToList(List<ErrorHandlerData> errorHandlerList, ErrorHandler errorHandler) {
		if (errorHandler!=null) {
			errorHandlerList.add(ErrorHandlerUtils.convertErrorHandlerAnnotationToData(errorHandler));
		}
	}

	private static void addToList(ErrorHandlers errorHandlers, List<ErrorHandlerData> errorHandlerList) {
		/* If the errorHandlers is not null, then put them in the list. */
		if (errorHandlers!=null) {
			for (ErrorHandler errorHandler : errorHandlers.value()) {
				errorHandlerList.add(ErrorHandlerUtils.convertErrorHandlerAnnotationToData(errorHandler));
			}
			
		}
	}
	
	/**
	 * Converts an annotation into its correspondign meta-data laden POJO.
	 * @param annotation
	 * @return
	 */
	private static ErrorHandlerData convertErrorHandlerAnnotationToData(ErrorHandler annotation) {
		ErrorHandlerData data = new ErrorHandlerData();
		data.setDefaultHandler(annotation.defaultHandler());
		data.setExceptionClass(annotation.exceptionClass());
		data.setId(annotation.id());
		data.setMessageDetail(annotation.messageDetail());
		data.setMessageDetailArgKeys(annotation.messageDetailArgKeys());
		data.setMessageDetailArgs(annotation.messageDetailArgs());
		data.setMessageDetailKey(annotation.messageDetailKey());
		data.setMessageSummary(annotation.messageSummary());
		data.setMessageSummaryKey(annotation.messageSummaryKey());
		data.setMessageSummaryArgKeys(annotation.messageSummaryArgKeys());
		data.setMessageSummaryArgs(annotation.messageSummaryArgs());
		data.setOutcome(annotation.outcome());
		data.setSeverity(annotation.severity());
		data.setType(annotation.type());
		data.setUseExceptionForDetail(annotation.useExceptionForDetail());
		data.setUseMessageBundleForArgs(annotation.useMessageBundleForArgs());
		data.setUseMessageBundleForMessage(annotation.useMessageBundleForMessage());
		return data;
		
	}

}

package org.crank.message.support.aopalliance;



import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.crank.annotations.design.ExpectsInjection;
import org.crank.message.ErrorHandlerAnnotationProcessor;
import org.crank.message.ErrorHandlerMetaDataProcessor;
import org.crank.message.MessageProcessor;
import org.crank.metadata.ErrorHanlderMethodInfo;

/**
 * Processes the ErrorHandler annotations and turns them into error messages.
 * 
 * @author Rick Hightower
 *
 */
public class ErrorHandlerMetaDataProcessorMethodInterceptor implements
		MethodInterceptor {
	
	private ErrorHandlerMetaDataProcessor errorHandlerMetaDataProcessor = new ErrorHandlerAnnotationProcessor();
	private MessageProcessor messageProcessor;

	
	


	/**
	 * Intercept method calls and decorate them with JSF error message handling.
	 */
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		
		/* Get the ErrorHandleMethodInfo for this method. */
		ErrorHanlderMethodInfo errorHanlderInfo = errorHandlerMetaDataProcessor
		                    .findOrBuildErrorHandlerMethodInfo(methodInvocation.getMethod());
		
		Object returnValue = null;

		try {
			/* Try to excecute the method in question. */
			returnValue = methodInvocation.proceed();
			
		} catch (Exception exception) {
			
			/* See if this is an exception that we handle. */
			if (errorHanlderInfo.getExceptions().contains(exception.getClass())) {
				return messageProcessor.handleException(exception, errorHanlderInfo.getErrorHandlerMap().get(exception.getClass()));
			/* If it was not a method that we handle, is there a default handler for this method. */
			} else if (errorHanlderInfo.isDefaultHandlerPresent()) {
				return messageProcessor.handleException(exception, errorHanlderInfo.getDefaultErrorHandler());
			} //else process as usual... and let the default exception hanlding take place.
			
		}
		return returnValue;
	}


	@ExpectsInjection
	public void setErrorHandlerMetaDataProcessor(
			ErrorHandlerMetaDataProcessor errorHandlerMetaDataProcessor) {
		this.errorHandlerMetaDataProcessor = errorHandlerMetaDataProcessor;
	}

	@ExpectsInjection
	public void setMessageProcessor(MessageProcessor messageProcessor) {
		this.messageProcessor = messageProcessor;
	}
	
}

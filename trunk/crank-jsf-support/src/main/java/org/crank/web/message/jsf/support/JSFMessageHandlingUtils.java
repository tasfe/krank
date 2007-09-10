package org.crank.web.message.jsf.support;

import org.crank.annotations.design.DependsOnJSF;
import org.crank.message.ErrorHandlerAnnotationProcessor;
import org.crank.message.support.aopalliance.ErrorHandlerMetaDataProcessorMethodInterceptor;

@DependsOnJSF
public class JSFMessageHandlingUtils {
	
	/** Create the JSF MessageSpecification interceptor so we can reuse it without
	 * knowing all of the details of the internals. */
	public static ErrorHandlerMetaDataProcessorMethodInterceptor getMessageInterceptor() {
		ErrorHandlerMetaDataProcessorMethodInterceptor methodInterceptor =
			new ErrorHandlerMetaDataProcessorMethodInterceptor();
		
		methodInterceptor.setErrorHandlerMetaDataProcessor(new ErrorHandlerAnnotationProcessor());
		methodInterceptor.setMessageProcessor(new JSFMessageProcessor());
		return methodInterceptor;
	}

}

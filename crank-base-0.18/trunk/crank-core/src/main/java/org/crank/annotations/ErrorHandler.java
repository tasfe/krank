package org.crank.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.crank.metadata.ErrorHandlerType;
import org.crank.metadata.Severity;

import java.lang.annotation.RetentionPolicy;


/** Annotation that holds information on 
 *  how we would like to handle an error. 
 *  @author Rick Hightower
 *  */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
public @interface ErrorHandler {
	Class<? extends Exception> exceptionClass() default java.lang.Exception.class; //The exception that we are hanlding
	String messageDetail() default "Problem"; //The message we are going to send the end user
	String messageSummary() default "Problem"; //The message we are going to send the end user
	String id() default "";
	boolean defaultHandler() default false; //Handle all unhandled exceptions
	boolean useMessageBundleForMessage() default false; //
	boolean useMessageBundleForArgs() default false; //
	boolean useExceptionForDetail() default true; //
	String messageDetailKey() default ""; 
	String[] messageDetailArgs() default "";
	String[] messageDetailArgKeys() default "";
	String messageSummaryKey() default "";
	String[] messageSummaryArgs() default "";
	String[] messageSummaryArgKeys() default "";
	ErrorHandlerType type() default ErrorHandlerType.DISPLAY_MESSAGE; //How should we handle this error message
	Severity severity() default Severity.ERROR;
	String outcome() default "success";
}

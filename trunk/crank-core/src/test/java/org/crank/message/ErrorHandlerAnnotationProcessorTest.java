package org.crank.message;


import java.lang.reflect.Method;

import org.crank.annotations.ErrorHandler;
import org.crank.annotations.ErrorHandlers;
import org.crank.metadata.ErrorHanlderMethodInfo;
import org.crank.metadata.Severity;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

public class ErrorHandlerAnnotationProcessorTest {
	private ErrorHandlerAnnotationProcessor errorHandlerAnnotationProcessor;
	private Method method;
	private Method method2;
	private Method method3;

	
	@BeforeTest
	@SuppressWarnings("all")
	protected void setUp() throws Exception {
		errorHandlerAnnotationProcessor = new ErrorHandlerAnnotationProcessor();
		method = TestClass.class.getMethod("testMethod", null);
		method2 = TestClass.class.getMethod("testMethod2", null);
		method3 = TestClass.class.getMethod("testMethod3", null);
	}


	@Test
	public void testFindOrBuildErrorHandlerMethodInfoForSingle() {
		ErrorHanlderMethodInfo methodInfo = 
			errorHandlerAnnotationProcessor.findOrBuildErrorHandlerMethodInfo(method);
		methodInfo.getExceptions().contains(TestException.class);
		assertEquals(true, methodInfo.isDefaultHandlerPresent());
		
		ErrorHanlderMethodInfo methodInfo2 = 
			errorHandlerAnnotationProcessor.findOrBuildErrorHandlerMethodInfo(method);
		assertEquals("DEFAULT HANDLER AT CLASS LEVEL", methodInfo.getDefaultErrorHandler().getMessageSummary());		
		assertSame(methodInfo, methodInfo2);

	}

	@Test
	public void testFindOrBuildErrorHandlerMethodInfoForMultiples() {
		ErrorHanlderMethodInfo methodInfo = 
			errorHandlerAnnotationProcessor.findOrBuildErrorHandlerMethodInfo(method2);
		
		methodInfo.getExceptions().contains(ArithmeticException.class);
		assertEquals(true, methodInfo.isDefaultHandlerPresent());
		assertEquals("Some sort of fatal issue see details", methodInfo.getDefaultErrorHandler().getMessageSummary());
		
	}

	@Test
	public void testFindOrBuildErrorHandlerMethodInfoForClassLevelAnnotation() {
		ErrorHanlderMethodInfo methodInfo = 
			errorHandlerAnnotationProcessor.findOrBuildErrorHandlerMethodInfo(method3);
		
		methodInfo.getExceptions().contains(ArithmeticException.class);
		assertEquals(true, methodInfo.isDefaultHandlerPresent());
		assertEquals("DEFAULT HANDLER AT METHOD LEVEL", methodInfo.getDefaultErrorHandler().getMessageSummary());

	}

}

class TestException extends Exception {
	private static final long serialVersionUID = 1L;
	
}

@ErrorHandler(defaultHandler=true, 
	     messageSummary="DEFAULT HANDLER AT CLASS LEVEL",
	     severity=Severity.FATAL, outcome="fatal")
class TestClass {
	
	@ErrorHandler(exceptionClass=TestException.class)
	public String testMethod() {
		return null;
	}
	
    @ErrorHandlers (value={
    		@ErrorHandler(defaultHandler=true, 
    		     messageSummary="Some sort of fatal issue see details",
    		     severity=Severity.FATAL, outcome="fatal"),
    		@ErrorHandler(id="secondNumber",
    				messageSummary="Problem dividing first number by second number",
    				exceptionClass=ArithmeticException.class)
    	    }
    )
	public String testMethod2() {
		return null;
	}

	@ErrorHandler(defaultHandler=true, 
		     messageSummary="DEFAULT HANDLER AT METHOD LEVEL",
		     severity=Severity.FATAL, outcome="fatal")
	public String testMethod3() {
		return null;
	}
    

}


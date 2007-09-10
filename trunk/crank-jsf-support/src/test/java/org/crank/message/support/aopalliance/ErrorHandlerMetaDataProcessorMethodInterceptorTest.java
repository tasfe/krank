package org.crank.message.support.aopalliance;

import java.lang.reflect.Method;

import javax.faces.application.FacesMessage;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.shale.testng.base.AbstractJsfTestCase;
import org.crank.annotations.ErrorHandler;
import org.crank.annotations.ErrorHandlers;
import org.crank.message.support.aopalliance.ErrorHandlerMetaDataProcessorMethodInterceptor;
import org.crank.metadata.Severity;
import org.crank.web.message.jsf.support.JSFMessageHandlingUtils;
import org.easymock.EasyMock;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

public class ErrorHandlerMetaDataProcessorMethodInterceptorTest extends AbstractJsfTestCase{
	ErrorHandlerMetaDataProcessorMethodInterceptor errorHandlerMetaDataProcessorMethodInterceptor;
	private Method method;
	private Method method2;
	private MethodInvocation methodInvocation;

	
	@BeforeMethod
	@SuppressWarnings("all")
	protected void setUpLocal() throws Exception {
		errorHandlerMetaDataProcessorMethodInterceptor = 
			JSFMessageHandlingUtils.getMessageInterceptor();
		method = TestClass.class.getMethod("testMethod", null);
		method2 = TestClass.class.getMethod("testMethod2", null);
		

	}
	
	@AfterMethod
	protected void tearDownLocal() throws Exception {
		
	}


	@Test
	public void testInvokeGenerateException() throws Throwable {
		methodInvocation = EasyMock.createMock(MethodInvocation.class);
		
		EasyMock.expect(methodInvocation.getMethod()).andReturn(method);
		EasyMock.expect(methodInvocation.proceed()).andThrow(new TestException("TROUBLE"));
		EasyMock.replay(methodInvocation);
		
		String outcome = (String) errorHandlerMetaDataProcessorMethodInterceptor
		                              .invoke(methodInvocation);
		
		FacesMessage facesMessage = (FacesMessage) facesContext.getMessages("foo").next();
		assertEquals("TROUBLE", facesMessage.getDetail());
		assertEquals("fatal", outcome);
		EasyMock.verify(methodInvocation);
	}

	@Test
	public void testInvokeNoException() throws Throwable {
		methodInvocation = EasyMock.createMock(MethodInvocation.class);
		
		EasyMock.expect(methodInvocation.getMethod()).andReturn(method2);
		EasyMock.expect(methodInvocation.proceed()).andReturn("success");
		EasyMock.replay(methodInvocation);
		
		String outcome = (String) errorHandlerMetaDataProcessorMethodInterceptor
		                              .invoke(methodInvocation);
		
		assertEquals("success", outcome);
		EasyMock.verify(methodInvocation);
	}
	
	@Test
	public void testInvokeDefaultHandler() throws Throwable {
		methodInvocation = EasyMock.createMock(MethodInvocation.class);
		
		EasyMock.expect(methodInvocation.getMethod()).andReturn(method2);
		EasyMock.expect(methodInvocation.proceed()).andThrow(new RuntimeException("TROUBLE"));
		EasyMock.replay(methodInvocation);
		
		String outcome = (String) errorHandlerMetaDataProcessorMethodInterceptor
		                              .invoke(methodInvocation);
		
		FacesMessage facesMessage = (FacesMessage) facesContext.getMessages().next();
		assertEquals("TROUBLE", facesMessage.getDetail());
		assertEquals("METHOD LEVEL DEFAULT HANDLER", facesMessage.getSummary());		
		assertEquals("fatal", outcome);
		EasyMock.verify(methodInvocation);
	}

	@Test
	public void testInvokeUseClassLevelDefaultHandler() throws Throwable {
		methodInvocation = EasyMock.createMock(MethodInvocation.class);
		
		EasyMock.expect(methodInvocation.getMethod()).andReturn(method);
		EasyMock.expect(methodInvocation.proceed()).andThrow(new RuntimeException("TROUBLE 2"));
		EasyMock.replay(methodInvocation);
		
		String outcome = (String) errorHandlerMetaDataProcessorMethodInterceptor
		                              .invoke(methodInvocation);
		
		FacesMessage facesMessage = (FacesMessage) facesContext.getMessages().next();
		assertEquals("TROUBLE 2", facesMessage.getDetail());
		assertEquals("CLASS LEVEL DEFAULT HANDLER", facesMessage.getSummary());
		assertEquals("fatal", outcome);
		EasyMock.verify(methodInvocation);
	}

}

class TestException extends Exception {
	TestException (String message) {
		super(message);
	}
	
}

@ErrorHandler(defaultHandler=true, 
	     messageSummary="CLASS LEVEL DEFAULT HANDLER",
	     severity=Severity.FATAL, outcome="fatal")
class TestClass {
	
	@ErrorHandler(id="foo",
			 exceptionClass=TestException.class,
		     messageSummary="Some sort of fatal issue see details",
		     severity=Severity.FATAL, outcome="fatal")
	public String testMethod() {
		return null;
	}
	
    @ErrorHandlers (value={
    		@ErrorHandler(defaultHandler=true, 
    		     messageSummary="METHOD LEVEL DEFAULT HANDLER",
    		     severity=Severity.FATAL, outcome="fatal"),
    		@ErrorHandler(id="secondNumber",
    				messageSummary="Problem dividing first number by second number",
    				exceptionClass=ArithmeticException.class)
    	    }
    )
	public String testMethod2() {
		return null;
	}
}
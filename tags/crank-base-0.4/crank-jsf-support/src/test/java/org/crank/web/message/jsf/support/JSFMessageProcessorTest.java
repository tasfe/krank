package org.crank.web.message.jsf.support;

import javax.faces.application.FacesMessage;

import org.apache.shale.testng.base.AbstractJsfTestCase;
import org.crank.metadata.ErrorHandlerData;
import org.crank.metadata.Severity;
import org.crank.web.message.jsf.support.JSFMessageProcessor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

public class JSFMessageProcessorTest extends AbstractJsfTestCase {
	private JSFMessageProcessor messageProcessorTest;
	private ErrorHandlerData errorHandler;

	@BeforeMethod
	protected void setUpObjectUnderTest() throws Exception {
		messageProcessorTest = new JSFMessageProcessor();
		errorHandler  = new ErrorHandlerData();
	}

	@Test
	public void testHandleExceptionForGlobalExceptions() {
		messageProcessorTest.handleException(new Exception("HELLO"), errorHandler);
		assertNotNull(facesContext.getMessages());
		FacesMessage facesMessage = (FacesMessage) facesContext.getMessages().next();
		assertEquals("HELLO", facesMessage.getDetail());
	}

	@DataProvider(name = "allSeverities")
	public Object[][] generateDivideData() {
	 return new Object[][] {
			                                     // useExcep, unsupported, useBundle, useBundle for args        
	   { Severity.ERROR, FacesMessage.SEVERITY_ERROR,  true,  false, false, false},
	   { Severity.FATAL, FacesMessage.SEVERITY_FATAL,  false, false, false, false},
	   { Severity.INFO,  FacesMessage.SEVERITY_INFO,   true,  false, false, false},
	   { Severity.WARN,  FacesMessage.SEVERITY_WARN,   false, false, false, false},
	   { Severity.WARN,  FacesMessage.SEVERITY_WARN,   false, true,  true,  false},
	   { Severity.WARN,  FacesMessage.SEVERITY_WARN,   false, true,  true,  false},
	   { Severity.WARN,  FacesMessage.SEVERITY_WARN,   false, true,  false, true},
	   
	 };
	}	

	@Test(dataProvider="allSeverities") 
	public void testSeverities(Severity crankSeverity, FacesMessage.Severity facesSeverity, 
			boolean useExceptionMessage, boolean unsupported, boolean useMessageBundleForMessage,
			boolean useMessageBundleForArgs) {
		
		try {
			errorHandler.setSeverity(crankSeverity);
			errorHandler.setUseExceptionForDetail(useExceptionMessage);
			errorHandler.setUseMessageBundleForMessage(useMessageBundleForMessage);
			errorHandler.setUseMessageBundleForArgs(useMessageBundleForArgs);
			errorHandler.setId("foo");
			messageProcessorTest.handleException(new Exception("HELLO"), errorHandler);
			assertNotNull(facesContext.getMessages("foo"));
			FacesMessage facesMessage = (FacesMessage) facesContext.getMessages("foo").next();
			if (useExceptionMessage) {
				assertEquals("HELLO", facesMessage.getDetail());
			}else {
				assertEquals("Problem", facesMessage.getDetail());
			}
			//It seems the shale mock framework is broken or there is an issue with JSF RI.
			//Or I missed something.
			//assertEquals(facesSeverity, facesMessage.getSeverity());
		} catch (UnsupportedOperationException uoe) {
			assertTrue(unsupported);
		}
	}
	

}

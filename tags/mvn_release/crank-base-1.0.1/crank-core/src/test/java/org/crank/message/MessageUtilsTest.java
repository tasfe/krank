package org.crank.message;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.ResourceBundle;

import org.testng.annotations.Test;
public class MessageUtilsTest{

	@Test
	public void test () throws Exception {
    	String label= MessageUtils.getLabel( "allowUSA", ResourceBundle.getBundle( "org/crank/message/resources" ));
        assertNotNull(label);
        assertEquals( "Allow USA", label );

    	label = MessageUtils.getLabel( "foo", ResourceBundle.getBundle( "org/crank/message/resources" ));
        assertNotNull(label);
        assertEquals( "Foo", label );

        label = MessageUtils.getLabel( "Dad", ResourceBundle.getBundle( "org/crank/message/resources" ));
        assertNotNull(label);
        assertEquals( "Dad", label );
        
        label = MessageUtils.getLabel( "mom.lovesDad", ResourceBundle.getBundle( "org/crank/message/resources" ));
        assertNotNull(label);
        assertEquals( "Mom Loves Dad", label );

        label = MessageUtils.getLabel( "USA rocks", ResourceBundle.getBundle( "org/crank/message/resources" ));
        assertNotNull(label);
        assertEquals( "USA rocks", label );


        label = MessageUtils.getLabel( "address.line3", ResourceBundle.getBundle( "org/crank/message/resources" ));
        assertNotNull(label);
        assertEquals( "Address Line 3", label );

        label = MessageUtils.getLabel( "address.line_2", ResourceBundle.getBundle( "org/crank/message/resources" ));
        assertNotNull(label);
        assertEquals( "Address Line 2", label );
        
        
//        label = MessageUtils.getLabel( "USARocks", ResourceBundle.getBundle( "org/crank/message/resources" ));
//        assertNotNull(label);
//        assertEquals( "USA Rocks", label );
        
    }
	
	@Test
	public void testCreateLabelNoPlural() {
		String fooMessage = MessageUtils.createLabelNoPlural( "foos", ResourceBundle.getBundle( "org/crank/message/resources" ));
		String foosMessage = MessageUtils.createLabelNoPlural( "foos", ResourceBundle.getBundle( "org/crank/message/resources" ));
		assertEquals(fooMessage, foosMessage);
		String fooesMessage = MessageUtils.createLabelNoPlural( "fooes", ResourceBundle.getBundle( "org/crank/message/resources" ));
		assertEquals(fooMessage, fooesMessage);
	}
	
	@Test
	public void testCreateLabelWithNameSpace() {
		String fooMessage = MessageUtils.createLabelWithNameSpace("foo", "bar", ResourceBundle.getBundle( "org/crank/message/resources" ));
		assertEquals("Yo Mtv raps!", fooMessage);
	}
	
	
	@Test
	public void testMessageManagerUtils() {
		MessageManager defaultMessageManager = MessageManagerUtils.getCurrentInstance();
		assertNotNull(defaultMessageManager);
		
		MessageManager messageManager = new NoOpMessageManager();
		MessageManagerUtils.setCurrentInstance(messageManager);
		assertEquals(messageManager, MessageManagerUtils.getCurrentInstance());
	}
	
	@Test
	public void testSimpleMessageManager() {
		MessageManager messageManager = new SimpleMessageManager();

		String errorMessage1 = "this is the error message";
		messageManager.addErrorMessage(errorMessage1);
		messageManager.getErrorMessages().get(0).equals(errorMessage1);
		String errorMessage2 = "this is the error message 2";
		messageManager.addErrorMessage(errorMessage2);
		messageManager.getErrorMessages().get(1).equals(errorMessage2);

		String warningMessage1 = "this is the warning message";
		messageManager.addWarningMessage(warningMessage1);
		messageManager.getWarningMessages().get(0).equals(warningMessage1);
		String warningMessage2 = "this is the warning message 2";
		messageManager.addWarningMessage(warningMessage2);
		messageManager.getWarningMessages().get(1).equals(warningMessage2);

		String fatalMessage1 = "this is the fatal message";
		messageManager.addFatalMessage(fatalMessage1);
		messageManager.getFatalMessages().get(0).equals(fatalMessage1);
		String fatalMessage2 = "this is the fatal message 2";
		messageManager.addFatalMessage(fatalMessage2);
		messageManager.getFatalMessages().get(1).equals(fatalMessage2);

		String statusMessage1 = "this is the status message";
		messageManager.addStatusMessage(statusMessage1);
		messageManager.getStatusMessages().get(0).equals(statusMessage1);
		String statusMessage2 = "this is the status message 2";
		messageManager.addStatusMessage(statusMessage2);
		messageManager.getStatusMessages().get(1).equals(statusMessage2);

		
	}	

}


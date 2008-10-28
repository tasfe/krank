package org.crank.message;


import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MessageSpecificationTest {

    private MessageSpecification messageSpecification;
    
    @BeforeMethod
    protected void setUp() throws Exception {
        messageSpecification = new MessageSpecification();
        messageSpecification.setResourceBundleLocator( new ResourceBundleLocatorTest() );
        messageSpecification.setDetailMessage( "{foo.bar}" );
        messageSpecification.setCurrentSubject(null);
    }


    @Test
    public void testCreateDetailMessage() {
        //args
        String string = messageSpecification.createDetailMessage(  );
        assertNotNull(string);
        assertEquals( "Yo Mtv raps!", string);
    }

    @Test
    public void testCRANK108() {
        //args
        messageSpecification.setDetailMessage( "I love Canada." );
        String string = messageSpecification.createDetailMessage();
        assertEquals( "I love Canada.", string);
    }   
    
    @Test
    public void testCreateDetailMessageWithArgs() {
        //args
        messageSpecification.setDetailMessage( "{foo.bar.withargs}" );
        messageSpecification.setCurrentSubject( "Dad" );
        String string = messageSpecification.createDetailMessage( "Mom" );
        assertNotNull(string);
        assertEquals( "Hi Mom and Dad!", string);
    }

    @Test
    public void testCreateDetailMessageUseSubjectAsKey() {
        messageSpecification.setDetailMessage( "{foo.bar}" );
        messageSpecification.setCurrentSubject( "subjectKey" );
        String string = messageSpecification.createDetailMessage(  );
        assertNotNull(string);
        assertEquals( "VH1", string);
    }
    
    @Test 
    public void testMessageSpecificationInit() {
        messageSpecification.setName(null);
        messageSpecification.setParent(null);
        messageSpecification.init();
    	assertEquals("{" + MessageSpecification.class.getName() + ".detail}", messageSpecification.getDetailMessage());
    	assertEquals("{" + MessageSpecification.class.getName() + ".summary}", messageSpecification.getSummaryMessage());
    	
        messageSpecification.setName("test");
        messageSpecification.setParent(null);
        messageSpecification.init();
    	assertEquals("{message.test.detail}", messageSpecification.getDetailMessage());
    	assertEquals("{message.test.summary}", messageSpecification.getSummaryMessage());

        messageSpecification.setName(null);
        messageSpecification.setParent("parent");
        messageSpecification.init();
    	assertEquals("{message.parent.detail}", messageSpecification.getDetailMessage());
    	assertEquals("{message.parent.summary}", messageSpecification.getSummaryMessage());
    }
    
    @Test
    public void testExpressionLanguage() {
        try {
			messageSpecification.setDetailMessage( "#{foo.bar}" );
			messageSpecification.createDetailMessage(  );
			assertTrue(false);
		} catch (UnsupportedOperationException e) {
			// expected
		}
    }
    
    @Test
    public void testDottedNoBraces() {
		messageSpecification.setDetailMessage( "foo.bar" );
		String string = messageSpecification.createDetailMessage(  );
		assertNotNull(string);
		assertEquals( "Yo Mtv raps!", string);
		
		messageSpecification.setDetailMessage( "not.really.a.key" );
		string = messageSpecification.createDetailMessage(  );
		assertNotNull(string);
		assertEquals( "not.really.a.key", string);
		
		messageSpecification.setDetailMessage( "also_not_really_a_key" );
		string = messageSpecification.createDetailMessage(  );
		assertNotNull(string);
		assertEquals( "also_not_really_a_key", string);
    }
    
}


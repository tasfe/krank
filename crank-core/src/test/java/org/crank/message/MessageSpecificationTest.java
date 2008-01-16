package org.crank.message;


import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

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
    
}


package org.crank.message;

import java.util.ResourceBundle;

import org.crank.core.ResourceBundleLocator;

import junit.framework.TestCase;

public class MessageSpecificationTest extends TestCase {

    private MessageSpecification messageSpecification;
    
    protected void setUp() throws Exception {
        messageSpecification = new MessageSpecification();
        messageSpecification.setResourceBundleLocator( new ResourceBundleLocatorTest() );
        messageSpecification.setDetailMessage( "{foo.bar}" );
        messageSpecification.setCurrentSubject(null);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCreateDetailMessage() {
        //args
        String string = messageSpecification.createDetailMessage(  );
        assertNotNull(string);
        assertEquals( "Yo Mtv raps!", string);
    }

    public void testCreateDetailMessageWithArgs() {
        //args
        messageSpecification.setDetailMessage( "{foo.bar.withargs}" );
        messageSpecification.setCurrentSubject( "Dad" );
        String string = messageSpecification.createDetailMessage( "Mom" );
        assertNotNull(string);
        assertEquals( "Hi Mom and Dad!", string);
    }

    public void testCreateDetailMessageUseSubjectAsKey() {
        messageSpecification.setDetailMessage( "{foo.bar}" );
        messageSpecification.setCurrentSubject( "subjectKey" );
        String string = messageSpecification.createDetailMessage(  );
        assertNotNull(string);
        assertEquals( "VH1", string);
    }
    
}


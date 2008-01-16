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

}


package org.crank.core;

import junit.framework.TestCase;

public class TypeUtilsTest extends TestCase {
    
    class Test {
        private boolean bool;
        private String text;
        public boolean isBool() {
            return bool;
        }
        public void setBool( boolean bool ) {
            this.bool = bool;
        }
        public String getText() {
            return text;
        }
        public void setText( String text ) {
            this.text = text;
        }
    }

    protected void setUp() throws Exception {
    }


    public void testIsText() {
        assertFalse(TypeUtils.isText( Test.class, "bool" ));
        assertTrue(TypeUtils.isText( Test.class, "text" ));
    }

    public void testIsBoolean() {
        assertTrue(TypeUtils.isBoolean( Test.class, "bool" ));
        assertFalse(TypeUtils.isBoolean( Test.class, "text" ));
    }

}

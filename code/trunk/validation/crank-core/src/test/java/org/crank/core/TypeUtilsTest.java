package org.crank.core;

import java.util.Date;

import junit.framework.TestCase;

public class TypeUtilsTest extends TestCase {
    
    class Test {
        private boolean bool;
        private String text;
        private Date date;
        private java.sql.Date sqlDate;

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
        public Date getDate() {
            return date;
        }
        public void setDate( Date date ) {
            this.date = date;
        }
        public java.sql.Date getSqlDate() {
            return sqlDate;
        }
        public void setSqlDate( java.sql.Date sqlDate ) {
            this.sqlDate = sqlDate;
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

    public void testIsDate() {
        assertFalse(TypeUtils.isDate( Test.class, "bool" ));
        assertFalse(TypeUtils.isDate( Test.class, "text" ));
        assertTrue(TypeUtils.isDate( Test.class, "date" ));
        assertTrue(TypeUtils.isDate( Test.class, "sqlDate" ));
        
    }
    
}

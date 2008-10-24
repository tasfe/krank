package org.crank.core;


import java.util.Date;

import junit.framework.TestCase;

public class TypeUtilsTest extends TestCase{

    public static class SuperFoo {
        @SuppressWarnings("unused")
        private String bar;
    }

    public static class Foo extends SuperFoo {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Test {
        private boolean bool;
        private String text;
        private Date date;
        private java.sql.Date sqlDate;
        private Foo theFoo;

        public Foo getTheFoo() {
            return theFoo;
        }

        public void setTheFoo(Foo theFoo) {
            this.theFoo = theFoo;
        }

        public boolean isBool() {
            return bool;
        }

        public void setBool(boolean bool) {
            this.bool = bool;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public java.sql.Date getSqlDate() {
            return sqlDate;
        }

        public void setSqlDate(java.sql.Date sqlDate) {
            this.sqlDate = sqlDate;
        }
    }

    @org.testng.annotations.Test
    public void testGetField() {
        assertNotNull(TypeUtils.getField(Test.class, "theFoo.name"));
        assertNotNull(TypeUtils.getField(Test.class, "theFoo.bar"));
    }

    @org.testng.annotations.Test
    public void testGetProperyDescriptor() {
        assertNotNull(TypeUtils
                .getPropertyDescriptor(Test.class, "theFoo.name"));
    }

    @org.testng.annotations.Test
    public void testIsText() {

        assertFalse(TypeUtils.isText(Test.class, "bool"));
        assertTrue(TypeUtils.isText(Test.class, "text"));
    	try {
    		TypeUtils.isText(null, "test");
    		assertFalse(true);
    	}
    	catch(CrankException ce) {
    		// expected
    	}
       	try {
    		TypeUtils.isText(Test.class, null);
    		assertFalse(true);
    	}
    	catch(CrankException ce) {
    		// expected
    	}
    	
    	try {
    		TypeUtils.isText(Test.class, "notAProperty");
    		//assertFalse(true);
    	}
    	catch(CrankException ce) {
    		// expected
    	}

    }

    @org.testng.annotations.Test
    public void testIsTextCompoundProperty() {
        assertTrue(TypeUtils.isText(Employee.class, "address.line1"));
    }

    @org.testng.annotations.Test
    public void testIsDate() {
        assertFalse(TypeUtils.isDate(Test.class, "bool"));
        assertFalse(TypeUtils.isDate(Test.class, "text"));
        assertTrue(TypeUtils.isDate(Test.class, "date"));
        assertTrue(TypeUtils.isDate(Test.class, "sqlDate"));
    	try {
    		TypeUtils.isDate(null, "test");
    		assertFalse(true);
    	}
    	catch(CrankException ce) {
    		// expected
    	}
       	try {
    		TypeUtils.isDate(Test.class, null);
    		assertFalse(true);
    	}
    	catch(CrankException ce) {
    		// expected
    	}
    	
    	try {
    		TypeUtils.isDate(Test.class, "notAProperty");
    		//assertFalse(true);
    	}
    	catch(CrankException ce) {
    		// expected
    	}
        
    }
    
    @org.testng.annotations.Test
    public void testIsUrl() {
    	
    	assertFalse(TypeUtils.isUrl("nourlddotcom"));
    	assertTrue(TypeUtils.isUrl("nourlddot.com"));
    }
    
    @org.testng.annotations.Test
    public void testIsInCollection() {    	
    	assertFalse(TypeUtils.isInCollection("one", "two,three,four,five"));
    	assertTrue(TypeUtils.isInCollection("two", "two,three,four,five"));
    	assertTrue(TypeUtils.isInCollection("five", "two,three,four,five"));
    	assertTrue(TypeUtils.isInCollection("three", "two,three,four,five"));
    	assertFalse(TypeUtils.isInCollection("three", "twothreefourfive"));
    	assertFalse(TypeUtils.isInCollection("three", "two,threefourfive"));
    	assertFalse(TypeUtils.isInCollection("three", "twothree,fourfive"));
    }  
    
    @org.testng.annotations.Test
    public void testIsBoolean() {    
    	assertTrue(TypeUtils.isBoolean(BooleanTestSupport.class, "primitiveBoolean"));
    	assertTrue(TypeUtils.isBoolean(BooleanTestSupport.class, "wrapperBoolean"));
    	assertFalse(TypeUtils.isBoolean(BooleanTestSupport.class, "notABoolean"));
    	assertTrue(TypeUtils.isBoolean(BooleanTestSupport.class, "testSupport.testSupport.primitiveBoolean"));
    	
    	try {
    		TypeUtils.isBoolean(null, "test");
    		//assertFalse(true);
    	}
    	catch(CrankException ce) {
    		// expected
    	}
       	try {
    		TypeUtils.isBoolean(BooleanTestSupport.class, null);
    		assertFalse(true);
    	}
    	catch(CrankException ce) {
    		// expected
    	}
    	
    	try {
    		TypeUtils.isBoolean(BooleanTestSupport.class, "notAProperty");
    		//assertFalse(true);
    	}
    	catch(CrankException ce) {
    		// expected
    	}
    	
    }
    
    @org.testng.annotations.Test
    public void testIsEnum() {    
    	assertTrue(TypeUtils.isEnum(EnumTestSupport.class, "testEnum"));
    	assertFalse(TypeUtils.isEnum(EnumTestSupport.class, "notAnEnum"));
    	assertTrue(TypeUtils.isEnum(EnumTestSupport.class, "testSupport.testSupport.testEnum"));
    	
    	try {
    		TypeUtils.isEnum(null, "test");
    		//assertFalse(true);
    	}
    	catch(CrankException ce) {
    		// expected
    	}
       	try {
    		TypeUtils.isEnum(EnumTestSupport.class, null);
    		assertFalse(true);
    	}
    	catch(CrankException ce) {
    		// expected
    	}
    	
    	try {
    		TypeUtils.isEnum(BooleanTestSupport.class, "notAProperty");
    		//assertFalse(true);
    	}
    	catch(CrankException ce) {
    		// expected
    	}
    	
    }    
    
   	static class EnumTestSupport {
		enum TestEnum { one, two, three};
		private TestEnum testEnum = TestEnum.three;
		private Object notAnEnum;
		private EnumTestSupport testSupport;
		public TestEnum getTestEnum() {
			return testEnum;
		}
		public void setTestEnum(TestEnum testEnum) {
			this.testEnum = testEnum;
		}
		public Object getNotAnEnum() {
			return notAnEnum;
		}
		public void setNotAnEnum(Object notAnEnum) {
			this.notAnEnum = notAnEnum;
		}
		public EnumTestSupport getTestSupport() {
			return testSupport;
		}
		public void setTestSupport(EnumTestSupport testSupport) {
			this.testSupport = testSupport;
		}
   	}
    
   	class BooleanTestSupport {
		private boolean primitiveBoolean;
		private Boolean wrapperBoolean;
		private Object notABoolean;
		private BooleanTestSupport testSupport;
		public boolean isPrimitiveBoolean() {
			return primitiveBoolean;
		}
		public void setPrimitiveBoolean(boolean primitiveBoolean) {
			this.primitiveBoolean = primitiveBoolean;
		}
		public Boolean getWrapperBoolean() {
			return wrapperBoolean;
		}
		public void setWrapperBoolean(Boolean wrapperBoolean) {
			this.wrapperBoolean = wrapperBoolean;
		}
		public Object getNotABoolean() {
			return notABoolean;
		}
		public void setNotABoolean(Object notABoolean) {
			this.notABoolean = notABoolean;
		}
		public BooleanTestSupport getTestSupport() {
			return testSupport;
		}
		public void setTestSupport(BooleanTestSupport testSupport) {
			this.testSupport = testSupport;
		}
	};
    
}

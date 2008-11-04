package org.crank.crud.controller;

import junit.framework.TestCase;

import java.io.Serializable;

@SuppressWarnings("unchecked")
public class SelectOneControllerTest extends TestCase {

    private TestClass testObject;
    private TestParent parent;
    private SelectOneController controller;
    
    class TestParent implements Serializable {
        private TestClass obj;

        TestParent() {}
        
        TestParent(TestClass obj) {
            this.obj = obj;
        }

        public TestClass getObj() {
            return obj;
        }

        public void setObj(TestClass obj) {
            this.obj = obj;
        }
    }
    class TestClass implements Serializable {
        private String name;

        TestClass(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    
	protected void setUp() throws Exception {
        controller = new SelectOneController(TestClass.class, null) {
            public Row getSelectedRow() {
                return new Row(testObject);
            }

            public void prepareUI() {
            }
        };

		testObject = new TestClass("oops");
        parent = new TestParent();

    }

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSelectNoParent() {

        controller = new SelectOneController(TestClass.class, null) {
            public Row getSelectedRow() {
                return new Row(testObject);
            }

            public void prepareUI() {
            }
        };


        controller.addSelectListener(new SelectListener(){
            public void select(SelectEvent event) {
                TestClass tc = (TestClass)event.getValue();
                assertEquals("oops", tc.getName());
                tc.setName("scoop");
            }

            public void unselect(SelectEvent event) {

            }
        });

        controller.select();
        assertEquals("scoop", testObject.getName());

    }

    public void testSelectWithParent() {

        controller = new SelectOneController(TestClass.class, this.parent, "obj", null) {
            public Row getSelectedRow() {
                return new Row(testObject);
            }

            public void prepareUI() {
            }
        };


        controller.addSelectListener(new SelectListener(){
            public void select(SelectEvent event) {
            		event.getValue();
            }

            public void unselect(SelectEvent event) {

            }
        });

        controller.select();
        assertEquals("oops", this.parent.getObj().getName());

    }


}

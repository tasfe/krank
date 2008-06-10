package org.crank.crud.controller;


import junit.framework.TestCase;

import org.crank.crud.test.model.Employee;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CrudUtilsTest extends TestCase{

    @BeforeMethod
    protected void setUp() throws Exception {
    }

    @Test()
    public void isEntity() {
        assertTrue(CrudUtils.isEntity( Employee.class));
        assertFalse(CrudUtils.isEntity( Object.class));
    }


    @Test()
    public void testTextSize() {
        assertEquals(0, CrudUtils.textSize( Employee.class, "firstName"));
        assertEquals(81, CrudUtils.textSize( Employee.class, "description"));
        assertEquals(255, CrudUtils.textSize( Employee.class, "ssn"));

    }

    @Test()
    public void testIsLargeText() {
        assertFalse(CrudUtils.isLargeText( Employee.class, "firstName"));
        assertFalse(CrudUtils.isLargeText( Employee.class, "numberOfPromotions"));        
        assertTrue(CrudUtils.isLargeText( Employee.class, "description"));
    	assertTrue(CrudUtils.isLargeText( Employee.class, "address.line1"));

    }

    @Test()
    public void testIsManyToOne() {
        assertFalse(CrudUtils.isManyToOne( Employee.class, "age"));
        assertTrue(CrudUtils.isManyToOneOptional(Employee.class, "clientDepartment"));
        assertTrue(CrudUtils.isManyToOne( Employee.class, "department"));

    }
    
    @Test()
    public void testPropertyEntityname() {
        assertEquals( "Department", CrudUtils.getPropertyEntityName( Employee.class, "department" ));

    }

    @Test
    public void testIsRequired() {
//        assertTrue(CrudUtils.isRequired( Employee.class, "numberOfPromotions"));
//        assertTrue(CrudUtils.isRequired( Employee.class, "age"));
        assertTrue(CrudUtils.isRequired( Employee.class, "department"));
        
    }
    
}

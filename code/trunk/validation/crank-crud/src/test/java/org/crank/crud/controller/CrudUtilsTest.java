package org.crank.crud.controller;

import org.crank.crud.test.model.Employee;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

public class CrudUtilsTest {

    @BeforeMethod
    protected void setUp() throws Exception {
    }


    @Test()
    public void testIsManyToOne() {
        assertFalse(CrudUtils.isManyToOne( Employee.class, "age"));
        assertTrue(CrudUtils.isManyToOne( Employee.class, "department"));

    }
    
    @Test()
    public void testPropertyEntityname() {
        assertEquals( "Department", CrudUtils.getPropertyEntityName( Employee.class, "department" ));

    }

    @Test
    public void testIsRequired() {
        assertTrue(CrudUtils.isRequired( Employee.class, "numberOfPromotions"));
        assertTrue(CrudUtils.isRequired( Employee.class, "age"));
    }
    
}

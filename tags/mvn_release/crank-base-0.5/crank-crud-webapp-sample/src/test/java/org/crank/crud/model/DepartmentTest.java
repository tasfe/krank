package org.crank.crud.model;

import static org.testng.AssertJUnit.*;
import org.crank.crud.model.Department;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class DepartmentTest {

    Department a;
    Department a1;
    Department b;
    
    
    @BeforeTest
    protected void setUp() throws Exception {
        a = new Department();
        a.setName( "a" );
        a.setId( 1L );
        a1 = new Department();
        a1.setName( "a" );
        a1.setId( 1L );
        b = new Department();
        b.setName( "b");
        b.setId( 2L );
    }


    @Test
    public void testEquals() {
        assertFalse(a.equals( b ));
        assertTrue(a.equals( a ));
        assertTrue( a.equals(a1));
    }

}

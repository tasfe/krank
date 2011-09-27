package org.crank.crud.controller;


import java.util.Date;

import org.crank.crud.criteria.Between;
import org.crank.crud.criteria.Operator;
import org.testng.annotations.Test;

import junit.framework.TestCase;

public class FilterablePropertyTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }


    @Test
    public void testFilterableProperty() {
        FilterableProperty fp = new FilterableProperty("name", String.class, FilterablePropertyTest.class);
        assertEquals( fp.getComparison().getOperator(), Operator.LIKE_START );
        
        fp = new FilterableProperty("check", Boolean.class, FilterablePropertyTest.class);
        assertEquals( fp.getComparison().getOperator(), Operator.EQ );

        fp = new FilterableProperty("foo", Date.class, FilterablePropertyTest.class);
        assertTrue( fp.getComparison() instanceof Between);
        
        fp = new FilterableProperty("foo", Long.class, FilterablePropertyTest.class);
        assertTrue( fp.isLong());

        fp = new FilterableProperty("foo", Integer.class, FilterablePropertyTest.class);
        assertTrue( fp.isInteger());
        
        fp = new FilterableProperty("foo", String.class, FilterablePropertyTest.class);
        assertFalse( fp.isInteger());
        assertTrue( fp.isString());

        
    }
    
    

}

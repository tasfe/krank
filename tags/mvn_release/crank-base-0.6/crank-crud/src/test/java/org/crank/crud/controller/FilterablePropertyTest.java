package org.crank.crud.controller;


import java.util.Date;

import org.crank.crud.criteria.Between;
import org.crank.crud.criteria.Operator;

import junit.framework.TestCase;

public class FilterablePropertyTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }


    public void testFilterableProperty() {
        FilterableProperty fp = new FilterableProperty("name", String.class);
        assertEquals( fp.getComparison().getOperator(), Operator.LIKE_START );
        
        fp = new FilterableProperty("check", Boolean.class);
        assertEquals( fp.getComparison().getOperator(), Operator.EQ );

        fp = new FilterableProperty("foo", Date.class);
        assertTrue( fp.getComparison() instanceof Between);
        
    }

}

package org.crank.controller;

import org.crank.crud.test.DbUnitTestBase;
import org.crank.crud.test.model.Employee;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;


public class JPAPaginatableDataSourceTest extends DbUnitTestBase {
    
    private JPAPaginatableDataSource paginatableDataSource;
    
    public String getDataSetXml() {
        return "data/Employee.xml";
    }

    @Test
    public void test() {
        int count = paginatableDataSource.getCount();
        AssertJUnit.assertEquals( 9, count );
        AssertJUnit.assertEquals( 9, paginatableDataSource.list( 0, 10 ).size());
        Employee employee = (Employee) paginatableDataSource.list( 0, 3 ).get( 0 );
        AssertJUnit.assertEquals("Rick", employee.getFirstName());
        employee = (Employee) paginatableDataSource.list( 3, 3 ).get( 0 );
        AssertJUnit.assertEquals("Bob", employee.getFirstName());
        employee = (Employee) paginatableDataSource.list( 6, 3 ).get( 0 );
        AssertJUnit.assertEquals("Juan", employee.getFirstName());
    }


    public void setPaginatableDataSource( JPAPaginatableDataSource paginatableDataSource ) {
        this.paginatableDataSource = paginatableDataSource;
    }

}

package org.crank.crud.controller.datasource;

import org.crank.crud.controller.datasource.DaoPagingDataSource;
import org.crank.crud.test.DbUnitTestBase;
import org.crank.crud.test.model.Employee;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;


public class DaoPaginatableDataSourceTest extends DbUnitTestBase {
    
    private DaoPagingDataSource paginatableDataSource;
    
    public String getDataSetXml() {
        return "data/Employee.xml";
    }

    @Test
    public void test() {
//        int count = paginatableDataSource.getCount();
//        AssertJUnit.assertEquals( 14, count );
//        AssertJUnit.assertEquals( 10, paginatableDataSource.list( 0, 10 ).size());
//        Employee employee = (Employee) paginatableDataSource.list( 0, 3 ).get( 0 );
//        AssertJUnit.assertEquals("Rick", employee.getFirstName());
//        employee = (Employee) paginatableDataSource.list( 3, 3 ).get( 0 );
//        AssertJUnit.assertEquals("Bob", employee.getFirstName());
//        employee = (Employee) paginatableDataSource.list( 6, 3 ).get( 0 );
//        AssertJUnit.assertEquals("Juan", employee.getFirstName());
    }


    public void setPaginatableDataSource( DaoPagingDataSource paginatableDataSource ) {
        this.paginatableDataSource = paginatableDataSource;
    }

}

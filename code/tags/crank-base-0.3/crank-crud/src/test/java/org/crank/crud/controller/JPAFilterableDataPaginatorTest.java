package org.crank.crud.controller;

import org.crank.crud.GenericDao;
import org.crank.crud.controller.datasource.JPAFilterablePaginatableDataSource;
import org.crank.crud.test.DbUnitTestBase;
import org.crank.crud.test.model.Employee;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;


public class JPAFilterableDataPaginatorTest extends DbUnitTestBase {
    
    private JPAFilterablePaginatableDataSource paginatableDataSource;
    private GenericDao<Employee, Long> employeeDao;
    
    public String getDataSetXml() {
        return "data/Employee.xml";
    }


    @Test
    public void test2() {
        FilterableDataPaginatorImpl paginator = new FilterableDataPaginatorImpl(paginatableDataSource, Employee.class );
        
        for (int index = 0; index < 100; index++) {
            Employee employee = new Employee();
            employee.setFirstName( "FOO" + index);
            employeeDao.create(employee);
        }
        
        paginator.getFilterableProperties().get( "firstName" ).getComparison().setValue("FOO");
        paginator.getFilterableProperties().get( "firstName" ).getComparison().setEnabled( true );
        paginator.filter();
        Employee employee = (Employee)paginator.getPage().get( 0 );
        AssertJUnit.assertEquals("FOO0", employee.getFirstName());
        AssertJUnit.assertEquals(10, paginator.getPageCount());

        paginator.getFilterableProperties().get( "firstName" ).getComparison().setValue("R");
        paginator.filter();
        employee = (Employee)paginator.getPage().get( 0 );
        AssertJUnit.assertEquals("Rick", employee.getFirstName());
        AssertJUnit.assertEquals(1, paginator.getPageCount());
    
    }

    public void setPaginatableFilterableDataSource( JPAFilterablePaginatableDataSource paginatableDataSource ) {
        this.paginatableDataSource = paginatableDataSource;
    }

    public void setEmployeeDao( GenericDao<Employee, Long> employeeDao ) {
        this.employeeDao = employeeDao;
    }

}

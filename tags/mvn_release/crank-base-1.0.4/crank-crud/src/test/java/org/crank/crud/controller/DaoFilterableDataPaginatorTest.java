package org.crank.crud.controller;

import org.crank.crud.GenericDao;
import org.crank.crud.controller.datasource.DaoFilteringPagingDataSource;
import org.crank.crud.test.DbUnitTestBase;
import org.crank.crud.test.model.Employee;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;

@SuppressWarnings("unchecked")
public class DaoFilterableDataPaginatorTest extends DbUnitTestBase {
    
    
	private DaoFilteringPagingDataSource paginatableDataSource;
    private GenericDao<Employee, Long> employeeDao;
    
    public String getDataSetXml() {
        return "data/Employee.xml";
    }


    @Test(groups={"broken"})
    public void test() {
        FilteringPaginator paginator = new FilteringPaginator(paginatableDataSource, Employee.class );
        
        for (int index = 0; index < 100; index++) {
            Employee employee = new Employee();
            employee.setFirstName( "FOO" + index);
            employee.setNumberOfPromotions( 1 );
            employeeDao.persist(employee);
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
    
        paginator.getFilterableProperties().get( "firstName" ).getComparison().disable();
        paginator.filter();
        employee = (Employee)paginator.getPage().get( 0 );
        AssertJUnit.assertEquals("Rick", employee.getFirstName());
        AssertJUnit.assertEquals(11, paginator.getPageCount());

        paginator.getFilterableProperties().get( "firstName" ).getComparison().enable();
        paginator.getFilterableProperties().get( "firstName" ).getComparison().setValue("FOO");
        paginator.filter();
        employee = (Employee)paginator.getPage().get( 0 );
        AssertJUnit.assertEquals("FOO0", employee.getFirstName());
        AssertJUnit.assertEquals(10, paginator.getPageCount());

        
        paginator.getFilterableProperties().get( "firstName" ).getComparison().disable();
        paginator.getFilterableProperties().get( "firstName" ).getOrderBy().toggle();
        employee = (Employee)paginator.getPage().get( 0 );
        AssertJUnit.assertEquals("Scott", employee.getFirstName());
        AssertJUnit.assertEquals(11, paginator.getPageCount());
        
        paginator.getFilterableProperties().get( "firstName" ).getOrderBy().toggle();
        employee = (Employee)paginator.getPage().get( 0 );
        AssertJUnit.assertEquals("Bob", employee.getFirstName());
        AssertJUnit.assertEquals(11, paginator.getPageCount());

        paginator.getFilterableProperties().get( "department.name" ).getOrderBy().toggle();
        employee = (Employee)paginator.getPage().get( 0 );
        AssertJUnit.assertEquals("Bob", employee.getFirstName());
        AssertJUnit.assertEquals(11, paginator.getPageCount());
        
        paginator.getFilterableProperties().get( "address.zipCode" ).getOrderBy().toggle();
        employee = (Employee)paginator.getPage().get( 0 );
        
        
    }

    public void setPaginatableFilterableDataSource( DaoFilteringPagingDataSource paginatableDataSource ) {
        this.paginatableDataSource = paginatableDataSource;
    }

    public void setEmployeeDao( GenericDao<Employee, Long> employeeDao ) {
        this.employeeDao = employeeDao;
    }

}

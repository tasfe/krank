package org.crank.crud.controller.datasource;

import org.crank.crud.GenericDao;
import org.crank.crud.controller.datasource.JpaFilteringPagingDataSource;
import org.crank.crud.criteria.Comparison;
import org.crank.crud.criteria.Operator;
import org.crank.crud.test.DbUnitTestBase;
import org.crank.crud.test.model.Employee;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;


public class JPAFilterablePaginatableDataSourceTest extends DbUnitTestBase {
    
    private JpaFilteringPagingDataSource paginatableDataSource;
    private GenericDao<Employee, Long> employeeDao;
    
    public String getDataSetXml() {
        return "data/Employee.xml";
    }

    @Test
    public void test() {
        paginatableDataSource.group().add( new Comparison("firstName", Operator.LIKE, "J%") );
        int count = paginatableDataSource.getCount();
        AssertJUnit.assertEquals( 2, count );
        AssertJUnit.assertEquals( 2, paginatableDataSource.list( 0, 10 ).size());
        Employee employee = (Employee) paginatableDataSource.list( 0, 3 ).get( 0 );
        AssertJUnit.assertEquals("Joe", employee.getFirstName());
    }

    @Test
    public void test2() {
        for (int index = 0; index < 100; index++) {
            Employee employee = new Employee();
            employee.setFirstName( "FOO" + index);
            employee.setNumberOfPromotions( 1 );
            employeeDao.create(employee);
        }
        paginatableDataSource.group().add( new Comparison("firstName", Operator.LIKE, "FOO%") );
        int count = paginatableDataSource.getCount();
        AssertJUnit.assertEquals( 100, count );
//        AssertJUnit.assertEquals( 100, paginatableDataSource.list( 0, 200 ).size());
//        Employee employee = (Employee) paginatableDataSource.list( 0, 3 ).get( 0 );
//        AssertJUnit.assertEquals("FOO0", employee.getFirstName());
    }

    public void setPaginatableFilterableDataSource( JpaFilteringPagingDataSource paginatableDataSource ) {
        this.paginatableDataSource = paginatableDataSource;
    }

    public void setEmployeeDao( GenericDao<Employee, Long> employeeDao ) {
        this.employeeDao = employeeDao;
    }

}

package org.crank.crud.controller.datasource;

import java.util.List;

import org.crank.crud.EmployeeDataCreationUtility;
import org.crank.crud.GenericDao;
import org.crank.crud.controller.datasource.DaoPagingDataSource;
import org.crank.crud.test.DbUnitTestBase;
import org.crank.crud.test.model.Department;
import org.crank.crud.test.model.Employee;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class DaoPaginatableDataSourceTest extends DbUnitTestBase {
    

	//private DaoPagingDataSource paginatableDataSource;
    private EmployeeDataCreationUtility creationUtility = new EmployeeDataCreationUtility();
	private GenericDao<Employee, Long> employeeDao;
	private GenericDao<Department, Long> departmentDao;
	private List<Employee> testEmployees;
    
    
	public void setEmployeeDao(GenericDao<Employee, Long> employeeDao) {
		this.employeeDao = employeeDao;
	}

	public void setDepartmentDao(GenericDao<Department, Long> departmentDao) {
		this.departmentDao = departmentDao;
	}

	@BeforeClass (dependsOnGroups={"initPersist"})
	public void setupEmployeeData() {
		creationUtility.init(employeeDao, departmentDao);
		creationUtility.setupEmployeeData();
		testEmployees = creationUtility.getTestEmployees();
	}

	@AfterClass 
	public void deleteTestEmployeeData() {
		employeeDao.delete(testEmployees);
		employeeDao.delete(employeeDao.find());		
	}
    
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


    @SuppressWarnings("unchecked")
	public void setPaginatableDataSource( DaoPagingDataSource paginatableDataSource ) {
        //this.paginatableDataSource = paginatableDataSource;
    	System.out.println("p" + paginatableDataSource);
    }

	public EmployeeDataCreationUtility getCreationUtility() {
		return creationUtility;
	}

	public void setCreationUtility(EmployeeDataCreationUtility creationUtility) {
		this.creationUtility = creationUtility;
	}

}

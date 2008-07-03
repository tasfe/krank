package org.crank.crud;

import java.util.Arrays;
import java.util.List;

import org.crank.crud.test.model.Department;
import org.crank.crud.test.model.Employee;
import org.testng.annotations.AfterClass;


public class EmployeeDataCreationUtility {
	private GenericDao<Employee, Long> employeeDao;
	private GenericDao<Department, Long> departmentDao;
	private List<Employee> testEmployees;
	
	public void init(GenericDao<Employee, Long> employeeDao, 
			GenericDao<Department, Long> departmentDao){
		this.departmentDao = departmentDao;
		this.employeeDao = employeeDao;
	}

	public void setupEmployeeData() {
		Department engineeringDept = departmentDao.read(1L);
		Department itDept = departmentDao.read(2L);
		Department hrDept = departmentDao.read(3L);

		Employee[] employees = {
			new Employee("Rick", "Hightower", engineeringDept, true, 40, 1, "333333331"),
			new Employee("Chris", null, engineeringDept, true, 40, 1, "333333332"),
			new Employee("Scott", null, engineeringDept, true, 40, 1, "333333333"),
			new Employee("Bob", null, itDept, true, 40, 1, "333333334"),			
			new Employee("Joe", null, itDept, true, 40, 1, "333333335"),
			new Employee("Same", null, itDept, true, 40, 1, "333333336"),
			new Employee("Juan", "Highb", hrDept, true, 40, 1, "333333337"),
			new Employee("Carlos", null, hrDept, true, 40, 1, "333333338"),
			new Employee("Marcello", null, hrDept, true, 40, 1, "333333339"),
			new Employee("Richard", "Higha", hrDept, true, 40, 1, "333333310"),
			new Employee("Rickard", null, hrDept, true, 40, 1, "333333311"),
			new Employee("Rich", null, hrDept, true, 40, 1, "333333312"),
			new Employee("Ric", null, hrDept, true, 40, 1, "333333313"),
			new Employee("Vanilla", "GORILLA", hrDept, true, 40, 1, "333333314")
		};
		testEmployees = Arrays.asList(employees);
		
		employeeDao.persist(testEmployees);
	}
	
	@AfterClass 
	public void deleteTestEmployeeData() {
		employeeDao.delete(testEmployees);
	}

	public List<Employee> getTestEmployees() {
		return testEmployees;
	}

	
}

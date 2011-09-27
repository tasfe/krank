package org.crank.crud.test.dao;

import java.util.List;

import org.crank.crud.GenericDao;
import org.crank.crud.test.model.Employee;

public interface EmployeeDAO extends GenericDao<Employee, Long>{
	List<Employee> findEmployeesByDepartment(String deptName);
	@SuppressWarnings("unchecked")
	List<Employee> findInEmployeeIds (List ids);
}

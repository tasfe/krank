package org.crank.crud.dao;

import java.util.List;

import org.crank.crud.GenericDao;
import org.crank.crud.model.Employee;

public interface EmployeeDAO extends GenericDao<Employee, Long> {
	List<Employee> findEmployeesByDepartment(String deptName);
	List<Employee> findInEmployeeIds (List<Long> ids);
}


package org.crank.sample;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.crank.crud.GenericDao;
import org.crank.crud.model.Address;
import org.crank.crud.model.Department;
import org.crank.crud.model.Employee;

public class DataInitServlet implements Servlet {
	private List<Department> departments = new ArrayList<Department>(3);

	public void destroy() {
	}

	public ServletConfig getServletConfig() {
		return null;
	}

	public String getServletInfo() {
		return null;
	}

	public void init(ServletConfig config) throws ServletException {
//		ApplicationContext context = WebApplicationContextUtils
//				.getWebApplicationContext(config.getServletContext());
//		// repositoryies is built from the managedObjects in
//		// CrankCrudExampleApplicationContext.managedObjects()
//		Map<String, GenericDao> daos = (Map<String, GenericDao>) context
//				.getBean("repositories");
//		Department department = persistDepartments(daos.get("Department"));
//		GenericDao empDao = daos.get("Employee");
//		Employee employee = null;
//		for (int index = 0; index < 100; index++) {
//			employee = new Employee();
//			employee.setFirstName("FOO" + index);
//			employee.setLastName("BAR" + index);
//			employee.setActive(true);
//			employee.setAddress(getNewAddress());
//			employee.setAge(40);
//			employee.setDescription("Big Dood");
//			employee.setDob(new Date());
//			employee.setEmail("bob@bobby.com");
//			employee.setNumberOfPromotions(0);
//			employee.setPhone("333-000-9876");
//			employee.setRank(1);
//			employee.setDepartment(department);
//			empDao.persist(employee);
//			addEmployee(daos.get("Department"), departments.get(index % 3), employee);
//		}
	}

	public Address getNewAddress() {
		Address address = new Address();
		address.setLine_1("One Two St");
		address.setZipCode("90210");
		return address;
	}
	
	public Department persistDepartments(GenericDao<Department, Long> deptDao) {
		Department dep = new Department();
		dep.setName("Gizmot");
		deptDao.persist(dep);
		departments.add(dep);
		dep = new Department();
		dep.setName("Bistor");
		deptDao.persist(dep);
		departments.add(dep);
		dep = new Department();
		dep.setName("Zible");
		deptDao.persist(dep);
		departments.add(dep);
		return deptDao.read(dep.getId());
	}
	
	public void addEmployee(GenericDao<Department, Long> deptDao, Department dep, Employee emp) {
		dep.addEmployee(emp);
		deptDao.merge(dep);
	}

	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
	}

}

package org.crank.sample;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.crank.crud.GenericDao;
import org.crank.crud.model.*;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.context.ApplicationContext;

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
		ApplicationContext context = WebApplicationContextUtils
				.getWebApplicationContext(config.getServletContext());
//		// repositoryies is built from the managedObjects in
		// CrankCrudExampleApplicationContext.managedObjects()
		Map<String, GenericDao> daos = (Map<String, GenericDao>) context
				.getBean("repositories");

        //Create Spring User kregester/1234/ROLE_USER
        addUser(daos.get("Users"), daos.get("Authorities"), "kregester", "1234", "ROLE_USER");
        //Create Spring User rdigital/1234/ROLE_SUPERVISOR
        addUser(daos.get("Users"), daos.get("Authorities"), "rdigital", "1234", "ROLE_SUPERVISOR");        




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


    /**
     * Adds Spring Security User and Authority
     * @param usersDao
     * @param authoritiesDao
     * @param userName
     * @param authority
     */
    public void addUser(GenericDao<Users, Long> usersDao, GenericDao<Authorities, Long> authoritiesDao,
                        String userName, String password, String authority) {
        Users user = new Users(userName, password, authority);
        usersDao.persist(user);
        Authorities role = new Authorities(userName, authority);
        authoritiesDao.persist(role);
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

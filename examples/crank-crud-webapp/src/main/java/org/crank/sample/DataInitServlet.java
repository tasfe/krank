package org.crank.sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.crank.crud.GenericDao;
import org.crank.crud.model.Address;
import org.crank.crud.model.Department;
import org.crank.crud.model.Employee;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

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

	@SuppressWarnings("unchecked")
	public void init(ServletConfig config) throws ServletException {
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
		// repositoryies is built from the managedObjects in CrankCrudExampleApplicationContext.managedObjects()
		Map<String, GenericDao> daos = (Map<String,GenericDao>)context.getBean("repositories");
		GenericDao empDao = daos.get("Employee");
        Employee employee = null;
        for (int index = 0; index < 100; index ++) {
            employee = new Employee();
            employee.setFirstName( "FOO" + index );
            employee.setLastName( "BAR" + index );
            employee.setActive(true);
            employee.setAddress(getNewAddress(context));
            employee.setAge(40);
            employee.setDepartment(getDepartment(context, index % 3));
            employee.setDescription("Big Dood");
            employee.setDob(new Date());
            employee.setEmail("bob@bobby.com");
            employee.setNumberOfPromotions(0);
            employee.setPhone("333-000-9876");
            employee.setRank(1);
            empDao.create( employee );
        }
	}
	
	@SuppressWarnings("unchecked")
	public Address getNewAddress(ApplicationContext context) {
		Address address = new Address();
		address.setLine_1("One Two St");
		address.setZipCode("90210");
		return address;
	}
	
	@SuppressWarnings("unchecked")
	public Department getDepartment(ApplicationContext context, int index) {
		Department dep = null;
		if(departments.size() > 0) {
			dep = departments.get(index);
		} else {
			// repositoryies is built from the managedObjects in CrankCrudExampleApplicationContext.managedObjects()
			Map<String, GenericDao> daos = (Map<String,GenericDao>)context.getBean("repositories");
			GenericDao depDao = daos.get("Department");
			dep = new Department();
			dep.setName("Gizmot");
			depDao.create(dep);
			dep = new Department();
			dep.setName("Bistor");
			depDao.create(dep);
			dep = new Department();
			dep.setName("Zible");
			depDao.create(dep);
			departments.add((Department)depDao.find("name", "Gizmot").get(0));
			departments.add((Department)depDao.find("name", "Bistor").get(0));
			departments.add((Department)depDao.find("name", "Zible").get(0));
			dep = departments.get(index);
		}
		return dep;
	}

	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
	}

}

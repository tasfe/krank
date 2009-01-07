package org.crank.examples.employeetask;

import java.util.ArrayList;
import java.util.List;


import org.crank.config.spring.support.CrudJSFConfig;
import org.crank.crud.controller.CrudManagedObject;
import org.crank.crud.jsf.support.JsfCrudAdapter;
import org.crank.crud.jsf.support.JsfDetailController;
import org.crank.crud.jsf.support.JsfSelectManyController;
import org.springframework.config.java.annotation.Bean;
import org.springframework.config.java.annotation.Configuration;
import org.springframework.config.java.annotation.Lazy;
import org.springframework.config.java.util.DefaultScopes;

@Configuration(defaultLazy = Lazy.TRUE)
public abstract class EmployeeTaskApplicationContext extends CrudJSFConfig {

	private static List<CrudManagedObject> managedObjects;
	private static String DEPARTMENT = "department";
	private static String EMPLOYEE = "employee";
	private static String ROLE = "role";
	private static String EMPLOYEE_RELATIONSHIP = "employees";
	private static String ROLE_RELATIONSHIP = "roles";

	@Bean(scope = DefaultScopes.SINGLETON)
	public List<CrudManagedObject> managedObjects() {
		if (managedObjects == null) {
			managedObjects = new ArrayList<CrudManagedObject>();
			managedObjects.add(new CrudManagedObject(Department.class));
			managedObjects.add(new CrudManagedObject(Employee.class));
			managedObjects.add(new CrudManagedObject(Role.class));
		}
		return managedObjects;

	}

	
	@Bean(scope = DefaultScopes.SESSION)
	public JsfCrudAdapter<Department, Long> departmentCrud() throws Exception {
		JsfCrudAdapter<Department, Long> adapter = (JsfCrudAdapter<Department, Long>) cruds().get(DEPARTMENT);
		adapter.getController().addChild(EMPLOYEE_RELATIONSHIP, new JsfDetailController(Employee.class, true));
		return adapter;
	}

	@Bean(scope = DefaultScopes.SESSION)
	public JsfCrudAdapter<Employee, Long> employeeCrud() throws Exception {
		JsfCrudAdapter<Employee, Long> adapter = (JsfCrudAdapter<Employee, Long>) cruds().get(EMPLOYEE);
		return adapter;
	}

	@Bean(scope = DefaultScopes.SESSION)
	public JsfCrudAdapter<Role, Long> roleCrud() throws Exception {
		JsfCrudAdapter<Role, Long> adapter = (JsfCrudAdapter<Role, Long>) cruds().get(ROLE);
		return adapter;
	}

	@Bean(scope = DefaultScopes.SESSION)
	public JsfSelectManyController<Role, Long> employeeToRoleController()
			throws Exception {
		JsfSelectManyController<Role, Long> controller = new JsfSelectManyController<Role, Long>(
				Role.class, ROLE_RELATIONSHIP, paginators().get(ROLE), employeeCrud()
						.getController());
		return controller;
	}
	
	@Bean
	public String persistenceUnitName() {
		return "employee-task-project";
	}

}

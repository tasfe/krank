package com.somecompany.employeetask;

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
public abstract class CrudApplicationContext extends CrudJSFConfig {

	private static List<CrudManagedObject> managedObjects;
	
    /* Entity Constants. */
	private static String DEPARTMENT = "department";
	private static String EMPLOYEE = "employee";
	private static String ROLE = "role";
    /* End Entity Constants.  */
	
    /* Relationship Constants. */
	private static String DEPARTMENT_EMPLOYEES_RELATIONSHIP = "employees";
	private static String EMPLOYEE_ROLES_RELATIONSHIP = "roles";
	private static String ROLE_EMPLOYEES_RELATIONSHIP = "employees";
    /* End Relationship Constants.  */
	
    
	@Bean(scope = DefaultScopes.SINGLETON)
	public List<CrudManagedObject> managedObjects() {
		if (managedObjects == null) {
			managedObjects = new ArrayList<CrudManagedObject>();
            /* Managed objects. */
			managedObjects.add(new CrudManagedObject(Department.class));
			managedObjects.add(new CrudManagedObject(Employee.class));
			managedObjects.add(new CrudManagedObject(Role.class));
            /* End Managed objects.  */
		}
		return managedObjects;
	}
	
    /* Crud adapters. */  
	@Bean(scope = DefaultScopes.SESSION)
	public JsfCrudAdapter<Department, Long> departmentCrud() throws Exception {
		JsfCrudAdapter<Department, Long> adapter = (JsfCrudAdapter<Department, Long>) cruds().get(DEPARTMENT);
		
		adapter.getController().addChild(DEPARTMENT_EMPLOYEES_RELATIONSHIP, new JsfDetailController(Employee.class, true));
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

    /* End Crud adapters.  */
    
    /* ManyToMany controllers. */
	@Bean(scope = DefaultScopes.SESSION)
	public JsfSelectManyController<Role, Long> employeeToRolesController()
			throws Exception {
		JsfSelectManyController<Role, Long> controller = new JsfSelectManyController<Role, Long>(
				Role.class, EMPLOYEE_ROLES_RELATIONSHIP, paginators().get(ROLE), employeeCrud()
						.getController());
		return controller;
	}

	@Bean(scope = DefaultScopes.SESSION)
	public JsfSelectManyController<Employee, Long> roleToEmployeesController()
			throws Exception {
		JsfSelectManyController<Employee, Long> controller = new JsfSelectManyController<Employee, Long>(
				Employee.class, ROLE_EMPLOYEES_RELATIONSHIP, paginators().get(EMPLOYEE), roleCrud()
						.getController());
		return controller;
	}

    /* End ManyToMany controllers.  */
	
	@Bean
	public String persistenceUnitName() {
		return "archetype-example-app";
	}

}

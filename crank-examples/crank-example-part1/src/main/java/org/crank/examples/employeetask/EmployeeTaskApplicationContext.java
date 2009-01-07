package org.crank.examples.employeetask;

import java.util.ArrayList;
import java.util.List;

import org.crank.config.spring.support.CrudJSFConfig;
import org.crank.crud.controller.CrudManagedObject;
import org.crank.crud.controller.CrudOperations;
import org.crank.crud.jsf.support.JsfCrudAdapter;
import org.crank.crud.jsf.support.JsfDetailController;
import org.springframework.config.java.annotation.Bean;
import org.springframework.config.java.annotation.Configuration;
import org.springframework.config.java.annotation.Lazy;
import org.springframework.config.java.util.DefaultScopes;

@Configuration(defaultLazy = Lazy.TRUE)
public abstract class EmployeeTaskApplicationContext extends CrudJSFConfig {

	private static List<CrudManagedObject> managedObjects;
	private static String DEPARTMENT_CRUD = "department";
	private static String EMPLOYEE_RELATIONSHIP = "employees";

	@Bean(scope = DefaultScopes.SINGLETON)
	public List<CrudManagedObject> managedObjects() {
		if (managedObjects == null) {
			managedObjects = new ArrayList<CrudManagedObject>();
			managedObjects.add(new CrudManagedObject(Department.class));
		}
		return managedObjects;

	}

	@SuppressWarnings("unchecked")
	@Bean(scope = DefaultScopes.SESSION)
	public JsfCrudAdapter departmentCrud() throws Exception {
		JsfCrudAdapter adapter = cruds().get(DEPARTMENT_CRUD);
		adapter.getController().setDeleteStrategy(CrudOperations.DELETE_BY_ENTITY);
		adapter.getController().addChild(EMPLOYEE_RELATIONSHIP, new JsfDetailController(Employee.class, true));
		return adapter;
	}

	@Bean
	public String persistenceUnitName() {
		return "employee-task-project";
	}

}

package org.crank.security.app;

import java.util.ArrayList;
import java.util.List;

import org.crank.config.spring.support.CrudJSFConfig;

import org.crank.crud.controller.CrudManagedObject;
import org.crank.crud.jsf.support.JsfSelectManyController;
import org.crank.crud.jsf.support.JsfSelectOneListingController;

import org.crank.security.model.Group;
import org.crank.security.model.Role;
import org.crank.security.model.User;

import org.springframework.config.java.annotation.Bean;
import org.springframework.config.java.annotation.Configuration;
import org.springframework.config.java.annotation.Lazy;
import org.springframework.config.java.util.DefaultScopes;

@Configuration(defaultLazy = Lazy.TRUE)
public abstract class SecurityApplicationContext extends CrudJSFConfig {

	private static List<CrudManagedObject> managedObjects;

	@Bean(scope = DefaultScopes.SINGLETON)
	public List<CrudManagedObject> managedObjects() {
		if (managedObjects == null) {
			managedObjects = new ArrayList<CrudManagedObject>();
			managedObjects.add(new CrudManagedObject(User.class));
			managedObjects.add(new CrudManagedObject(Role.class));
			managedObjects.add(new CrudManagedObject(Group.class));
		}
		return managedObjects;

	}

	@Bean
	public String persistenceUnitName() {
		return "crank-security";
	}
	
	@Bean(scope = DefaultScopes.SESSION)        
	public JsfSelectManyController<Role, Long> userToRoleController() throws Exception {
		JsfSelectManyController<Role, Long> controller =
			new JsfSelectManyController<Role, Long>(Role.class, "roles",
					paginators().get("Role"),
					cruds().get("SecurityUser").getController());
		return controller;
	}

	@Bean(scope = DefaultScopes.SESSION)
	public JsfSelectOneListingController<Group, Long> userToGroupController()
			throws Exception {
		JsfSelectOneListingController<Group, Long> controller = new JsfSelectOneListingController<Group, Long>(
				Group.class, "parentGroup", paginators().get("SecurityGroup"),
				cruds().get("SecurityUser").getController());
		return controller;
	}
	
}
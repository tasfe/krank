package org.crank.sample;

import java.util.ArrayList;
import java.util.List;

import org.crank.config.spring.support.CrudJSFConfig;
import org.crank.crud.controller.CrudManagedObject;
import org.springframework.config.java.annotation.Bean;
import org.springframework.config.java.annotation.Configuration;
import org.springframework.config.java.annotation.Lazy;
import org.springframework.config.java.util.DefaultScopes;

@Configuration(defaultLazy = Lazy.TRUE)
public abstract class CrankCrudExampleApplicationContext extends CrudJSFConfig {

	private static List<CrudManagedObject> managedObjects;

	@Bean(scope = DefaultScopes.SINGLETON)
	public List<CrudManagedObject> managedObjects() {
		if (managedObjects == null) {
			managedObjects = new ArrayList<CrudManagedObject>();
		}
		return managedObjects;

	}

	@Bean
	public String persistenceUnitName() {
		return "blank-project";
	}

}

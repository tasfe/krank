package src.main.java.org.yomama;

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
	private static String FOO = "foo";
	private static String BAR = "bar";
	private static String BAZ = "baz";
    /* End Entity Constants.  */
	
    /* Relationship Constants. */
	private static String FOO_BARS_RELATIONSHIP = "bars";
	private static String FOO_BAZ_RELATIONSHIP = "baz";
    /* End Relationship Constants.  */
	
    
	@Bean(scope = DefaultScopes.SINGLETON)
	public List<CrudManagedObject> managedObjects() {
		if (managedObjects == null) {
			managedObjects = new ArrayList<CrudManagedObject>();
            /* Managed objects. */
			managedObjects.add(new CrudManagedObject(Foo.class));
			managedObjects.add(new CrudManagedObject(Bar.class));
			managedObjects.add(new CrudManagedObject(Baz.class));
            /* End Managed objects.  */
		}
	}
	
    /* Crud adapters. */  
	@Bean(scope = DefaultScopes.SESSION)
	public JsfCrudAdapter<Foo, Long> fooCrud() throws Exception {
		JsfCrudAdapter<Foo, Long> adapter = (JsfCrudAdapter<Foo, Long>) cruds().get(FOO);
		
		adapter.getController().addChild(FOO_BAZ_RELATIONSHIP, new JsfDetailController(Baz.class, true));
		return adapter;
	}
  
	@Bean(scope = DefaultScopes.SESSION)
	public JsfCrudAdapter<Bar, Long> barCrud() throws Exception {
		JsfCrudAdapter<Bar, Long> adapter = (JsfCrudAdapter<Bar, Long>) cruds().get(BAR);
		
		return adapter;
	}
  
	@Bean(scope = DefaultScopes.SESSION)
	public JsfCrudAdapter<Baz, Long> bazCrud() throws Exception {
		JsfCrudAdapter<Baz, Long> adapter = (JsfCrudAdapter<Baz, Long>) cruds().get(BAZ);
		
		return adapter;
	}

    /* End Crud adapters.  */
    
    /* ManyToMany controllers. */
	@Bean(scope = DefaultScopes.SESSION)
	public JsfSelectManyController<Bar, Long> fooTobarsController()
			throws Exception {
		JsfSelectManyController<Bar, Long> controller = new JsfSelectManyController<Bar, Long>(
				Bar.class, FOO_BARS_RELATIONSHIP, paginators().get(BAR), fooCrud()
						.getController());
		return controller;
	}

    /* End ManyToMany controllers.  */

}

package org.crank.sample;


import org.crank.config.spring.support.CrudJSFConfig;
import org.crank.controller.ExcelExportControllerBean;
import org.crank.controller.SelectEmployeeListingController;
import org.crank.controller.TreeControllerBean;
import org.crank.crud.controller.CrudManagedObject;
import org.crank.crud.controller.FilterablePageable;
import org.crank.crud.controller.datasource.DaoFilteringDataSource;
import org.crank.crud.controller.datasource.EnumDataSource;
import org.crank.crud.criteria.Comparison;
import org.crank.crud.criteria.OrderBy;
import org.crank.crud.dao.DepartmentDAO;
import org.crank.crud.dao.EmployeeDAO;
import org.crank.crud.dao.RoleDAO;
import org.crank.crud.dao.SpecialtyDAO;
import org.crank.crud.join.Fetch;
import org.crank.crud.jsf.support.*;
import org.crank.crud.model.*;
import org.crank.crud.relationships.RelationshipManager;
import org.crank.model.jsf.support.RichFacesTreeModelBuilder;
import org.springframework.config.java.annotation.Bean;
import org.springframework.config.java.annotation.Configuration;
import org.springframework.config.java.annotation.ExternalBean;
import org.springframework.config.java.annotation.Lazy;
import org.springframework.config.java.annotation.aop.ScopedProxy;
import org.springframework.config.java.util.DefaultScopes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Configuration (defaultLazy=Lazy.TRUE)
public abstract class CrankCrudExampleApplicationContext extends CrudJSFConfig {
    
    private static List<CrudManagedObject> managedObjects;
    
	@Bean (scope = DefaultScopes.SINGLETON)    
    public List<CrudManagedObject> managedObjects() {
    	if (managedObjects==null) {
	    	managedObjects = new ArrayList<CrudManagedObject>();
	        managedObjects.add( new CrudManagedObject(Employee.class, EmployeeDAO.class) );
	        managedObjects.add( new CrudManagedObject(Department.class, DepartmentDAO.class) );
	        managedObjects.add( new CrudManagedObject(Specialty.class, SpecialtyDAO.class) );
	        managedObjects.add( new CrudManagedObject(Role.class, RoleDAO.class) );
	        managedObjects.add( new CrudManagedObject(Skill.class, null) );
	        
    	}
    	return managedObjects;
		
	}

    @SuppressWarnings("unchecked")
    @Bean(scope = DefaultScopes.SESSION, aliases = "pagers")
    public Map<String, FilterablePageable> paginators() throws Exception {
        Map<String, FilterablePageable> paginators =  super.paginators();
        paginators.get("Employee").addOrderBy(OrderBy.asc("firstName"));
        return paginators;
    }

    @Bean (scope = DefaultScopes.SESSION)
    @ScopedProxy
    public DataTableScrollerBean dataTableScrollerBean() throws Exception {
        DataTableScrollerBean bean = new DataTableScrollerBean();
        bean.setEmployeeDataPaginator(paginators().get("Employee"));
        bean.setEmployeeDAO(repos().get("Employee"));
        return bean;
    }
    @SuppressWarnings("unchecked")
    @Bean (scope = DefaultScopes.REQUEST)
    public SelectEmployeeListingController selectEmployeeListingController() throws Exception {
           SelectEmployeeListingController controller = new SelectEmployeeListingController(paginators().get("Employee"));
           return controller;
    }


    @ExternalBean
    public abstract JsfCrudAdapter deptCrud();

    @SuppressWarnings("unchecked")
    @Bean (scope = DefaultScopes.SESSION, aliases = "deptCrud") 
    public JsfCrudAdapter deptCrudController() throws Exception {
        JsfCrudAdapter adapter = cruds().get( "Department");
        adapter.getController().addChild( "employees", new JsfDetailController(Employee.class))
        .addChild( "tasks", new JsfDetailController(Task.class) );
        return adapter;
    }
    
    @SuppressWarnings("unchecked")
    @Bean(scope = DefaultScopes.SESSION, aliases = "empCrud")
	public JsfCrudAdapter employeeCrud() throws Exception {
		JsfCrudAdapter adapter = cruds().get("Employee");

		/*
		 * Filter out employees who do not have a manager. This will create a
		 * "where employee.manager is null" to the query.
		 */
		adapter.getPaginator().addCriterion(Comparison.eq("manager", null));
		adapter.getPaginator().filter();

		/* Setup tasks and contacts DetailControllers. */
		adapter.getController().addChild("tasks",
				new JsfDetailController(Task.class));
		adapter.getController().addChild("contacts",
				new JsfDetailController(ContactInfo.class));

		/*
		 * Setup directReports detail controller. Make sure framework calls
		 * add/remove methods.
		 */
		JsfDetailController directReports = new JsfDetailController(
				Employee.class);
		RelationshipManager relationshipManager = directReports
				.getRelationshipManager();
		relationshipManager.setChildCollectionProperty("directReports");
		relationshipManager.setAddToParentMethodName("addDirectReport");
		relationshipManager.setRemoveFromParentMethodName("removeDirectReport");

		adapter.getController().addChild("directReports", directReports);
		
		
		return adapter;
	}

    @ExternalBean
    abstract JsfCrudAdapter<Employee, Long> empCrud();

    @Bean(scope = DefaultScopes.SESSION)
	public JsfSelectManyController<Role, Long> employeeToRoleController() throws Exception {
		JsfSelectManyController<Role, Long> controller = new JsfSelectManyController<Role, Long>(Role.class, "roles", paginators().get("Role"), empCrud().getController()); 
    	return controller;
    }
    

    @Bean(scope = DefaultScopes.SESSION)
	public JsfSelectOneListingController<Specialty, Long> employeeToSpecialtyController() throws Exception {
    	JsfSelectOneListingController<Specialty, Long> controller = new JsfSelectOneListingController<Specialty, Long>(Specialty.class, "specialty", paginators().get("Specialty"), empCrud().getController()); 
    	return controller;
    }

    @Bean(scope = DefaultScopes.SESSION)
	public JsfSelectOneListingController<Skill, Long> employeeToSkillController() throws Exception {
    	JsfSelectOneListingController<Skill, Long> controller = new JsfSelectOneListingController<Skill, Long>(Skill.class, "primarySkill", paginators().get("Skill"), empCrud().getController()); 
    	return controller;
    }

    @SuppressWarnings("unchecked")
    @Bean (scope = DefaultScopes.SINGLETON)
    public Map<String, SelectItemGenerator> selectItemGenerators() throws Exception {
        Map<String, SelectItemGenerator> selectItemGenerators = super.selectItemGenerators();

        SelectItemGenerator selectItemGenerator = new SelectItemGenerator();
        EnumDataSource<EmployeeStatus> dataSource = new EnumDataSource();
        dataSource.setType(EmployeeStatus.class);
        selectItemGenerator.setDataSource( dataSource );
        selectItemGenerators.put("EmployeeStatus", selectItemGenerator);
        
        return selectItemGenerators;
    }
    
    
    @Bean (scope = DefaultScopes.SESSION)
    @ScopedProxy
    public ExcelExportControllerBean controllerBean() throws Exception {
      ExcelExportControllerBean bean = new ExcelExportControllerBean();
        return bean;
    }
    
    @SuppressWarnings("unchecked")
	@Bean (scope = DefaultScopes.SESSION)
    @ScopedProxy
    public TreeControllerBean treeControllerBean() throws Exception {
    	/* Create TreeControllerBean. */
    	TreeControllerBean bean = new TreeControllerBean();
    	
    	/* Setup a datasource for the tree. */
    	DaoFilteringDataSource<Department, Long> dataSource = new DaoFilteringDataSource<Department, Long>();
    	dataSource.setDao(this.repos().get("Department"));
    	dataSource.setFetches(Fetch.leftJoinFetch("employees"));
    	
    	/* Create a tree builder for the tree model. */
    	RichFacesTreeModelBuilder treeBuilder = new RichFacesTreeModelBuilder();
    	/* Inject build instructions for tree nodes. */
    	treeBuilder.setTreeBuildDirections("Departments->this.name->employees.firstName,lastName");
    	treeBuilder.setNoRoot(false);
    	
    	/* Inject dependencies in tree controller. */
    	bean.setDataSource(dataSource);
    	bean.setTreeBuilder(treeBuilder);
    	bean.setEmployeeCrud(this.employeeCrud());
    	bean.setDeptCrud(this.deptCrud());
    	return bean;
    }

    @SuppressWarnings("unchecked")
    @Bean (scope = DefaultScopes.SESSION) 
    public Map<String, AutocompleteController> autocomplete () throws Exception {
        Map<String, AutocompleteController> autocomplete = new HashMap<String, AutocompleteController>();

        // Add the data source which contains the auto-complete data
        DaoFilteringDataSource dataSource = new DaoFilteringDataSource();
        dataSource.setDao( repos().get( "Specialty" ));
        
        // Create the auto-complete controller for the particular property on the data source
        // Arguments are:
        //     dataSource -   the data source for the lookup values
        //     propertyName - the property on the lookup results which will be used query against
        //     fieldName -    the field name of the associated entity which will be assigned the  
        //                    validated auto-complete entity upon successful lookup
        AutocompleteController autoController = new AutocompleteController(dataSource, "name", "specialty");
        
        // Wire in the event handler to the associated controller for dealing with 
        // conversion / validation from the Many to One association to the controller's entity
        cruds().get("Employee").getController().addCrudControllerListener(autoController);
        
        // Add to the auto-complete map...
        //     This will be accessed via the field.xhtml as the AutoComplete controller / validator
        //     for the associated property
        autocomplete.put("Specialty", autoController);

        return autocomplete;
    }
    
    @Bean
    public String persistenceUnitName() {
    	return "crank-crud-app"; 
    }

}

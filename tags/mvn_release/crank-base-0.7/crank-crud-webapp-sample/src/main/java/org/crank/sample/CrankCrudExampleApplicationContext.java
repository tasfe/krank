package org.crank.sample;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.config.java.annotation.Bean;
import org.springframework.config.java.annotation.Configuration;
import org.springframework.config.java.annotation.Lazy;
import org.springframework.config.java.annotation.aop.ScopedProxy;
import org.springframework.config.java.util.DefaultScopes;
import org.crank.config.spring.support.CrudJSFConfig;
import org.crank.controller.ExcelExportControllerBean;
import org.crank.controller.TreeControllerBean;
import org.crank.crud.controller.CrudManagedObject;
import org.crank.crud.controller.datasource.DaoFilteringDataSource;
import org.crank.crud.controller.datasource.EnumDataSource;
import org.crank.crud.criteria.Comparison;
import org.crank.crud.dao.SpecialtyDAO;
import org.crank.crud.dao.DepartmentDAO;
import org.crank.crud.dao.EmployeeDAO;
import org.crank.crud.join.Fetch;
import org.crank.crud.jsf.support.AutocompleteController;
import org.crank.crud.jsf.support.JsfCrudAdapter;
import org.crank.crud.jsf.support.JsfDetailController;
import org.crank.crud.jsf.support.SelectItemGenerator;
import org.crank.crud.model.Specialty;
import org.crank.crud.model.ContactInfo;
import org.crank.crud.model.Department;
import org.crank.crud.model.Employee;
import org.crank.crud.model.EmployeeStatus;
import org.crank.crud.model.Task;
import org.crank.crud.relationships.RelationshipManager;
import org.crank.model.jsf.support.RichFacesTreeModelBuilder;


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
    	}
    	return managedObjects;
		
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
    @Bean (scope = DefaultScopes.SESSION) 
    public JsfCrudAdapter deptCrud() throws Exception {
        JsfCrudAdapter adapter = cruds().get( "Department");
        adapter.getController().addChild( "employees", new JsfDetailController(Employee.class))
        .addChild( "tasks", new JsfDetailController(Task.class) );
        return adapter;
    }
    
    @SuppressWarnings("unchecked")
    @Bean (scope = DefaultScopes.SESSION) 
    public JsfCrudAdapter employeeCrud() throws Exception {
        JsfCrudAdapter adapter = cruds().get( "Employee");
        
        adapter.getPaginator().addCriterion(Comparison.eq("manager",null));
        adapter.getPaginator().filter();
        
        adapter.getController().addChild( "tasks", new JsfDetailController(Task.class));
        adapter.getController().addChild( "contacts", new JsfDetailController(ContactInfo.class));

        JsfDetailController directReports = new JsfDetailController(Employee.class);
        RelationshipManager relationshipManager = directReports.getRelationshipManager();
        relationshipManager.setChildCollectionProperty("directReports");
        relationshipManager.setAddToParentMethodName("addDirectReport");
        relationshipManager.setRemoveFromParentMethodName("removeDirectReport");
        
        
        adapter.getController().addChild( "directReports", directReports);
        return adapter;
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
        //     dataSource - the datasource for the lookup values
        //     propertyName - the property on the lookup results which will be used query against
        //     fieldName - the field name of the associated entity which will be assigned the validated autocomplete entity upon successful lookup
        AutocompleteController autoController = new AutocompleteController(dataSource, "name", "specialty");
        
        // Wire in the event handler to the associated controller for dealing with 
        // conversion / validation from the Many to One association to the controller's entity
        cruds().get("Employee").getController().addCrudControllerListener(autoController);
        
        // Add to the auto-complete map
        autocomplete.put("Specialty", autoController);

        return autocomplete;
    }
    
    @Bean
    public String persistenceUnitName() {
    	return "crank-crud-app"; 
    }

}

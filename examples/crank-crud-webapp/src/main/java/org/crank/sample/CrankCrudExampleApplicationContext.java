package org.crank.sample;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.crank.config.spring.support.CrudJSFConfig;
import org.crank.controller.ExcelExportControllerBean;
import org.crank.controller.SayHelloController;
import org.crank.controller.SelectEmployeeListingController;
import org.crank.controller.SelectManyByIdController;
import org.crank.controller.TagController;
import org.crank.crud.controller.AutoCompleteController;
import org.crank.crud.controller.CrudManagedObject;
import org.crank.crud.controller.CrudOperations;
import org.crank.crud.controller.FilteringPaginator;
import org.crank.crud.controller.Row;
import org.crank.crud.controller.datasource.DaoFilteringDataSource;
import org.crank.crud.controller.datasource.EnumDataSource;
import org.crank.crud.criteria.Comparison;
import org.crank.crud.criteria.OrderBy;
import org.crank.crud.dao.DepartmentDAO;
import org.crank.crud.dao.EmployeeDAO;
import org.crank.crud.dao.RoleDAO;
import org.crank.crud.dao.SpecialtyDAO;
import org.crank.crud.dao.TagDAO;
import org.crank.crud.jsf.support.JsfAutoCompleteController;
import org.crank.crud.jsf.support.JsfCrudAdapter;
import org.crank.crud.jsf.support.JsfDetailController;
import org.crank.crud.jsf.support.JsfSelectManyController;
import org.crank.crud.jsf.support.JsfSelectOneListingController;
import org.crank.crud.jsf.support.SelectItemGenerator;
import org.crank.crud.model.ContactInfo;
import org.crank.crud.model.Department;
import org.crank.crud.model.Employee;
import org.crank.crud.model.EmployeeStatus;
import org.crank.crud.model.Role;
import org.crank.crud.model.Skill;
import org.crank.crud.model.Specialty;
import org.crank.crud.model.Tag;
import org.crank.crud.model.Task;
import org.crank.crud.relationships.RelationshipManager;
import org.crank.sample.datasource.EmployeeDataSource;
import org.crank.sample.datasource.EmployeeReportObject;
import org.springframework.config.java.annotation.Bean;
import org.springframework.config.java.annotation.Configuration;
import org.springframework.config.java.annotation.ExternalBean;
import org.springframework.config.java.annotation.Lazy;
import org.springframework.config.java.annotation.aop.ScopedProxy;
import org.springframework.config.java.util.DefaultScopes;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration(defaultLazy = Lazy.TRUE)
public abstract class CrankCrudExampleApplicationContext extends CrudJSFConfig {

	private static List<CrudManagedObject> managedObjects;
	

	@Bean(scope = DefaultScopes.SINGLETON)
	public List<CrudManagedObject> managedObjects() {
		if (managedObjects == null) {
			managedObjects = new ArrayList<CrudManagedObject>();
			managedObjects.add(new CrudManagedObject(Employee.class,
					EmployeeDAO.class));
			managedObjects.add(new CrudManagedObject(Department.class,
					DepartmentDAO.class));
			managedObjects.add(new CrudManagedObject(Specialty.class,
					SpecialtyDAO.class));
			
			managedObjects.add(new CrudManagedObject(Tag.class,
					TagDAO.class));
			
			
			CrudManagedObject crudManagedObject = new CrudManagedObject(Role.class, RoleDAO.class);
			crudManagedObject.setNewSelect("new Role(o.id, o.name)");
			managedObjects
					.add(crudManagedObject);
			managedObjects.add(new CrudManagedObject(Skill.class, null));
			try {
				dataTableScrollerBean();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return managedObjects;

	}

	@ScopedProxy
	@Bean(scope = DefaultScopes.SESSION)
	public DataTableScrollerBean dataTableScrollerBean() throws Exception {
		DataTableScrollerBean bean = new DataTableScrollerBean();
		bean.setEmployeeDataPaginator(paginators().get("Employee"));
		bean.setEmployeeDAO(repos().get("Employee"));
		bean.populate();
		return bean;
	}

	@SuppressWarnings("unchecked")
	@Bean(scope = DefaultScopes.REQUEST)
	public SelectEmployeeListingController selectEmployeeListingController()
			throws Exception {
		SelectEmployeeListingController controller = new SelectEmployeeListingController(
				paginators().get("Employee"));
		return controller;
	}

	@SuppressWarnings("unchecked")
	@ExternalBean
	public abstract JsfCrudAdapter deptCrud();

	@SuppressWarnings("unchecked")
	@Bean(scope = DefaultScopes.SESSION, aliases = "deptCrud")
	public JsfCrudAdapter deptCrudController() throws Exception {
		JsfCrudAdapter adapter = cruds().get("Department");
		adapter.getController().addChild("employees",
				new JsfDetailController(Employee.class, true)).addChild("tasks",
						new JsfDetailController(Task.class));
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

		adapter.getPaginator().addOrderBy(OrderBy.asc("lastName"));
		adapter.getPaginator().filter();

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

	@SuppressWarnings("unchecked")
	@ExternalBean
	abstract JsfCrudAdapter empCrud();
	
	@ExternalBean
	abstract DataSource employeeDataSource();

	@SuppressWarnings({ "unchecked", "serial" })
	@Bean(scope = "view")
	public JsfCrudAdapter empRecordCrud() {
		EmployeeDataSource dataSource = new EmployeeDataSource();
		dataSource.setJdbcTemplate(new JdbcTemplate(employeeDataSource()));
		FilteringPaginator filteringPaginator = new FilteringPaginator(dataSource, EmployeeReportObject.class);
		JsfCrudAdapter adapter = new JsfCrudAdapter(filteringPaginator, (CrudOperations)empCrud().getController()){
		    public Serializable getEntity() {
		        Object object = ((Row)getModel().getRowData()).getObject();
		        EmployeeReportObject employeeReportObject = (EmployeeReportObject) object;
		        Employee employee = new Employee();
		        employee.setId(employeeReportObject.getId());
		        return employee;
		     }
		    
		};
		
		return adapter;
	}

	@ExternalBean
	abstract JsfSelectManyController<Role, Long> empToRole ();
	
	@Bean(scope = DefaultScopes.SESSION, aliases="empToRole")
	public JsfSelectManyController<Role, Long> employeeToRoleController()
			throws Exception {
		JsfSelectManyController<Role, Long> controller = new JsfSelectManyController<Role, Long>(
				Role.class, "roles", paginators().get("Role"), empCrud()
						.getController());
		return controller;
	}

	@Bean(scope = DefaultScopes.SESSION)
	public JsfSelectOneListingController<Specialty, Long> employeeToSpecialtyController()
			throws Exception {
		JsfSelectOneListingController<Specialty, Long> controller = new JsfSelectOneListingController<Specialty, Long>(
				Specialty.class, "specialty", paginators().get("Specialty"),
				empCrud().getController());
		return controller;
	}

	@Bean(scope = DefaultScopes.SESSION)
	public JsfSelectOneListingController<Skill, Long> employeeToSkillController()
			throws Exception {
		JsfSelectOneListingController<Skill, Long> controller = new JsfSelectOneListingController<Skill, Long>(
				Skill.class, "primarySkill", paginators().get("Skill"),
				empCrud().getController());
		return controller;
	}

	@SuppressWarnings("unchecked")
	@Bean(scope = DefaultScopes.SINGLETON)
	public Map<String, SelectItemGenerator> selectItemGenerators()
			throws Exception {
		Map<String, SelectItemGenerator> selectItemGenerators = super
				.selectItemGenerators();

		SelectItemGenerator selectItemGenerator = new SelectItemGenerator();
		EnumDataSource<EmployeeStatus> dataSource = new EnumDataSource();
		dataSource.setType(EmployeeStatus.class);
		selectItemGenerator.setDataSource(dataSource);
		selectItemGenerators.put("EmployeeStatus", selectItemGenerator);
		return selectItemGenerators;
	}

    @SuppressWarnings("unchecked")
    @Bean (scope = DefaultScopes.SESSION) 
    public Map<String, AutoCompleteController> autocomplete () throws Exception {
        Map<String, AutoCompleteController> autocomplete = new HashMap<String, AutoCompleteController>();
        
        // Create a data source to be used by the auto-complete controller
        // and add DAO for the entity containing the auto-complete data.
        DaoFilteringDataSource dataSource = new DaoFilteringDataSource();
        dataSource.setDao( repos().get( "Specialty" ));
      
        // Resolve the CRUD controller for the entity to be affected by the 
        // auto-complete selected value.
        CrudOperations controller = cruds().get("Employee").getController();

        // Create the auto-complete controller.
        // Arguments are:
        //     sourceClass -         
        //	      the class of the entity containing the source value property.
        //     sourceProperty - 
        //	      the property on sourceClass containing the source values.
        //     dataSource - 
        //	      the data source for sourceClass        	
        //     targetCrudController -   
        //	      the CRUD controller for the entity to be affected.
        //     targetProperty -
        //        the property of the target entity to be completed by the 
        //        auto-complete value.	
        AutoCompleteController autoController = new JsfAutoCompleteController( 
        		Specialty.class, "name", dataSource, controller, "specialty");
        
        // Add to the auto-complete map...
        //     This will be accessed via the field.xhtml as the AutoComplete 
        //     controller for the associated target property.
        autocomplete.put("Specialty", autoController);
        return autocomplete;
    }

	@Bean(scope = DefaultScopes.SESSION)
	@ScopedProxy
	public ExcelExportControllerBean controllerBean() throws Exception {
		ExcelExportControllerBean bean = new ExcelExportControllerBean();
		return bean;
	}

	@Bean
	public String persistenceUnitName() {
		return "employee-example";
	}
	
	@Bean(scope = DefaultScopes.SESSION) 
	public SayHelloController hello(){
		SayHelloController helloController = new SayHelloController();
		
		helloController.setEmployeesController((JsfDetailController) deptCrud().getController().getChildren().get("employees"));
		return helloController;
	}
	
	@SuppressWarnings("unchecked")
	@Bean(scope = DefaultScopes.REQUEST)
	public TagController tagController1() {
		TagController tagController = new TagController();
		tagController.setTagRepo((TagDAO) repos().get("Tag"));
		JsfCrudAdapter<Employee, Long> jsfCrudAdapter = (JsfCrudAdapter<Employee, Long>) cruds().get("Employee");
		tagController.setParentCrudController(jsfCrudAdapter.getController());
		return tagController;
	}
	
	@SuppressWarnings("unchecked")
	@Bean(scope = DefaultScopes.REQUEST)
	public SelectManyByIdController<Employee, Tag, Long> tagController() {
		SelectManyByIdController<Employee, Tag, Long> tagController = new SelectManyByIdController<Employee, Tag, Long>();
		tagController.setTargetProperty("employeeId");
		tagController.setRepo((TagDAO) repos().get("Tag"));
		JsfCrudAdapter<Employee, Long> jsfCrudAdapter = (JsfCrudAdapter<Employee, Long>) cruds().get("Employee");
		tagController.setParentCrudController(jsfCrudAdapter.getController());
		return tagController;
	}
	
	

}

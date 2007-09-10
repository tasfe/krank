package org.crank.sample;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.convert.Converter;
import javax.persistence.EntityManagerFactory;

import org.springframework.config.java.annotation.Bean;
import org.springframework.config.java.annotation.Configuration;
import org.springframework.config.java.annotation.Lazy;
import org.springframework.config.java.annotation.aop.ScopedProxy;
import org.springframework.config.java.util.DefaultScopes;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.crank.controller.ExcelExportControllerBean;
import org.crank.controller.TreeControllerBean;
import org.crank.core.spring.support.SpringBeanWrapperPropertiesUtil;
import org.crank.crud.controller.CrudController;
import org.crank.crud.controller.CrudManagedObject;
import org.crank.crud.controller.FilterablePageable;
import org.crank.crud.controller.FilteringPaginator;
import org.crank.crud.controller.datasource.DaoFilteringDataSource;
import org.crank.crud.controller.datasource.EnumDataSource;
import org.crank.crud.controller.datasource.DaoFilteringPagingDataSource;
import org.crank.crud.controller.support.tomahawk.TomahawkFileUploadHandler;
import org.crank.crud.dao.SpecialtyDAO;
import org.crank.crud.dao.DepartmentDAO;
import org.crank.crud.dao.EmployeeDAO;
import org.crank.crud.join.Fetch;
import org.crank.crud.jsf.support.AutocompleteController;
import org.crank.crud.jsf.support.EntityConverter;
import org.crank.crud.jsf.support.JsfCrudAdapter;
import org.crank.crud.jsf.support.JsfDetailController;
import org.crank.crud.jsf.support.SelectItemGenerator;
import org.crank.crud.model.Specialty;
import org.crank.crud.model.ContactInfo;
import org.crank.crud.model.Department;
import org.crank.crud.model.Employee;
import org.crank.crud.model.EmployeeStatus;
import org.crank.crud.model.Task;
import org.crank.crud.GenericDao;
import org.crank.crud.GenericDaoFactory;
import org.crank.model.jsf.support.RichFacesTreeModelBuilder;
import org.crank.web.RequestParameterMapFinderImpl;

@Configuration (defaultLazy=Lazy.TRUE)
public class CrankCrudExampleApplicationContext {
    
    private static List<CrudManagedObject> managedObjects = new ArrayList<CrudManagedObject>();
    
    static
    {
        
        managedObjects.add( new CrudManagedObject(Employee.class, EmployeeDAO.class) );
        managedObjects.add( new CrudManagedObject(Department.class, DepartmentDAO.class) );
        managedObjects.add( new CrudManagedObject(Specialty.class, SpecialtyDAO.class) );
    }
    
    @Bean (scope = DefaultScopes.SESSION)
    @ScopedProxy
    public DataTableScrollerBean dataTableScrollerBean() throws Exception {
        DataTableScrollerBean bean = new DataTableScrollerBean();
        bean.setEmployeeDataPaginator(paginators().get("Employee"));
        bean.setEmployeeDAO(repositories().get("Employee"));
        return bean;
    }
    
    @SuppressWarnings("unchecked")
    @Bean (scope = DefaultScopes.SESSION) 
    public Map<String, JsfCrudAdapter> cruds() throws Exception {
        Map <String, JsfCrudAdapter> cruds = new HashMap<String, JsfCrudAdapter>();
        for (CrudManagedObject mo : managedObjects) {
            CrudController crudController = new CrudController();
            crudController.setFileUploadHandler( new TomahawkFileUploadHandler() );
            crudController.setPropertyUtil( new SpringBeanWrapperPropertiesUtil() );
            crudController.setEntityClass( mo.getEntityType() );
            crudController.setDao( repositories().get( mo.getName() ) );
            JsfCrudAdapter jsfCrudAdapter = new JsfCrudAdapter(paginators().get(mo.getName()), crudController);
            cruds.put(mo.getName(), jsfCrudAdapter);
        }
        return cruds;
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
        adapter.getController().addChild( "tasks", new JsfDetailController(Task.class));
        adapter.getController().addChild( "contacts", new JsfDetailController(ContactInfo.class));
        return adapter;
    }

    
    @SuppressWarnings("unchecked")
    @Bean (scope = DefaultScopes.SINGLETON) 
    public Map<String, GenericDao> repositories() throws Exception {
        Map<String, GenericDao> repositories = new HashMap<String, GenericDao>();
        for (CrudManagedObject mo : managedObjects) {
            GenericDao dao = createDao(mo.getDaoInterface(), mo.getEntityType());  
            repositories.put(mo.getName(), dao);
        }
        return repositories;
    }

    @SuppressWarnings("unchecked")
    @Bean (scope = DefaultScopes.SESSION) 
    public Map<String, FilterablePageable> paginators () throws Exception {
        Map<String, FilterablePageable> paginators = new HashMap<String, FilterablePageable>();
        for (CrudManagedObject mo : managedObjects) {
            DaoFilteringPagingDataSource dataSource = new DaoFilteringPagingDataSource();
            dataSource.setDao( repositories().get( mo.getName() ));
            FilteringPaginator dataPaginator = new FilteringPaginator(dataSource, mo.getEntityType());
            dataPaginator.setRequestParameterMapFinder(new RequestParameterMapFinderImpl());
            paginators.put(mo.getName(), dataPaginator);
        }
        return paginators;
    }
    
    @SuppressWarnings("unchecked")
    @Bean (scope = DefaultScopes.SINGLETON)
    public Map<String, SelectItemGenerator> selectItemGenerators() throws Exception {
        Map<String, SelectItemGenerator> selectItemGenerators = new HashMap<String, SelectItemGenerator>();

        SelectItemGenerator selectItemGenerator = new SelectItemGenerator();
        EnumDataSource<EmployeeStatus> dataSource = new EnumDataSource();
        dataSource.setType(EmployeeStatus.class);
        selectItemGenerator.setDataSource( dataSource );
        selectItemGenerators.put("EmployeeStatus", selectItemGenerator);
        
        for (CrudManagedObject mo : managedObjects) {
            selectItemGenerator = new SelectItemGenerator();
            DaoFilteringPagingDataSource<Department, Long> jpaDataSource = new DaoFilteringPagingDataSource<Department, Long>();
            jpaDataSource.setDao( repositories().get( mo.getName() ));
            selectItemGenerator.setDataSource( jpaDataSource );
            selectItemGenerators.put(mo.getName(), selectItemGenerator);
        }
        return selectItemGenerators;
    }
    
    @SuppressWarnings("unchecked")
    @Bean (scope = DefaultScopes.SINGLETON)
    public Map<String, Converter> converters() throws Exception {
        Map<String, Converter> converters = new HashMap<String, Converter>();
        for (CrudManagedObject mo : managedObjects) {
            EntityConverter entityConverter = new EntityConverter();
            entityConverter.setManagedObject( mo );
            entityConverter.setDao( repositories().get( mo.getName()) );
            converters.put(mo.getName(), entityConverter);            
        }
        return converters;
    }
    
    public LocalEntityManagerFactoryBean entityManagerFactoryFactory() throws Exception {
        LocalEntityManagerFactoryBean entityManagerFactoryFactory = new LocalEntityManagerFactoryBean();
        entityManagerFactoryFactory.setPersistenceUnitName( "crank-crud-app" );
        entityManagerFactoryFactory.afterPropertiesSet();
        return entityManagerFactoryFactory;
    }
    @Bean (scope = DefaultScopes.SINGLETON)
    public EntityManagerFactory entityManagerFactory() throws Exception {
        return entityManagerFactoryFactory().getObject();
    }

    @Bean (scope = DefaultScopes.SINGLETON)
    public PlatformTransactionManager transactionManager() throws Exception {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory( entityManagerFactory() );
        return transactionManager;
    }
    
    @Bean (scope = DefaultScopes.SINGLETON)
    public TransactionInterceptor transactionInterceptor() throws Exception {
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor((PlatformTransactionManager) transactionManager(), 
                new AnnotationTransactionAttributeSource());
        return transactionInterceptor;
    }
        
    public GenericDao createDao(Class daoClass, Class entityClass) throws Exception {
        GenericDaoFactory genericDaoFactory = new GenericDaoFactory(transactionInterceptor());
        genericDaoFactory.setInterface( daoClass );
        genericDaoFactory.setBo( entityClass );
        genericDaoFactory.setEntityManagerFactory( entityManagerFactory() );
        genericDaoFactory.afterPropertiesSet();
        return (GenericDao) genericDaoFactory.getObject();    
    }
    
    @SuppressWarnings("unchecked")
    @Bean (scope = DefaultScopes.SINGLETON)
    public SelectItemGenerator departmentDropDown() throws Exception {
        return selectItemGenerators().get( "Department" );
    }
    
    @Bean (scope = DefaultScopes.SINGLETON)
    public Converter org_crank_crud_model_Department_Converter() throws Exception {
        return converters().get( "Department" );
    }
    
    @Bean (scope = DefaultScopes.SINGLETON)
    public List<Class> jsfConverterTypes () {
        List<Class> list = new ArrayList<Class>();
        list.add( Department.class );
        return list;
    }
    
    @Bean (scope = DefaultScopes.SESSION)
    @ScopedProxy
    public ExcelExportControllerBean controllerBean() throws Exception {
      ExcelExportControllerBean bean = new ExcelExportControllerBean();
        return bean;
    }
    
    @Bean (scope = DefaultScopes.SESSION)
    @ScopedProxy
    public TreeControllerBean treeControllerBean() throws Exception {
    	/* Create TreeControllerBean. */
    	TreeControllerBean bean = new TreeControllerBean();
    	
    	/* Setup a datasource for the tree. */
    	DaoFilteringDataSource<Department, Long> dataSource = new DaoFilteringDataSource<Department, Long>();
    	dataSource.setDao(this.repositories().get("Department"));
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
        dataSource.setDao( repositories().get( "Specialty" ));
        
        // Create the auto-complete controller for the particular property on the data source
        // Arguments are:
        //     dataSource - the datasource for the lookup values
        //     propertyName - the property on the lookup results which will be used query against
        //     fieldName - the field name of the associated entity which will be assigned the validated autocomplete entity upon successful lookup
        AutocompleteController autoController = new AutocompleteController(dataSource, "name", "specialty");
        
        // Wire in the event handler to the associated controller for dealing with 
        // conversion / validation from the Many to One association to the controller's entity
        cruds().get("Employee").getController().addCrudControllerListener(autoController);
        
        // Add to the autocomplete map
        autocomplete.put("Specialty", autoController);

        return autocomplete;
    }
    
    
}

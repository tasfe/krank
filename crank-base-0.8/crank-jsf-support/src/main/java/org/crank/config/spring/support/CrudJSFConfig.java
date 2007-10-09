package org.crank.config.spring.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.convert.Converter;

import org.crank.core.StringUtils;
import org.crank.core.spring.support.SpringBeanWrapperPropertiesUtil;
import org.crank.crud.GenericDao;
import org.crank.crud.controller.CrudController;
import org.crank.crud.controller.CrudManagedObject;
import org.crank.crud.controller.FilterablePageable;
import org.crank.crud.controller.FilteringPaginator;
import org.crank.crud.controller.datasource.DaoFilteringPagingDataSource;
import org.crank.crud.controller.support.tomahawk.TomahawkFileUploadHandler;
import org.crank.crud.jsf.support.EntityConverter;
import org.crank.crud.jsf.support.JsfCrudAdapter;
import org.crank.crud.jsf.support.SelectItemGenerator;
import org.crank.web.RequestParameterMapFinderImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.config.java.annotation.Bean;
import org.springframework.config.java.annotation.ExternalBean;
import org.springframework.config.java.util.DefaultScopes;


public abstract class CrudJSFConfig implements InitializingBean {

	public void afterPropertiesSet() throws Exception {
	}

	protected abstract List<CrudManagedObject> managedObjects();
	
    /** Creates backing beans for CRUD operations, create, read, delete, etc. */
    @SuppressWarnings("unchecked")
    @Bean (scope = DefaultScopes.SESSION, aliases="cruds") 
    public Map<String, JsfCrudAdapter> crudControllers() throws Exception {
    	/* Cruds holds a map of Crud objects. */
        Map <String, JsfCrudAdapter> cruds = new HashMap<String, JsfCrudAdapter>();
        
        
        for (CrudManagedObject mo : managedObjects()) {
        	/* Create a new controller. */
            CrudController crudController = new CrudController();
            /* Associate controller with file upload subcontroller. */
            crudController.setFileUploadHandler( new TomahawkFileUploadHandler() );
            /* Register property utils. */
            crudController.setPropertyUtil( new SpringBeanWrapperPropertiesUtil() );
            
            /* Set the entity class into the controller. */
            crudController.setEntityClass( mo.getEntityType() );
            
            /* Inject the repositories. */
            crudController.setDao( repos().get( mo.getName() ) );
            JsfCrudAdapter jsfCrudAdapter = new JsfCrudAdapter(paginators().get(StringUtils.unCapitalize(mo.getName())), crudController);
            
            /* Put the controller into the map. */
            cruds.put(StringUtils.unCapitalize(mo.getName()), jsfCrudAdapter);
            cruds.put(mo.getName(), jsfCrudAdapter);
        }
        
        return cruds;
    }
    
    @SuppressWarnings("unchecked")
    @ExternalBean
    public abstract Map<String, GenericDao> repos() ;
    
    @SuppressWarnings("unchecked")
    @ExternalBean
    public abstract Map<String, JsfCrudAdapter> cruds() ; 

    @SuppressWarnings("unchecked")
    @ExternalBean
    public abstract Map<String, FilterablePageable> pagers() ;

    /**
     * Paginators used to paginate listings.
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Bean (scope = DefaultScopes.SESSION, aliases="pagers") 
    public Map<String, FilterablePageable> paginators () throws Exception {
        Map<String, FilterablePageable> paginators = new HashMap<String, FilterablePageable>();
        for (CrudManagedObject mo : managedObjects()) {
            DaoFilteringPagingDataSource dataSource = new DaoFilteringPagingDataSource();
            dataSource.setDao( repos().get( mo.getName() ));
            FilteringPaginator dataPaginator = new FilteringPaginator(dataSource, mo.getEntityType());
            dataPaginator.setRequestParameterMapFinder(new RequestParameterMapFinderImpl());
            paginators.put(StringUtils.unCapitalize(mo.getName()), dataPaginator);
            paginators.put(mo.getName(), dataPaginator);
        }
        return paginators;
    }
    
    @SuppressWarnings("unchecked")
    @Bean (scope = DefaultScopes.SINGLETON)
    public Map<String, SelectItemGenerator> selectItemGenerators() throws Exception {
        Map<String, SelectItemGenerator> selectItemGenerators = new HashMap<String, SelectItemGenerator>();

        SelectItemGenerator selectItemGenerator = new SelectItemGenerator();
        
        for (CrudManagedObject mo : managedObjects()) {
            selectItemGenerator = new SelectItemGenerator();
            DaoFilteringPagingDataSource dataSource = new DaoFilteringPagingDataSource();
            dataSource.setDao( repos().get( mo.getName() ));
            selectItemGenerator.setDataSource( dataSource );
            selectItemGenerators.put(mo.getName(), selectItemGenerator);
            selectItemGenerators.put(StringUtils.unCapitalize(mo.getName()), selectItemGenerator);
        }
        return selectItemGenerators;
    }
    /**
     * Register JSF converters.
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Bean (scope = DefaultScopes.SINGLETON)
    public Map<String, Converter> converters() throws Exception {
        Map<String, Converter> converters = new HashMap<String, Converter>();
        for (CrudManagedObject mo : managedObjects()) {
            EntityConverter entityConverter = new EntityConverter();
            entityConverter.setManagedObject( mo );
            entityConverter.setDao( repos().get( mo.getName()) );
            converters.put(mo.getName(), entityConverter);
            converters.put(StringUtils.unCapitalize(mo.getName()), entityConverter);            
        }
        return converters;
    }
}

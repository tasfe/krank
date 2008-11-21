package org.crank.config.spring.support;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.convert.Converter;

import org.crank.core.StringUtils;
import org.crank.core.spring.support.SpringBeanWrapperPropertiesUtil;
import org.crank.crud.GenericDao;
import org.crank.crud.controller.CrudController;
import org.crank.crud.controller.CrudControllerListenerAdapter;
import org.crank.crud.controller.CrudEvent;
import org.crank.crud.controller.CrudManagedObject;
import org.crank.crud.controller.FilterablePageable;
import org.crank.crud.controller.FilteringPaginator;
import org.crank.crud.controller.datasource.DaoFilteringPagingDataSource;
import org.crank.crud.controller.support.tomahawk.TomahawkFileUploadHandler;
import org.crank.crud.jsf.support.EntityConverter;
import org.crank.crud.jsf.support.JsfCrudAdapter;
import org.crank.crud.jsf.support.JsfDetailController;
import org.crank.crud.jsf.support.JsfMessageInterceptor;
import org.crank.crud.jsf.support.SelectItemGenerator;
import org.crank.web.RequestParameterMapFinderImpl;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.config.java.annotation.Bean;
import org.springframework.config.java.annotation.ExternalBean;
import org.springframework.config.java.util.DefaultScopes;
import org.springframework.transaction.interceptor.TransactionInterceptor;


public abstract class CrudJSFConfig implements InitializingBean {
	
	private boolean autoWireCrudToPaginators = true;

	public boolean isAutoWireCrudToPaginators() {
		return autoWireCrudToPaginators;
	}

	public void setAutoWireCrudToPaginators(boolean autoWireCrudToPaginators) {
		this.autoWireCrudToPaginators = autoWireCrudToPaginators;
	}

	public void afterPropertiesSet() throws Exception {
	}

	protected abstract List<CrudManagedObject> managedObjects();
	
    /** Creates backing beans for CRUD operations, create, read, delete, etc. */
    @SuppressWarnings("unchecked")
    @Bean (scope = DefaultScopes.SESSION, aliases="cruds") 
    public Map<String, JsfCrudAdapter<? extends Serializable, ? extends Serializable>> crudControllers() throws Exception {

        DeferredResourceCreator drc = new DeferredResourceCreator(){
               public void createResource(Map map, CrudManagedObject mo) throws Exception{
                       /* Create a new crudController. */
                       CrudController crudControllerTarget = new CrudController();

                       if (mo.isTransactionalController()) {
                           crudControllerTarget = addTransactionSupport(crudControllerTarget);
                           crudControllerTarget.setTransactional(true);
                       }

                       /* Associate crudController with file upload subcontroller. */
                       crudControllerTarget.setFileUploadHandler( new TomahawkFileUploadHandler() );
                       /* Register property utils. */
                       crudControllerTarget.setPropertyUtil( new SpringBeanWrapperPropertiesUtil() );

                       /* Set the entity class into the crudController. */
                       crudControllerTarget.setEntityClass( mo.getEntityType() );

                       if (autoWireCrudToPaginators) {
                    	   final FilterablePageable filterablePageable = pagers().get(mo.getName());
                    	   if (filterablePageable != null) {
                    		   crudControllerTarget.addCrudControllerListener(new CrudControllerListenerAdapter() {
								
                    			public void afterCreate(CrudEvent event) {
                    				filterablePageable.reset();
								}

								public void afterDelete(CrudEvent event) {
									filterablePageable.reset();
								}

								public void afterUpdate(CrudEvent event) {
									filterablePageable.reset();
								}

									
								});
                    	   }
                       }

                       /* Inject the repositories. */
                       crudControllerTarget.setDao( repos().get( mo.getName() ) );
                       JsfCrudAdapter jsfCrudAdapter = new JsfCrudAdapter(StringUtils.unCapitalize(mo.getName()),
                               pagers().get(mo.getName()), crudControllerTarget);

                       jsfCrudAdapter.setEntityName(StringUtils.unCapitalize(mo.getName()));


                       /* Put the crudController into the map. */
                       map.put(StringUtils.unCapitalize(mo.getName()), jsfCrudAdapter);
                       map.put(mo.getName(), jsfCrudAdapter);
                       crudAdded(mo.getName(), jsfCrudAdapter);

               }
        };

        /* Cruds holds a map of Crud objects. */
        Map <String, JsfCrudAdapter<? extends Serializable, ? extends Serializable>> cruds = new ManagedObjectsLazyInitMap<String, JsfCrudAdapter<? extends Serializable, ? extends Serializable>>(managedObjects(), drc);
        
        

        return cruds;
    }

	protected void crudAdded(String name, JsfCrudAdapter<?, ?> jsfCrudAdapter) {
	}

	public Object addJSFMessageHandling(
			Object target) {
		ProxyFactoryBean proxyCreatorSupport = new ProxyFactoryBean();
		proxyCreatorSupport.setTarget(target);
		proxyCreatorSupport.addAdvice(new JsfMessageInterceptor());
		proxyCreatorSupport.setOptimize(true);
		proxyCreatorSupport.setOpaque(false);
		return proxyCreatorSupport.getObject();
	}
	
	@Bean (scope = DefaultScopes.SINGLETON)
	public CrudJSFConfig crudJSFConfig() {
		return this;
	}
    
	@SuppressWarnings("unchecked")
	public <E extends Serializable, PK extends Serializable> JsfDetailController<E, PK> createDetailController(Class<E> entityClass) {
		return (JsfDetailController<E, PK>) addJSFMessageHandling(new JsfDetailController<E, PK>(entityClass));
	}
	
    @SuppressWarnings("unchecked")
    @ExternalBean
    public abstract Map<String, GenericDao> repos() ;
       
    @ExternalBean
    public abstract Map<String, JsfCrudAdapter<? extends Serializable, ? extends Serializable>> cruds() ; 

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

        DeferredResourceCreator drc = new DeferredResourceCreator(){
               public void createResource(Map map, CrudManagedObject mo) throws Exception{
                   DaoFilteringPagingDataSource dataSource = new DaoFilteringPagingDataSource();
                   dataSource.setDao( repos().get( mo.getName() ));
                   FilteringPaginator dataPaginator = null;
                   if (mo.getPropertyNames() == null) {
                        dataPaginator = new FilteringPaginator(dataSource, mo.getEntityType());
                   } else {
                       dataPaginator = new FilteringPaginator(dataSource, mo.getEntityType(), mo.getPropertyNames());
                   }
                   dataPaginator.setRequestParameterMapFinder(new RequestParameterMapFinderImpl());
                   map.put(StringUtils.unCapitalize(mo.getName()), dataPaginator);
                   map.put(mo.getName(), dataPaginator);
                   //dataPaginator.filter();

               }
        };

        Map<String, FilterablePageable> paginators = new ManagedObjectsLazyInitMap<String, FilterablePageable>(managedObjects(), drc);
        return paginators;
    }
    
    @SuppressWarnings("unchecked")
    @Bean (scope = DefaultScopes.SINGLETON)
    public Map<String, SelectItemGenerator> selectItemGenerators() throws Exception {

        DeferredResourceCreator drc = new DeferredResourceCreator(){
               public void createResource(Map map, CrudManagedObject mo) throws Exception{
                   SelectItemGenerator selectItemGenerator = new SelectItemGenerator();
                   DaoFilteringPagingDataSource dataSource = new DaoFilteringPagingDataSource();
                   dataSource.setDao( repos().get( mo.getName() ));
                   selectItemGenerator.setDataSource( dataSource );
                   map.put(mo.getName(), selectItemGenerator);
                   map.put(StringUtils.unCapitalize(mo.getName()), selectItemGenerator);
               }
        };

        Map<String, SelectItemGenerator> selectItemGenerators = new ManagedObjectsLazyInitMap<String, SelectItemGenerator>(managedObjects(), drc);
        return selectItemGenerators;
    }
    /**
     * Register JSF converters.
     * @return
     * @throws Exception
     */
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
    
	@ExternalBean
	public abstract TransactionInterceptor transactionInterceptor();
	
	@SuppressWarnings("unchecked")
	public <T> T addTransactionSupport(T target) {
		ProxyFactoryBean proxyCreatorSupport = new ProxyFactoryBean();
		proxyCreatorSupport.setTarget(target);
		proxyCreatorSupport.addAdvice(transactionInterceptor());
		proxyCreatorSupport.setOptimize(true);
		proxyCreatorSupport.setOpaque(false);
		return (T)proxyCreatorSupport.getObject();
	}
	
    
}

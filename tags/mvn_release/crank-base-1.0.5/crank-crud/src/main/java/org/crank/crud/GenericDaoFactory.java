package org.crank.crud;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.crank.crud.cache.PreloadConfiguration;
import org.crank.crud.cache.PreloadableCacheableGenericDaoJpa;
import org.crank.crud.cache.CachingAdvisor;

/**
 * This simplifies Spring configuration for Generic DAOs.
 *
 *  @version $Revision:$
 *  @author Rick Hightower
 */
public class GenericDaoFactory extends ProxyFactoryBean implements InitializingBean, ApplicationContextAware {

    private Class<?> intf;

    private Class<?> bo;

    private EntityManagerFactory entityManagerFactory;
    
    private List<QueryHint<?>> queryHints;
    
    private TransactionInterceptor transactionInterceptor;
    
    private PlatformTransactionManager transactionManager;
    
    private String transactionManagerName = "transactionManager";
    
    private String entityManagerFactoryName = "entityManagerFactory";

    public void setEntityManagerFactoryName(String entityManagerFactoryName) {
		this.entityManagerFactoryName = entityManagerFactoryName;
	}

	public void setTransactionManagerName(String transactionManagerName) {
		this.transactionManagerName = transactionManagerName;
	}

	public void setTransactionInterceptor(
			TransactionInterceptor transactionInterceptor) {
		this.transactionInterceptor = transactionInterceptor;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	private boolean preloadEnabled = false;
    {
    	if(preloadEnabled) {
    		
    	}
    }
    private PreloadConfiguration preloadConfiguration;


    public void setPreloadEnabled(boolean preloadEnabled) {
        this.preloadEnabled = preloadEnabled;
    }

    public void setPreloadConfiguration(PreloadConfiguration preloadConfiguration) {
        this.preloadConfiguration = preloadConfiguration;
    }

    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void setInterface( final Class<?> intf ) {
        this.intf = intf;
    }

    @Deprecated
    public GenericDaoFactory( TransactionInterceptor transactionInterceptor ) {
        this.transactionInterceptor = transactionInterceptor;
    }
    
    public GenericDaoFactory() {
    }    

    @SuppressWarnings( "unchecked" )
    public void afterPropertiesSet() throws Exception {
        if (bo == null) {
            throw new RuntimeException( "The business object property must be set." );
        }
        if (intf == null) {
        	intf = GenericDao.class;
        }

        this.setInterfaces( new Class[] { intf } );

        GenericDaoJpa dao;

        if (isCachingEnabled()) {
            dao = loadCachingDao();
        } else {
            dao = createNonCachingDao();
        }

        this.setTarget( dao );
        if (transactionInterceptor == null) {
            if (transactionManager == null) {
            	if (applicationContext.containsBean(transactionManagerName)) {
            		transactionManager = (PlatformTransactionManager) applicationContext.getBean(this.transactionManagerName);
            	} else {
            		throw new RuntimeException (String.format("transactionManager property not set and transactionPropertyName %s not found", this.transactionManagerName));
            	}
            }
        	transactionInterceptor = new TransactionInterceptor(transactionManager, new AnnotationTransactionAttributeSource());
        } 
        this.addAdvice(this.transactionInterceptor);
        this.addAdvisor( new DaoMethodsIntroductionAdvisor() );

    }
    
    @SuppressWarnings("unchecked")
    private GenericDaoJpa createNonCachingDao() {
        GenericDaoJpa dao = new GenericDaoJpa( bo );
        dao.setNewSelectStatement(newSelect);
        if (entityManagerFactory==null) {
        	if (this.applicationContext.containsBean(entityManagerFactoryName)) {
        		this.entityManagerFactory = (EntityManagerFactory) this.applicationContext.getBean(this.entityManagerFactoryName);
        	} else {
        		throw new RuntimeException(String.format("entityManagerFactory was not set and entityManagerFactoryName not found (%s)", this.entityManagerFactoryName));
        	}
        }
        dao.setEntityManagerFactory( entityManagerFactory );
        dao.setQueryHints(queryHints);
        return dao;
    }

    @SuppressWarnings("unchecked")
	private GenericDaoJpa loadCachingDao() {
        if (preloadConfiguration == null) {
            throw new RuntimeException( "The Caching Configuration property must be set to use a caching dao." );
        }
        PreloadableCacheableGenericDaoJpa dao = new PreloadableCacheableGenericDaoJpa( bo );
        dao.setPreloadConfiguration( preloadConfiguration );
        dao.setEntityManagerFactory( entityManagerFactory );
        dao.setNewSelectStatement(newSelect);
        dao.setQueryHints(queryHints);        
        dao.preload();
        this.addAdvisor( new CachingAdvisor() );
        return dao;
    }

    private boolean isCachingEnabled() {
        return preloadConfiguration != null;
    }

    public void setBo( Class<?> bo ) {
        this.bo = bo;
    }

    private String newSelect;
	public void setNewSelect(String newSelect) {
		this.newSelect = newSelect;
		
	}

	public void setQueryHints(List<QueryHint<?>> queryHints) {
		this.queryHints = queryHints;
	}

	private ApplicationContext applicationContext;
	
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
}

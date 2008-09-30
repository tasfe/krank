package org.crank.crud;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.InitializingBean;
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
public class GenericDaoFactory extends ProxyFactoryBean implements InitializingBean {

    private Class<?> intf;

    private Class<?> bo;

    private EntityManagerFactory entityManagerFactory;
    
    private List<QueryHint<?>> queryHints;

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

    public GenericDaoFactory( TransactionInterceptor transactionInterceptor ) {
        this.addAdvice( transactionInterceptor );
    }

    @SuppressWarnings( "unchecked" )
    public void afterPropertiesSet() throws Exception {
        if (bo == null) {
            throw new RuntimeException( "The business object property must be set." );
        }
        if (intf == null) {
            throw new RuntimeException( "The interface property must be set." );
        }

        this.setInterfaces( new Class[] { intf } );

        GenericDaoJpa dao;

        if (isCachingEnabled()) {
            dao = loadCachingDao();
        } else {
            dao = loadNonCachingDao();
        }

        this.setTarget( dao );
        this.addAdvisor( new DaoMethodsIntroductionAdvisor() );

    }
    
    @SuppressWarnings("unchecked")
    private GenericDaoJpa loadNonCachingDao() {
        GenericDaoJpa dao = new GenericDaoJpa( bo );
        dao.setNewSelectStatement(newSelect);
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
}

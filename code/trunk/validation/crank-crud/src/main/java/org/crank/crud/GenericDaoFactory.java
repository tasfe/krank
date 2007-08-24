package org.crank.crud;

import javax.persistence.EntityManagerFactory;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.crank.crud.cache.PreloadConfiguration;
import org.crank.crud.cache.PreloadableCacheableGenericDaoJpa;
import org.crank.crud.cache.CachingAdvisor;

/**
 * This simplifies Spring configuration.
 *
 *  @version $Revision:$
 *  @author Rick Hightower
 */
public class GenericDaoFactory extends ProxyFactoryBean implements InitializingBean {

    private Class intf;

    private Class bo;

    private EntityManagerFactory entityManagerFactory;

    private boolean preloadEnabled = false;
    {
    	if(preloadEnabled) {
    		
    	}
    }
    private PreloadConfiguration preloadConfiguration;


    public void setPreloadEnabled(boolean preloadEnabled) {
        this.preloadEnabled = preloadEnabled;
    }

    public void setCacheConfiguration(PreloadConfiguration preloadConfiguration) {
        this.preloadConfiguration = preloadConfiguration;
    }

    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void setInterface( final Class intf ) {
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

        GenericDaoJpa dao = null;

        if (isCachingEnabled()) {
            dao = loadCachingDao();
        } else {
            dao = loadNonCachingDao();
        }

        this.setTarget( dao );
        this.addAdvisor( new FinderIntroductionAdvisor() );

    }
    
    private GenericDaoJpa loadNonCachingDao() {
        GenericDaoJpa dao = new GenericDaoJpa( bo );
        dao.setEntityManagerFactory( entityManagerFactory );
        return dao;
    }

    private GenericDaoJpa loadCachingDao() {
        if (preloadConfiguration == null) {
            throw new RuntimeException( "The Caching Configuration property must be set to use a caching dao." );
        }
        PreloadableCacheableGenericDaoJpa dao = new PreloadableCacheableGenericDaoJpa( bo );
        dao.setCacheConfiguration( preloadConfiguration );
        dao.setEntityManagerFactory( entityManagerFactory );
        processPreloading( dao );
        this.addAdvisor( new CachingAdvisor() );
        return dao;
    }

    private void processPreloading(PreloadableCacheableGenericDaoJpa dao) {
        if (dao.getCacheConfiguration().getPreloadingHQL().length() > 0) {
            dao.preload(dao.getCacheConfiguration().getPreloadingHQL());
        } else if (dao.getCacheConfiguration().getPreloadingRecordCount() > 0) {
            dao.preload(dao.getCacheConfiguration().getPreloadingRecordCount());            
        } else {
            dao.preload();
        }
    }

    private boolean isCachingEnabled() {
        return preloadConfiguration != null;
    }

    public void setBo( Class bo ) {
        this.bo = bo;
    }
}

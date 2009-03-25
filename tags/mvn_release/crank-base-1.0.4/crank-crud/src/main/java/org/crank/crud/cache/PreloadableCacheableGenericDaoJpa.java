package org.crank.crud.cache;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.crank.crud.GenericDaoJpa;

/**
 * Extension of generic dao for cache and preload awareness.
 * 
 * @author Chris Mathias
 * @version $Revision$
 */
public class PreloadableCacheableGenericDaoJpa<T, PK extends Serializable> extends GenericDaoJpa<T, PK> implements
        PreloadableGenericDao {

    private int defaultPreloadCacheSize = 500;
    private PreloadConfiguration preloadConfiguration;
    private List<T> preloadResults;

    public PreloadableCacheableGenericDaoJpa() {
    }

    public PreloadableCacheableGenericDaoJpa( Class<T> aType ) {
        super( aType );
    }

    public int getDefaultPreloadCacheSize() {
        return defaultPreloadCacheSize;
    }

    public void setDefaultPreloadCacheSize( int defaultPreloadCacheSize ) {
        this.defaultPreloadCacheSize = defaultPreloadCacheSize;
    }

    public void preload() {
        if (getPreloadConfiguration().getPreloadingHQL().length() > 0) {
            logger.info( "Preloading cache for:" + type.getName() + " using hql:" + getPreloadConfiguration().getPreloadingHQL() );
            preload( getPreloadConfiguration().getPreloadingHQL() );
        } else if (getPreloadConfiguration().getPreloadingRecordCount() > 0) {
            logger.info( "Preloading cache for:" + type.getName() + " with " + getPreloadConfiguration().getPreloadingRecordCount() + " records.");
            preload( getPreloadConfiguration().getPreloadingRecordCount() );
        } else {
            logger.info( "Preloading cache for:" + type.getName() + " with " + defaultPreloadCacheSize + " records." );
            preloadDefault();
        }
        initializeChildren();
    }

    public void preloadDefault() {
        preloadResults = find( 0, defaultPreloadCacheSize );
    }

    public void preload( int numberOfObjects ) {
        preloadResults = find( 0, numberOfObjects );
    }

    @SuppressWarnings("unchecked")
	public void preload( String hql ) {
        EntityManager entityManager = getJpaTemplate().getEntityManagerFactory().createEntityManager();
        Query query = entityManager.createQuery( hql );
        preloadResults = query.getResultList();
    }

    private void initializeChildren() {
        if (preloadConfiguration.getChildrenToInitialize() != null
                && !preloadConfiguration.getChildrenToInitialize().isEmpty()) {
            final Object[] noargs = (Object[]) null;
            for (String methodName : preloadConfiguration.getChildrenToInitialize()) {
                try {
                    Method method = type.getMethod( methodName, (Class []) null );
                    if (method.getParameterTypes().length == 0 && methodName.equals( method.getName() )) {
                        logger.info("Initializing method " + methodName + " for " + preloadResults.size() + " preloads of type " + type.getName());
                        for (T instanceOfType : preloadResults) {
                            /* Has to be noarg method. */
                            method.invoke( instanceOfType, noargs );
                        }
                    }    
                    
                } catch (Exception ex) {
                    throw new RuntimeException( ex );
                }
            }
        }
    }

    public void setPreloadConfiguration( PreloadConfiguration preloadConfiguration ) {
        this.preloadConfiguration = preloadConfiguration;
    }

    public PreloadConfiguration getPreloadConfiguration() {
        return preloadConfiguration;
    }

}

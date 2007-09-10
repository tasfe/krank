package org.crank.crud.cache;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

import org.crank.crud.GenericDaoJpa;

/**
 * Extension of generic dao for cache and preload awareness.
 * 
 * @author Chris Mathias
 * @version $Revision$
 */
public class PreloadableCacheableGenericDaoJpa<T, PK extends Serializable> extends GenericDaoJpa implements
        PreloadableGenericDao {

    private int defaultPreloadCacheSize = 500;
    private PreloadConfiguration preloadConfiguration;
    private List<T> preloadResults;

    public PreloadableCacheableGenericDaoJpa() {
    }

    public PreloadableCacheableGenericDaoJpa( Class aType ) {
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
            preload( getPreloadConfiguration().getPreloadingHQL() );
        } else if (getPreloadConfiguration().getPreloadingRecordCount() > 0) {
            preload( getPreloadConfiguration().getPreloadingRecordCount() );
        } else {
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

    public void preload( String hql ) {
        preloadResults = getJpaTemplate().findByNamedQuery( hql );
    }

    private void initializeChildren() {
        if (preloadConfiguration.getChildrenToInitialize() != null
                && !preloadConfiguration.getChildrenToInitialize().isEmpty()) {
            final Object[] noargs = (Object[]) null;
            for (String methodName : preloadConfiguration.getChildrenToInitialize()) {
                try {
                    Method method = type.getMethod( methodName, null );
                    if (method.getParameterTypes().length == 0 && methodName.equals( method.getName() )) {
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

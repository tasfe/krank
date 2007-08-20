package org.crank.crud.cache;

import org.crank.crud.GenericDaoJpa;

import java.io.Serializable;


/**
 * Extension of generic dao for cache and preload awareness.
 *
 * @author Chris Mathias
 * @version $Revision$
 */
public class PreloadableCacheableGenericDaoJpa<T, PK extends Serializable> extends GenericDaoJpa implements CacheableGenericDao, PreloadableGenericDao {

    private int defaultPreloadCacheSize = 500;
    private CacheConfiguration cacheConfiguration;

    public PreloadableCacheableGenericDaoJpa() {
    }

    public PreloadableCacheableGenericDaoJpa(Class aType) {
        super(aType);
    }

    public int getDefaultPreloadCacheSize() {
        return defaultPreloadCacheSize;
    }

    public void setDefaultPreloadCacheSize(int defaultPreloadCacheSize) {
        this.defaultPreloadCacheSize = defaultPreloadCacheSize;
    }

    public void preload() {
        find(0, defaultPreloadCacheSize);
    }

    public void preload(int numberOfObjects) {
        find(0, numberOfObjects);
    }

    public void preload(String hql) {
        getJpaTemplate().findByNamedQuery(hql);
    }

    public void setCacheConfiguration(CacheConfiguration cacheConfiguration) {
        this.cacheConfiguration = cacheConfiguration;
    }

    public CacheConfiguration getCacheConfiguration() {
        return cacheConfiguration;
    }


}

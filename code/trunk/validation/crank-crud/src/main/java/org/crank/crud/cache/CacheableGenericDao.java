package org.crank.crud.cache;

/**
 * Interface for cache awareness/capability
 *
 * @author Chris Mathias
 * @version $Revision$
 */
public interface CacheableGenericDao {
    void setCacheConfiguration(CacheConfiguration cacheConfiguration);
    org.crank.crud.cache.CacheConfiguration getCacheConfiguration();
}

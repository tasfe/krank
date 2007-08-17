package org.crank.crud.cache;


import java.io.Serializable;

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

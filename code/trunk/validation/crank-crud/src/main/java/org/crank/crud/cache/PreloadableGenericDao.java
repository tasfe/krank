package org.crank.crud.cache;


/**
 * Interface for preload awareness/capability.
 *
 * @author Chris Mathias
 * @version $Revision$
 */
public interface PreloadableGenericDao {
    /**
     * Load up the first N records so they wind up in the cache
     */
    void preload();

    /**
     * Load up the first numberOfObjects records so they wind up in cache
     * @param numberOfObjects
     */
    void preload(int numberOfObjects);

    /**
     * Use a string of hql to preload a set of objects so they wind up in cache
     * @param hql
     */
    void preload(String hql);
}

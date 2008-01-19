package org.crank.crud;

import java.lang.reflect.Method;

/**
 * Mixin that gets used with DAO support.
 *  @param <T> DAO class
 *  @version $Revision:$
 *  @author Rick Hightower
 */
public interface Finder<T> {
    public Object executeFinder( Method method, final Object[] queryArgs );
}

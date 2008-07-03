package org.crank.crud;

import java.lang.reflect.Method;

/**
 * Mixin that gets used with DAO support.
 *  @version $Revision:$
 *  @author Rick Hightower
 */
@Deprecated
public interface Finder {
    public Object executeFinder( Method method, final Object[] queryArgs );
}

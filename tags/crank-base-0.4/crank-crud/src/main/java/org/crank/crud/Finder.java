package org.crank.crud;

import java.lang.reflect.Method;

/**
*  @param <T> DAO class
*  @version $Revision:$
*  @author Rick Hightower
*/
public interface Finder<T> {
    public Object executeFinder( Method method, final Object[] queryArgs );
}

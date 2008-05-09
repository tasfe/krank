package org.crank.crud;

import java.lang.reflect.Method;

/**
 * Mixin that gets used with DAO support.
 *  @version $Revision:$
 *  @author Rick Hightower
 */
public interface DaoMethods {
    public Object executeFinder( Method method, final Object[] queryArgs );
    public Object executeDelete( Method method, final Object[] queryArgs );
    public Object executeUpdate( Method method, final Object[] queryArgs );
}
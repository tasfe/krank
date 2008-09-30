package org.crank.web;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * Request scope map.
 * 
 * @author Rick Hightower
 * @version $Revision$
 *
 */
@SuppressWarnings("unchecked")
public class RequestMap implements Map {

    private HttpServletRequest request;

    public RequestMap( final HttpServletRequest aRequest ) {
        this.request = aRequest;
    }

    public void clear() {
        throw new UnsupportedOperationException();

    }

    public boolean containsKey( Object key ) {
        throw new UnsupportedOperationException();
    }

    public boolean containsValue( Object value ) {
        throw new UnsupportedOperationException();
    }

    public Set entrySet() {
        throw new UnsupportedOperationException();
    }

    public Object get( Object key ) {
        return request.getAttribute( (String) key );
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    public Set keySet() {
        throw new UnsupportedOperationException();
    }

    public Object put( Object key, Object value ) {
        request.setAttribute( (String) key, value );
        return null;
    }

    public void putAll( Map t ) {
        throw new UnsupportedOperationException();
    }

    public Object remove( Object key ) {
        request.removeAttribute( (String) key );
        return null;
    }

    public int size() {
        throw new UnsupportedOperationException();
    }

    public Collection values() {
        throw new UnsupportedOperationException();
    }

}

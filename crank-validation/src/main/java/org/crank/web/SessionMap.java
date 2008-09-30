package org.crank.web;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

/**
 * Session scope map.
 * 
 * @author Rick Hightower
 * @version $Revision$
 *
 */
@SuppressWarnings("unchecked")
public class SessionMap implements Map {

    private HttpSession session;

    public SessionMap( final HttpSession aSession ) {
        this.session = aSession;
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
        return session.getAttribute( (String) key );
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    public Set keySet() {
        throw new UnsupportedOperationException();
    }

    public Object put( Object key, Object value ) {
        session.setAttribute( (String) key, value );
        return null;
    }

    public void putAll( Map t ) {
        throw new UnsupportedOperationException();
    }

    public Object remove( Object key ) {
        session.removeAttribute( (String) key );
        return null;
    }

    public int size() {
        throw new UnsupportedOperationException();
    }

    public Collection values() {
        throw new UnsupportedOperationException();
    }

}

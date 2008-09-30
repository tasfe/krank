package org.crank.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Map of cookies.
 * 
 * @author Rick Hightower
 * @version $Revision$
 * 
 */
@SuppressWarnings("unchecked")
public class CookieMap implements Map {

    private Map<String, String> cookies = new HashMap<String, String>();

    private HttpServletResponse response;

    /**
     * 
     * @param aCookies
     *            cookies
     * @param aResponse
     *            response
     */
    public CookieMap( final Cookie[] aCookies, final HttpServletResponse aResponse ) {
        this.response = aResponse;

        if (aCookies == null) {
            return;
        }
        for (Cookie cookie : aCookies) {
            cookies.put( cookie.getName(), cookie.getValue() );
        }
    }

    /**
     * 
     */
    public void clear() {
        throw new UnsupportedOperationException();
    }

    /**
     * @param key key
     * @return does it contain a key
     */
    public boolean containsKey( Object key ) {
        throw new UnsupportedOperationException();
    }

    public boolean containsValue( Object value ) {
        throw new UnsupportedOperationException();
    }

    public Set entrySet() {
        return cookies.entrySet();
    }

    public Object get( Object key ) {
        return cookies.get( (String) key );
    }

    public boolean isEmpty() {
        return cookies.isEmpty();
    }

    public Set keySet() {
        return cookies.keySet();
    }

    public Object put( Object key, Object value ) {
        String sKey = key.toString();
        String sValue = value.toString();
        Cookie cookie = new Cookie( sKey, sValue );
        cookie.setMaxAge( Integer.MAX_VALUE );
        response.addCookie( cookie );
        return cookies.put( (String) key, (String) value );
    }

    public void putAll( Map t ) {
        throw new UnsupportedOperationException();
    }

    public Object remove( Object key ) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        return cookies.size();
    }

    public Collection values() {
        return cookies.values();
    }

}

package org.crank.web;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Map of headers.
 * 
 * @author Rick Hightower
 * @version $Revision$
 *
 */
@SuppressWarnings("unchecked")
public class HeaderMap implements Map {

    private HttpServletRequest request;

    private HttpServletResponse response;

    public HeaderMap( final HttpServletRequest aRequest, final HttpServletResponse aResponse ) {
        this.request = aRequest;
        this.response = aResponse;
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
        return request.getHeader( (String) key );
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    public Set keySet() {
        throw new UnsupportedOperationException();
    }

    public Object put( final Object key, final Object value ) {
        if (value instanceof Date) {
            response.addDateHeader( key.toString(), ( (Date) value ).getTime() );
        } else if (value instanceof Integer) {
            response.addIntHeader( key.toString(), ( (Integer) value ).intValue() );
        } else {
            response.addHeader( key.toString(), value.toString() );
        }
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

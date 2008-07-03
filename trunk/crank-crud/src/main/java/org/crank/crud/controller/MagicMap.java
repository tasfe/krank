package org.crank.crud.controller;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Collection;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: richardhightower
 * Date: Jun 23, 2008
 * Time: 1:11:17 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
class MagicMap implements Map<String, Object>, Serializable {
    private BeanWrapper beanWrapper;
    private BeanWrapper thisWrapper;
    private Map<String, Object> map = new HashMap<String, Object>();

    public MagicMap (Object object){
        init(object);
    }

    public void init (Object object){
        beanWrapper = new BeanWrapperImpl(object);
        thisWrapper = new BeanWrapperImpl(this);
    }

    public MagicMap (){

    }


    public void clear() {
    }

    public boolean containsKey( Object key ) {
        throw new UnsupportedOperationException();
    }

    public boolean containsValue( Object arg0 ) {
        throw new UnsupportedOperationException();
    }

    public Set<Entry<String, Object>> entrySet() {
        throw new UnsupportedOperationException();
    }

    public Object get( Object oKey ) {
        try {
            String key = (String) oKey;
            if (this.map.containsKey(key)) {
            	return this.map.get(key);
            } else if (beanWrapper.isReadableProperty(key)) {
            	return beanWrapper.getPropertyValue( key );
            } else if (thisWrapper.isReadableProperty(key)){
            	return thisWrapper.getPropertyValue( key ) ;
            } else {
            	if (key.contains(".")) {
            		int index = key.indexOf(".");
            		String newKey = key.substring(0, index);
            		String newProp = key.substring(index+1);
            		Object object = this.map.get(newKey);
            		if (object==null) {
            			return null;
            		} else {
	            		BeanWrapper wrapper = new BeanWrapperImpl(object);
	            		return wrapper.getPropertyValue(newProp);
            		}
            	} else {
            		return null;
            	}
            }
        } catch (org.springframework.beans.NullValueInNestedPathException nvinpe) {
            return null;
        } catch (org.springframework.beans.NotReadablePropertyException nrpe) {
        	return null;
        }
    }

    public boolean isEmpty() {
        return false;
    }

    public Set<String> keySet() {
        throw new UnsupportedOperationException();
    }

    public Object put( String oKey, Object value ) {
        String key = (String) oKey;
        if (beanWrapper.isWritableProperty(key)) {
        	beanWrapper.setPropertyValue( key, value );
        } else if (thisWrapper.isWritableProperty(key)){
        	thisWrapper.setPropertyValue( key, value );
        } else {
        	return map.put(oKey, value);
        }
        return null;
    }
    public Object putInMap( String oKey, Object value ) {
    	return map.put(oKey, value);
    }

    public void putAll( Map<? extends String, ? extends Object> arg0 ) {
        throw new UnsupportedOperationException();
    }

    public Object remove( Object arg0 ) {
        throw new UnsupportedOperationException();

    }

    public int size() {
        throw new UnsupportedOperationException();
    }

    public Collection<Object> values() {
        throw new UnsupportedOperationException();
    }


}

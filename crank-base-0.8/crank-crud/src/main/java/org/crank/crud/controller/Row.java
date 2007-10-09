package org.crank.crud.controller;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;


import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

class MagicMap implements Map<String, Object>, Serializable {
    private BeanWrapper beanWrapper;
    
    public MagicMap (Object object){
        beanWrapper = new BeanWrapperImpl(object);
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

    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        throw new UnsupportedOperationException();
    }

    public Object get( Object oKey ) {
        try {
            String key = (String) oKey;
            return beanWrapper.getPropertyValue( key );
        } catch (org.springframework.beans.NullValueInNestedPathException nvinpe) {
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
        beanWrapper.setPropertyValue( key, value );
        return null;
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

public class Row implements Serializable {
    private boolean selected;
    private Object object;
    private MagicMap magicMap;

    public Map getMap() {
        if (magicMap==null) {
            magicMap=new MagicMap(object);
        }
        return magicMap;
    }

    public Object getObject() {
        return object;
    }

    public void setObject( Object object ) {
        this.object = object;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected( boolean selected ) {
        this.selected = selected;
    }
}

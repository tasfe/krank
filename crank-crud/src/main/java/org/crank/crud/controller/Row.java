package org.crank.crud.controller;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

@SuppressWarnings("serial")
public class Row extends MagicMap implements Serializable {
    private boolean selected;
    private Object object;
    
    public Row (Object object) {
    	super(object);
    	this.object = object;
    }
    
    public Row () {
    	
    }

    @Deprecated
    public Map<String, Object> getMap() {
        return this;
    }

    public Object getObject() {
        return object;
    }

    public void setObject( Object object ) {
        this.object = object;
        super.init(object);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected( boolean selected ) {
        this.selected = selected;
    }

    public String toString() {
    	return String.format("row ((%s) selected=%s)", object, selected);
    }
}

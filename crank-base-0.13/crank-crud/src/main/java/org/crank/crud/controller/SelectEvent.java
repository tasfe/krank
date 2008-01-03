package org.crank.crud.controller;

import java.io.Serializable;
import java.util.EventObject;

public class SelectEvent extends EventObject implements Serializable {
	private Object value;
	public SelectEvent( Object source, Object value ) {
        super( source );
        this.value = value;
    }
    public Object getValue() {
		return value;
	}

}

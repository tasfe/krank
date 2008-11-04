package org.crank.crud.controller;

import java.io.Serializable;
import java.util.EventObject;

public class CrudEvent extends EventObject implements Serializable {
	private Object entity;
    public CrudEvent( Object source, Object entity ) {
        super( source );
        this.entity = entity;
    }
	public Object getEntity() {
		return entity;
	}

}

package org.crank.crud.controller;

import java.io.Serializable;
import java.util.EventObject;

public class SelectEvent extends EventObject implements Serializable {

    public SelectEvent( Object source ) {
        super( source );
    }

}

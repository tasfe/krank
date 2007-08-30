package org.crank.crud.controller;

import java.io.Serializable;
import java.util.EventObject;

public class ToggleEvent extends EventObject implements Serializable {

    public ToggleEvent( Object source ) {
        super( source );
    }

}

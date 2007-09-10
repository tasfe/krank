package org.crank.crud.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.crank.crud.criteria.Between;

public class BetweenWithEvents extends Between implements Serializable, Toggleable{
    private List<ToggleListener> list = new ArrayList<ToggleListener>();
    public BetweenWithEvents() {
        super();
    }
    public BetweenWithEvents( String aName, Object aValue, Object aValue2, boolean alias ) {
        super( aName, aValue, aValue2, alias );
    }
    public BetweenWithEvents( String aName, Object aValue, Object aValue2 ) {
        super( aName, aValue, aValue2 );
    }
    public void addToggleListener(ToggleListener listener) {
        list.add( listener );
    }
    public void removeToggleListener(ToggleListener listener) {
        list.remove( listener );
    }

    @Override
    public void disable() {
        super.disable();
        fireToggle();
    }

    @Override
    public void enable() {
        super.enable();
        fireToggle();
    }

    private void fireToggle() {
        ToggleEvent te = new ToggleEvent(this);
        for (ToggleListener tl : list) {
            tl.toggle( te );
        }
    }


}

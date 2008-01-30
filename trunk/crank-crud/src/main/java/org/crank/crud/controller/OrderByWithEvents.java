package org.crank.crud.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.crank.crud.criteria.OrderBy;
import org.crank.crud.criteria.OrderDirection;

public class OrderByWithEvents extends OrderBy implements Serializable, Toggleable {

    private List<ToggleListener> list = new ArrayList<ToggleListener>();

    public OrderByWithEvents() {
        super();
    }
    public OrderByWithEvents( String aName, OrderDirection aDirection ) {
        super( aName, aDirection );
    }
    
    public OrderByWithEvents( String aName, OrderDirection aDirection, boolean alias ) {
        super( aName, aDirection );
        super.setAlias(alias);
    }
    

    public List<ToggleListener> getToggleListeners(){
        return list;
    }
    
    public void addToggleListener(ToggleListener listener) {
        list.add( listener );
    }
    public void removeToggleListener(ToggleListener listener) {
        list.remove( listener );
    }
    
    @Override
    public void toggle() {
        super.toggle();
        ToggleEvent te = new ToggleEvent(this);
        for (ToggleListener tl : list) {
            tl.toggle( te );
        }
    }
    
}

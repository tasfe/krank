package org.crank.crud.controller;

import java.util.ArrayList;
import java.util.List;

public class SelectSupport implements Selectable {
    private List<SelectListener> list = new ArrayList<SelectListener>();
	/**
     * @see Selectable#addSelectListener(SelectListener)
     */
    public void addSelectListener(SelectListener listener) {
        list.add( listener );
    }
    /**
     * @see Selectable#removeSelectListener(SelectListener)
     */    
    public void removeSelectListener(SelectListener listener) {
        list.remove( listener );
    }

    /**
     * Fire an event to the listeners.
     *
     */
    public void fireSelect() {
    	fireSelect(null);
    }
    /**
     * Fire an event to the listeners, setting the selected value.
     *
     */
    public void fireSelect(Object value) {
    	SelectEvent se = new SelectEvent(this, value);
        for (SelectListener sl : list) {
            sl.select( se );
        }
    }
    
}

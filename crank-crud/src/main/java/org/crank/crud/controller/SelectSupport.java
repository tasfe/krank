package org.crank.crud.controller;

import java.util.ArrayList;
import java.util.List;

public class SelectSupport implements Selectable {
    private List<SelectListener> list = new ArrayList<SelectListener>();
    private Object value;

    public Object getValue() {
		return value;
	}
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
    	SelectEvent se = new SelectEvent(this);
        for (SelectListener sl : list) {
            sl.select( se );
        }
    }
    /**
     * Fire an event to the listeners, setting the selected value.
     *
     */
    public void fireSelect(Object value) {
    	this.value = value;
    	fireSelect();
    }
    
}

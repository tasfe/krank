package org.crank.crud.controller;

import java.util.ArrayList;
import java.util.List;

public class SelectSupport implements Selectable {
	private Object source=this;
    private List<SelectListener> list = new ArrayList<SelectListener>();
    
    public SelectSupport() {
    	
    }
    
    public SelectSupport(Object source) {
    	this.source = source;
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
    	fireSelect(null);
    }
    /**
     * Fire an event to the listeners, setting the selected value.
     *
     */
    public void fireSelect(Object value) {
    	SelectEvent se = new SelectEvent(source, value);
        for (SelectListener sl : list) {
            sl.select( se );
        }
    }
    
    /**
     * Fire an event to the listeners.
     *
     */
    public void fireUnselect() {
    	fireUnselect(null);
    }
    /**
     * Fire an event to the listeners, setting the selected value.
     *
     */
    public void fireUnselect(Object value) {
    	SelectEvent se = new SelectEvent(source, value);
        for (SelectListener sl : list) {
            sl.unselect(se);
        }
    }
	public Object getSource() {
		return source;
	}
	public void setSource(Object source) {
		this.source = source;
	}

}

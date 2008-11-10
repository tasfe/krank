package org.crank.crud.controller;

import java.util.ArrayList;
import java.util.List;

public class ToggleSupport implements Toggleable {
    private List<ToggleListener> list = new ArrayList<ToggleListener>();

    /**
     * @see Toggleable#addToggleListener(ToggleListener)
     */
    public void addToggleListener(ToggleListener listener) {
        list.add( listener );
    }
    /**
     * @see Toggleable#addToggleListener(ToggleListener)
     */    
    public void removeToggleListener(ToggleListener listener) {
        list.remove( listener );
    }

    /**
     * Fire and event to the listeners.
     *
     */
    public void fireToggle() {
        ToggleEvent te = new ToggleEvent(this);
        for (ToggleListener tl : list) {
            tl.toggle( te );
        }
    }
    
}

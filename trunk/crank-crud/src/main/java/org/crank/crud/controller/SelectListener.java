package org.crank.crud.controller;

import java.util.EventListener;


public interface SelectListener extends EventListener {
    public void select(SelectEvent event);
    public void unselect(SelectEvent event);
    
}

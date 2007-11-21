package org.crank.crud.controller;

public interface Toggleable {

    void addToggleListener(ToggleListener listener);
    void removeToggleListener(ToggleListener listener);
    
}

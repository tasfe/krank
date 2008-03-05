package org.crank.crud.controller;

public interface Selectable {

    void addSelectListener(SelectListener listener);
    void removeSelectListener(SelectListener listener);
    
}

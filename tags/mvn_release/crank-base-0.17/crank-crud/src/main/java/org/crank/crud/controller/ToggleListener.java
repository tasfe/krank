package org.crank.crud.controller;

import java.util.EventListener;


public interface ToggleListener extends EventListener {
    public void toggle(ToggleEvent event);
}

package org.crank.crud.controller;

import java.io.Serializable;
import java.util.List;

public interface EntityLocator<T extends Serializable> {
    T getEntity();
    List<T> getSelectedEntities();
}

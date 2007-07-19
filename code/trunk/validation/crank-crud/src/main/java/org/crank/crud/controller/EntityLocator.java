package org.crank.crud.controller;

import java.io.Serializable;
import java.util.List;

public interface EntityLocator {
    Serializable getEntity();
    List getSelectedEntities();
}

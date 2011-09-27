package org.crank.crud.controller;

import java.util.EventListener;

public interface PaginationListener extends EventListener {
    void pagination(PaginationEvent pe);
}

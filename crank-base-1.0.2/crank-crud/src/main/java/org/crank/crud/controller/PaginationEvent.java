package org.crank.crud.controller;

import java.util.EventObject;
import java.io.Serializable;

public class PaginationEvent extends EventObject implements Serializable {
    private int page;

    public PaginationEvent(Object source, int page) {

        super(source);
        this.page = page;
    }

    public int getPage() {
        return page;
    }

}

package org.crank.crud.controller;

public class Row {
    private boolean selected;
    private Object object;

    public Object getObject() {
        return object;
    }

    public void setObject( Object object ) {
        this.object = object;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected( boolean selected ) {
        this.selected = selected;
    }
}

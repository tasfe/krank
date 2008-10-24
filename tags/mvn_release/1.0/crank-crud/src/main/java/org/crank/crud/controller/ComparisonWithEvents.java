package org.crank.crud.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.crank.crud.criteria.Comparison;
import org.crank.crud.criteria.Operator;

public class ComparisonWithEvents extends Comparison implements Serializable, Toggleable {
    private List<ToggleListener> list = new ArrayList<ToggleListener>();
    
    public ComparisonWithEvents() {
        super();
    }


	@Override
    public void setValue( Object value ) {
        if (value==null) {
            this.setOperator( Operator.EQ );
        } else if (value instanceof String) {
            String sValue = (String) value;
            if (sValue.contains( "%" )) {
                this.setOperator( Operator.LIKE );
            }
        } else if (value instanceof Boolean || value.getClass().isPrimitive() 
                || value.getClass().isInstance( Number.class )) {
        	if (this.getOperator()==null) {
        		this.setOperator( Operator.EQ );
        	}
        }
        super.setValue( value );
    }

    public ComparisonWithEvents( String aName, Operator aOperator, Object aValue, boolean alias ) {
        super( aName, aOperator, aValue, alias );
    }

    public ComparisonWithEvents( String aName, Operator aOperator, Object aValue ) {
        super( aName, aOperator, aValue );
    }

    public void addToggleListener(ToggleListener listener) {
        list.add( listener );
    }
    public void removeToggleListener(ToggleListener listener) {
        list.remove( listener );
    }
    
    @Override
    public void disable() {
        super.disable();
        fireToggle();
    }

    @Override
    public void enable() {
        super.enable();
        fireToggle();
    }

    private void fireToggle() {
        ToggleEvent te = new ToggleEvent(this);
        for (ToggleListener tl : list) {
            tl.toggle( te );
        }
    }
    
}

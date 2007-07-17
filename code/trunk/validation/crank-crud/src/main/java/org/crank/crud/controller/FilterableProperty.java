package org.crank.crud.controller;

import java.io.Serializable;
import java.util.Date;

import org.crank.crud.criteria.Comparison;
import org.crank.crud.criteria.Operator;
import org.crank.crud.criteria.OrderDirection;

public class FilterableProperty implements Serializable, Toggleable {
    public Comparison comparison;
    public OrderByWithEvents orderBy;
    private Class type;
    
    public FilterableProperty () {
        
    }
    
    @SuppressWarnings("unchecked")
    public FilterableProperty(String name, Class type) {
        this.type = type;
        if (this.type.isAssignableFrom( String.class )) {
            comparison = new ComparisonWithEvents(name, Operator.LIKE_START, null);
        } else if (Date.class.isAssignableFrom( type )) {
            comparison = new BetweenWithEvents(name, new Date(), new Date());
        } else {
            comparison = new ComparisonWithEvents(name, Operator.EQ, null);
        }
        orderBy = new OrderByWithEvents(name, OrderDirection.ASC);
        this.type = type;
    }

    public Comparison getComparison() {
        return comparison;
    }

    public OrderByWithEvents getOrderBy() {
        return orderBy;
    }

    public Class getType() {
        return type;
    }

    public void addToggleListener( ToggleListener listener ) {
        this.orderBy.addToggleListener( listener );
        ((Toggleable)comparison).addToggleListener( listener );
    }

    public void removeToggleListener( ToggleListener listener ) {
        this.orderBy.removeToggleListener( listener );
        ((Toggleable)comparison).removeToggleListener( listener );
        
    }
}

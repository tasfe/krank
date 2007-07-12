package org.crank.crud.controller;

import java.io.Serializable;

import org.crank.crud.criteria.Operator;
import org.crank.crud.criteria.OrderDirection;

public class FilterableProperty implements Serializable {
    public ComparisonWithEvents comparison;
    public OrderByWithEvents orderBy;
    private Class type;
    
    public FilterableProperty () {
        
    }
    
    @SuppressWarnings("unchecked")
    public FilterableProperty(String name, Class type) {
        this.type = type;
        if (this.type.isAssignableFrom( String.class )) {
            comparison = new ComparisonWithEvents(name, Operator.LIKE_START, null);
        } else {
            comparison = new ComparisonWithEvents(name, Operator.EQ, null);
        }
        orderBy = new OrderByWithEvents(name, OrderDirection.ASC);
        this.type = type;
    }

    public ComparisonWithEvents getComparison() {
        return comparison;
    }

    public OrderByWithEvents getOrderBy() {
        return orderBy;
    }

    public Class getType() {
        return type;
    }
}

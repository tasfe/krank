package org.crank.crud.controller;

import org.crank.crud.criteria.Comparison;
import org.crank.crud.criteria.Operator;
import org.crank.crud.criteria.OrderBy;
import org.crank.crud.criteria.OrderDirection;

public class FilterableProperty {
    public Comparison comparison;
    public OrderBy orderBy;
    private Class type;
    
    @SuppressWarnings("unchecked")
    public FilterableProperty(String name, Class type) {
        this.type = type;
        if (this.type.isAssignableFrom( String.class )) {
            comparison = new Comparison(name, Operator.LIKE_START, null);
        } else {
            comparison = new Comparison(name, Operator.EQ, null);
        }
        orderBy = new OrderBy(name, OrderDirection.ASC);
        this.type = type;
    }

    public Comparison getComparison() {
        return comparison;
    }

    public void setComparison( Comparison comparison ) {
        this.comparison = comparison;
    }

    public OrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy( OrderBy orderBy ) {
        this.orderBy = orderBy;
    }

    public Class getType() {
        return type;
    }
}

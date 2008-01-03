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
    
    public boolean isEnum() {
    	return Enum.class.isAssignableFrom(type); 
    }
    
    public boolean isString() {
    	return String.class.isAssignableFrom(type); 
    }

    public boolean isBigInteger() {
    	return java.math.BigInteger.class.isAssignableFrom(type);
    }

    public boolean isBigDecimal() {
    	return java.math.BigDecimal.class.isAssignableFrom(type);
    }

    public boolean isFloat() {
    	if (Float.class.isAssignableFrom(type)){
    		return true;
    	} else {
    		if (type.isPrimitive() && type.getName().equals("float")){
    			return true;
    		}    		
    	}
    	return false;
    }

    public boolean isDouble() {
    	if (Double.class.isAssignableFrom(type)){
    		return true;
    	} else {
    		if (type.isPrimitive() && type.getName().equals("double")){
    			return true;
    		}    		
    	}
    	return false;
    }


    public boolean isLong() {
    	if (Long.class.isAssignableFrom(type)){
    		return true;
    	} else {
    		if (type.isPrimitive() && type.getName().equals("long")){
    			return true;
    		}    		
    	}
    	return false;
    }

    public boolean isInteger() {
    	if (Integer.class.isAssignableFrom(type)) {
    		return true;
    	} else {
    		if (type.isPrimitive() && type.getName().equals("int")){
    			return true;
    		}
    	}
    	return false;
    }

    public boolean isShort() {
    	if (Short.class.isAssignableFrom(type)) {
    		return true;
    	} else {
    		if (type.isPrimitive() && type.getName().equals("short")){
    			return true;
    		}
    	}
    	return false;
    }

    
    @SuppressWarnings("unchecked")
    public FilterableProperty(String name, Class type) {
        this.type = type;
        if (this.type.isAssignableFrom( String.class )) {
            comparison = new ComparisonWithEvents(name, Operator.LIKE_START, null);
        } else if (Date.class.isAssignableFrom( type )) {
            comparison = new BetweenWithEvents(name, null, null);
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

package org.crank.crud.controller;

import java.io.Serializable;
import java.util.Date;

import org.crank.crud.criteria.Comparison;
import org.crank.crud.criteria.Operator;
import org.crank.crud.criteria.OrderDirection;

/**
 * A FilterableProperty is an object that typically gets bound
 * to the column of a dataTable.
 * 
 * It has a Comparison, OrderBy, Type, and autoCreatePrependParentAlias.
 * 
 * @author Rick Hightower
 *
 */
@SuppressWarnings("serial")
public class FilterableProperty implements Serializable, Toggleable {
	/** Comparison object used so the end user can filter the listing resutls. */
    public Comparison comparison;
    /** OrderBy used to order listing. */
    public OrderByWithEvents orderBy;
    /** The type of the property that we are adding sorting/filtering for. */
    @SuppressWarnings("unchecked")
	private Class type;
    
    /** Parent class type. */
    private Class<?> parentType;
    
    /** Should we append an o.prop or was it already add as in e.prop. */
    private boolean autoCreatePrependParentAlias=true;
    
    /** Init filter. */
    public FilterableProperty () {
        
    }

    /** Init filter. */
    @SuppressWarnings("unchecked")
	public FilterableProperty(String name,
			Class type, Class parentType) {
    	this(name, type, parentType, true);
	}

    /** Init filter. */
    @SuppressWarnings("unchecked")
    public FilterableProperty(String name, Class type, Class parentType, boolean autoCreatePrependParentAlias) {
    	this.parentType = parentType;
        this.type = type;
        this.autoCreatePrependParentAlias = autoCreatePrependParentAlias;
        if (this.type.isAssignableFrom( String.class )) {
            comparison = new ComparisonWithEvents(name, Operator.LIKE_START, null, !autoCreatePrependParentAlias);
        } else if (Date.class.isAssignableFrom( type )) {
            comparison = new BetweenWithEvents(name, null, null, !autoCreatePrependParentAlias);
        } else {
            comparison = new ComparisonWithEvents(name, Operator.EQ, null, !autoCreatePrependParentAlias);
        }
        orderBy = new OrderByWithEvents(name, OrderDirection.ASC, !autoCreatePrependParentAlias);
        this.type = type;
    }

    /** Is the property an enum? */
	public boolean isEnum() {
    	return Enum.class.isAssignableFrom(type); 
    }
	/** Is the property a String? */
    public boolean isString() {
    	return String.class.isAssignableFrom(type); 
    }

    /** Is the property a BigInteger? */
    public boolean isBigInteger() {
    	return java.math.BigInteger.class.isAssignableFrom(type);
    }

    /** Is the property a BigDecimal? */
    public boolean isBigDecimal() {
    	return java.math.BigDecimal.class.isAssignableFrom(type);
    }

    /** Is the property a Date? */
    public boolean isDate() {
    	return Date.class.isAssignableFrom(type);
    }


    /** Is the property a Float or float? */
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

    /** Is the property Double or double? */
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

    /** Is the property Long or long? */
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

    /** Is the property a Integer or int? */    
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
    /** Is the property a Short or short? */
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

    
    /** Comparison object bound for example to the header area of a dataTable. */
    public Comparison getComparison() {
        return comparison;
    }

    /** OrderBy object bound for example to the header area of a dataTable. */    
    public OrderByWithEvents getOrderBy() {
        return orderBy;
    }

    /** Get the property type. */    
    @SuppressWarnings("unchecked")
	public Class getType() {
        return type;
    }

    /** Toggle listener for enabling/disabling this filter. */    
    public void addToggleListener( ToggleListener listener ) {
        this.orderBy.addToggleListener( listener );
        ((Toggleable)comparison).addToggleListener( listener );
    }

    /** Toggle listener for enabling/disabling this filter. */    
    public void removeToggleListener( ToggleListener listener ) {
        this.orderBy.removeToggleListener( listener );
        ((Toggleable)comparison).removeToggleListener( listener );
        
    }

    /** Should we append an o.prop or was it already add as in e.prop. */
    public boolean isAutoCreatePrependParentAlias() {
		return autoCreatePrependParentAlias;
	}

    /** Should we append an o.prop or was it already add as in e.prop. */    
	public void setAutoCreatePrependParentAlias(boolean autoCreatePrependParentAlias) {
		this.autoCreatePrependParentAlias = autoCreatePrependParentAlias;
	}

	public Class<?> getParentType() {
		return parentType;
	}

	public void setParentType(Class<?> parentType) {
		this.parentType = parentType;
	}

    public String toString() {
        return String.format("FilterableProperty(comparison=%s, comparison enabled=%s, orderBy=%s, type=%s, parentType=%s, autoCreatePrependParentAlias=%s)",
                this.getComparison(), this.getComparison().isEnabled(), this.orderBy, this.getType(), this.getParentType(), this.isAutoCreatePrependParentAlias());
    }
}

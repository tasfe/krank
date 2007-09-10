package org.crank.crud.criteria;

import java.io.Serializable;

public class OrderBy implements Serializable{
	private String name;
	private OrderDirection direction;
    private boolean enabled = false;

    public OrderBy () {
	}
	public OrderBy (final String aName, final OrderDirection aDirection) {
		this.name = aName;
		this.direction = aDirection;
	}
	public static OrderBy[] orderBy(OrderBy... orderBy) {
        return orderBy;
	}
	public static OrderBy asc (String name) {
		return new OrderBy(name, OrderDirection.ASC);
	}
	public static OrderBy desc (String name) {
		return new OrderBy(name, OrderDirection.DESC);
	}

	public OrderDirection getDirection() {
		return direction;
	}
	public void setDirection(OrderDirection direction) {
		this.direction = direction;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled( boolean enabled ) {
        this.enabled = enabled;
    }
    public boolean isAsc() {
        return direction == OrderDirection.ASC;
    }
    public void setAsc( boolean asc ) {
        if (asc) {
            direction = OrderDirection.ASC;
        } else {
            direction = OrderDirection.DESC;
        }
    }
    public void enable() {
        this.enabled = true;
    }
    
    public void disable() {
        this.enabled = false;
    }
    
    public void asc() {
        direction = OrderDirection.ASC;
    }
    
    public void desc() {
        direction = OrderDirection.DESC;
    }

    public void toggle() {
        this.enabled = true;
        this.setAsc( !isAsc() );
    }

}

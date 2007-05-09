package org.crank.crud.criteria;

import java.io.Serializable;

public class OrderBy implements Serializable{
	private String name;
	private OrderDirection direction;
	
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
}

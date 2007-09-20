package com.arcmind.jpa.course.cascade.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class LineItem {
	
	/* This works for Hibernate. */
	//@ManyToOne (cascade={CascadeType.MERGE})
	/* This is a safer bet for more than one JPA implementation: TopLink, JODO, etc.. */
	@ManyToOne (cascade={CascadeType.MERGE, CascadeType.PERSIST})
	//@ManyToOne ()
	private Order order;

	@Id @GeneratedValue
	private Long id;
	private String name;
	private BigDecimal price;

	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LineItem(String name, BigDecimal price) {
		super();
		this.name = name;
		this.price = price;
	}
	
	public LineItem(){
		
	}
}

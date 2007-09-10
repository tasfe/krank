package com.arcmind.jpa.course.cascade.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name="ORDER_TABLE")
public class Order {
	
	@Id @GeneratedValue
	private Long id;
	private String name;

	@OneToMany (cascade={CascadeType.ALL},
			    fetch=FetchType.EAGER) 
	private List<LineItem> lineItems;
	
	public Order(String name) {
		super();
		this.name = name;
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
	public List<LineItem> getLineItems() {
		return lineItems;
	}
	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}
	
	public void addLineItem(LineItem li) {
		if (lineItems==null) {
			lineItems = new ArrayList<LineItem>();
		}
		li.setOrder(this);
		lineItems.add(li);
	}
	
	public Order() {
		
	}
}

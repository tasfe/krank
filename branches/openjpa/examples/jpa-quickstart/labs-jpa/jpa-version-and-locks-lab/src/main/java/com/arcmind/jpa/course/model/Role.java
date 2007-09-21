package com.arcmind.jpa.course.model;


import javax.persistence.Entity;
import javax.persistence.NamedQuery;


@NamedQuery(name = "loadRole", 
		query = "select role from Role role where role.name=:name")
@Entity		
public class Role extends Identifiable{
	
	

	private String name;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Role(String name) {
		this.name = name;
	}

	public Role() {
	}

	@Override
	protected String name() {
		return this.getName();
	}
	
}

package com.arcmind.jpa.course.inheritance.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@NamedQuery(name = "in.loadRole", 
		query = "select role from InRole role where role.name=:name")
		
@Table(name="IN_ROLE")
@Entity(name="InRole")
public class Role {
	
	
	@Id
	@GeneratedValue
	private Long id;
	

	private String name;

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

	public Role(String name) {
		this.name = name;
	}

	public Role() {
	}

}

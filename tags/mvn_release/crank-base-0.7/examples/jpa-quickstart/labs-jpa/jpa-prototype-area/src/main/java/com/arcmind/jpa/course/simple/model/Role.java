package com.arcmind.jpa.course.simple.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

// TODO: Declare this class as an entity.  
// Question, what table is it mapped to? ______________________

// TODO: Add named query called "loadRole"... use the lecture slides for guidance.
public class Role {

	// TODO: Specify Id / Generator type
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

package com.arcmind.jpa.course.improved.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;

//TODO: Name this entity "ImprovedRole"
//TODO: Refactor the commented named query below to be renamed to "improved.loadRole" and to use the ImprovedRole entity
//@NamedQuery(name = "improved.loadRole", 
//		query = "select role from ImprovedRole role where role.name=:name")
public class Role {
	@Id
	@GeneratedValue
	private Long id;

	// TODO: Map this relationship to the "roles" property of the Group object
	private List<Group> groups;

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

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

}
